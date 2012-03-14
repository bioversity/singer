package org.sgrp.singer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.Marker;
import org.sgrp.singer.ObjectStore;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.SearchResults;
import org.sgrp.singer.StringComparableValues;
import org.sgrp.singer.form.AccessionForm;
import org.sgrp.singer.form.GenericForm;
import org.sgrp.singer.indexer.AccessionIndex;

public class AccessionManager extends DataManager {
	public static final String			attribList	= AccessionConstants.STATUSCODE + "|" + AccessionConstants.TRUSTCODE + "|" + AccessionConstants.SOURCECODE + "|" + AccessionConstants.GENUSCODE + "|" + AccessionConstants.SPECIESCODE + "|"
														+ AccessionConstants.COLLECTIONCODE + "|" + AccessionConstants.COUNTRYCODE + "|" + "latlongd";
	protected static AccessionManager	mgr;
	protected static final String		objType		= AccessionConstants.KEYCOUNT;

	public static AccessionManager getInstance() {
		if (mgr == null) {
			mgr = new AccessionManager();
		}
		return mgr;
	}

	private AccessionIndex	accIndex	= AccessionIndex.getInstance();

	public AccessionManager() {

	}

	public AccessionForm getAccession(String acceid, String[] colls) throws Exception {
		if ((acceid == null) || (acceid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		AccessionForm cbForm = null;
		try {
			Map<String, String> list = accIndex.search(accIndex.ID + ":" + acceid, accIndex.ID, accIndex.NAME, colls, true);
			if (list.size() > 0) {
				cbForm = getAccessionFromString(list.get(acceid));
			}
		} catch (Exception se) {
			System.out.println("getAccession String " + se.toString());
		}
		return cbForm;
	}

	public int getAccessionCount(String query, String[] colls) throws Exception {
		
		//return searchAccession(query, colls).size();
		int size = searchAccessionCount(query,AccessionConstants.KEYWORDS,colls);
		return size;
	}
	
	public AccessionForm getAccessionFromString(String data) throws Exception {
		AccessionForm accForm = null;
		try {
			accForm = new AccessionForm(AccessionConstants.getRegExValue(data,AccessionConstants.ACCESSIONCODE));
			accForm.setAcceid(AccessionConstants.getRegExValue(data, AccessionConstants.ACCESSIONCODE));
			accForm.setInstcode(AccessionConstants.getRegExValue(data,AccessionConstants.INSTITUTECODE));
			accForm.setInstname(AccessionConstants.getRegExValue(data,AccessionConstants.INSTITUTENAME));
			accForm.setCollcode(AccessionConstants.getRegExValue(data,AccessionConstants.COLLECTIONCODE));
			accForm.setCollname(AccessionConstants.getRegExValue(data,AccessionConstants.COLLECTIONNAME));
			accForm.setTaxcode(AccessionConstants.getRegExValue(data,AccessionConstants.TAXONCODE));
			accForm.setTaxname(AccessionConstants.getRegExValue(data,AccessionConstants.TAXONNAME));
			accForm.setGenuscode(AccessionConstants.getRegExValue(data,AccessionConstants.GENUSCODE));
			accForm.setGenusname(AccessionConstants.getRegExValue(data,AccessionConstants.GENUSNAME));
			accForm.setSpeciescode(AccessionConstants.getRegExValue(data,AccessionConstants.SPECIESCODE));
			accForm.setSpeciesname(AccessionConstants.getRegExValue(data,AccessionConstants.SPECIESNAME));
			accForm.setStatcode(AccessionConstants.getRegExValue(data,AccessionConstants.STATUSCODE));
			accForm.setStatname(AccessionConstants.getRegExValue(data,AccessionConstants.STATUSNAME));
			accForm.setSrccode(AccessionConstants.getRegExValue(data,AccessionConstants.SOURCECODE));
			accForm.setSrcname(AccessionConstants.getRegExValue(data,AccessionConstants.SOURCENAME));
			accForm.setTrustcode(AccessionConstants.getRegExValue(data,AccessionConstants.TRUSTCODE));
			accForm.setTrustname(AccessionConstants.getRegExValue(data,AccessionConstants.TRUSTNAME));
			accForm.setOrigcode(AccessionConstants.getRegExValue(data,AccessionConstants.COUNTRYCODE));
			accForm.setOrigname(AccessionConstants.getRegExValue(data,AccessionConstants.COUNTRYNAME));
			accForm.setMisscode(AccessionConstants.getRegExValue(data,"misscode"));
			accForm.setDonorcode(AccessionConstants.getRegExValue(data,"donorcode"));
			accForm.setAccename(AccessionConstants.getRegExValue(data,"accename"));
			accForm.setAccenumb(AccessionConstants.getRegExValue(data,"accenumb"));
			accForm.setOrigacceid(AccessionConstants.getRegExValue(data,"origaccenumb"));
			accForm.setAcqdate(AccessionConstants.getRegExValue(data,"acqdate"));
			accForm.setCollnumb(AccessionConstants.getRegExValue(data,"collnumb"));
			accForm.setColldate(AccessionConstants.getRegExValue(data,"colldate"));
			accForm.setLatitude(AccessionConstants.getRegExValue(data,"latitude"));
			accForm.setLongitude(AccessionConstants.getRegExValue(data,"longitude"));
			accForm.setLatres(AccessionConstants.getRegExValue(data,"latres"));
			accForm.setLonres(AccessionConstants.getRegExValue(data,"lonres"));
			accForm.setElevation(AccessionConstants.getRegExValue(data,"elevation"));
			accForm.setLatituded(AccessionConstants.getRegExValue(data,"latituded"));
			accForm.setLongituded(AccessionConstants.getRegExValue(data,"longituded"));
			accForm.setAvailability(AccessionConstants.getRegExValue(data,"availability"));
			accForm.setInsvalbard(AccessionConstants.getRegExValue(data,"insvalbard"));
			accForm.setCollsite(AccessionConstants.getRegExValue(data,"collsite"));
			accForm.setOthernumb(AccessionConstants.getRegExValue(data,"othernumb"));
			accForm.setPedigree(AccessionConstants.getRegExValue(data,"pedigree"));
			accForm.setParentfemale(AccessionConstants.getRegExValue(data,"parentfemale"));
			accForm.setParentmale(AccessionConstants.getRegExValue(data,"parentmale"));
		} catch (Exception se) {
			System.out.println("getCapacityBuilding Document " + se.toString());
			se.printStackTrace();
		}
		return accForm;
	}

	public HashMap getIntrustCollectionCount() {
		String type = "intrustcollection";
		HashMap<String, Integer> resultsMap = (HashMap<String, Integer>) ObjectStore.getObject(objType, type);
		if (resultsMap == null) {
			resultsMap = new HashMap<String, Integer>();
			Map<String, String> map = SearchResults.getInstance().getKeywordListBySearch("type=" + AccessionConstants.INSTITUTE);
			for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
				String keyid = it.next();
				Map<String, String> colmap = SearchResults.getInstance().getInstituteCollections(keyid);
				for (Iterator<String> colit = colmap.keySet().iterator(); colit.hasNext();) {
					String colkeyid = colit.next();
					int itccount = SearchResults.getInstance().getAccessionCount(AccessionConstants.COLLECTIONCODE + AccessionConstants.SPLIT_KEY + colkeyid + " AND " + AccessionConstants.TRUSTCODE + AccessionConstants.SPLIT_KEY + "try",
						new String[] { colkeyid });

					resultsMap.put(colkeyid, itccount);
				}
			}
			ObjectStore.storeObject(objType, type, resultsMap);
		}
		return resultsMap;

	}

	@SuppressWarnings("unchecked")
	public HashMap getAccessionKeyCount(String query, String[] colls) {
		// HashMap<String, HashMap<String, StringComparableValues>> resultsMap =
		// (HashMap<String, HashMap<String, StringComparableValues>>)
		// ObjectStore.getObject(objType, query);

		HashMap<String, HashMap> resultsMap = (HashMap<String, HashMap>) ObjectStore.getObject(objType, query);
		if (resultsMap != null) { return resultsMap; }
		resultsMap = new HashMap<String, HashMap>();
		try {
			HashMap<String, HashMap<String, Integer>> attribValuesMap = new HashMap<String, HashMap<String, Integer>>();

			// System.out.println(" searchString is:"+searchString);
			Map<String, String> accList = accIndex.search(query, AccessionConstants.KEYWORDS, accIndex.NAME, colls, true);
			// System.out.println("Records found in Keycount :"+count);
			
			if (accList.size() > 0) {
				for (Iterator<String> iter = accList.keySet().iterator(); iter.hasNext();) {
					String id = iter.next();
					Map<String, String> values = AccessionConstants.getRegExValueMap(accList.get(id), attribList);
					for (Iterator<String> iterator = values.keySet().iterator(); iterator.hasNext();) {
						String currentAttribName = iterator.next();
						HashMap<String, Integer> valuesMap = null;
						if (attribValuesMap.containsKey(currentAttribName)) {
							valuesMap = attribValuesMap.get(currentAttribName);
						} else {
							valuesMap = new HashMap<String, Integer>();
						}
						updateHashMap(valuesMap, values.get(currentAttribName));
						attribValuesMap.remove(currentAttribName);
						attribValuesMap.put(currentAttribName, valuesMap);
					}
				}

				for (Iterator<String> iterator = attribValuesMap.keySet().iterator(); iterator.hasNext();) {
					String attrName = iterator.next();
					HashMap<String, Integer> valuesMap = attribValuesMap.get(attrName);
					if (attrName.equals("latlongd")) {
						int totCount = 0;
						List<Marker> markers = new ArrayList<Marker>();
						// Marker markers[] = new Marker[valuesMap.size()];
						// int i=0;
						for (Iterator<String> iter = valuesMap.keySet().iterator(); iter.hasNext();) {
							String keyId = iter.next();
							Integer keyValue = valuesMap.get(keyId);
							totCount = totCount + keyValue;
							markers.add(new Marker(keyId, keyValue));
							// markers[i] = new Marker(keyId, keyValue);
							// i++;
						}
						if (markers.size() > 0) {
							ObjectStore.storeObject(AccessionConstants.MARKER, query, markers);
							// System.out.println("totCount is :"+totCount);
							HashMap<String, StringComparableValues> gisMap = new HashMap<String, StringComparableValues>();
							gisMap.put(AccessionConstants.ALL_GIS_MAP_COUNT, new StringComparableValues(AccessionConstants.ALL_GIS_MAP_COUNT, totCount + "", AccessionConstants.ALL_GIS_MAP_COUNT));
							resultsMap.put(AccessionConstants.ALL_GIS_MAP_COUNT, gisMap);
						}
					} else {

						HashMap<String, StringComparableValues> currValuesMap = new HashMap<String, StringComparableValues>();
						for (Iterator<String> iter = valuesMap.keySet().iterator(); iter.hasNext();) {
							String keyId = iter.next();
							Integer keyValue = valuesMap.get(keyId);
							String keyName = keyId;

							keyName = AccessionServlet.getKeywords().getName(keyId);
							currValuesMap.put(keyId, new StringComparableValues(keyId, keyValue + "", keyName));
						}
						resultsMap.put(attrName, currValuesMap);
					}
				}
				HashMap<String, StringComparableValues> allMap = new HashMap<String, StringComparableValues>();
				allMap.put(AccessionConstants.ALL_KEY_COUNT, new StringComparableValues(AccessionConstants.ALL_KEY_COUNT, accList.size() + "", AccessionConstants.ALL_KEY_COUNT));
				resultsMap.put(AccessionConstants.ALL_KEY_COUNT, allMap);

				if (accList.size() > AccessionConstants.getMaxResultsStore()) {
					ObjectStore.storeObject(objType, query, resultsMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultsMap;
	}

	public int getMaxYear() {
		return getYear("select max(edate) from capbuild");
	}

	public int getMinYear() {
		return getYear("select min(sdate) from capbuild");
	}

	public int getYear(String sql) {
		Connection conn = null;
		int year = Integer.parseInt(GenericForm.getlongAsDate((new Date()).getTime(), "yyyy"));
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			String date = null;
			if (rs.next()) {
				date = rs.getDate(1).getTime() + "";
			}
			if (date != null) {
				year = Integer.parseInt(GenericForm.getlongAsDate(date, "yyyy"));
			}
			rs.close();
			stmt.close();
			conn.commit();
		} catch (SQLException se) {

		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
		return year;
	}

	public void saveAccession(AccessionForm cbForm) throws SQLException {
		if (cbForm == null) { throw new SQLException(ResourceManager.getString("save.Accession.null")); }
	}

	public Map<String, AccessionForm> searchAccession(String query, String[] colls) throws Exception {
		Map<String, AccessionForm> data = new LinkedHashMap<String, AccessionForm>();
		AccessionForm accForm = null;
		try {
			Map<String, String> list = searchAccessionString(query, true, colls);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				accForm = getAccessionFromString(list.get(id));
				data.put(id, accForm);
			}
		} catch (Exception se) {
			System.out.println("searchAccession" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchAccessionString(String query, boolean addContents, String[] colls) throws Exception {
		return searchAccessionString(query, addContents, colls, 0, -1);
	}

	public Map<String, String> searchAccessionString(String query, boolean addContents, String sortField[], boolean[] reverse, String[] colls, int from, int to) throws Exception
	{
		//query = AccessionConstants.replaceString(query, AccessionConstants.SPLIT_KEY, ":", 0);
		return accIndex.search(query, AccessionConstants.KEYWORDS, sortField, reverse, colls, addContents, from, to);
	}
	
	public Map<String, String> searchAccessionString(String query, boolean addContents, String sortField[], String[] colls, int from, int to) throws Exception
	{
		//query = AccessionConstants.replaceString(query, AccessionConstants.SPLIT_KEY, ":", 0);
		return accIndex.search(query, AccessionConstants.KEYWORDS, sortField, colls, addContents, from, to);
	}	
	
	public Map<String, String> searchAccessionString(String query, boolean addContents, String sortField, String[] colls, int from, int to) throws Exception
	{
		//query = AccessionConstants.replaceString(query, AccessionConstants.SPLIT_KEY, ":", 0);
		return accIndex.search(query, AccessionConstants.KEYWORDS, sortField, colls, addContents, from, to);
	}
	
	public Map<String, String> searchAccessionString(String query, boolean addContents, String[] colls, int from, int to) throws Exception {
		//query = AccessionConstants.replaceString(query, AccessionConstants.SPLIT_KEY, ":", 0);
		return accIndex.search(query, AccessionConstants.KEYWORDS, accIndex.NAME, colls, addContents, from, to);
	}
	
	public int searchAccessionCount(String query, String fieldName, String[] colls)
	{
		return accIndex.searchCount(query, fieldName, accIndex.NAME,colls);
	}
	public void updateAccessionIndex(String accid, boolean indexed) throws SQLException {
		Connection conn = null;
		try {
			conn = AccessionServlet.getCP().newConnection(this.toString());
			// System.out.println("Updating inst setting indexed "+indexed+" for
			// "+instid+" sysid="+sysid);
			PreparedStatement pstmt = conn.prepareStatement("update acc set indexed=? where lower(accenumb_)=?");
			pstmt.setString(1, indexed ? "Y" : "N");
			pstmt.setString(2, accid.toLowerCase());
			pstmt.executeUpdate();
			pstmt.close();
			conn.commit();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new SQLException(se.toString());
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
		}
	}

	public void updateAccessionIndexed(String id, boolean indexed) {

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
