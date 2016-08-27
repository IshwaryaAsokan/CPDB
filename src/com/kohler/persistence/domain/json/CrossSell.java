package com.kohler.persistence.domain.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CrossSell implements Jsonable {
	private String id;
	private String name;
	private List<Item> items;

	public CrossSell(String id, String name) {
		this.id = id;
		this.name = name;
		items = new ArrayList<Item>();
		items.add(new Item(id + "IT1", name + "ITEM1"));
		items.add(new Item(id + "IT2", name + "ITEM2"));

	}

	public CrossSell() {
		// TODO Auto-generated constructor stub
	}

	public void populate(JsonNode node) {
		try {
			ObjectNode oNode = (ObjectNode) node;
			ArrayNode itemsCont = Json.newArray();
			oNode.set(name, itemsCont);
			for (Item item : items) {
				item.populate(itemsCont);
			}
		} catch (Exception e) {
			System.out.println("CS exce " + e.getMessage());
		}
	}

	public void constructFrom(JsonNode node) {
		final ObjectNode oNode = (ObjectNode) node;
		items = new ArrayList<Item>();
		Iterable<String> names = new Iterable<String>() {
			public Iterator<String> iterator() {
				return oNode.fieldNames();
			}
		};
		for (String fName : names) {
			this.name = fName;
			ArrayNode aNode = (ArrayNode) oNode.get(fName);
			for (JsonNode jsonNode : aNode) {
				Item item = new Item();
				items.add(item);
				item.constructFrom(jsonNode);
			}
		}
		// TODO Auto-generated method stub

	}

	public boolean hasJsonContent() {
		return (items != null && items.size() > 0);
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}