package org.sgrp.singer.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ObjectStore;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.form.MissionForm;
import org.sgrp.singer.indexer.MissionIndex;

public class MissionManager extends DataManager {
	protected static MissionManager	mgr;
	protected static final String	objType	= "cmission";

	public static MissionManager getInstance() {
		if (mgr == null) {
			mgr = new MissionManager();
		}
		return mgr;
	}

	private MissionIndex	missIndex	= MissionIndex.getInstance();

	public MissionForm getMission(String missid) throws Exception {
		if ((missid == null) || (missid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		MissionForm coopForm = null;
		try {
			Map<String, String> list = missIndex.search(missIndex.ID + ":" + missid, missIndex.ID, missIndex.NAME, null, true);
			if (list.size() > 0) {
				coopForm = getMissionFromText(list.get(missid));
			}
		} catch (Exception se) {
			System.out.println("getMission String " + se.toString());
		}
		return coopForm;
	}

	public MissionForm getMissionFromText(String data) throws Exception {
		MissionForm missForm = null;
		try {
			// System.out.println("Data "+data);
			missForm = new MissionForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			missForm.setCtycode(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYCODE));
			missForm.setCtyname(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYNAME));
			missForm.setMisscode(AccessionConstants.getRegExValue(data, "misscode"));
			missForm.setInstcode(AccessionConstants.getRegExValue(data, AccessionConstants.INSTITUTECODE));
			missForm.setSdd(AccessionConstants.getRegExValue(data, "sdd"));
			missForm.setSdm(AccessionConstants.getRegExValue(data, "sdm"));
			missForm.setSdy(AccessionConstants.getRegExValue(data, "sdy"));
			missForm.setEdd(AccessionConstants.getRegExValue(data, "edd"));
			missForm.setEdm(AccessionConstants.getRegExValue(data, "edm"));
			missForm.setEdy(AccessionConstants.getRegExValue(data, "edy"));
			missForm.setRepdate(AccessionConstants.getRegExValue(data, "repdate"));
		} catch (Exception se) {
			System.out.println("getCoopFromText Document " + se.toString());
			se.printStackTrace();
		}
		return missForm;
	}

	public Map<String, MissionForm> searchMission(String query) throws Exception {
		Map<String, MissionForm> data = (Map<String, MissionForm>) ObjectStore.getObject(objType, query);
		if (data == null) {
			data = new LinkedHashMap<String, MissionForm>();
			MissionForm missForm = null;
			try {
				Map<String, String> list = searchMissionString(query);
				for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
					String id = iter.next();
					missForm = getMissionFromText(list.get(id));
					data.put(id, missForm);
				}
				ObjectStore.storeObject(objType, query, data);
			} catch (Exception se) {
				System.out.println("searchMission" + se.toString());
			}
		}

		return data;
	}

	/*Added by Gautier to create a pagination of Collecting Missions*/
	public Map<String, MissionForm> searchMission(String query,int from, int to) throws Exception {
		Map data;
	
		data = new LinkedHashMap<String, MissionForm>();
		MissionForm missForm = null;
		try {
			Map<String, String> list = searchMissionString(query, from, to);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				missForm = getMissionFromText(list.get(id));
				data.put(id, missForm);
			}
		} catch (Exception se) {
			System.out.println("searchMission" + se.toString());
		}
	

		return data;
	}
	
	public Map<String, String> searchMissionString(String query) throws Exception {
		return missIndex.search(query, AccessionConstants.CONTENTS, missIndex.NAME, null, true);
	}
	
	public Map<String, String> searchMissionString(String query, int from, int to) throws Exception {
		return missIndex.search(query, AccessionConstants.CONTENTS, missIndex.NAME, null, true, from, to);
	}
}
