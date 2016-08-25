package com.kohler.persistence.domain;

import java.util.Date;

public class ItemInfo {

	Long itemId;//(PK)
	// Move them to one-one association from item groups POJO
	//int itemInfoId;
	//int parentId; 
	 
	private String itemName;
	private String eventId = "0";
	private String statusId;
	private String itemRegionId = "6";
	private Date statusDate = new Date();
	private String itemKind;
	private String defaultItemStyleId;
	private String keepWith;
	private String priority;
	private String syncGroupId;
	private String displayOrder;
	private String displayText;
	private String createdBy;
	private Date createdDate = new Date();
	private String updatedBy;
	private Date updatedDate = new Date();
	private String languageCode ="EN";
	//private ItemGroups itemGroupsMap;
	
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getItemRegionId() {
		return itemRegionId;
	}
	public void setItemRegionId(String itemRegionId) {
		this.itemRegionId = itemRegionId;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}
	public String getItemKind() {
		return itemKind;
	}
	public void setItemKind(String itemKind) {
		this.itemKind = itemKind;
	}
	public String getDefaultItemStyleId() {
		return defaultItemStyleId;
	}
	public void setDefaultItemStyleId(String defaultItemStyleId) {
		this.defaultItemStyleId = defaultItemStyleId;
	}
	public String getKeepWith() {
		return keepWith;
	}
	public void setKeepWith(String keepWith) {
		this.keepWith = keepWith;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getSyncGroupId() {
		return syncGroupId;
	}
	public void setSyncGroupId(String syncGroupId) {
		this.syncGroupId = syncGroupId;
	}
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getDisplayText() {
		return displayText;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	/*public ItemGroups getItemGroupsMap() {
		return itemGroupsMap;
	}
	public void setItemGroupMap(ItemGroups itemGroupsMap) {
		this.itemGroupsMap = itemGroupsMap;
	}*/
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
}
