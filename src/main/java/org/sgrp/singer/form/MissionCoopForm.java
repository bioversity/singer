package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class MissionCoopForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String	coopcode;

	public CoopForm	coopForm;

	public String	misscode;

	public String	misscoopid;

	public String	repdate;

	public MissionCoopForm() {
		super();
	}

	public MissionCoopForm(String misscoopid) {
		super();
		this.misscoopid = misscoopid;
	}

	public String getCoopcode() {
		return coopcode;
	}

	public CoopForm getCoopForm() {
		return coopForm;
	}

	public String getMisscode() {
		return misscode;
	}

	public String getMisscoopid() {
		return misscoopid;
	}

	public String getRepdate() {
		return repdate;
	}

	public void setCoopcode(String coopcode) {
		this.coopcode = coopcode;
	}

	public void setCoopForm(CoopForm coopForm) {
		this.coopForm = coopForm;
	}

	public void setMisscode(String misscode) {
		this.misscode = misscode;
	}

	public void setMisscoopid(String misscoopid) {
		this.misscoopid = misscoopid;
	}

	public void setRepdate(String repdate) {
		this.repdate = repdate;
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
		System.out.println("In Mission Coop Form");
		ActionErrors errors = new ActionErrors();
		setSearchAttributes(request);
		if (errors.size() > 0) {
			status = AccessionConstants.FAILURE;
		} else {
			status = AccessionConstants.SUCCESS;
		}
		return errors;
	}

}
