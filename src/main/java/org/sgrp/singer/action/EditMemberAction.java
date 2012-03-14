package org.sgrp.singer.action;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.db.UserManager;
import org.sgrp.singer.form.GenericForm;
import org.sgrp.singer.form.MemberForm;

public class EditMemberAction extends GenericAction {
	public EditMemberAction() {
		super();
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setPageParmeters(mapping, form, request, response);
		
		if (isCancelled(request)) { return getPreviousLinkForward(AccessionConstants.SUCCESS); }
		MemberForm memberForm = (MemberForm) form;
		try {
			UserManager.getInstance().updateUser(memberForm);
			memberForm.setStatus(AccessionConstants.SUCCESS);
			GenericForm.doLogin(request, memberForm.getNuserid(), memberForm.getNpassword());
		} catch (SQLException sqle) {
			ActionMessages errors = new ActionErrors();
			ActionMessage error = new ActionMessage("error.generic", sqle.getMessage());
			errors.add("error", error);
			saveErrors(request, errors);
			return mapping.findForward(AccessionConstants.FAILURE);
		}
		// return mapping.findForward(memberForm.getStatus());
		//return getNewActionForward(memberForm.getStatus(), "?page=admin");
		return getPreviousLinkForward(memberForm.getStatus());
	}
	
	/* Method added by Guilhem (change involved by the fact of moving the form into a popup) */
	@Override
	public ActionForward getPreviousLinkForward(String status, boolean doRedirect) {
		ActionForward actionForward = mapping.findForward(status);
		ActionForward newActionForward = new ActionForward(actionForward);
		if (!isNull(prevlink)) {
			String sPath = newActionForward.getPath();
			sPath += (sPath.contains("?") ? "&" : "?") + AccessionConstants.PREV_LINK + "=" + prevlink+"&action=edit";
			newActionForward.setPath(sPath);
		}
		if(doRedirect)
		{
		newActionForward.setRedirect(true);
		}
		return newActionForward;
	}
}
