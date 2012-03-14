package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class MissionCollectionForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String	code;
	public String	misscode;
	public String	sp;
	public String	samp;

	public MissionCollectionForm() {
		super();
	}

	public MissionCollectionForm(String code) {
		super();
		this.code = code;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	public String getSamp() {
		return samp;
	}

	public void setSamp(String samp) {
		this.samp = samp;
	}

	public String getMisscode() {
		return misscode;
	}

	public void setMisscode(String misscode) {
		this.misscode = misscode;
	}

}
