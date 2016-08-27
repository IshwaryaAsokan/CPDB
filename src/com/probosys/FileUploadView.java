package com.probosys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.json.JSONObject;
import org.primefaces.model.UploadedFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.probosys.file.utils.FileUtils;
import com.kohler.persistence.domain.json.Item;
import com.probosys.fileupload.model.FileItem;
import com.probosys.fileupload.model.PimPojo;
import com.probosys.fileupload.service.FileUploadService;

@ManagedBean
public class FileUploadView {

	private UploadedFile file;

	private String schemaName;

	private String jsonValue;

	private String uploadType;

	private List<FileItem> items = new ArrayList<FileItem>();

	@PostConstruct
	public void init() {
		items = getSessionMap().get("itemsValue") != null ? (List<FileItem>) getSessionMap()
				.get("itemsValue") : new ArrayList<FileItem>();
	}

	public void upload() {
		FacesMessage message = null;
		try {
			if (file != null) {
				message = new FacesMessage("Succesful", file.getFileName()
						+ " is uploaded.");
				List<PimPojo> inpItems = new ArrayList<PimPojo>();
								items = new ArrayList<FileItem>();
				ResponseEntity<?> csResponse = null;
				FileUploadService fileUploadService = new FileUploadService();
				ResponseEntity<?> response2 = null;

				if (uploadType.equals("C"))
					inpItems = FileUtils.readCSXlFile(file.getInputstream());
				else
					inpItems = FileUtils.readXslsFile(file.getInputstream());

				if (inpItems != null) {
					// Parse the json and convert to items list

					if (uploadType.equals("P")) {//Parse the json and convert to items list
						Map<String,JSONObject> requestJsons = fileUploadService.getRequestJsonMap(inpItems);
						
						// Sending webservice calls to itemhierarchy and iteminfo
						ResponseEntity<?> response = fileUploadService.getItemHierResponse(requestJsons.get("itemHierJson"), schemaName);
						if ((response.getStatusCode().toString()).equals("200")) {
							items.addAll(fileUploadService.parseItemHierJSON(String.valueOf(response.getBody())));
						} else {
							throw new HttpClientErrorException(response.getStatusCode(), (String) response.getBody());
						}
						response2 = fileUploadService.getItemInfoResponse(requestJsons.get("itemInfoJson"), schemaName);
						if ((response2.getStatusCode().toString()).equals("200")) {
						items.addAll(fileUploadService.parseItemInfoJson(String.valueOf(response2.getBody())));
						} else {
							throw new HttpClientErrorException(response2.getStatusCode(), (String) response2.getBody());
						}
							if ((response2.getStatusCode().toString())
								.equals("200")) {
							items.addAll(fileUploadService
									.parseItemInfoJson(String.valueOf(response2
											.getBody())));
						} else {
							throw new HttpClientErrorException(
									response2.getStatusCode(),
									(String) response2.getBody());
						}
					} else {
						Map<String, JSONObject> requestJsons = fileUploadService
								.getCSRequestJsonMap(inpItems);
						
						csResponse = fileUploadService .getCSResponse(requestJsons.get("csItemInfoJson"), schemaName);
			
					}
					jsonValue = (String) csResponse.toString();
					Collections.sort(items, FileItem.statusComparator);

					getSessionMap().put("itemsValue", items);

				} else {
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"FileUpload Error",
							"Empty Response from Rest Server.");
				}
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"FileUpload Error", "Kindly Upload File");
			}
		} catch (IOException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"FileUpload Error", "File Processing Error"
							+ e.getMessage());
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"FileUpload Error", "Unable to connect to Rest Server"
							+ e.getMessage());
		} catch (RuntimeException re) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"FileUpload Error", re.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Loggin Error", "Unable to connect to Rest Server");
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

	public List<FileItem> getItems() {
		return items;
	}

	public void setItems(List<FileItem> items) {
		this.items = items;
	}
	

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	private Map<String, Object> getSessionMap() {
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		return sessionMap;
	}

}