/**
 * 
 */
package org.sgrp.singer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;

import org.sgrp.singer.indexer.BaseIndexer;
import org.sgrp.singer.utils.XStreamUtils;

/**
 * @author kviparthi
 */
public class ObjectStore {

	
	
	public static File getFile(String type, String fileName) {
		
		String rootDir;
		
		/*Added By Gautier for SearchHistory save*/
		if(type != null && type.trim().length()>0 && type.equals(AccessionConstants.SEARCHHISTORY))
		{
			rootDir = AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.USER_DATA_ROOT), ".");
		}/*End added By Gautier*/
		else
		{
			rootDir = AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.CACHE_ROOT), ".");
		}
		
		File f = new File(rootDir);
		if (!f.exists()) {
			f.mkdirs();
		}
		
		
		if (type != null && type.trim().length() > 0) {
			File typeFile = new File(rootDir, type);
			if (!typeFile.exists()) {
				typeFile.mkdirs();
			}
			f = typeFile;
		}
		File file = new File(f, fileName + ".ser");
		return file;
	}

	public static String getMarkerXMLString(String type, String filename) {
		String xml = null;System.out.println("File"+filename);
		String md5 =getMD5(filename);
		
		File file = getFile(type,md5);
		if (file.exists()) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(file));
				StringBuffer sb = new StringBuffer();
				String str;
				while ((str = in.readLine()) != null) {
					sb.append(str);
				}
				xml = sb.toString();
				in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}System.out.println("XMLFile: "+file.getAbsolutePath());
		return xml;
	}

	public static Object getObject(String type, String filename) {
		Object obj = null;
		if (type != null && type.equals(AccessionConstants.MARKER)) {
			obj = getMarkerXMLString(type, filename);
		}
		else {
			String md5=filename;
			if(!type.equals(AccessionConstants.SEARCHHISTORY))
			{
				md5 =getMD5(filename);
			}
			else
			{
				md5=BaseIndexer.mangleKeywordValue(md5);
			}
			
			File file = getFile(type, md5);
			if (file.exists()) {
				try {
					// System.out.println("Getting file :"+type +" filename
					// :"+filename);
					obj = XStreamUtils.getObjectFromFile(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return obj;
	}

	public static String getXMLString(String type, String filename) {
		String xml = null;
		if (type != null && type.equals(AccessionConstants.MARKER)) {
			xml = getMarkerXMLString(type, filename);
		} else {
			Object obj = getObject(type, filename);
			if (obj != null) {
				try {
					xml = XStreamUtils.getXMLfromObject(obj);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return xml;
	}

	public static boolean storeObject(String type, String filename, Object obj) {
		/*Use of MD5 allows us to have unique name for each File and to be sure that
		 * the file name will not be too long
		 */
		String md5 = filename;
		if(!type.equals(AccessionConstants.SEARCHHISTORY))
		{
			md5 = getMD5(filename);
		}
		else
		{
			md5=BaseIndexer.mangleKeywordValue(md5);
		}
		
		File file = getFile(type,md5);
		try {
			System.out.println("Saving file :"+type +" filename :"+md5);//+"
			// Object is "+obj);
			if (type != null && type.equals(AccessionConstants.MARKER)) {
				return XStreamUtils.saveMarkerObjectToFile(file, obj);
			} else {
				return XStreamUtils.saveObjectToFile(file, obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static String getMD5(String name)
	{
		String md5 ="";
		try
		{
			MessageDigest msgDigest = MessageDigest.getInstance("sha-256");
			msgDigest.update(name.getBytes());
			byte[] digest = msgDigest.digest();
			
			for (int i = 0; i < digest.length; ++i) {
	             int value = digest[i];
	             if (value < 0) {
	                 value += 256;
	             }
	            md5+=Integer.toHexString(value);
	         }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return md5;
	}

}
