package com.kohler.persistence.domain.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Action implements Jsonable {
	private ActionType action;
	private List<Product> products;

	public Action(ActionType action) {
		this.action = action;
		products = new ArrayList<Product>();
		/*
		 * products.add(new Product("P1", "SAMPLE1")); products.add(new
		 * Product("P2", "SAMPLE2"));
		 */}

	public Action() {
		// TODO Auto-generated constructor stub
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void populate(JsonNode node) {

		try {
			ObjectNode oNode = (ObjectNode) node;

			ObjectNode prods = Json.newObject();
			oNode.set(action.getAction(), prods);
			for (Product product : products) {
				product.populate(prods);
			}
		} catch (Exception e) {
			System.out.println("Äction Exce" + e.getMessage());
		}

	}

	public void constructFrom(JsonNode node) {
		final ObjectNode mainNode = (ObjectNode) node;
		Iterable<String> actNames = new Iterable<String>() {
			public Iterator<String> iterator() {
				return mainNode.fieldNames();
			}
		};
		for (String aName : actNames) {
			this.action = ActionType.getType(aName);
			final ObjectNode csNode = (ObjectNode) mainNode.get(aName);
			products = new ArrayList<Product>();
			Iterable<String> names = new Iterable<String>() {
				public Iterator<String> iterator() {
					return csNode.fieldNames();
				}
			};
			for (String pName : names) {
				ObjectNode aNode = (ObjectNode) csNode.get(pName);
				Product product = new Product();
				products.add(product);
				ObjectNode newPNode = Json.newObject();
				newPNode.set(pName, aNode);
				product.constructFrom(newPNode);
			}

		}

	}

	public boolean hasJsonContent() {
		// TODO Auto-generated method stub
		return false;
	}

	public ActionType getAction() {
		return action;
	}

	public void setAction(ActionType action) {
		this.action = action;
	}

}
