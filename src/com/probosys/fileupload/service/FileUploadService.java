package com.probosys.fileupload.service;

import java.io.IOException;
import java.io.InputStream;
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

import org.json.JSONObject;
import org.primefaces.model.UploadedFile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.probosys.fileupload.model.Action;
import com.probosys.fileupload.model.DataWrapper;
import com.probosys.fileupload.model.Item;
import com.probosys.fileupload.model.Parent;
import com.probosys.fileupload.model.PimPojo;
import com.probosys.fileupload.model.Schema;

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
	
	public FileUploadService(){
		try {
			input = getClass().getClassLoader().getResourceAsStream("pim.properties");
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error when loading properties file"+e.getMessage());
		}
	}


	public Map<String, JSONObject> getCSRequestJsonMap(List<PimPojo> inputFileList) {

		JSONObject requestJsonCSItemInfo = new JSONObject();	
		
		// The records from excel have a hierarchy of Actions -> Parents -> CrossSellingType -> Children. Sorting below based on this order
		Comparator<PimPojo> groupByComparator = Comparator.comparing(PimPojo::getAction).thenComparing(PimPojo::getParent)
				.thenComparing(PimPojo::getCrossSellType).thenComparing(PimPojo::getCrossSellChild);
	
		inputFileList.sort(groupByComparator);

		// Converting list to a stream of model classes allow using collectors 
		Stream<PimPojo> records = inputFileList.stream();

		// Collectors groupingby generates a complex nested map based on the provided JSON schema
		Map<String, Map<String, Map<String, List<String>>>> map = records.collect(Collectors.groupingBy(PimPojo::getAction, Collectors
						.groupingBy(PimPojo::getParent, Collectors.groupingBy(PimPojo::getCrossSellType, 
								Collectors.mapping(PimPojo::childValue,Collectors.toList())))));
		
					
		// Using Gson, the java map is made as a json supported map to add to request object
		Map<String,Object> actions = new Gson().fromJson(new JSONObject(map).toString(), HashMap.class); 
		
		// Build request json for all actions A,M,D
		// 3 action maps are appended to json obj - requestJsonCSItemInfo
		for(Entry<String, Object> itr : actions.entrySet()){
			requestJsonCSItemInfo.append("request",(new JSONObject().accumulate(itr.getKey(), actions.get(itr.getKey()))));
		}
		
		// Place requests in a map 	// TO DO need to check real need of a map here
		Map<String, JSONObject> requestJsonMap = new HashMap<String, JSONObject>();
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

		Map<String, Parent> actionMap = new HashMap<>();
		Parent parent = new Parent();
		parentJsonItemInfo = new JSONObject();
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
			parent.setPimPojo(parentMap.getValue());
			actionMap.put(parentMap.getKey(), parent);
			if (parentKey != "NULL") {
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

		action.setParents(actionMap);

		Schema schema = new Schema();
		Map<String, Action> schemaMap = new HashMap<>();
		schemaMap.put("M", action);
		schema.setActions(schemaMap);

		DataWrapper datWrapper = new DataWrapper();
		Map<String, Schema> datWrapMap = new HashMap<>();
		datWrapMap.put(schemaName, schema);
		datWrapper.setSchemas(datWrapMap);

		Map<String, JSONObject> requestJsonMap = new HashMap<String, JSONObject>();
		requestJsonMap.put("itemInfoJson", requestJsonItemInfo);
		requestJsonMap.put("itemHierJson", requestJsonItemHier);

		return requestJsonMap;
	}

	public ResponseEntity getCSResponse(JSONObject json,String schema) throws Exception {
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
			if(schema.equals("PUNI"))
				postUrl = prop.getProperty("puniItemUrl"); 
			else 
				postUrl = prop.getProperty("pcenItemUrl"); 
			// send request and parse result
			System.out.println ("posturl"+postUrl);
			csResponse = restTemplate.exchange(postUrl,
					HttpMethod.POST, entity, String.class);
			return csResponse;
		} else {
			return null;
		}

	}
	
	public ResponseEntity getItemInfoResponse(JSONObject json,String schema) throws Exception {
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
			if(schema.equals("PUNI"))
				postUrl = prop.getProperty("puniItemUrl"); 
			else 
				postUrl = prop.getProperty("pcenItemUrl"); 
			// send request and parse result
			System.out.println ("posturl"+postUrl);
			itemInfoResponse = restTemplate.exchange(postUrl,
					HttpMethod.POST, entity, String.class);
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
		
			if(schema.equals("PUNI"))
				postUrl = prop.getProperty("puniItemGroupUrl"); 
			else 
				postUrl = prop.getProperty("pcenItemGroupUrl"); 
			// send request and parse result
			itemHierResponse = restTemplate.exchange(postUrl,
					HttpMethod.POST, entity, String.class);
			// String result = restTemplate.postForObject(SERVER_URI,
			// itemJsonStr, String.class);
			return itemHierResponse;
		} else {
			return null;
		}

	}

	public List<Item> parseItemInfoJson(String response) {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject(response.trim());
		JSONObject responseJsonObj = jsonObj.getJSONObject("response");
		JSONObject outputJSON = new JSONObject();
		JSONObject ancestorOutputJSON = null, parentOutputJson = null, childOutputJSON = null, actionOutputJSON = null;
		Item itemObj = null;
		List<Item> itemsList = new ArrayList();

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

					itemObj = new Item();
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

	public List<Item> parseItemHierJSON(String response) {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject(response.trim());
		JSONObject responseJsonObj = jsonObj.getJSONObject("response");
		JSONObject outputJSON = new JSONObject();
		JSONObject ancestorOutputJSON = null, parentOutputJson = null, childOutputJSON = null, actionOutputJSON = null, parentJSON = null;
		Item itemObj = null;
		List<Item> itemsList = new ArrayList();

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
						itemObj = new Item();
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
