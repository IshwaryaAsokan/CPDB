package com.probosys.fileupload.model;

import java.util.List;
import java.util.Map;

public class Action {
	// @SerializedName("PARENT_ITEM_NO")
	private Map<String, Parent> parents;

	public Map<String, Parent> getParents() {
		return parents;
	}

	public void setParents(Map<String, Parent> parents) {
		this.parents = parents;
	}

}
