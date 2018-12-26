package com.sumslack.compile.dyna;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticValue {
	private static final Logger LOG = LoggerFactory.getLogger(StaticValue.class);
	public static final String PREFIX = "sumslack_";
	public static final String HOME = getValueOrCreate("home", new File(System.getProperty("user.home"), ".sumslack").getAbsolutePath());
	
	
	public static String getValueOrCreate(String key, String def) {
		String value = System.getProperty(PREFIX + key);
		if (LOG.isDebugEnabled()) {
			LOG.debug("get property " + key + ":" + value);
		}
		if (value == null) {
			if (def != null) {
				System.setProperty(PREFIX + key, def);
			}
			return def;
		} else {
			return value;
		}
	}
}
