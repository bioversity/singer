package org.sgrp.singer.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.form.CoopForm;
import org.sgrp.singer.indexer.CoopIndex;

public class CoopManager extends DataManager {
	public static String			attribList	= AccessionConstants.STATUSCODE + "|" + AccessionConstants.TRUSTCODE + "|" + AccessionConstants.SOURCECODE + "|" + AccessionConstants.GENUSCODE + "|" + AccessionConstants.SPECIESCODE + "|"
													+ AccessionConstants.COLLECTIONCODE + "|" + AccessionConstants.COUNTRYCODE + "|" + "latlongd";

	protected static CoopManager	mgr;

	public static CoopManager getInstance() {
		if (mgr == null) {
			mgr = new CoopManager();
		}
		return mgr;
	}

	private CoopIndex	coopIndex	= CoopIndex.getInstance();

	public CoopForm getCoop(String coopid) throws Exception {
		if ((coopid == null) || (coopid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		CoopForm coopForm = null;
		try {
			Map<String, String> list = coopIndex.search(coopIndex.ID + ":" + coopid, coopIndex.ID, coopIndex.NAME, null, true);
			if (list.size() > 0) {
				coopForm = getCoopFromText(list.get(coopid));
			}
		} catch (Exception se) {
			System.out.println("getCoop String " + se.toString());
		}
		return coopForm;
	}

	public CoopForm getCoopFromText(String data) throws Exception {
		CoopForm coopForm = null;
		try {
			// System.out.println("Data "+data);
			coopForm = new CoopForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			coopForm.setUsercode(AccessionConstants.getRegExValue(data, AccessionConstants.USERCODE));
			coopForm.setUsername(AccessionConstants.getRegExValue(data, AccessionConstants.USERNAME));
			coopForm.setCtycode(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYCODE));
			coopForm.setCtyname(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYNAME));
			coopForm.setCoopcode(AccessionConstants.getRegExValue(data, "coopcode"));
			coopForm.setLname(AccessionConstants.getRegExValue(data, "lname"));
			coopForm.setFname(AccessionConstants.getRegExValue(data, "fname"));
			coopForm.setCooporg(AccessionConstants.getRegExValue(data, "org"));
			coopForm.setRepdate(AccessionConstants.getRegExValue(data, "repdate"));

		} catch (Exception se) {
			System.out.println("getCoopFromText Document " + se.toString());
			se.printStackTrace();
		}
		return coopForm;
	}

	public Map<String, CoopForm> searchCoop(String query) throws Exception {
		Map<String, CoopForm> data = new LinkedHashMap<String, CoopForm>();
		CoopForm coopForm = null;
		try {
			Map<String, String> list = searchCoopString(query);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				coopForm = getCoopFromText(list.get(id));
				data.put(id, coopForm);
			}
		} catch (Exception se) {
			System.out.println("searchAccession" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchCoopString(String query) throws Exception {
		return coopIndex.search(query, AccessionConstants.CONTENTS, coopIndex.NAME, null, true);
	}
}
