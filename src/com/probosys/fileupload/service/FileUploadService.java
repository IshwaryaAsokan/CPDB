package com.probosys.fileupload.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kohler.persistence.domain.ItemInfo;
import com.kohler.persistence.domain.ItemLinks;
import com.kohler.persistence.domain.ItemLinkTypes;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.model.UploadedFile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import play.libs.Json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.probosys.fileupload.model.FileItem;
import com.probosys.fileupload.model.PimPojo;
import com.kohler.persistence.domain.json.*;

public class FileUploadService {
	final String SERVER_URI = "http://localhost:8180/persistence-0.1/pim/postItems/PUNI";
	final String SERVER_URI_II = "http://localhost:8180/persistence-0.1/pim/postItemGroups/PUNI";
	final String SERVER_URI_CS = "http://localhost:8180/persistence-0.1/pim/postCSItems/PUNI";

	Properties prop = new Properties();

	InputStream input = null;

	private UploadedFile file;

	private String schemaName;

	private String jsonValue;

	private List<Item> items;

	public FileUploadService() {
		try {
			input = getClass().getClassLoader().getResourceAsStream(
					"pim.properties");
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error when loading properties file"
					+ e.getMessage());
		}
	}

	private List<ItemLinks> buildCSList(Map<String, List<ItemLinks>> actions) {

		List<ItemLinks> masterList = new ArrayList<ItemLinks>();

		List<ItemLinks> parentsList = new ArrayList<>();
		List<ItemLinks> csList = new ArrayList<>();

		ItemLinks itemObj = new ItemLinks();
		ItemInfo ii = new ItemInfo();

		for (String aItr : actions.keySet()) {

			parentsList = actions.get(aItr);
			Map<String, List<ItemLinks>> parents = parentsList.stream()
					.collect(Collectors.groupingBy(ItemLinks::getParent));

			for (String pItr : parents.keySet()) {

				csList = parents.get(pItr);
				Map<String, Map<String, List<ItemLinks>>> parentsFinalMap = csList
						.stream()
						.collect(
								Collectors
										.groupingBy(
												ItemLinks::getParent,
												Collectors
														.groupingBy(ItemLinks::getCrossSellType)));

				for (String cItr : parentsFinalMap.keySet()) {

					itemObj = new ItemLinks();
					System.out.println(cItr);

					itemObj.setParent(cItr);
					Map<String, List<ItemLinks>> csKeyMap = parentsFinalMap
							.get(cItr);

					// itemObj.setAction(csK);
					List<ItemInfo> iiList = new ArrayList<ItemInfo>();
					List<ItemLinkTypes> iltList = new ArrayList<>();

					for (String childItr : csKeyMap.keySet()) {
						ii = new ItemInfo();

						List<ItemLinks> childList = csKeyMap.get(childItr);
						ItemLinkTypes csType = new ItemLinkTypes();

						csType.setItemLinkType(childItr);

						for (ItemLinks ilink : childList) {

							ii = new ItemInfo();
							ii.setItemName(ilink.getItemLinkText());
							iiList.add(ii);
						}

						itemObj.setAction(childList.get(0).getAction());

						csType.setItemInfoList(iiList);
						iltList.add(csType);

					}
					itemObj.setCsTypesList(iltList);
					masterList.add(itemObj);

				}
			}
		}
		return masterList;
	}

	private Schema buildCSSchema(Map<String, Map<String, List<PimPojo>>> map) {

		Schema schema = new Schema();
		Action action = null;
		List<Action> actions;

		List<CrossSell> crossSells;
		CrossSell crossSell;
		Map<String, Map<String, List<PimPojo>>> cstypesMap;

		Map<String, List<PimPojo>> parentsMap;
		Product product;
		List<PimPojo> parentsList;
		List<Product> products;

		Map<String, List<PimPojo>> childMap;
		Item item;
		List<Item> items;
		List<PimPojo> childList;

		actions = new ArrayList<Action>();
		for (String actKey : map.keySet()) {

			System.out.println(actKey);

			action = new Action();
			action.setAction(ActionType.getType(actKey));

			parentsMap = map.get(actKey);
			products = new ArrayList<Product>();
			for (String parent : parentsMap.keySet()) {
				System.out.println(parent);

				parentsList = new ArrayList<PimPojo>();

				parentsList = parentsMap.get(parent);
				product = new Product();

				product.setName(parent);
				cstypesMap = parentsList
						.stream()
						.collect(
								Collectors.groupingBy(
										PimPojo::getCrossSellType,
										Collectors
												.groupingBy(PimPojo::getCrossSellChild)));

				crossSell = new CrossSell();
				crossSells = new ArrayList<CrossSell>();

				for (String csType : cstypesMap.keySet()) {
					crossSell = new CrossSell();
					crossSell.setName(csType);

					childMap = cstypesMap.get(csType);

					item = new Item();
					items = new ArrayList<Item>();

					for (String child : childMap.keySet()) {
						childList = childMap.get(child);

						for (PimPojo pojo : childList) {
							item = new Item();
							item.setName(pojo.getCrossSellChild());
							items.add(item);
						}
					}

					crossSell.setItems(items);
					crossSells.add(crossSell);
				}
				product.setList(crossSells);
				products.add(product);

			}
			action.setProducts(products);
			actions.add(action);

		}
		schema.setActions(actions);
		return schema;

	}

	public Map<String, JSONObject> getCSRequestJsonMap(
			List<PimPojo> inputFileList) {

		JSONObject requestJsonCSItemInfo = new JSONObject();

		//List<ItemLinks> listLinks = new ArrayList<ItemLinks>();

		// The records from excel have a hierarchy of Actions -> Parents ->
		// CrossSellingType -> Children. Sorting below based on this order
		Comparator<PimPojo> groupByComparator = Comparator
				.comparing(PimPojo::getAction)
				.thenComparing(PimPojo::getParent)
				.thenComparing(PimPojo::getCrossSellType)
				.thenComparing(PimPojo::getCrossSellChild);

		inputFileList.sort(groupByComparator);

		// Converting list to a stream of model classes allow using collectors
		Stream<PimPojo> recordsCS = inputFileList.stream();

		Map<String, Map<String, List<PimPojo>>> map = recordsCS
				.collect(Collectors.groupingBy(PimPojo::getAction,
						Collectors.groupingBy(PimPojo::getParent)));

		Schema schema = buildCSSchema(map);
		/*
		 * , Collectors.mapping( ItemLinkTypes::getItemInfoList,
		 * Collectors.toList())))));
		 */
		// OLD DESIGN
		/*
		 * Stream<PimPojo> records = inputFileList.stream();
		 * 
		 * // Collectors groupingby generates a complex nested map based on the
		 * // provided JSON schema Map<String, Map<String, Map<String,
		 * List<String>>>> map = records
		 * .collect(Collectors.groupingBy(PimPojo::getAction, Collectors
		 * .groupingBy(PimPojo::getParent, Collectors.groupingBy(
		 * PimPojo::getCrossSellType, Collectors.mapping( PimPojo::childValue,
		 * Collectors.toList())))));
		 * 
		 * Map<String, Object> actions = new Gson().fromJson( new
		 * JSONObject(map).toString(), HashMap.class);
		 * 
		 * // Build request json for all actions A,M,D // 3 action maps are
		 * appended to json obj - requestJsonCSItemInfo for (Entry<String,
		 * Object> itr : actions.entrySet()) { requestJsonCSItemInfo.append(
		 * "request", (new JSONObject().accumulate(itr.getKey(),
		 * actions.get(itr.getKey())))); }
		 */

		// NEW RE DESIGN
		/*
		 * // Get list based on action Map<String, List<ItemLinks>> actions =
		 * recordsCS.collect(Collectors .groupingBy(ItemLinks::getAction));
		 * 
		 * List<ItemLinks> masterList = new ArrayList<>();
		 * 
		 * // build list of item links with item info and item link type
		 * masterList = buildCSList(actions);
		 * 
		 * Type type = new TypeToken<List<ItemLinks>>() { }.getType(); String
		 * json = new Gson().toJson(masterList, type);
		 * 
		 * // array of json object of item links JSONArray array = new
		 * JSONArray(json);
		 */
		Map<String, JSONObject> requestJsonMap = new HashMap<String, JSONObject>();
		
		
		ObjectNode result2 = Json.newObject();
		try{
		schema.populate(result2);
		//System.out.println(result2);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		//String jString = result2.toString();
		//System.out.println(jString);
		
		
		 Type type = new TypeToken<List<Action>>() { }.getType(); 
		 String json = new Gson().toJson(schema.getActions(), type);
		  
		 // array of json object 
		 JSONArray array = new JSONArray(json);
		 
		// place request
		 requestJsonCSItemInfo.append("request", (array));
 
		requestJsonMap.put("csItemInfoJson", requestJsonCSItemInfo);

		return requestJsonMap;

	}

	public Map getRequestJsonMap(List<PimPojo> inputFileList) {
		Gson gson = new Gson();
		ResponseEntity<String> loginResponse = null;
		// Parents
		Map<String, List<PimPojo>> pimPojos = new HashMap<>();
		List<PimPojo> pojos = null;
		JSONObject itemInfoOutputJson = null, itemHierOutputJson = null, ancestorJson = null, parentJsonItemInfo = null, parentJsonItemHier = null, requestJsonItemInfo = null, requestJsonItemHier = null;

		/*
		 * Map<String, Parent> actionMap = new HashMap<>(); Parent parent = new
		 * Parent();
		 */parentJsonItemInfo = new JSONObject();
		int count = 0;
		parentJsonItemHier = new JSONObject();

		for (PimPojo pimPojo : inputFileList) {
			itemInfoOutputJson = new JSONObject();
			count++;
			if (pimPojo.getParent() == null) {
				pimPojo.setParent("NULL");
			}
			if (pimPojos.containsKey(pimPojo.getParent())) {
				pojos = pimPojos.get(pimPojo.getParent());
			} else {
				pojos = new ArrayList<>();
			}
			pojos.add(pimPojo);
			pimPojos.put(pimPojo.getParent(), pojos);
			if (pimPojo.getParent() != "NULL") {
				itemInfoOutputJson
						.put("status", String.valueOf(pimPojo.getStatusId()))
						.put("itemType", pimPojo.getItemType())
						.put("itemNo", pimPojo.getChild());
				parentJsonItemInfo.put(String.valueOf(count),
						itemInfoOutputJson);
			}

		}

		Action action = new Action();
		/*
		 * requestJsonItemInfo /** JSON for inserting into item groups
		 * hierarchies { "PUNI": { "M": { "Root": { "ancestors": [{ "itemNo":
		 * "KitchenMM", "itemType": "G" }, { "itemNo": "Plumbing", "itemType":
		 * "I" }] } } } }
		 */

		for (Entry<String, List<PimPojo>> parentMap : pimPojos.entrySet()) {
			String parentKey = parentMap.getKey();

			ancestorJson = new JSONObject();
			/*
			 * parent.setPimPojo(parentMap.getValue());
			 * actionMap.put(parentMap.getKey(), parent);
			 */if (parentKey != "NULL") {
				List<PimPojo> childrenList = parentMap.getValue();

				count++;

				for (PimPojo child : childrenList) {

					itemHierOutputJson = new JSONObject();

					if (child.getChild() != null) {

						itemHierOutputJson.put("itemNo", child.getChild()).put(
								"itemType", child.getItemType());

					}
					ancestorJson.append("ANCESTORS", itemHierOutputJson);

				}

				parentJsonItemHier.put(parentMap.getKey(), ancestorJson);
			}

		}
		requestJsonItemInfo = new JSONObject().put("request",
				(new JSONObject().accumulate("M", parentJsonItemInfo)));
		requestJsonItemHier = new JSONObject().put("request",
				(new JSONObject().accumulate("M", parentJsonItemHier)));

		// action.setParents(actionMap);

		/*
		 * Schema schema = new Schema(); Map<String, Action> schemaMap = new
		 * HashMap<>(); schemaMap.put("M", action);
		 * schema.setActions(schemaMap);
		 * 
		 * DataWrapper datWrapper = new DataWrapper(); Map<String, Schema>
		 * datWrapMap = new HashMap<>(); datWrapMap.put(schemaName, schema);
		 * datWrapper.setSchemas(datWrapMap);
		 */
		Map<String, JSONObject> requestJsonMap = new HashMap<String, JSONObject>();
		requestJsonMap.put("itemInfoJson", requestJsonItemInfo);
		requestJsonMap.put("itemHierJson", requestJsonItemHier);

		return requestJsonMap;
	}

	public ResponseEntity getCSResponse(JSONObject json, String schema)
			throws Exception {
		// Sending request for itemHierarchies
		String itemJsonStr = json.toString();
		String postUrl = null;

		ResponseEntity<String> csResponse = null;
		if (itemJsonStr != null) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(itemJsonStr,
					headers);
			if (schema.equals("PUNI"))
				postUrl = prop.getProperty("puniCSUrl");
			else
				postUrl = prop.getProperty("pcenCSUrl");
			// send request and parse result
			System.out.println("posturl" + postUrl);
			csResponse = restTemplate.exchange(postUrl, HttpMethod.POST,
					entity, String.class);
			return csResponse;
		} else {
			return null;
		}

	}

	public ResponseEntity getItemInfoResponse(JSONObject json, String schema)
			throws Exception {
		// Sending request for itemHierarchies
		String itemJsonStr = json.toString();
		String postUrl = null;

		ResponseEntity<String> itemInfoResponse = null;
		if (itemJsonStr != null) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(itemJsonStr,
					headers);
			if (schema.equals("PUNI"))
				postUrl = prop.getProperty("puniItemUrl");
			else
				postUrl = prop.getProperty("pcenItemUrl");
			// send request and parse result
			System.out.println("posturl" + postUrl);
			itemInfoResponse = restTemplate.exchange(postUrl, HttpMethod.POST,
					entity, String.class);
			return itemInfoResponse;
		} else {
			return null;
		}

	}

	public ResponseEntity getItemHierResponse(JSONObject json, String schema) {
		// Sending request for item info
		ResponseEntity<String> itemHierResponse = null;
		String itemJsonStr = json.toString();
		String postUrl = null;
		if (itemJsonStr != null) {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(itemJsonStr,
					headers);

			if (schema.equals("PUNI"))
				postUrl = prop.getProperty("puniItemGroupUrl");
			else
				postUrl = prop.getProperty("pcenItemGroupUrl");
			// send request and parse result
			itemHierResponse = restTemplate.exchange(postUrl, HttpMethod.POST,
					entity, String.class);
			// String result = restTemplate.postForObject(SERVER_URI,
			// itemJsonStr, String.class);
			return itemHierResponse;
		} else {
			return null;
		}

	}

	public List<FileItem> parseItemInfoJson(String response) {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject(response.trim());
		JSONObject responseJsonObj = jsonObj.getJSONObject("response");
		JSONObject outputJSON = new JSONObject();
		JSONObject ancestorOutputJSON = null, parentOutputJson = null, childOutputJSON = null, actionOutputJSON = null;
		FileItem itemObj = null;
		List<FileItem> itemsList = new ArrayList();

		try {
			for (Iterator keys = responseJsonObj.keys(); keys.hasNext();) {
				// getting the action
				String action = (String) keys.next();

				JSONObject ancestorJSON = (JSONObject) responseJsonObj
						.getJSONObject(action);

				// getting the parent
				for (Iterator parents = ancestorJSON.keys(); parents.hasNext();) {

					String parentId = (String) parents.next();
					JSONObject parentJSON = (JSONObject) ancestorJSON
							.getJSONObject((String) parentId);

					ancestorOutputJSON = new JSONObject();
					childOutputJSON = new JSONObject();

					itemObj = new FileItem();
					itemObj.setParentId("");

					itemObj.setItemName(parentJSON.get("itemNo").toString());
					itemObj.setStatusLevel(parentJSON.getString("status_level"));
					itemObj.setStatusMessage(parentJSON
							.getString("status_message"));
					itemObj.setItemType(parentJSON.getString("itemType"));

					itemsList.add(itemObj);

				}

			}

		} catch (Exception ex) {
			System.out.println("exception" + ex.getMessage());
			return null;
		} finally {

		}
		return itemsList;
	}

	public List<FileItem> parseItemHierJSON(String response) {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject(response.trim());
		JSONObject responseJsonObj = jsonObj.getJSONObject("response");
		JSONObject outputJSON = new JSONObject();
		JSONObject ancestorOutputJSON = null, parentOutputJson = null, childOutputJSON = null, actionOutputJSON = null, parentJSON = null;
		FileItem itemObj = null;
		List<FileItem> itemsList = new ArrayList();

		try {
			for (Iterator keys = responseJsonObj.keys(); keys.hasNext();) {
				// getting the action
				String action = (String) keys.next();

				JSONObject actionJSON = (JSONObject) responseJsonObj
						.getJSONObject(action);

				// getting the parent
				for (Iterator parents = actionJSON.keys(); parents.hasNext();) {

					String parentId = (String) parents.next();
					parentJSON = actionJSON.getJSONObject(parentId);

					JSONObject ancestorJSON = parentJSON
							.getJSONObject("ANCESTORS");
					ancestorOutputJSON = new JSONObject();
					childOutputJSON = new JSONObject();
					for (Iterator childKey = ancestorJSON.keys(); childKey
							.hasNext();) {
						String childKeyValue = (String) childKey.next();
						JSONObject thisObj = ancestorJSON
								.getJSONObject(childKeyValue);
						itemObj = new FileItem();
						itemObj.setParentId(parentId);

						itemObj.setItemName(childKeyValue);
						itemObj.setStatusLevel(thisObj
								.getString("status_level"));
						itemObj.setStatusMessage(thisObj
								.getString("status_message"));
						itemObj.setItemType(thisObj.getString("itemType"));

						itemsList.add(itemObj);
					}

				}

			}

			// }
		} catch (Exception ex) {
			System.out.println("exception" + ex.getMessage());
			return null;
		} finally {

		}

		return itemsList;

	}
}
