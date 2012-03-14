package org.sgrp.singer.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;

public class LogoutAction extends GenericAction {
	public LogoutAction() {
		super();
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (isCancelled(request)) { return mapping.findForward(AccessionConstants.HOME); }
		setPageParmeters(mapping, form, request, response);
		String status = removeUser();
		/*Added by Gautier*/
		request.setAttribute("loginAction", "logout");
		// setPreviousLinks();
		return getPreviousLinkForward(status, false);
	}
}
