package com.probosys.fileupload.model;

import java.util.Comparator;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileItem implements Comparable<FileItem> {

	private String createdBy;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + (int) (itemInfoId ^ (itemInfoId >>> 32));
		result = prime * result
				+ ((itemName == null) ? 0 : itemName.hashCode());
		result = prime * result + itemRegion;
		result = prime * result
				+ ((itemType == null) ? 0 : itemType.hashCode());
		result = prime * result
				+ ((languaggeCode == null) ? 0 : languaggeCode.hashCode());
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result
				+ ((statusDate == null) ? 0 : statusDate.hashCode());
		result = prime * result + (int) (statusId ^ (statusId >>> 32));
		result = prime * result
				+ ((statusLevel == null) ? 0 : statusLevel.hashCode());
		result = prime * result
				+ ((statusMessage == null) ? 0 : statusMessage.hashCode());
		result = prime * result
				+ ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result
				+ ((updatedBy == null) ? 0 : updatedBy.hashCode());
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
		FileItem other = (FileItem) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (itemInfoId != other.itemInfoId)
			return false;
		if (itemName == null) {
			if (other.itemName != null)
				return false;
		} else if (!itemName.equals(other.itemName))
			return false;
		if (itemRegion != other.itemRegion)
			return false;
		if (itemType == null) {
			if (other.itemType != null)
				return false;
		} else if (!itemType.equals(other.itemType))
			return false;
		if (languaggeCode == null) {
			if (other.languaggeCode != null)
				return false;
		} else if (!languaggeCode.equals(other.languaggeCode))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (statusDate == null) {
			if (other.statusDate != null)
				return false;
		} else if (!statusDate.equals(other.statusDate))
			return false;
		if (statusId != other.statusId)
			return false;
		if (statusLevel == null) {
			if (other.statusLevel != null)
				return false;
		} else if (!statusLevel.equals(other.statusLevel))
			return false;
		if (statusMessage == null) {
			if (other.statusMessage != null)
				return false;
		} else if (!statusMessage.equals(other.statusMessage))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		return true;
	}

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
	public int compareTo(FileItem obj) {
		// TODO Auto-generated method stub
		String compareQuantity = obj.getStatusLevel();
		
		//ascending order
		return this.statusLevel.compareTo(compareQuantity);
	}

	public static Comparator<FileItem> statusComparator = new Comparator<FileItem>() {

		public int compare(FileItem item1, FileItem item2) {
			// ascending order
			return item1.getStatusLevel().compareTo(item2.getStatusLevel());

		}

	};
}
