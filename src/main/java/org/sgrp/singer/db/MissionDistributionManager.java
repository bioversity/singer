package org.sgrp.singer.db;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.SearchResults;
import org.sgrp.singer.form.MissionDistributionForm;
import org.sgrp.singer.indexer.MissionDistributionIndex;

public class MissionDistributionManager extends DataManager {
	protected static MissionDistributionManager	mgr;

	public static MissionDistributionManager getInstance() {
		if (mgr == null) {
			mgr = new MissionDistributionManager();
		}
		return mgr;
	}

	private MissionDistributionIndex	mdistIndex	= MissionDistributionIndex.getInstance();

	public MissionDistributionForm getMissionDistribution(String mdistid) throws Exception {
		if ((mdistid == null) || (mdistid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		MissionDistributionForm coopForm = null;
		try {
			Map<String, String> list = mdistIndex.search(mdistIndex.ID + ":" + mdistid, mdistIndex.ID, mdistIndex.NAME, null, true);
			if (list.size() > 0) {
				coopForm = getMissionDistributionFromText(list.get(mdistid));
			}
		} catch (Exception se) {
			System.out.println("getMissionDist String " + se.toString());
		}
		return coopForm;
	}

	public String getMissionDistributionDirForColl(String collcode) {
		return mdistIndex.getSearchDir(collcode).getAbsolutePath();
	}

	public HashMap<String, String> getMissionDistributionDirForInstArray(String coll[]) {
		HashMap<String, String> dirs = new HashMap<String, String>();
		for (int i = 0; i < coll.length; i++) {
			try {
				if (isMissionDistributionAvailableForColl(coll[i])) {
					String loc = mdistIndex.getSearchDir(coll[i]).getAbsolutePath();
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

	public List<File> getMissionDistributionDirs() {
		return mdistIndex.getSearchDirs();

	}

	public String[] getMissionDistributionDirsArray() {
		List<File> fileList = getMissionDistributionDirs();
		String files[] = new String[fileList.size()];
		for (int i = 0; i < fileList.size(); i++) {
			files[i] = fileList.get(i).getAbsolutePath();
		}
		return files;

	}

	public MissionDistributionForm getMissionDistributionFromText(String data) throws Exception {
		MissionDistributionForm mdistForm = null;
		try {
			// System.out.println("Data "+data);
			mdistForm = new MissionDistributionForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			mdistForm.setMisscode(AccessionConstants.getRegExValue(data, "misscode"));
			mdistForm.setSp(AccessionConstants.getRegExValue(data, "sp"));
			mdistForm.setTinstcode(AccessionConstants.getRegExValue(data, "tinstcode"));
			mdistForm.setSamp(AccessionConstants.getRegExValue(data, "samp"));
			mdistForm.setFaocode(AccessionConstants.getRegExValue(data, "faocode"));
			mdistForm.setAcronym(AccessionConstants.getRegExValue(data, "acronym"));
			mdistForm.setName(AccessionConstants.getRegExValue(data, "name"));
			mdistForm.setStreet(AccessionConstants.getRegExValue(data, "street"));
			mdistForm.setCity(AccessionConstants.getRegExValue(data, "city"));
			mdistForm.setZip(AccessionConstants.getRegExValue(data, "zip"));
			mdistForm.setTlf(AccessionConstants.getRegExValue(data, "tlf"));
			mdistForm.setTlx(AccessionConstants.getRegExValue(data, "tlx"));
			mdistForm.setFax(AccessionConstants.getRegExValue(data, "fax"));
			mdistForm.setEmail(AccessionConstants.getRegExValue(data, "ema"));
		} catch (Exception se) {
			System.out.println("getMissDistributionFromText Document " + se.toString());
			se.printStackTrace();
		}
		return mdistForm;
	}

	public boolean isMissionDistributionAvailableForColl(String collcode) {
		return mdistIndex.isIndexValid(mdistIndex.getSearchDir(collcode).getAbsolutePath());
	}

	public Map<String, MissionDistributionForm> searchMissionDistribution(String query) throws Exception {
		return searchMissionDistribution(query, null);
	}

	public Map<String, MissionDistributionForm> searchMissionDistribution(String query, String collcode) throws Exception {
		Map<String, MissionDistributionForm> data = new LinkedHashMap<String, MissionDistributionForm>();
		MissionDistributionForm distForm = null;
		try {
			Map<String, String> list = searchMissionDistributionString(query, collcode);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				distForm = getMissionDistributionFromText(list.get(id));
				data.put(id, distForm);
			}
		} catch (Exception se) {
			System.out.println("searchDistribution" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchMissionDistributionString(String query) throws Exception {
		return searchMissionDistributionString(query, null);
	}

	public Map<String, String> searchMissionDistributionString(String query, String collcode) throws Exception {
		String coll[] = null;
		if ((collcode != null) && (collcode.trim().length() > 0)) {
			coll = new String[1];
			coll[0] = collcode;
		}
		return mdistIndex.search(query, AccessionConstants.CONTENTS, mdistIndex.NAME, coll, true);
	}
}
