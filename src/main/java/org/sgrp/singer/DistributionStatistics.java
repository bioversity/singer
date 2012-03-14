package org.sgrp.singer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.sgrp.singer.db.DistributionManager;
import org.sgrp.singer.indexer.BaseIndexer;
import org.sgrp.singer.indexer.CompTermInfo;

public class DistributionStatistics {
	protected static final String			fields[]	= new String[] { "year", "usercode", "devstat", "region", "ctycode", AccessionConstants.TAXONCODE, AccessionConstants.SPECIESCODE, AccessionConstants.GENUSCODE };
	protected static DistributionStatistics	mgr;

	public static DistributionStatistics getInstance() {
		if (mgr == null) {
			mgr = new DistributionStatistics();
		}
		return mgr;
	}

	public String	objType			= AccessionConstants.DISTRIBUTION;
	public int		totDistCount	= 0;

	// public HashMap<String, Integer> yearDistMap = null;
	// public HashMap<String, Integer> collDistMap = null;
	// public Map<String, ArrayList<CompTermInfo>> globalMap = null;

	public DistributionStatistics() {
		loadData();
	}

	public Map<String, ArrayList> getCollArrayDistributionMap(String colls[]) throws Exception {
		return getCollArrayDistributionMap(colls, false, null);
	}

	@SuppressWarnings("unchecked")
	public Map<String, ArrayList> getCollArrayDistributionMap(String colls[], boolean fromCopy, String filename) throws Exception {
		Map<String, ArrayList> resTerm = null;
		if (fromCopy) {
			resTerm = (Map<String, ArrayList>) ObjectStore.getObject(objType, filename);
		}
		if (resTerm == null) {
			resTerm = new HashMap<String, ArrayList>();
			HashMap<String, String> dirs = DistributionManager.getInstance().getDistributionDirForCollArray(colls);
			// System.out.println("Dirs are :"+dirs);
			if (dirs == null && dirs.size() == 0) { return null; }
			boolean loadData = false;
			int i = 0;
			Iterator<String> iter = dirs.keySet().iterator();
			while (iter.hasNext()) {
				String col = iter.next();
				if (dirs.size() == 1) {
					resTerm = getCollectionDistributionMap(col);
					break;
				} else {
					if (i == dirs.size() - 1) {
						loadData = true;
					}
					Map<String, ArrayList> cresTerm = getCollectionDistributionMap(col);
					BaseIndexer.mergeTerms(resTerm, cresTerm, loadData, false);
					i++;
				}
			}
			if (fromCopy) {
				ObjectStore.storeObject(objType, filename, resTerm);
			}
		}
		return resTerm;
	}

	// public HashMap<String, HashMap<String, Integer>> distColMap = null;

