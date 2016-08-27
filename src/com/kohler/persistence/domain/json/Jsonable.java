package com.kohler.persistence.domain.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface Jsonable {


	public void populate(JsonNode node);

	public void constructFrom(JsonNode node);

	public boolean hasJsonContent();
 

} 
