package org.sgrp.singer.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.form.RecepientForm;
import org.sgrp.singer.indexer.RecepientIndex;

public class RecepientManager extends DataManager {
	public static String				attribList	= AccessionConstants.STATUSCODE + "|" + AccessionConstants.TRUSTCODE + "|" + AccessionConstants.SOURCECODE + "|" + AccessionConstants.GENUSCODE + "|" + AccessionConstants.SPECIESCODE + "|"
														+ AccessionConstants.COLLECTIONCODE + "|" + AccessionConstants.COUNTRYCODE + "|" + "latlongd";

	protected static RecepientManager	mgr;

	public static RecepientManager getInstance() {
		if (mgr == null) {
			mgr = new RecepientManager();
		}
		return mgr;
	}

	private RecepientIndex	recepientIndex	= RecepientIndex.getInstance();

	public RecepientForm getRecepient(String recepientid) throws Exception {
		if ((recepientid == null) || (recepientid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		RecepientForm RecepientForm = null;
		try {
			Map<String, String> list = recepientIndex.search(recepientIndex.ID + ":" + recepientid, recepientIndex.ID, recepientIndex.NAME, null, true);
			if (list.size() > 0) {
				RecepientForm = getRecepientFromText(list.get(recepientid));
			}
		} catch (Exception se) {
			System.out.println("getRecepient String " + se.toString());
		}
		return RecepientForm;
	}

	public RecepientForm getRecepientFromText(String data) throws Exception {
		RecepientForm RecepientForm = null;
		try {
			// System.out.println("Data "+data);
			RecepientForm = new RecepientForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			RecepientForm.setUsercode(AccessionConstants.getRegExValue(data, AccessionConstants.USERCODE));
			RecepientForm.setUsername(AccessionConstants.getRegExValue(data, AccessionConstants.USERNAME));
			RecepientForm.setCtycode(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYCODE));
			RecepientForm.setCtyname(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYNAME));
			RecepientForm.setRecepientcode(AccessionConstants.getRegExValue(data, "recpcode"));
			RecepientForm.setLname(AccessionConstants.getRegExValue(data, "lname"));
			RecepientForm.setFname(AccessionConstants.getRegExValue(data, "fname"));
			RecepientForm.setRecepientorg(AccessionConstants.getRegExValue(data, "org"));
			RecepientForm.setRepdate(AccessionConstants.getRegExValue(data, "repdate"));

		} catch (Exception se) {
			System.out.println("getCapacityBuilding Document " + se.toString());
			se.printStackTrace();
		}
		return RecepientForm;
	}

	public Map<String, RecepientForm> searchRecepient(String query) throws Exception {
		Map<String, RecepientForm> data = new LinkedHashMap<String, RecepientForm>();
		RecepientForm RecepientForm = null;
		try {
			Map<String, String> list = searchRecepientString(query);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				RecepientForm = getRecepientFromText(list.get(id));
				data.put(id, RecepientForm);
			}
		} catch (Exception se) {
			System.out.println("searchAccession" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchRecepientString(String query) throws Exception {
		return recepientIndex.search(query, AccessionConstants.CONTENTS, recepientIndex.NAME, null, true);
	}
}
