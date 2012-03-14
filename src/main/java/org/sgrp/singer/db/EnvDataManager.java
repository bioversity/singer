package org.sgrp.singer.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.indexer.EnvDataIndex;

public class EnvDataManager extends DataManager {
	public static final String						attribList	= "alt|bio1|bio2|bio3|bio4|bio5|bio6|bio7|bio8|bio9|bio10|bio11|bio12|bio13|bio14|bio15|bio16|bio17|bio18|bio19";
	public static LinkedHashMap<String, String[]>	lmap		= new LinkedHashMap<String, String[]>();
	protected static EnvDataManager					mgr;

	protected static final String					objType		= "gisenvdata";

	public static String fromObject(Object obj) {
		String keyVal = null;
		if (obj instanceof String) {
			keyVal = (String) obj;
		} else if (obj instanceof String[]) {
			keyVal = ((String[]) obj)[0];
		}
		return keyVal;
	}

	public static LinkedHashMap<String, String[]> getEnvMap() {
		if (lmap.size() == 0) {
			lmap.clear();
			lmap.put("alt", new String[] { "0", "0", "0", "3500", "1", "250", "270" });
			lmap.put("bio1", new String[] { "0", "0", "-10.0", "50.0", "0.1", "25.5", "26.0" });
			lmap.put("bio2", new String[] { "0", "0", "-10.0", "50.0", "0.1", "26", "27" });
			lmap.put("bio3", new String[] { "0", "0", "-10.0", "50.0", "0.1", "22", "23" });
			lmap.put("bio4", new String[] { "0", "0", "-10.0", "50.0", "0.1", "28", "30" });
			lmap.put("bio5", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio6", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio7", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio8", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio9", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio10", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio11", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio12", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio13", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio14", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio15", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio16", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio17", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio18", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
			lmap.put("bio19", new String[] { "0", "0", "-10.0", "50.0", "0.1", "41", "42" });
		}
		return lmap;
	}

	public static EnvDataManager getInstance() {
		if (mgr == null) {
			mgr = new EnvDataManager();
		}
		return mgr;
	}

	public static String makeSearchQuery(Map reqMap) {
		String squery = null;
		ArrayList<String> arrList = new ArrayList<String>();
		for (Iterator<String> itr = lmap.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			if (reqMap.containsKey(key + "_check")) {
				String rStart = fromObject(reqMap.get(key + "_start"));
				String rEnd = fromObject(reqMap.get(key + "_end"));
				String query = key + ":";
				if (!key.equals("alt")) {
					if (rStart.indexOf(".") > 0)
						rStart = AccessionConstants.replaceString(rStart, ".", "", 0);
					else
						rStart = rStart + "0";

					if (rEnd.indexOf(".") > 0)
						rEnd = AccessionConstants.replaceString(rEnd, ".", "", 0);
					else
						rEnd = rEnd + "0";
				}
				rStart = AccessionConstants.encodeEnvVar(rStart);
				rEnd = AccessionConstants.encodeEnvVar(rEnd);
				if (reqMap.containsKey(key + "_range")) {
					query = query + "[" + rStart + " TO " + rEnd + "]";
				} else {
					query = query + rStart;
				}
				arrList.add(query);
			}
		}

		StringBuffer sb = new StringBuffer();

		int size = arrList.size();
		if (size > 0) {
			for (int pos = 0; pos < arrList.size(); pos++) {
				sb.append(arrList.get(pos));
				if (pos != size - 1) {
					sb.append(" AND ");
				}
			}
			squery = sb.toString();
		}
		// /System.out.println("Final Query is :"+squery);
		return squery;
	}

	private EnvDataIndex	envIndex	= EnvDataIndex.getInstance();

	public EnvDataManager() {

	}

	public Map<String, String> searchEnvData(String query, String[] colls) throws Exception {
		return searchEnvDataString(query, true, colls);
	}

	public Map<String, String> searchEnvDataString(String query, boolean addContents, String[] colls) throws Exception {
		return searchEnvDataString(query, addContents, colls, 0, -1);
	}

	public Map<String, String> searchEnvDataString(String query, boolean addContents, String[] colls, int from, int to) throws Exception {
		return envIndex.search(query, AccessionConstants.CONTENTS, envIndex.NAME, colls, addContents, from, to);
	}

	public void updateHashMap(HashMap<String, Integer> map, String value) {
		if ((value != null) && (value.trim().length() > 0)) {
			// ComparableValues compValues = null;
			int count = 1;
			if (map.containsKey(value)) {
				count = map.get(value);
				count++;
			}
			// compValues = new ComparableValues(value, count + "");
			map.remove(value);
			map.put(value, count);
		}
	}

}
