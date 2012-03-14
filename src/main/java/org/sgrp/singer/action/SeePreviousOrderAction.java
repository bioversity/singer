package org.sgrp.singer.action;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.db.PreviousOrderManager;

/**
 * This action allow the user to see his previous orders
 * 
 * @author gsarah
 *
 */

public class SeePreviousOrderAction extends GenericAction {
	
	public static Date PID_SYSTEM_ACTIVATION_DATE = new Date(2010 - 1900, 10, 3);
	
	public SeePreviousOrderAction()
	{
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{	
		if (isCancelled(request)) { return mapping.findForward(AccessionConstants.HOME); }
		setPageParmeters(mapping, form, request, response);
		String userid = (String)request.getSession().getAttribute(org.sgrp.singer.AccessionConstants.SYSUSERID);
		
		if(userid == null || userid.trim().length()==0)
		{
			return mapping.findForward(AccessionConstants.SUCCESS);
		}
		else
		{
			try {
				PreviousOrderManager pom = PreviousOrderManager.getInstance();
				Map<String, Date> ordersDate = pom.getPreviousOrdersDateByUserId(userid);
				
				Set<String> orderIDs = ordersDate.keySet();
				request.setAttribute("ordersDate", ordersDate);
				
				/*Check if we should suggest to recover "pre-PID" orders (that is if we have no order that was placed before switching to the new system) */
				boolean fSuggestOldOrderRecovery = true;
				for (Date orderDate : ordersDate.values())
					if (orderDate.before(PID_SYSTEM_ACTIVATION_DATE))
					{
						fSuggestOldOrderRecovery = false;
						break;
					}
				if (fSuggestOldOrderRecovery)
					request.setAttribute("suggestOldOrderRecovery", new Boolean(true));
				
				/*For each order recovered we look for the accessions and their informations*/				
				Map<String, List<String>> accessionsByOrder = pom.getAccessionsByOrder(orderIDs);
				Set<String> allAccessions = pom.getAccessionListFromOrder(orderIDs);
				Map<String, Map<String,String>> accessionInformation =pom.getAccessionsInfo(allAccessions);
											
				request.setAttribute("accessionByOrder", accessionsByOrder);
				request.setAttribute("accessionInformation", accessionInformation);
			}
			catch (SQLException se) 
			{
				se.printStackTrace();
				throw new SQLException();
			}
		}
		
		
		return mapping.findForward(AccessionConstants.SUCCESS);
	}
}
