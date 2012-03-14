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
import org.sgrp.singer.form.AdditionalLinksForm;
import org.sgrp.singer.indexer.AdditionalLinksIndex;

public class AdditionalLinksManager extends DataManager {
	protected static AdditionalLinksManager	mgr;

	public static AdditionalLinksManager getInstance() {
		if (mgr == null) {
			mgr = new AdditionalLinksManager();
		}
		return mgr;
	}

	private AdditionalLinksIndex	addlIndex	= AdditionalLinksIndex.getInstance();

	public AdditionalLinksForm getAdditionalLinks(String missid) throws Exception {
		if ((missid == null) || (missid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		AdditionalLinksForm coopForm = null;
		try {
			Map<String, String> list = addlIndex.search(addlIndex.ID + ":" + missid, addlIndex.ID, addlIndex.NAME, null, true);
			if (list.size() > 0) {
				coopForm = getAdditionalLinksFromText(AccessionConstants.getRegExValue(list.get(missid),addlIndex.ID));
			}
		} catch (Exception se) {
			System.out.println("getAdditionalLinks String " + se.toString());
		}
		return coopForm;
	}

	public String getAdditionalLinksDirForColl(String collcode) {
		return addlIndex.getSearchDir(collcode).getAbsolutePath();
	}

	public HashMap<String, String> getAdditionalLinksDirForCollArray(String coll[]) {
		HashMap<String, String> dirs = new HashMap<String, String>();
		for (int i = 0; i < coll.length; i++) {
			try {
				if (isAdditionalLinksAvailableForColl(coll[i])) {
					String loc = addlIndex.getSearchDir(coll[i]).getAbsolutePath();
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

	public List<File> getAdditionalLinksDirs() {
		return addlIndex.getSearchDirs();

	}

	public String[] getAdditionalLinksDirsArray() {
		List<File> fileList = getAdditionalLinksDirs();
		String files[] = new String[fileList.size()];
		for (int i = 0; i < fileList.size(); i++) {
			files[i] = fileList.get(i).getAbsolutePath();
		}
		return files;

	}

	public AdditionalLinksForm getAdditionalLinksFromText(String data) throws Exception {
		AdditionalLinksForm distForm = null;
		try {
			// System.out.println("Data "+data);
			distForm = new AdditionalLinksForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			distForm.setAccid(AccessionConstants.getRegExValue(data, "accenumb"));
			distForm.setSyslinkid(AccessionConstants.getRegExValue(data, "syslinkid"));
			distForm.setSyslink(AccessionConstants.getRegExValue(data, "syslink"));
			distForm.setType(AccessionConstants.getRegExValue(data, "type"));
		} catch (Exception se) {
			System.out.println("getAdditionalLinksFromText Document " + se.toString());
			se.printStackTrace();
		}
		return distForm;
	}
	

	public boolean isAdditionalLinksAvailableForColl(String collcode) {
		return addlIndex.isIndexValid(addlIndex.getSearchDir(collcode).getAbsolutePath());
	}

	public Map<String, AdditionalLinksForm> searchAdditionalLinks(String query) throws Exception {
		return searchAdditionalLinks(query, null);
	}

	public Map<String, AdditionalLinksForm> searchAdditionalLinks(String query, String collcode) throws Exception {
		Map<String, AdditionalLinksForm> data = new LinkedHashMap<String, AdditionalLinksForm>();
		AdditionalLinksForm distForm = null;
		try {
			Map<String, String> list = searchAdditionalLinksString(query, collcode);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				distForm = getAdditionalLinksFromText(list.get(id));
				data.put(id, distForm);
			}
		} catch (Exception se) {
			System.out.println("searchAdditionalLinks" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchAdditionalLinksString(String query) throws Exception {
		return searchAdditionalLinksString(query, null);
	}

	public Map<String, String> searchAdditionalLinksString(String query, String collcode) throws Exception {
		String coll[] = null;
		if ((collcode != null) && (collcode.trim().length() > 0)) {
			coll = new String[1];
			coll[0] = collcode;
		}
		return addlIndex.search(query, AccessionConstants.CONTENTS, addlIndex.NAME, coll, true);
	}
}
