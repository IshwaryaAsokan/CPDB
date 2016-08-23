package com.probosys.fileupload.model;

import javax.xml.bind.annotation.XmlRootElement;
import com.google.gson.annotations.SerializedName;

@XmlRootElement
public class PimPojo {
	
	private String parent;
	
	@SerializedName("child")
	private String child;

	@SerializedName("status_id")
	private long statusId;

	@SerializedName("item_type")
	private String itemType;

	@SerializedName("cs_type")
	private String csType;


	@SerializedName("cs_child")
	private String csChild;

	@SerializedName("action")
	private String action;

	
	public PimPojo() {
		super();
	}

	public PimPojo(String parent, String child, long statusId, String itemType) {
		super();
		this.parent = parent;
		this.child = child;
		this.statusId = statusId;
		this.itemType = itemType;
	}

	public PimPojo(String parent, String child, String csType, String action){
		super();
		this.parent = parent;
		this.child = child;
		this.csType = csType;
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	public String getCrossSellChild() {
		return csChild;
	}

	public void setCrossSellChild(String csChild) {
		this.csChild = csChild;
	}

	public String getCrossSellType() {
		return csType;
	}

	public void setCrossSellType(String csType) {
		this.csType = csType;
	}

	
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String childValue(){
		return getCrossSellChild().toString();
	}
	
}
