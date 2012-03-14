package org.sgrp.singer.db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ObjectStore;
import org.sgrp.singer.indexer.AscDataIndex;

public class AscDataManager extends DataManager {
	protected static AscDataManager	mgr;
	protected static final String	objType	= "envdata";

	public static Map<String, Integer[]> calculateSD(Map<String, ArrayList<Integer>> values) {
		Map<String, Integer[]> mapValues = new LinkedHashMap<String, Integer[]>();
		for (Iterator<String> iterator = values.keySet().iterator(); iterator.hasNext();) {
			String type = iterator.next();
			ArrayList<Integer> arrVal = values.get(type);
			if (arrVal.size() > 0) {

				// System.out.println("Values from "+type+" ="+arrVal);
				int size = arrVal.size();
				int sum = 0;
				int minVal = Integer.MAX_VALUE;
				int maxVal = 0;
				for (int i = 0; i < arrVal.size(); i++) {
					int currVal = arrVal.get(i);
					if (minVal > currVal) {
						minVal = currVal;
					}
					if (maxVal < currVal) {
						maxVal = currVal;
					}
					sum = sum + currVal;
				}
				// System.out.println("Sum ="+sum);
				double mean = sum / size;
				// System.out.println("Mean ="+mean);
				double dev = 0;
				for (int i = 0; i < arrVal.size(); i++) {
					int val = arrVal.get(i);
					double min = val - mean;
					if (min < 0) {
						min = -(min);
					}
					dev = dev + (min * min);
				}
				// System.out.println("dev ="+dev);
				double div = dev / (size - 1);
				// System.out.println("div ="+div);
				double sd = Math.sqrt(div);
				// System.out.println("SD="+sd);
				double tot = mean + sd;
				// System.out.println("Total ="+tot);

				BigDecimal totBVal = new BigDecimal(tot);
				totBVal = totBVal.setScale(0, RoundingMode.HALF_UP);
				int totVal = totBVal.intValue();
				mapValues.put(type, new Integer[] { new Integer(totVal), new Integer(minVal), new Integer(maxVal) });
			} else {
				mapValues.put(type, new Integer[] { new Integer(-9999), new Integer(0), new Integer(0) });
			}
		}
		return mapValues;
	}

	public static Map<String, String> cleanLatLng(String lat, String lng) {
		int dig = 5;
		Map<String, String> values = new HashMap<String, String>();
		boolean radius = false;
		int index = 0;
		if ((index = lat.indexOf(".")) > 0 && lat.length() - index >= dig) {
			lat = lat.substring(0, index + dig);
		} else {
			radius = true;
		}
		if ((index = lng.indexOf(".")) > 0 && lng.length() - index >= dig) {
			lng = lng.substring(0, index + dig);
		} else {
			radius = true;
		}

		double latVal = 0.0;
		try {
			latVal = Double.parseDouble(lat);
		} catch (Exception e) {
			latVal = 0.0;
		}
		double lngVal = 0.0;
		try {
			lngVal = Double.parseDouble(lng);
		} catch (Exception e) {
			lngVal = 0.0;
		}
		values.put("lat", latVal + "");
		values.put("lng", lngVal + "");
		values.put("radius", radius ? "Y" : "N");

		return values;
	}

	public static AscDataManager getInstance() {
		if (mgr == null) {
			mgr = new AscDataManager();
		}
		return mgr;
	}

	public static final double roundDouble(double d, int places) {
		return Math.rint(d * (places * 10d)) / (places * 10d);
	}

	private AscDataIndex	ascIndex	= AscDataIndex.getInstance();

	public AscDataManager() {

	}

	public Map<String, Integer[]> getAscDataLatLng(String lat, String lng, String types[]) throws Exception {
		Map<String, Integer[]> mapValues = null;
		if ((lat != null && lat.trim().length() > 0) && (lng != null && lng.trim().length() > 0)) {
			
			Map<String, String> values = cleanLatLng(lat, lng);
			String latStr = values.get("lat");
			String lngStr = values.get("lng");
			String query = latStr + lngStr;
			mapValues = (Map<String, Integer[]>) ObjectStore.getObject(objType, query);
			if (mapValues != null) { return mapValues; }
			mapValues = getAscDataLatLng(latStr, lngStr, types, values.get("radius").equals("Y"));
			ObjectStore.storeObject(objType, query, mapValues);
		}
		return mapValues;
	}

