package com.probosys.fileupload.model;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class Schema {

//	@SerializedName("ACTION")
	private Map<String, Action> actions;

	public Map<String, Action> getActions() {
		return actions;
	}

	public void setActions(Map<String, Action> actions) {
		this.actions = actions;
	}

}
