package com.cisco.clip.lookup;

import java.util.Map;

import org.graylog2.plugin.Message;

public class LookupService {

	public static void lookup(Message msg, String fieldToLookup, String newField) {

		System.out.println("Performing lookup...");
		Map<String, String> lookupDataMap = LookupComponent.dataMap;
		if (lookupDataMap.containsKey(fieldToLookup)) {
			msg.addField(newField, lookupDataMap.get(fieldToLookup));
		}

	}

}
