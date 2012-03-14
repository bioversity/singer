package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class DistributionForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String			acccode;

	public String			accrecptid;

	public String			datetrans;

	public RecepientForm	recepientForm;

	public String			recptcode;

	public String			repdate;

	public DistributionForm() {
		super();
	}

	public DistributionForm(String accrecptid) {
		super();
		this.accrecptid = accrecptid;
	}

	public String getAcccode() {
		return acccode;
	}

	public String getAccrecptid() {
		return accrecptid;
	}

	public String getDatetrans() {
		return datetrans;
	}

	public RecepientForm getRecepientForm() {
		return recepientForm;
	}

	public String getRecptcode() {
		return recptcode;
	}

	public String getRepdate() {
		return repdate;
	}

	public void setAcccode(String acccode) {
		this.acccode = acccode;
	}

	public void setAccrecptid(String accrecptid) {
		this.accrecptid = accrecptid;
	}

	public void setDatetrans(String datetrans) {
		this.datetrans = datetrans;
	}

	public void setRecepientForm(RecepientForm recepientForm) {
		this.recepientForm = recepientForm;
	}

	public void setRecptcode(String recptcode) {
		this.recptcode = recptcode;
	}

	public void setRepdate(String repdate) {
		this.repdate = repdate;
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

}
