package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class MissionForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String	instcode;
	public String	ctycode;

	public String	ctyname;

	public String	edd;

	public String	edm;

	public String	edy;
	public String	misscode;
	public String	missid;

	public String	repdate;
	public String	sdd;
	public String	sdm;

	public String	sdy;

	public MissionForm() {
		super();
	}

	public MissionForm(String missid) {
		super();
		this.missid = missid;
	}

	public String getCtycode() {
		return ctycode;
	}

	public String getCtyname() {
		return ctyname;
	}

	public String getEdd() {
		return edd;
	}

	public String getEdm() {
		return edm;
	}

	public String getEdy() {
		return edy;
	}

	public String getEnddate() {
		String date = null;
		if (getEdy() != null && !(getEdy().equals("null") || getEdy().equals("0000"))) {
			date = getEdy();
			if (getEdm() != null && !(getEdm().equals("null") || getEdm().equals("0"))) {
				date = date + "/" + getEdm();
				if (getEdd() != null && !getEdd().equals("null")) {
					date = date + "/" + getEdd();
				}
			}
		}
		if (date == null)
			date = " No data ";
		return date;
	}

	public String getMisscode() {
		return misscode;
	}

	public String getMissid() {
		return missid;
	}

	public String getRepdate() {
		return repdate;
	}

	public String getSdd() {
		return sdd;
	}

	public String getSdm() {
		return sdm;
	}

	public String getSdy() {
		return sdy;
	}

	public String getStartdate() {
		String date = null;
		if (getSdy() != null && !(getSdy().equals("null") || getSdy().equals("0000"))) {
			date = getSdy();
			if (getSdm() != null && !(getSdm().equals("null") || getSdm().equals("0"))) {
				date = date + "/" + getSdm();
				if (getSdd() != null && !getSdd().equals("null")) {
					date = date + "/" + getSdd();
				}
			}
		}
		if (date == null)
			date = " No data";
		return date;
	}

	public void setCtycode(String ctycode) {
		this.ctycode = ctycode;
	}

	public void setCtyname(String ctyname) {
		this.ctyname = ctyname;
	}

	public void setEdd(String edd) {
		this.edd = edd;
	}

	public void setEdm(String edm) {
		this.edm = edm;
	}

	public void setEdy(String edy) {
		this.edy = edy;
	}

	public void setMisscode(String misscode) {
		this.misscode = misscode;
	}

	public void setMissid(String missid) {
		this.missid = missid;
	}

	public void setRepdate(String repdate) {
		this.repdate = repdate;
	}

	public void setSdd(String sdd) {
		this.sdd = sdd;
	}

	public void setSdm(String sdm) {
		this.sdm = sdm;
	}

	public void setSdy(String sdy) {
		this.sdy = sdy;
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
		System.out.println("In  Form");
		ActionErrors errors = new ActionErrors();
		setSearchAttributes(request);
		if (errors.size() > 0) {
			status = AccessionConstants.FAILURE;
		} else {
			status = AccessionConstants.SUCCESS;
		}
		return errors;
	}

	public String getInstcode() {
		return instcode;
	}

	public void setInstcode(String instcode) {
		this.instcode = instcode;
	}

}
