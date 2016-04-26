package com.probosys.fileupload.model;

import java.util.Comparator;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item implements Comparable<Item> {

	private String createdBy;
	private Date createdDate;
	private String itemName;
	private long itemInfoId;
	private int itemRegion;
	private String itemType;
	private Date statusDate;
	private long statusId;
	private Date timeStamp;
	private String updatedBy;
	private String languaggeCode;
	private String statusLevel;
	private String statusMessage;
	private String parentId;

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

	public long getItemInfoId() {
		return itemInfoId;
	}

	public void setItemInfoId(long itemInfoId) {
		this.itemInfoId = itemInfoId;
	}

	public int getItemRegion() {
		return itemRegion;
	}

	public void setItemRegion(int itemRegion) {
		this.itemRegion = itemRegion;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getLanguaggeCode() {
		return languaggeCode;
	}

	public void setLanguaggeCode(String languaggeCode) {
		this.languaggeCode = languaggeCode;
	}

	public String getStatusLevel() {
		return statusLevel;
	}

	public void setStatusLevel(String statusLevel) {
		this.statusLevel = statusLevel;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	@Override
	public int compareTo(Item obj) {
		// TODO Auto-generated method stub
		String compareQuantity = obj.getStatusLevel();
		
		//ascending order
		return this.statusLevel.compareTo(compareQuantity);
	}

	public static Comparator<Item> statusComparator = new Comparator<Item>() {

		public int compare(Item item1, Item item2) {
			// ascending order
			return item1.getStatusLevel().compareTo(item2.getStatusLevel());

		}

	};
}
