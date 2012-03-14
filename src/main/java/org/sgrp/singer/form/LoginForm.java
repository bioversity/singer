package org.sgrp.singer.form;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ErrorConstants;
import org.sgrp.singer.db.UserManager;

@SuppressWarnings("serial")
public class LoginForm extends GenericForm {
	public LoginForm() {
		super();
	}

	private String	upassword;

	private String	userid;

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		boolean valid = true;
		ActionErrors errors = new ActionErrors();
		if (isNull(getUserid())) {
			errors.add("euserid", ErrorConstants.getActionMessage(ErrorConstants.ERROR_USERID_NULL));
		}
		if (isNull(getUpassword())) {
			errors.add("epassword", ErrorConstants.getActionMessage(ErrorConstants.ERROR_PASSWORD_NULL));
		}

		if (!isNull(getUserid()) && !isNull(getUpassword())) {
			valid = UserManager.getInstance().loginValid(getUserid(), getUpassword());
			if (!valid) {
				errors.add("error", ErrorConstants.getActionMessage(ErrorConstants.ERROR_INVALID_LOGIN));
			}
		}
		if (errors.size() > 0) {
			status = AccessionConstants.FAILURE;
		} else if (valid) {
			boolean loggedIn = doLogin(httpServletRequest, getUserid(), getUpassword());
			if(loggedIn)
			{
				status = AccessionConstants.SUCCESS;
			}
			else
			{
				status = AccessionConstants.FAILURE;
			}
		}
		return errors;
	}

	@Override
	public void reset(ActionMapping actionMapping, HttpServletRequest servletRequest) {
	}

	public String getUpassword() {
		return upassword;
	}

	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
