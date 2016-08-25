package com.kohler.persistence.domain;


import java.util.Date;
import java.util.List;

public class ItemLinkTypes {

	private String itemLinkTypeId;
	private String itemLinkTypeUc;
	private String itemLinkType;	
	private Date createdDate;	 
	private String updatedBy;
	private Date updatedDate;
	private String languageCode;
	public List<ItemInfo> itemInfoList;
	
	public String getItemLinkTypeId() {
		return itemLinkTypeId;
	}
	public void setItemLinkTypeId(String itemLinkTypeId) {
		this.itemLinkTypeId = itemLinkTypeId;
	}
	public String getItemLinkTypeUc() {
		return itemLinkTypeUc;
	}
	public void setItemLinkTypeUc(String itemLinkTypeUc) {
		this.itemLinkTypeUc = itemLinkTypeUc;
	}
	public String getItemLinkType() {
		return itemLinkType;
	}
	public void setItemLinkType(String itemLinkType) {
		this.itemLinkType = itemLinkType;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public List<ItemInfo> getItemInfoList() {
		return itemInfoList;
	}
	public void setItemInfoList(List<ItemInfo> itemInfoList) {
		this.itemInfoList = itemInfoList;
	}
}
