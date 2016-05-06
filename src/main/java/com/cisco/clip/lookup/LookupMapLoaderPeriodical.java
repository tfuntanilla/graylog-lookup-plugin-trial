package com.cisco.clip.lookup;

import java.util.Map;

import javax.inject.Inject;

import org.graylog2.database.MongoConnection;
import org.graylog2.plugin.periodical.Periodical;
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
 * Loads the lookup data map from MongoDB periodically.
 * 
 * @author tfuntani
 *
 */
public class LookupMapLoaderPeriodical extends Periodical {

	private static final Logger LOG = LoggerFactory.getLogger(LookupMapLoaderPeriodical.class);
	
	private MongoConnection mongoConnection;
	private static final String LOOKUP = "lookup";
	
	@Inject
	public LookupMapLoaderPeriodical(MongoConnection mongoConnection) {
		this.mongoConnection = mongoConnection;
	}

	@Override
	public synchronized void doRun() {
		
		LOG.info("Retrieving mongo collection for map reloading...");
		
		final MongoConnection mc = mongoConnection;
		
		// retrieve lookup data map from MongoDB
		DBCollection collection = mc.getDatabase().getCollection(LOOKUP);
		DBCursor cursor = collection.find();
		DBObject doc = cursor.one();
				
		JsonArray mappings = new JsonArray();
		JsonParser parser = new JsonParser();
		JsonElement je = parser.parse(doc.toString());
		JsonObject json = je.getAsJsonObject();
		mappings = json.getAsJsonArray("mappings");

		JsonElement mappingsObject = mappings.get(0);
		JsonObject map = mappingsObject.getAsJsonObject();
		
		LOG.info("Reloading lookup data map...");
		
		// clear lookup data map and reload mappings
		LookupComponent.dataMap.clear();
		for (Map.Entry<String, JsonElement> entry : map.entrySet()) {
			LookupComponent.dataMap.put(entry.getKey(), entry.getValue().getAsString());
		}
		
		LOG.info("Reloading complete.");
		
	}

	@Override
	public int getInitialDelaySeconds() {
		// first run is after 15 min of start up
		return 600;
	}

	@Override
	protected Logger getLogger() {
		return LOG;
	}

	@Override
	public int getPeriodSeconds() {
		// run every 8 hr
		return 28800;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public boolean masterOnly() {
		return false;
	}

	@Override
	public boolean runsForever() {
		// run periodically
		return false;
	}

	@Override
	public boolean startOnThisNode() {
		return false;
	}

	@Override
	public boolean stopOnGracefulShutdown() {
		return false;
	}

}
