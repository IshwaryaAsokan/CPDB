package com.probosys.fileupload.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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

}
