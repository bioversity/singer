package org.sgrp.singer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
	protected static final Properties	prop	= new Properties();

	public static String getString(String key) {
		return prop.getProperty(key);
	}
	
	public static void setString(String key, String value)
	{
		prop.setProperty(key, value);
	}

	public static void loadProperties(String fileName) {
		BufferedInputStream bis = null;
		try {
			System.out.println("Loading properties from :" + fileName);
			File file = new File(fileName);
			bis = new BufferedInputStream(new FileInputStream(file));
			prop.load(bis);
			System.out.println("Finished Loading properties from :" + fileName);
		} catch (Exception e) {
			System.out.println("Exception found" + e.toString());
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
