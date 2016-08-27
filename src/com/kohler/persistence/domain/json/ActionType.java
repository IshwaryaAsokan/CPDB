package com.kohler.persistence.domain.json;

public enum ActionType {

	ADD {
		public String getAction() {
			return "ADD";
		}
	},
	MODIFY {
		public String getAction() {
			return "MODIFY";
		}
	},
	DELETE {
		public String getAction() {
			return "DELETE";
		}
	};
	public abstract String getAction();

	public static ActionType getType(String action) {
		ActionType actionType = ADD;
		if ("M".equalsIgnoreCase(action)) {
			actionType = MODIFY;
		}
		if ("D".equalsIgnoreCase(action)) {
			actionType = DELETE;
		}
		if ("A".equalsIgnoreCase(action)) {
			actionType = ADD;
		}
		return actionType;
	}

}
