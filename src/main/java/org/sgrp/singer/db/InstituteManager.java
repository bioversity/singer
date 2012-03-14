package org.sgrp.singer.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.form.InstituteForm;
import org.sgrp.singer.indexer.InstituteIndex;

public class InstituteManager extends DataManager {
	protected static InstituteManager	mgr;

	public static InstituteManager getInstance() {
		if (mgr == null) {
			mgr = new InstituteManager();
		}
		return mgr;
	}

	private InstituteIndex	instIndex	= InstituteIndex.getInstance();

	public InstituteForm getInstitute(String instid) throws Exception {
		if ((instid == null) || (instid.length() == 0)) { throw new Exception(ResourceManager.getString("retrieve.Accession.null")); }
		InstituteForm instForm = null;
		try {
			Map<String, String> list = instIndex.search(instIndex.ID + ":" + instid, instIndex.ID, instIndex.NAME, null, true);
			if (list.size() > 0) {
				instForm = getInstituteFromText(list.get(instid));
			}
		} catch (Exception se) {
			System.out.println("getInstitute String " + se.toString());
		}
		return instForm;
	}

	public InstituteForm getInstituteFromText(String data) throws Exception {
		InstituteForm instForm = null;
		try {
			// System.out.println("Data "+data);
			instForm = new InstituteForm(AccessionConstants.getRegExValue(data, AccessionConstants.ID));
			instForm.setCtycode(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYCODE));
			instForm.setCtyname(AccessionConstants.getRegExValue(data, AccessionConstants.COUNTRYNAME));
			instForm.setName(AccessionConstants.getRegExValue(data, instIndex.NAME));
			instForm.setFaocode(AccessionConstants.getRegExValue(data, "faocode"));
			instForm.setFullname(AccessionConstants.getRegExValue(data, "fullname"));
			instForm.setAddress(AccessionConstants.getRegExValue(data, "address"));
			instForm.setPhone(AccessionConstants.getRegExValue(data, "phone"));
			instForm.setFax(AccessionConstants.getRegExValue(data, "fax"));
			instForm.setEmail(AccessionConstants.getRegExValue(data, "email"));
			instForm.setUrl(AccessionConstants.getRegExValue(data, "url"));
			instForm.setContact(AccessionConstants.getRegExValue(data, "contact"));
			instForm.setCaddress(AccessionConstants.getRegExValue(data, "caddress"));
			instForm.setCemail(AccessionConstants.getRegExValue(data, "cemail"));
			instForm.setLogo(AccessionConstants.getRegExValue(data, "logo"));

		} catch (Exception se) {
			System.out.println("getInstituteFromText Document " + se.toString());
			se.printStackTrace();
		}
		return instForm;
	}

	public Map<String, InstituteForm> searchInstitute(String query) throws Exception {
		Map<String, InstituteForm> data = new LinkedHashMap<String, InstituteForm>();
		InstituteForm instForm = null;
		try {
			Map<String, String> list = searchInstituteString(query);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				instForm = getInstituteFromText(list.get(id));
				data.put(id, instForm);
			}
		} catch (Exception se) {
			System.out.println("searchInstitute" + se.toString());
		}
		return data;
	}

	public Map<String, String> searchInstituteString(String query) throws Exception {
		return instIndex.search(query, AccessionConstants.CONTENTS, instIndex.NAME, null, true);
	}
}
