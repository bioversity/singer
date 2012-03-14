package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

@SuppressWarnings("serial")
public class DonorForm extends GenericForm {

	public static void setSearchAttributes(HttpServletRequest request) {
	}

	public String	ctycode;

	public String	ctyname;

	public String	donorcode;

	public String	donorid;

	public String	donororg;

	public String	fname;

	public String	lname;

	public String	repdate;

	public String	usercode;

	public String	username;

	public DonorForm() {
		super();
	}

	public DonorForm(String donorid) {
		super();
		this.donorid = donorid;
	}

	public String getCtycode() {
		return ctycode;
	}

	public String getCtyname() {
		return ctyname;
	}

	public String getDonorcode() {
		return donorcode;
	}

	public String getDonorid() {
		return donorid;
	}

	public String getDonorname() {
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

	public String getDonororg() {
		return donororg;
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

	public void setCtycode(String ctycode) {
		this.ctycode = ctycode;
	}

	public void setCtyname(String ctyname) {
		this.ctyname = ctyname;
	}

	public void setDonorcode(String donorcode) {
		this.donorcode = donorcode;
	}

	public void setDonorid(String donorid) {
		this.donorid = donorid;
	}

	public void setDonororg(String donororg) {
		if ((donororg != null) && !donororg.equals("null")) {
			this.donororg = donororg;
		} else {
			this.donororg = null;
		}
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
