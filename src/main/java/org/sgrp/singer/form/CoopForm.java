package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class CoopForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String	collname;

	public String	coopcode;

	public String	coopid;

	public String	cooporg;

	public String	ctycode;

	public String	ctyname;

	public String	fname;

	public String	lname;

	public String	repdate;

	public String	usercode;

	public String	username;

	public CoopForm() {
		super();
	}

	public CoopForm(String coopid) {
		super();
		this.coopid = coopid;
	}

	public String getCollname() {
		return collname;
	}

	public String getCoopcode() {
		return coopcode;
	}

	public String getCoopid() {
		return coopid;
	}

	public String getCoopname() {
		String name = null;
		if ((getLname() != null) && (getLname().trim().length() > 0)) {
			name = getLname();
		}
		if ((getFname() != null) && (getFname().trim().length() > 0)) {
			if ((name != null) && (name.trim().length() > 0)) {
				name = name + "," + getFname();
			} else {
				name = getFname();
			}
		}
		return name;
	}

	public String getCooporg() {
		return cooporg;
	}

	public String getCtycode() {
		return ctycode;
	}

	public String getCtyname() {
		return ctyname;
	}

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	public String getRepdate() {
		return repdate;
	}

	public String getUsercode() {
		return usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setCollname(String collname) {
		this.collname = collname;
	}

	public void setCoopcode(String coopcode) {
		this.coopcode = coopcode;
	}

	public void setCoopid(String coopid) {
		this.coopid = coopid;
	}

	public void setCooporg(String cooporg) {
		if ((cooporg != null) && !cooporg.equals("null")) {
			this.cooporg = cooporg;
		} else {
			this.cooporg = null;
		}
	}

	public void setCtycode(String ctycode) {
		this.ctycode = ctycode;
	}

	public void setCtyname(String ctyname) {
		this.ctyname = ctyname;
	}

	public void setFname(String fname) {
		if ((fname != null) && !fname.equals("null")) {
			this.fname = fname;
		} else {
			this.fname = null;
		}
	}

	public void setLname(String lname) {
		if ((lname != null) && !lname.equals("null")) {
			this.lname = lname;
		} else {
			this.lname = null;
		}
	}

	public void setRepdate(String repdate) {
		this.repdate = repdate;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public void setUsername(String username) {
		this.username = username;
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
