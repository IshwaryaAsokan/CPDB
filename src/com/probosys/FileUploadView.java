package com.probosys;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;
import org.primefaces.model.UploadedFile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.probosys.file.utils.FileUtils;
import com.probosys.fileupload.model.Action;
import com.probosys.fileupload.model.DataWrapper;
import com.probosys.fileupload.model.Item;
import com.probosys.fileupload.model.Parent;
import com.probosys.fileupload.model.PimPojo;
import com.probosys.fileupload.model.Schema;
import com.probosys.fileupload.service.FileUploadService;

@ManagedBean
public class FileUploadView {

	private UploadedFile file;

	private String schemaName;
	
	private String jsonValue;
	
	private List<Item> items =  new ArrayList<Item>();
	
	@PostConstruct
	public void init() {
		items=	getSessionMap().get("itemsValue") != null ? (List<Item>) getSessionMap().get("itemsValue") :  new ArrayList<Item>();
	}

	public void upload() {
		FacesMessage message = null;
		try {
			if (file != null) {
				message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
				// FacesContext.getCurrentInstance().addMessage(null, message);
				List<PimPojo> inpItems = new ArrayList<PimPojo>();
				/*if(! "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(file.getContents())){
					throw new RuntimeException("Invalid File Type. Only xlxs file are allowed for uploading.");
				}*/
				
				items= new ArrayList<Item>();
				
				inpItems = FileUtils.readXslsFile(file.getInputstream());
				
				if (inpItems != null) {
					FileUploadService fileUploadService = new FileUploadService();
					//Parse the json and convert to items list
					Map<String,JSONObject> requestJsons = fileUploadService.getRequestJsonMap(inpItems);
					
					// Sending webservice calls to itemhierarchy and iteminfo
					ResponseEntity<?> response = fileUploadService.getItemHierResponse(requestJsons.get("itemHierJson"), schemaName);
					if ((response.getStatusCode().toString()).equals("200")) {
						items.addAll(fileUploadService.parseItemHierJSON(String.valueOf(response.getBody())));
					} else {
						throw new HttpClientErrorException(response.getStatusCode(), (String) response.getBody());
					}
					ResponseEntity<?> response2 = fileUploadService.getItemInfoResponse(requestJsons.get("itemInfoJson"), schemaName);
					if ((response2.getStatusCode().toString()).equals("200")) {
					items.addAll(fileUploadService.parseItemInfoJson(String.valueOf(response2.getBody())));
					} else {
						throw new HttpClientErrorException(response2.getStatusCode(), (String) response2.getBody());
					}
					jsonValue =(String) response.toString()+ (String) response2.toString();
					Collections.sort(items, Item.statusComparator);
					
					getSessionMap().put("itemsValue", items);
					
					
					
				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "FileUpload Error",
							"Empty Response from Rest Server.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "FileUpload Error", "Kindly Upload File");
			}
		} catch (IOException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "FileUpload Error", "File Processing Error"+e.getMessage());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "FileUpload Error", "Unable to connect to Rest Server"+e.getMessage());
		} catch(RuntimeException re){
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "FileUpload Error", re.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loggin Error", "Unable to connect to Rest Server");
		}
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getJsonValue() {
		return jsonValue;
	}

	public void setJsonValue(String jsonValue) {
		this.jsonValue = jsonValue;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	private Map<String, Object> getSessionMap(){
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		  Map<String, Object> sessionMap = externalContext.getSessionMap();
		  return sessionMap;
	}
	
	

}