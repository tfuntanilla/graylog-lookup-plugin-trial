package com.cisco.clip.lookup;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.graylog2.database.MongoConnection;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.filters.MessageFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * This is the plugin. Your class should implement one of the existing plugin
 * interfaces. (i.e. AlarmCallback, MessageInput, MessageOutput)
 */
public class LookupComponent implements MessageFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(LookupComponent.class);
	
	private static final String LOOKUP = "lookup";
	public static Map<String, String> dataMap = new HashMap<String, String>();
	
	@Inject
	public LookupComponent(MongoConnection mongoConnection) {
		
		LOG.info("Starting lookup component...");
		
		// retrieve lookup data map from MongoDB
		DBCollection collection = mongoConnection.getDatabase().getCollection(LOOKUP);
		DBCursor cursor = collection.find();
		DBObject doc = cursor.one();	// get first document
				
		JsonArray mappings = new JsonArray();
		JsonParser parser = new JsonParser();
		JsonElement je = parser.parse(doc.toString());
		JsonObject json = je.getAsJsonObject();
		mappings = json.getAsJsonArray("mappings");	// get mappings array
		LOG.info("Retrieved lookup data map: " + mappings.toString());

		JsonElement mappingsObject = mappings.get(0);	// get first object in array
		JsonObject map = mappingsObject.getAsJsonObject();
		
		// put the mapping into the data map
		LOG.info("Populating data map for the first time...");
		for (Map.Entry<String, JsonElement> entry : map.entrySet()) {
		   dataMap.put(entry.getKey(), entry.getValue().getAsString());
		}	
		
	}

	@Override
	public boolean filter(Message msg) {
		
		LOG.info("Performing lookup...");
		Map<String, String> lookupDataMap = LookupComponent.dataMap;
		String valueOfExistingField = (String) msg.getField("uuid");
		if (lookupDataMap.containsKey(valueOfExistingField)) {
			String valueForNewField = lookupDataMap.get(valueOfExistingField);
			msg.addField("appurl", valueForNewField);
			LOG.info("appurl" + " : " + valueForNewField);
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "LookupComponent";
	}

	@Override
	public int getPriority() {
		// run this filter first
		return 1;
	}
	
}
