package org.sgrp.singer.action;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.Utils;

public class GenericAction extends Action {
	public static String getAction(HttpServletRequest request) {
		String action = request.getParameter(AccessionConstants.ACTION);
		if (isNull(action)) {
			action = "add";
		}
		return action;
	}

	public static boolean isNull(String value) {
		return AccessionConstants.isNull(value);
	}

	public ActionForm			form		= null;

	public ActionMapping		mapping		= null;

	public HashMap				paramMap	= new HashMap();

	public String				prevlink	= null;

	public HttpServletRequest	request		= null;

	public HttpServletResponse	response	= null;

	public String getAction() {
		return getAction(request);
	}

	public ActionForward getNewActionForward(String status, String param) {
		ActionForward actionForward = mapping.findForward(status);
		ActionForward newActionForward = new ActionForward(actionForward);
		if (status.equals(AccessionConstants.SUCCESS)) {
			newActionForward.setPath(actionForward.getPath() + param);
		}
		return newActionForward;
	}

	public HashMap getParamMap() {
		return paramMap;
	}

	public ActionForward getPreviousLinkForward(String status) {
		return getPreviousLinkForward(status, true);
	}
	public ActionForward getPreviousLinkForward(String status, boolean doRedirect) {
		ActionForward actionForward = mapping.findForward(status);
		ActionForward newActionForward = new ActionForward(actionForward);
		if (!isNull(prevlink)) {
			String nprevlink = AccessionConstants.decodeURL(prevlink);
			newActionForward.setPath(nprevlink);
		}
		if(doRedirect)
		{
		newActionForward.setRedirect(true);
		}
		return newActionForward;
	}

	public String getPrevlink() {
		return prevlink;
	}

	public String getUserName() {
		String s = (String) request.getSession().getAttribute(AccessionConstants.AUTHORIZATION);
		String uName = null;
		if (s != null) {
			String decoded = Utils.b64decode(s.substring(s.lastIndexOf(32) + 1));
			uName = decoded.substring(0, decoded.indexOf(58));
		}
		if (!isNull(uName)) {
			return uName;
		} else {
			return null;
		}
	}

	public boolean isAllowed() {
		String uName = getUserName();
		if (!isNull(uName)) {
			return true;
		} else {
			return false;
		}
	}

	public String removeUser() {
		request.getSession().removeAttribute(AccessionConstants.AUTHORIZATION);
		request.getSession().removeAttribute(AccessionConstants.SYSUSERNAME);
		request.getSession().removeAttribute(AccessionConstants.SYSUSERID);
		String userName = getUserName();
		if (!isNull(userName)) {
			return AccessionConstants.FAILURE;
		} else {
			return AccessionConstants.SUCCESS;
		}
	}

	public void setPageParmeters(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		paramMap.clear();
		this.request = request;
		this.response = response;
		this.mapping = mapping;
		this.form = form;
		this.prevlink = request.getParameter(AccessionConstants.PREV_LINK);
	}

	public void setParamMap(HashMap paramMap) {
		this.paramMap = paramMap;
	}

	public void setPrevlink(String prevlink) {
		this.prevlink = prevlink;
	}

}