	public Map<String, Integer[]> getAscDataLatLng(String lat, String lng, String types[], boolean calRadius) throws Exception {
		Map<String, Integer[]> mapValues = null;
		if ((lat != null && lat.trim().length() > 0) && (lng != null && lng.trim().length() > 0)) {
			double latVal = 0.0;
			try {
				latVal = Double.parseDouble(lat);
			} catch (Exception e) {
				latVal = 0.0;
			}
			double lngVal = 0.0;
			try {
				lngVal = Double.parseDouble(lng);
			} catch (Exception e) {
				lngVal = 0.0;
			}
			// 2.5min arc
			/*
			 * int nrows = 2880; int ncols = 8640; double xllcorner = -180;//.00833333377; double yllcorner = -60;//.008333333768; double cellsize = 0.04166666665;
			 */

			// 10min arc
			int nrows = 720;
			int ncols = 2160;
			double xllcorner = -180.00833333377;
			double yllcorner = -60.008333333768;
			double cellsize = 0.1666666666667;

			double row = ((((yllcorner) + (nrows * cellsize)) - latVal) / (nrows * cellsize)) * nrows;
			double col = ((lngVal - (xllcorner)) / (ncols * cellsize)) * ncols;

			// System.out.println("Asc Row ="+row);
			// System.out.println("Asc Col ="+col);

			BigDecimal rowBVal = new BigDecimal(row);
			rowBVal = rowBVal.setScale(0, RoundingMode.HALF_UP);
			BigDecimal colBVal = new BigDecimal(col);
			colBVal = colBVal.setScale(0, RoundingMode.HALF_UP);
			int rowVal = rowBVal.intValue();
			int colVal = colBVal.intValue();
			// System.out.println("Asc Row Value is "+rowVal);
			// System.out.println("Asc Col Value is "+colVal);
			if (rowVal < 0) {
				rowVal = -(rowVal);
			}
			if (colVal < 0) {
				colVal = -(colVal);
			}
			rowVal = rowVal - 1;
			colVal = colVal - 1;
			mapValues = getAscDataRowCol(rowVal, colVal, types, calRadius);
		}
		return mapValues;
	}

	public Map<String, Integer[]> getAscDataRowCol(int row, int col, String types[], boolean calRadius) throws Exception {
		Map<String, Integer[]> mapValues = new LinkedHashMap<String, Integer[]>();
		try {
			// System.out.println("CalRadius is :"+calRadius);
			if (calRadius) {
				int radius = 1;
				Map<String, ArrayList<Integer>> calList = new LinkedHashMap<String, ArrayList<Integer>>();
				String searchStr = "";
				for (int j = (row - radius); j <= (row + radius); j++) {
					searchStr = searchStr + j;
					if (j < (row + radius)) {
						searchStr = searchStr + " OR ";
					}
				}
				// = (row-1)+" OR "+row+" OR
				// "+(row+1);//"row"+AccessionConstants.SPLIT_KEY+row;
				// String searchStr = "row:"+(row-1)+" OR row:"+row+" OR
				// row:"+(row+1);//"row"+AccessionConstants.SPLIT_KEY+row;
				// System.out.println("Search String = "+searchStr);
				Map<String, String> list = searchAscString(searchStr, true, types);
				// System.out.println("List size is :"+list.size());
				if (list.size() > 0) {
					for (Iterator<String> iterator = list.keySet().iterator(); iterator.hasNext();) {
						String id = iterator.next();
						String text = list.get(id);
						String type = AccessionConstants.getRegExValue(text, "type");
						String line = AccessionConstants.getRegExValue(text, "val");
						String fields[] = line.split("\\s+");

						ArrayList<Integer> valList = new ArrayList<Integer>();
						if (calList.containsKey(type)) {
							valList = calList.get(type);
						}
						for (int i = (col - 1); i <= (col + 1); i++) {
							String value = fields[col];
							if (!value.equals("-9999")) {
								valList.add(Integer.parseInt(value));
							}
						}
						calList.put(type, valList);
					}
					mapValues = calculateSD(calList);
				}
			} else {
				String searchStr = row + "";// "row"+AccessionConstants.SPLIT_KEY+row;
				// System.out.println("Search String = "+searchStr);
				Map<String, String> list = searchAscString(searchStr, true, types);
				// System.out.println("List size is :"+list.size());
				if (list.size() > 0) {
					for (Iterator<String> iterator = list.keySet().iterator(); iterator.hasNext();) {
						String id = iterator.next();
						String text = list.get(id);
						String type = AccessionConstants.getRegExValue(text, "type");
						String line = AccessionConstants.getRegExValue(text, "val");
						String fields[] = line.split("\\s+");
						String value = fields[col];

						// if(!value.equals("-9999"))
						// {
						mapValues.put(type, new Integer[] { Integer.parseInt(value) });
						// }
					}
				}
			}
		} catch (Exception se) {
			System.out.println("getAscData String " + se.toString());
		}
		return mapValues;
	}

	public Map<String, String> searchAscString(String query, boolean addContents, String[] colls) throws Exception {
		return ascIndex.search(query, "row", ascIndex.NAME, colls, addContents);
	}
}
