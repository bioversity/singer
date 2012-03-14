package org.sgrp.singer;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ResourceManager {
	private static ResourceBundle			bundle		= ResourceBundle.getBundle("ApplicationResources");

	private static HashMap<String, String>	bundleMap	= new HashMap<String, String>();
	static {
		Enumeration<String> enumStr = bundle.getKeys();
		while (enumStr.hasMoreElements()) {
			String enumName = enumStr.nextElement();
			String enumValue = bundle.getString(enumName);
			bundleMap.put(enumName, enumValue);
		}

	}

	public static String getString(String key) {
		if (bundleMap.containsKey(key)) {
			return bundleMap.get(key);
		} else {
			return PropertiesManager.getString(key);
		}
	}
}
