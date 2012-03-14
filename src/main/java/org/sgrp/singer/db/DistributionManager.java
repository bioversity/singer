package org.sgrp.singer.db;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.SearchResults;
import org.sgrp.singer.form.DistributionForm;
import org.sgrp.singer.indexer.DistributionIndex;

public class DistributionManager extends DataManager {
	protected static DistributionManager	mgr;

	public static DistributionManager getInstance() {
		if (mgr == null) {
			mgr = new DistributionManager();
		}
		return mgr;
	}

	private DistributionIndex	distIndex	= DistributionIndex.getInstance();

	public DistributionForm getDistribution(String missid) throws Exception {
		if ((missid == null) || (missid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		DistributionForm coopForm = null;
		try {
			Map<String, String> list = distIndex.search(distIndex.ID + ":" + missid, distIndex.ID, distIndex.NAME, null, true);
			if (list.size() > 0) {
				coopForm = getDistributionFromText(list.get(missid));
			}
		} catch (Exception se) {
			System.out.println("getMission String " + se.toString());
		}
		return coopForm;
	}

	public String getDistributionDirForColl(String collcode) {
		return distIndex.getSearchDir(collcode).getAbsolutePath();
	}

	public HashMap<String, String> getDistributionDirForCollArray(String coll[]) {
		HashMap<String, String> dirs = new HashMap<String, String>();
		for (int i = 0; i < coll.length; i++) {
			try {
				if (isDistributionAvailableForColl(coll[i])) {
					String loc = distIndex.getSearchDir(coll[i]).getAbsolutePath();
					if (loc != null && loc.trim().length() > 0) {

						dirs.put(coll[i], loc);
					}
				} else {
					// System.out.println("Index is not valid for " + coll[i]);
				}
			} catch (Exception e) {
			}
		}
		return dirs;
	}

	public List<File> getDistributionDirs() {
		return distIndex.getSearchDirs();

	}

	public String[] getDistributionDirsArray() {
		List<File> fileList = getDistributionDirs();
		String files[] = new String[fileList.size()];
		for (int i = 0; i < fileList.size(); i++) {
			files[i] = fileList.get(i).getAbsolutePath();
		}
		return files;

	}

	public DistributionForm getDistributionFromText(String data) throws Exception {
		DistributionForm distForm = null;
		try {
			// System.out.println("Data "+data);
			distForm = new DistributionForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			distForm.setAcccode(AccessionConstants.getRegExValue(data, "accenumb"));
			distForm.setRecptcode(AccessionConstants.getRegExValue(data, "recptcode"));
			distForm.setDatetrans(AccessionConstants.getRegExValue(data, "datetrans"));
			distForm.setRepdate(AccessionConstants.getRegExValue(data, "repdate"));
			distForm.setRecepientForm(SearchResults.getInstance().getRecepient(distForm.getRecptcode()));
		} catch (Exception se) {
			System.out.println("getDistributionFromText Document " + se.toString());
			se.printStackTrace();
		}
		return distForm;
	}

	public boolean isDistributionAvailableForColl(String collcode) {
		return distIndex.isIndexValid(distIndex.getSearchDir(collcode).getAbsolutePath());
	}

	public Map<String, DistributionForm> searchDistribution(String query) throws Exception {
		return searchDistribution(query, null);
	}

	public Map<String, DistributionForm> searchDistribution(String query, String collcode) throws Exception {
		Map<String, DistributionForm> data = new LinkedHashMap<String, DistributionForm>();
		DistributionForm distForm = null;
		try {
			Map<String, String> list = searchDistributionString(query, collcode);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				distForm = getDistributionFromText(list.get(id));
				data.put(id, distForm);
			}
		} catch (Exception se) {
			System.out.println("searchDistribution" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchDistributionString(String query) throws Exception {
		return searchDistributionString(query, null);
	}

	public Map<String, String> searchDistributionString(String query, String collcode) throws Exception {
		String coll[] = null;
		if ((collcode != null) && (collcode.trim().length() > 0)) {
			coll = new String[1];
			coll[0] = collcode;
		}
		return distIndex.search(query, AccessionConstants.CONTENTS, distIndex.NAME, coll, true);
	}
	
	/**
	 * This method process the query and return a filter to put in another query
	 * 
	 * 
	 * 
	 * @param query The query to process
	 * @param sourceField The Field to recover the text
	 * @param targetField The Field to add in the filter
	 * @return
	 */
	/*public Filter createFilter(String query, String sourceField,String targetField)
	{
		First we process the query
		Map<String, String> results = searchDistributionString(query);
		
		We create an empty filter
		Filter filter = new QueryWrapperFilter(query);
		We loop on the result 
		for(Iterator<String> ite = results.keySet().iterator();ite.hasNext(); )
		{
			String key = ite.next();
			String value = results.get(key)
		}
		
		
	}*/
}
