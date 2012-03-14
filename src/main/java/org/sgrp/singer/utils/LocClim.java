/**
 * 
 */
package org.sgrp.singer.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kviparthi
 */
public class LocClim {

	private static final int	BUFSIZE	= 1024;
	public static String		host	= "www.fao.org";

	public static String		port	= "80";

	public static Map<String, Map<String, String>> getMapValues(String lat, String lng) {
		// "me","min","max","pr",
		String types[] = new String[] { "pet", "sufr", "windsp", "vap" };

		Map<String, Map<String, String>> res = new LinkedHashMap<String, Map<String, String>>();
		for (int i = 0; i < types.length; i++) {
			String variable = types[i];
			String values = getValues(lat, lng, variable);
			if (values != null && values.trim().length() > 0) {
				Map<String, String> map = getRegExValueMap(values, "*");
				res.put(variable, map);
			}
		}
		return res;

	}

	public static Map<String, String> getRegExValueMap(String text, String search) {
		if (search.equals("*")) {
			search = "\"(.*?)\">(.*?)<";
		}
		Map<String, String> values = new HashMap<String, String>();
		if ((text != null) && (text.trim().length() > 0)) {
			String regEx = search;
			// System.out.println("Regexp is "+regEx);
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(text);

			while (matcher.find()) {
				if (matcher.groupCount() >= 2) {
					// System.out.println("Month="+matcher.group(1)+"
					// value="+matcher.group(2));
					values.put(matcher.group(1), matcher.group(2));
				}
			}
		}
		// System.out.println("Map is :"+values);
		return values;

	}

	public static String getValues(String lat, String lng, String variable) {
		String values = null;
		try {
			if (variable == null || variable.trim().length() == 0) {
				variable = "me";
			}
			values = getValues(host, port, "inverseDistance", variable, lng, lat, null, null, null, null, false, null, null, null, null);

		} catch (Exception e) {
			System.out.println("Found exception with loclim for variable=" + variable);
			// e.printStackTrace();
		}
		return values;
	}

	private synchronized static String getValues(String host, String port, String method, String variable, String lon, String lat, String maxDist, String minStations, String maxStations, String exponent, boolean altCorrection, String alt,
		String altCorrMaxDist, String altCorrMinStations, String altCorrMaxStations) throws Exception {
		if (maxDist == null) {
			maxDist = "";
		}
		if (minStations == null) {
			minStations = "";
		}
		if (maxStations == null) {
			maxStations = "";
		}
		if (exponent == null) {
			exponent = "";
		}
		if (alt == null) {
			alt = "";
		}
		if (altCorrMaxDist == null) {
			altCorrMaxDist = "";
		}
		if (altCorrMinStations == null) {
			altCorrMinStations = "";
		}
		if (altCorrMaxStations == null) {
			altCorrMaxStations = "";
		}

		StringBuffer file = new StringBuffer("/sd/locclim/srv/en/locclim.getValueXML");
		file.append("?method=" + method);
		file.append("&variable=" + variable);
		file.append("&lon=" + lon);
		file.append("&lat=" + lat);
		file.append("&maxDist=" + maxDist);
		file.append("&minStations=" + minStations);
		file.append("&maxStations=" + maxStations);
		file.append("&exponent=" + exponent);
		file.append("&altCorrection=" + altCorrection);
		file.append("&alt=" + alt);
		file.append("&altCorrMaxDist=" + altCorrMaxDist);
		file.append("&altCorrMinStations=" + altCorrMinStations);
		file.append("&altCorrMaxStations=" + altCorrMaxStations);

		URL url = new URL("http", host, Integer.parseInt(port), file.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream input = conn.getInputStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte buffer[] = new byte[BUFSIZE];
		int nRead;
		while ((nRead = input.read(buffer, 0, BUFSIZE)) > 0) {
			output.write(buffer, 0, nRead);
		}
		conn.disconnect();
		output.close();
		return output.toString();
	}
}
