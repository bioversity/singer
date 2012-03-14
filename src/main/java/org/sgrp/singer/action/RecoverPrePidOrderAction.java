package org.sgrp.singer.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.db.PreviousOrderManager;
import org.sgrp.singer.form.LoginForm;

public class RecoverPrePidOrderAction extends GenericAction
{
	private static Logger LOG = Logger.getLogger(RecoverPrePidOrderAction.class);
	
	public RecoverPrePidOrderAction()
	{
		super();
	}
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String pidUserID = (String) request.getSession().getAttribute(AccessionConstants.SYSUSERID);
		if (isCancelled(request) || pidUserID == null || pidUserID.length() == 0) { return mapping.findForward(AccessionConstants.HOME); }
		setPageParmeters(mapping, form, request, response);
		
		String prePidUserID = ((LoginForm)form).getUserid();
		
		if (prePidUserID != null && prePidUserID.trim().length() > 0)
		{
			Connection conn = null;
			try
			{				
				PreviousOrderManager pom = PreviousOrderManager.getInstance();
				Map<String, Date> ordersDate = pom.getPreviousOrdersDateByUserId(prePidUserID);
				
				Set<String> orderIDs = ordersDate.keySet();
				request.setAttribute("ordersDate", ordersDate);
				int nOrderCount = ordersDate.size();
				
				if (nOrderCount > 0)
				{	// we have some old orders to transfer to the new account
				
					conn = AccessionServlet.getCP().newConnection(this.toString());

					String orderIdCsv = "";
					for (String anOrderID : orderIDs)
					{
						orderIdCsv += (orderIdCsv.length() == 0 ? "" : ",") + anOrderID;
					
						PreparedStatement stmt = conn.prepareStatement("update orders set userid=?, odate=? where orderid=?;");
						stmt.setString(1, pidUserID);
						stmt.setTimestamp(2, new java.sql.Timestamp(ordersDate.get(anOrderID).getTime()));
						stmt.setString(3, anOrderID);
						stmt.executeUpdate();
					}
					
					LOG.info("The following orders were moved from pre-pid user '" + prePidUserID + "' to pid user '" + pidUserID + "': " + orderIdCsv);
					request.setAttribute("Nrecovered", nOrderCount);
				}						
			}
			catch (SQLException se) 
			{
				se.printStackTrace();
				throw new SQLException();
			} 

			finally 
			{
				AccessionServlet.getCP().freeConnection(conn);
			}
		}		
		
		return mapping.findForward(AccessionConstants.SUCCESS);
	}
}
