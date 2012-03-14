package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class AdditionalLinksForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String	accid;

	public String	accsysid;

	public String	syslink;

	public String	syslinkid;

	public String	type;

	public AdditionalLinksForm() {
		super();
	}

	public AdditionalLinksForm(String accsysid) {
		super();
		this.accsysid = accsysid;
	}

	public String getAccid() {
		return accid;
	}

	public String getAccsysid() {
		return accsysid;
	}

	public String getSyslink() {
		return syslink;
	}

	public String getSyslinkid() {
		return syslinkid;
	}

	public String getType() {
		return type;
	}

	public void setAccid(String accid) {
		this.accid = accid;
	}

	public void setAccsysid(String accsysid) {
		this.accsysid = accsysid;
	}

	public void setSyslink(String syslink) {
		this.syslink = syslink;
	}

	public void setSyslinkid(String syslinkid) {
		this.syslinkid = syslinkid;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
		System.out.println("In Additional Links Form");
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
