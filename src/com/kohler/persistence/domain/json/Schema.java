package com.kohler.persistence.domain.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.kohler.persistence.domain.json.Action;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class Schema implements Jsonable {
	private String id;
	private String name;
	private List<Action> actions;

	public Schema(String schemaName) {
		this.name = schemaName;
		actions = new ArrayList<Action>();
		/*
		 * actions.add(new Action(ActionType.ADD)); actions.add(new
		 * Action(ActionType.DELETE)); actions.add(new
		 * Action(ActionType.MODIFY));
		 */}

	public Schema() {
		// TODO Auto-generated constructor stub
	}

	public void populate(JsonNode node) {
		try {
			ObjectNode oNode = (ObjectNode) node;
			ObjectNode schemaContainer = Json.newObject();
			oNode.set(name, schemaContainer);
			for (Action action : actions) {
				action.populate(schemaContainer);
			}
		} catch (Exception e) {
			System.out.println("Exception populate >>> " + e.getMessage());
		}
	}

	public void constructFrom(JsonNode node) {
		final ObjectNode mainNode = (ObjectNode) node;
		Iterable<String> schemaNames = new Iterable<String>() {
			public Iterator<String> iterator() {
				return mainNode.fieldNames();
			}
		};
		actions = new ArrayList<Action>();
		for (String schemaName : schemaNames) {
			this.name = schemaName;
			actions = new ArrayList<Action>();
			final ObjectNode schNode = (ObjectNode) mainNode.get(schemaName);
			Iterable<String> actNames = new Iterable<String>() {
				public Iterator<String> iterator() {
					return schNode.fieldNames();
				}
			};
			for (String aName : actNames) {
				ObjectNode actNode = (ObjectNode) schNode.get(aName);
				Action action = new Action();
				actions.add(action);
				ObjectNode jsonNode = Json.newObject();
				jsonNode.set(aName, actNode);
				action.constructFrom(jsonNode);
			}

		}

	}

	public boolean hasJsonContent() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

}
