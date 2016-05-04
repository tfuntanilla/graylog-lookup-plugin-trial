package com.cisco.clip.lookup;

import java.util.Map;

import org.graylog2.plugin.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LookupService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LookupService.class);

	/**
	 * Look up method that can be executed from outside the LookupComponent class (e.g. within a rules file)
	 * @param msg
	 * @param existingField - the field which has some values mapped to another field value in the data map (data map key)
	 * @param newFieldToAdd - the field to add when a mapping is found (it's value is the data map value)
	 */
	public static void lookup(Message msg, String existingField, String newFieldToAdd) {

		LOG.info("Performing lookup...");
		Map<String, String> lookupDataMap = LookupComponent.dataMap;
		String valueOfExistingField = (String) msg.getField(existingField);
		if (lookupDataMap.containsKey(valueOfExistingField)) {
			String valueForNewField = lookupDataMap.get(valueOfExistingField);
			msg.addField(newFieldToAdd, valueForNewField);
			LOG.debug(newFieldToAdd + " : " + valueForNewField);
		}

	}

}

