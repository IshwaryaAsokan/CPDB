package com.kohler.persistence.domain.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Item implements Jsonable {
	private String id;
	private String name;

	public Item() {
	}
 
	public Item(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public void populate(JsonNode node) {
		ArrayNode aNode = (ArrayNode) node;
		aNode.add(name);
	}

	public void constructFrom(JsonNode node) {
		name = node.asText();
	}

	public boolean hasJsonContent() {
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
