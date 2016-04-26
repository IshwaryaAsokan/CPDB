package com.probosys.fileupload.model;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class DataWrapper {
	
//	@SerializedName("PIM_SCHEMA")
	Map<String,Schema> schemas;

	public Map<String, Schema> getSchemas() {
		return schemas;
	}

	public void setSchemas(Map<String, Schema> schemas) {
		this.schemas = schemas;
	}
	
}
