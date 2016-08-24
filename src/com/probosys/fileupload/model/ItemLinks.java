package com.probosys.fileupload.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class ItemLinks  extends Object{
	
	//Item_Id in Item_Link
	private Long parentId;
	
	//Linked_Item_Id in Item_Link
	private Long itemId;
	
	private String itemLinkTypeId;
	
	private String createdBy;
	
	private String displayOrder;
	

	@SerializedName("child")
	private String itemLinkText;
	
	private String itemStyleId;
	
	@SerializedName("action")
	private String action;
	
	Date createdDate = new Date();
	
	private String updatedBy="null";
	
	public String parent;
	
	Date updatedDate = new Date();
	private String languageCode;
	List<ItemInfo> itemInfoList= new ArrayList<ItemInfo>();
	
	private String crossSellType;
	
	List<ItemLinkTypes> csTypesList = new ArrayList();
	
	Map<String, List<ItemInfo>> childMap = new HashMap<>();

	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
		public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemLinkTypeId() {
		return itemLinkTypeId;
	}
	public void setItemLinkTypeId(String itemLinkTypeId) {
		this.itemLinkTypeId = itemLinkTypeId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getItemLinkText() {
		return itemLinkText;
	}
	public void setItemLinkText(String itemLinkText) {
		this.itemLinkText = itemLinkText;
	}
	public String getItemStyleId() {
		return itemStyleId;
	}
	public void setItemStyleId(String itemStyleId) {
		this.itemStyleId = itemStyleId;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	
	public String getCrossSellType() {
		return crossSellType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setCrossSellType(String crossSellType) {
		this.crossSellType = crossSellType;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result
				+ ((crossSellType == null) ? 0 : crossSellType.hashCode());
		result = prime * result
				+ ((itemLinkText == null) ? 0 : itemLinkText.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemLinks other = (ItemLinks) obj;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (crossSellType == null) {
			if (other.crossSellType != null)
				return false;
		} else if (!crossSellType.equals(other.crossSellType))
			return false;
		if (itemLinkText == null) {
			if (other.itemLinkText != null)
				return false;
		} else if (!itemLinkText.equals(other.itemLinkText))
			return false;
				return true;
	}
	
	public void setCsTypesList(List<ItemLinkTypes> csTypesList) {
		this.csTypesList = csTypesList;
	}
	
	public List<ItemLinkTypes> getCsTypesList() {
		return csTypesList;
	}
	
	
}