	public HashMap<String, Integer> getCollectionCountMap() {
		HashMap<String, Integer> colDistMap = (HashMap<String, Integer>) ObjectStore.getObject(objType, "colcount");
		if (colDistMap == null) {
			colDistMap = loadCollectionCountMap();
			ObjectStore.storeObject(objType, "colcount", colDistMap);
		}
		return colDistMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, ArrayList> getCollectionDistributionMap(String colkeyid) {
		Map<String, ArrayList> colMap = (Map<String, ArrayList>) ObjectStore.getObject(objType, "collection_" + colkeyid);
		if (colMap == null) {
			try {
				colMap = BaseIndexer.getHighFreqTerms(DistributionManager.getInstance().getDistributionDirForColl(colkeyid), fields, false);
				ObjectStore.storeObject(objType, "collection_" + colkeyid, colMap);
			} catch (Exception e) {
			}
		}
		return colMap;
	}

	public int getCollectiontotal(String colkeyid) {
		if (getCollectionCountMap().containsKey(colkeyid)) {
			return getCollectionCountMap().get(colkeyid);
		} else {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getCollectionYearDistributionMap(String colkeyid) {
		Map<String, Integer> yearMap = new HashMap<String, Integer>();
		Map<String, ArrayList<CompTermInfo>> colMap = (Map<String, ArrayList<CompTermInfo>>) ObjectStore.getObject(objType, "collection_" + colkeyid);
		if (colMap != null) {
			ArrayList<CompTermInfo> arrList = colMap.get("year");
			for (int i = 0; i < arrList.size(); i++) {
				CompTermInfo tInfo = arrList.get(i);
				yearMap.put(tInfo.getText(), tInfo.getDocFreq());
			}
		}
		return yearMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, ArrayList> getGlobalDistributionMap() {
		Map<String, ArrayList> globalDistMap = (Map<String, ArrayList>) ObjectStore.getObject(objType, "global");
		if (globalDistMap == null) {
			try {
				globalDistMap = loadGlobalDistributionMap();
				ObjectStore.storeObject(objType, "global", globalDistMap);
			} catch (Exception e) {
			}
		}
		return globalDistMap;
	}

	public int getTotDistCount() {
		if (totDistCount == 0) {
			int count = 0;
			for (Iterator<String> itr = getYearDistribution().keySet().iterator(); itr.hasNext();) {
				Integer sum = getYearDistribution().get(itr.next());
				count = count + sum;
			}
			totDistCount = count;
		}

		return totDistCount;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Integer> getYearDistribution() {
		HashMap<String, Integer> yearDistMap = (HashMap<String, Integer>) ObjectStore.getObject(objType, "yearly");
		if (yearDistMap == null) {
			yearDistMap = loadYearlyDistribution();
			ObjectStore.storeObject(objType, "yearly", yearDistMap);
		}
		return yearDistMap;
	}

	public HashMap<String, Integer> loadCollectionCountMap() {
		HashMap<String, Integer> colsMap = new HashMap<String, Integer>();
		Map<String, String> colmap = AccessionServlet.getKeywords().getAllColMap();
		Iterator<String> colit = colmap.keySet().iterator();
		while (colit.hasNext()) {
			String colkeyid = colit.next();
			if (DistributionManager.getInstance().isDistributionAvailableForColl(colkeyid)) {
				String dir = DistributionManager.getInstance().getDistributionDirForColl(colkeyid);
				IndexReader reader = null;
				try {
					reader = BaseIndexer.getInstance().getIndexReader(dir);
					colsMap.put(colkeyid, reader.maxDoc());
					reader.close();
					reader = null;
				} catch (Exception e) {

				} finally {
					if (reader != null) {
						try {
							reader.close();
							reader = null;
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return colsMap;
	}

	@SuppressWarnings("unchecked")
	/*
	 * public void loadDataOLD() { yearDistMap = (HashMap<String, Integer>) ObjectStore.getObject(objType, "yearly"); collDistMap = (HashMap<String, Integer>) ObjectStore.getObject(objType,
	 * "collection"); if (yearDistMap == null || collDistMap == null) { yearDistMap = new HashMap<String, Integer>(); collDistMap = new HashMap<String, Integer>(); Map<String, String> colmap =
	 * AccessionServlet.getKeywords().getAllColMap(); // System.out.println("ColMap is "+colmap.size()); Iterator<String> colit = colmap.keySet().iterator(); while (colit.hasNext()) { String colkeyid =
	 * colit.next(); // System.out.println("Loading Collection :"+colkeyid); loadCollection(colkeyid); } } }
	 */
	public void loadData() {
		getGlobalDistributionMap();
	}

	/*
	 * public void loadCollection(String colkeyid) { if (DistributionManager.getInstance().isDistributionAvailableForColl( colkeyid)) { // System.out.println("Loading Distribution Collection
	 * :"+colkeyid); String dLoc = DistributionManager.getInstance() .getDistributionDirForColl(colkeyid); try { HashMap<String, Integer> collMap = new HashMap<String, Integer>(); ArrayList<CompTermInfo>
	 * tinfo = BaseIndexer.getHighFreqTerms(dLoc, "year", true).get("year"); int colcount = 0; for (int t = 0; t < tinfo.size(); t++) { CompTermInfo termInfo = tinfo.get(t); String termName =
	 * termInfo.getText(); Integer freq = termInfo.getDocFreq(); // System.out.println("Terms is :"+termName); int count = 0; if (yearDistMap.containsKey(termName)) { count =
	 * yearDistMap.get(termName); } colcount = colcount + freq; count = count + freq; totDistCount = totDistCount + freq; yearDistMap.remove(termName); yearDistMap.put(termName, count);
	 * collMap.put(termName, freq); } collDistMap.put(colkeyid, colcount); ObjectStore.storeObject(objType, colkeyid, collMap); // distColMap.put(colkeyid, collMap); } catch (Exception e) {
	 * e.printStackTrace(); } } }
	 */

	public Map<String, ArrayList> loadGlobalDistributionMap() throws Exception {
		Map<String, ArrayList> resTerm = new HashMap<String, ArrayList>();
		Map<String, String> colmap = AccessionServlet.getKeywords().getAllColMap();
		List<String> fDir = new ArrayList<String>();
		Iterator<String> colit = colmap.keySet().iterator();
		while (colit.hasNext()) {
			String colkeyid = colit.next();
			if (!fDir.contains(colkeyid) && DistributionManager.getInstance().isDistributionAvailableForColl(colkeyid)) {

				fDir.add(colkeyid);
			}
		}
		boolean loadData = false;
		for (int i = 0; i < fDir.size(); i++) {
			if (i == fDir.size() - 1) {
				loadData = true;
			}
			String colkeyid = fDir.get(i);
			Map<String, ArrayList> cresTerm = getCollectionDistributionMap(colkeyid);
			BaseIndexer.mergeTerms(resTerm, cresTerm, loadData, false);
		}
		return resTerm;
	}

	public HashMap<String, Integer> loadYearlyDistribution() {
		HashMap<String, Integer> yearsMap = new HashMap<String, Integer>();
		ArrayList<CompTermInfo> yearsList = getGlobalDistributionMap().get("year");
		Collections.sort(yearsList);
		for (int i = 0; i < yearsList.size(); i++) {
			CompTermInfo cTerm = yearsList.get(i);
			yearsMap.put(cTerm.getText(), cTerm.getDocFreq());
		}
		return yearsMap;
	}

	public void setTotDistCount(int totDistCount) {
		this.totDistCount = totDistCount;
	}

}