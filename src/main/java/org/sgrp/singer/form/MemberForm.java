package org.sgrp.singer.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.SearchResults;
import org.sgrp.singer.db.UserManager;

@SuppressWarnings("serial")
public class MemberForm extends GenericForm {

	public String	nuserid		= null;

	public String	nfname		= null;

	public String	nlname		= null;

	public String	niname		= null;

	public String	nstreet		= null;
	public String	nzip		= null;
	public String	ncity		= null;
	public String	nctycode	= null;
	public String	nusercode	= null;
	public String	nurl		= null;
	public String	nfaocode	= null;
	public String	npassword	= null;

	public String	nconfimpass	= null;

	public String	nemail		= null;

	public MemberForm() {
		super();
	}

	public MemberForm(String nuserid) {
		super();
		this.nuserid = nuserid;
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		ActionErrors errors = new ActionErrors();
		if (isNull(getNuserid())) {
			errors.add("euserid", new ActionMessage("null.check", "User ID"));
		} else if (getAction().equals(AccessionConstants.ACTION_ADD)) {
			if (UserManager.getInstance().userExists(getNuserid())) {
				errors.add("euserid", new ActionMessage("errors.userid.exists"));
			}
		}
		if (isNull(getNpassword())) {
			errors.add("epassword", new ActionMessage("null.check", "Password"));
		} else {
			if (isNull(getNconfimpass())) {
				errors.add("econfirmpass", new ActionMessage("null.check", "Confirm Password"));
			} else {
				if (!getNpassword().equals(getNconfimpass())) {
					errors.add("econfirmpass", new ActionMessage("errors.confirmpassword.notequal"));
				}
			}
		}

		if (isNull(getNfname())) {
			errors.add("efname", new ActionMessage("null.check", "First Name"));
		}
		if (isNull(getNlname())) {
			errors.add("elname", new ActionMessage("null.check", "Last Name"));
		}

		if (isNull(getNstreet())) {
			errors.add("estreet", new ActionMessage("null.check", "Street"));
		}
		if (isNull(getNzip())) {
			errors.add("ezip", new ActionMessage("null.check", "Zip code"));
		}
		if (isNull(getNcity())) {
			errors.add("ecity", new ActionMessage("null.check", "City"));
		}
		if (isNull(getNemail())) {
			errors.add("eemail", new ActionMessage("null.check", "Email"));
		} else
		{
			if (!getNemail().matches(EMAIL_REGEX)) {
				errors.add("eemail", new ActionMessage("invalid.check"," Email"));
			}
		}
		if(!isNull(getNurl()))
		{
			if(!getNurl().matches(URL_REGEX))
			{
				errors.add("eurl", new ActionMessage("invalid.check", "Url"));
			}
		}
		
		setSearchAttributes(httpServletRequest);
		if (errors.size() > 0) {
			status = AccessionConstants.FAILURE;
			if (getAction().equals(AccessionConstants.ACTION_EDIT)) {
				httpServletRequest.setAttribute("membForm", this);
			}
		} else {
			status = AccessionConstants.SUCCESS;
		}
		return errors;
	}

	public static void setSearchAttributes(HttpServletRequest request) {
		request.setAttribute("countries", AccessionServlet.getKeywords().getAllOrigMap());		
		request.setAttribute("users", AccessionServlet.getKeywords().getAllUserMap());
	}

	public String getNconfimpass() {
		return nconfimpass;
	}

	public void setNconfimpass(String nconfimpass) {
		this.nconfimpass = nconfimpass;
	}

	public String getNemail() {
		return nemail;
	}

	public void setNemail(String nemail) {
		this.nemail = nemail;
	}

	public String getNpassword() {
		return npassword;
	}

	public void setNpassword(String npassword) {
		this.npassword = npassword;
	}

	public String getNfname() {
		return nfname;
	}

	public void setNfname(String nfname) {
		this.nfname = nfname;
	}

	public String getNlname() {
		return nlname;
	}

	public void setNlname(String nlname) {
		this.nlname = nlname;
	}

	public String getNuserid() {
		return nuserid;
	}

	public void setNuserid(String nuserid) {
		this.nuserid = nuserid;
	}

	public String getNiname() {
		return niname;
	}

	public void setNiname(String niname) {
		this.niname = niname;
	}

	public String getNstreet() {
		return nstreet;
	}

	public void setNstreet(String nstreet) {
		this.nstreet = nstreet;
	}

	public String getNzip() {
		return nzip;
	}

	public void setNzip(String nzip) {
		this.nzip = nzip;
	}

	public String getNcity() {
		return ncity;
	}

	public void setNcity(String ncity) {
		this.ncity = ncity;
	}

	public String getNctycode() {
		return nctycode;
	}

	public void setNctycode(String nctycode) {
		this.nctycode = nctycode;
	}

	public String getNusercode() {
		return nusercode;
	}

	public void setNusercode(String nusercode) {
		this.nusercode = nusercode;
	}

	public String getNurl() {
		return nurl;
	}

	public void setNurl(String nurl) {
		this.nurl = nurl;
	}

	public String getNfaocode() {
		return nfaocode;
	}

	public void setNfaocode(String nfaocode) {
		this.nfaocode = nfaocode;
	}
}
