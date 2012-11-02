package org.sgrp.singer.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.xmlrpc.XmlRpcException;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ErrorConstants;
import org.sgrp.singer.Main;
import org.sgrp.singer.Utils;
import org.sgrp.singer.db.UserManager;

@SuppressWarnings("serial")
public class LoginPIDForm extends GenericForm {
	
	private static Logger LOG = Logger.getLogger(Main.class);
	
	private String	upassword;
	private String	userid;

	public LoginPIDForm() {
		super();
	}

	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
		ActionErrors errors = new ActionErrors();
		PIDMemberForm memberDetails = null;
		if (isNull(getUserid())) {
			errors.add("euserid", ErrorConstants.getActionMessage(ErrorConstants.ERROR_USERID_NULL));
		}
		if (isNull(getUpassword())) {
			errors.add("epassword", ErrorConstants.getActionMessage(ErrorConstants.ERROR_PASSWORD_NULL));
		}
		
		if (!isNull(getUserid()) && !isNull(getUpassword()))
		{
            //LOG.info("login info: " + getUserid() + " --- " + getUpassword());
            memberDetails = UserManager.getInstance().loginPID(getUserid(), getUpassword());
            if(memberDetails == null) {
                /*If the user is not registered by the PID, we check if he is an ancient SINGER member*/
                if(UserManager.getInstance().loginValid(getUserid(), getUpassword()))
                {
                    errors.add("oldLogin", ErrorConstants.getActionMessage(ErrorConstants.ERROR_OLD_SINGER_LOGIN));
                }
                else
                {
                    errors.add("error", ErrorConstants.getActionMessage(ErrorConstants.ERROR_INVALID_LOGIN));
                }
            }
		}
		
		if (errors.size() > 0) {
			status = AccessionConstants.FAILURE;
		} 
		else
		{		
			httpServletRequest.getSession().setAttribute("memberDetails", memberDetails);
			String name = memberDetails.getNfname() + " " + memberDetails.getNlname();
			// System.out.println("User logged with " + getUserid() + " and name " + name);
			httpServletRequest.getSession().setAttribute(AccessionConstants.SYSUSERNAME, name);
			httpServletRequest.getSession().setAttribute(AccessionConstants.SYSUSERID, memberDetails.getNuserpid());
			httpServletRequest.getSession().setAttribute(AccessionConstants.AUTHORIZATION, "Basic " + Utils.b64encode(userid+ ":" + getUpassword()));
			
			status = AccessionConstants.SUCCESS;
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



