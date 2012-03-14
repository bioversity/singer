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
import org.sgrp.singer.form.MissionCollectionForm;
import org.sgrp.singer.indexer.MissionCollectionIndex;

public class MissionCollectionManager extends DataManager {
	protected static MissionCollectionManager	mgr;

	public static MissionCollectionManager getInstance() {
		if (mgr == null) {
			mgr = new MissionCollectionManager();
		}
		return mgr;
	}

	private MissionCollectionIndex	mcollIndex	= MissionCollectionIndex.getInstance();

	public MissionCollectionForm getMissionCollection(String mdistid) throws Exception {
		if ((mdistid == null) || (mdistid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		MissionCollectionForm coopForm = null;
		try {
			Map<String, String> list = mcollIndex.search(mcollIndex.ID + ":" + mdistid, mcollIndex.ID, mcollIndex.NAME, null, true);
			if (list.size() > 0) {
				coopForm = getMissionCollectionFromText(list.get(mdistid));
			}
		} catch (Exception se) {
			System.out.println("getMissionDist String " + se.toString());
		}
		return coopForm;
	}

	public String getMissionCollectionDirForColl(String collcode) {
		return mcollIndex.getSearchDir(collcode).getAbsolutePath();
	}

	public HashMap<String, String> getMissionCollectionDirForInstArray(String coll[]) {
		HashMap<String, String> dirs = new HashMap<String, String>();
		for (int i = 0; i < coll.length; i++) {
			try {
				if (isMissionCollectionAvailableForColl(coll[i])) {
					String loc = mcollIndex.getSearchDir(coll[i]).getAbsolutePath();
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

	public List<File> getMissionCollectionDirs() {
		return mcollIndex.getSearchDirs();

	}

	public String[] getMissionCollectionDirsArray() {
		List<File> fileList = getMissionCollectionDirs();
		String files[] = new String[fileList.size()];
		for (int i = 0; i < fileList.size(); i++) {
			files[i] = fileList.get(i).getAbsolutePath();
		}
		return files;

	}

	public MissionCollectionForm getMissionCollectionFromText(String data) throws Exception {
		MissionCollectionForm mdistForm = null;
		try {
			mdistForm = new MissionCollectionForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			mdistForm.setMisscode(AccessionConstants.getRegExValue(data, "misscode"));
			mdistForm.setSp(AccessionConstants.getRegExValue(data, "sp"));
			mdistForm.setSamp(AccessionConstants.getRegExValue(data, "samp"));
		} catch (Exception se) {
			System.out.println("getMissCollectionFromText Document " + se.toString());
			se.printStackTrace();
		}
		return mdistForm;
	}

	public boolean isMissionCollectionAvailableForColl(String collcode) {
		return mcollIndex.isIndexValid(mcollIndex.getSearchDir(collcode).getAbsolutePath());
	}

	public Map<String, MissionCollectionForm> searchMissionCollection(String query) throws Exception {
		return searchMissionCollection(query, null);
	}

	public Map<String, MissionCollectionForm> searchMissionCollection(String query, String collcode) throws Exception {
		Map<String, MissionCollectionForm> data = new LinkedHashMap<String, MissionCollectionForm>();
		MissionCollectionForm distForm = null;
		try {
			Map<String, String> list = searchMissionCollectionString(query, collcode);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				distForm = getMissionCollectionFromText(list.get(id));
				data.put(id, distForm);
			}
		} catch (Exception se) {
			System.out.println("searchCollection" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchMissionCollectionString(String query) throws Exception {
		return searchMissionCollectionString(query, null);
	}

	public Map<String, String> searchMissionCollectionString(String query, String collcode) throws Exception {
		String coll[] = null;
		if ((collcode != null) && (collcode.trim().length() > 0)) {
			coll = new String[1];
			coll[0] = collcode;
		}
		return mcollIndex.search(query, AccessionConstants.CONTENTS, mcollIndex.NAME, coll, true);
	}
}
