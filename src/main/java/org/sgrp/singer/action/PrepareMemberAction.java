package org.sgrp.singer.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.db.UserManager;
import org.sgrp.singer.filters.SingerLoginFilter;
import org.sgrp.singer.form.MemberForm;

public class PrepareMemberAction extends AddMemberAction {

	public PrepareMemberAction() {
		super();

	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setPageParmeters(mapping, form, request, response);
		if (isCancelled(request)) { return getPreviousLinkForward(AccessionConstants.SUCCESS); } 
		MemberForm.setSearchAttributes(request);
		if (getAction().equals(AccessionConstants.ACTION_EDIT)) {
			
			String udata[] = SingerLoginFilter.getUserData(request);
			if(udata==null)
			{
				return mapping.findForward(AccessionConstants.FAILURE);
			}
			String userid = udata[0];
			form = UserManager.getInstance().getUser(request.getSession());
			MemberForm membForm = (MemberForm) form;
			request.setAttribute("membForm", membForm);
		}
		return mapping.findForward(AccessionConstants.SUCCESS);
	}
}
