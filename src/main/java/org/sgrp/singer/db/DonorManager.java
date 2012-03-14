package org.sgrp.singer.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.form.DonorForm;
import org.sgrp.singer.indexer.DonorIndex;

public class DonorManager extends DataManager {
	public static String			attribList	= AccessionConstants.STATUSCODE + "|" + AccessionConstants.TRUSTCODE + "|" + AccessionConstants.SOURCECODE + "|" + AccessionConstants.GENUSCODE + "|" + AccessionConstants.SPECIESCODE + "|"
													+ AccessionConstants.COLLECTIONCODE + "|" + AccessionConstants.COUNTRYCODE + "|" + "latlongd";

	protected static DonorManager	mgr;

	public static DonorManager getInstance() {
		if (mgr == null) {
			mgr = new DonorManager();
		}
		return mgr;
	}

	private DonorIndex	donorIndex	= DonorIndex.getInstance();

	public DonorForm getDonor(String donorid) throws Exception {
		if ((donorid == null) || (donorid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		DonorForm DonorForm = null;
		try {
			Map<String, String> list = donorIndex.search(donorIndex.ID + ":" + donorid, donorIndex.ID, donorIndex.NAME, null, true);
			if (list.size() > 0) {
				DonorForm = getDonorFromText(list.get(donorid));
			}
		} catch (Exception se) {
			System.out.println("getDonor String " + se.toString());
		}
		return DonorForm;
	}

	public DonorForm getDonorFromText(String data) throws Exception {
		DonorForm DonorForm = null;
		try {
			// System.out.println("Data "+data);
			DonorForm = new DonorForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			DonorForm.setUsercode(AccessionConstants.getRegExValue(data, AccessionConstants.USERCODE));
			DonorForm.setUsername(AccessionConstants.getRegExValue(data, AccessionConstants.USERNAME));
			DonorForm.setCtycode(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYCODE));
			DonorForm.setCtyname(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYNAME));
			DonorForm.setDonorcode(AccessionConstants.getRegExValue(data, "donorcode"));
			DonorForm.setLname(AccessionConstants.getRegExValue(data, "lname"));
			DonorForm.setFname(AccessionConstants.getRegExValue(data, "fname"));
			DonorForm.setDonororg(AccessionConstants.getRegExValue(data, "org"));
			DonorForm.setRepdate(AccessionConstants.getRegExValue(data, "repdate"));

		} catch (Exception se) {
			System.out.println("getDonor Document " + se.toString());
			se.printStackTrace();
		}
		return DonorForm;
	}

	public Map<String, DonorForm> searchDonor(String query) throws Exception {
		Map<String, DonorForm> data = new LinkedHashMap<String, DonorForm>();
		DonorForm DonorForm = null;
		try {
			Map<String, String> list = searchDonorString(query);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				DonorForm = getDonorFromText(list.get(id));
				data.put(id, DonorForm);
			}
		} catch (Exception se) {
			System.out.println("searchAccession" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchDonorString(String query) throws Exception {
		return donorIndex.search(query, AccessionConstants.CONTENTS, donorIndex.NAME, null, true);
	}
}
