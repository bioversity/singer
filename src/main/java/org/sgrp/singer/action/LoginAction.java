package org.sgrp.singer.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.form.LoginForm;

public class LoginAction extends GenericAction {
	public LoginAction() {
		super();
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setPageParmeters(mapping, form, request, response);
		if (isCancelled(request)) { return getPreviousLinkForward(AccessionConstants.SUCCESS); }
		LoginForm loginForm = (LoginForm) form;
		/*Added by Gautier*/
		request.setAttribute("loginAction", "login");
		// String prevPath = getPreviousPage(loginForm.getStatus());
		return getPreviousLinkForward(loginForm.getStatus(), false);
	}
}
