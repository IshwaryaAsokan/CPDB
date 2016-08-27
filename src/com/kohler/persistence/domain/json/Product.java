package com.kohler.persistence.domain.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Product implements Jsonable {
	private String id;
	private String name;
	private List<CrossSell> list;

	public Product(String id, String name) {
		this.id = id;
		this.name = name;
		/*
		 * list = new ArrayList<CrossSell>(); list.add(new CrossSell(id + "1",
		 * name + "CS1")); list.add(new CrossSell(id + "2", name + "CS2"));
		 */}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public void populate(JsonNode node) {
		try {
			ObjectNode oNode = (ObjectNode) node;
			ObjectNode csContainer = Json.newObject();
			ObjectNode cs = Json.newObject();

			oNode.set(name, csContainer);

			csContainer.set("crossSelling", cs);
			for (CrossSell crossSell : list) {
				crossSell.populate(cs);
			}
		} catch (Exception e) {
			System.out.println("Prod " + e.getMessage());
		}
	}

	public boolean hasJsonContent() {
		return (list != null && list.size() > 0);
	}

	public void constructFrom(JsonNode node) {
		final ObjectNode mainNode = (ObjectNode) node;

		Iterable<String> prodNames = new Iterable<String>() {
			public Iterator<String> iterator() {
				return mainNode.fieldNames();
			}
		};
		for (String pName : prodNames) {
			this.name = pName;
			final ObjectNode oNode = (ObjectNode) mainNode.get(name);
			final ObjectNode csNode = (ObjectNode) oNode.get("crossSelling");
			list = new ArrayList<CrossSell>();
			Iterable<String> names = new Iterable<String>() {
				public Iterator<String> iterator() {
					return csNode.fieldNames();
				}
			};
			for (String fName : names) {
				ArrayNode aNode = (ArrayNode) csNode.get(fName);
				CrossSell crossSell = new CrossSell();
				list.add(crossSell);
				ObjectNode newPNode = Json.newObject();
				newPNode.set(fName, aNode);
				crossSell.constructFrom(newPNode);
			}

		}

	}

	public List<CrossSell> getList() {
		return list;
	}

	public void setList(List<CrossSell> list) {
		this.list = list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
