package org.sgrp.singer.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.SearchResults;
import org.sgrp.singer.form.MissionCoopForm;
import org.sgrp.singer.indexer.MissionCoopIndex;

public class MissionCoopManager extends DataManager {
	protected static MissionCoopManager	mgr;

	public static MissionCoopManager getInstance() {
		if (mgr == null) {
			mgr = new MissionCoopManager();
		}
		return mgr;
	}

	private MissionCoopIndex	misscoopIndex	= MissionCoopIndex.getInstance();

	public MissionCoopForm getMissionCoop(String missid) throws Exception {
		if ((missid == null) || (missid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		MissionCoopForm coopForm = null;
		try {
			Map<String, String> list = misscoopIndex.search(misscoopIndex.ID + ":" + missid, misscoopIndex.ID, misscoopIndex.NAME, null, true);
			if (list.size() > 0) {
				coopForm = getMissionCoopFromText(list.get(missid));
			}
		} catch (Exception se) {
			System.out.println("getMission String " + se.toString());
		}
		return coopForm;
	}

	public MissionCoopForm getMissionCoopFromText(String data) throws Exception {
		MissionCoopForm missForm = null;
		try {
			// System.out.println("Data "+data);
			missForm = new MissionCoopForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			missForm.setMisscode(AccessionConstants.getRegExValue(data, "misscode"));
			missForm.setCoopcode(AccessionConstants.getRegExValue(data, "coopcode"));
			missForm.setRepdate(AccessionConstants.getRegExValue(data, "repdate"));
			missForm.setCoopForm(SearchResults.getInstance().getCoop(missForm.getCoopcode()));
		} catch (Exception se) {
			System.out.println("getCoopFromText Document " + se.toString());
			se.printStackTrace();
		}
		return missForm;
	}

	public Map<String, MissionCoopForm> searchMissionCoop(String query) throws Exception {
		Map<String, MissionCoopForm> data = new LinkedHashMap<String, MissionCoopForm>();
		MissionCoopForm misscoopForm = null;
		try {
			Map<String, String> list = searchMissionCoopString(query);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				misscoopForm = getMissionCoopFromText(list.get(id));
				data.put(id, misscoopForm);
			}
		} catch (Exception se) {
			System.out.println("searchAccession" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchMissionCoopString(String query) throws Exception {
		return misscoopIndex.search(query, AccessionConstants.CONTENTS, misscoopIndex.NAME, null, true);
	}
}
