<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/functions.jsp" %>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%
String prevlink = request.getParameter(AccessionConstants.PREV_LINK); 
if(prevlink==null || prevlink.trim().length()==0)
{
	prevlink =request.getRequestURI();
	String queryStr = request.getQueryString();
	
	if(queryStr!=null && queryStr.trim().length()>0)
	{
		prevlink= prevlink+"?"+queryStr;
	}
  prevlink = AccessionConstants.encodeURL(prevlink);
}
%>

<br>
<blockquote><blockquote><blockquote>
	<p><b>In order to be able to recover the orders you placed before switching to the new login system, you must provide now your previous login and password.</b></p>
	<br>
	<p class="login">
	<html:form action="/recoverPreviousOrders" method="POST">
	<html:hidden property="<%=AccessionConstants.PREV_LINK%>" value="<%=prevlink%>"/>
	Previous Login Id <br/><html:text property="userid" maxlength="25" /><br><br>Previous Password <br><html:password property="upassword" maxlength="25" /><br/>
	<br><html:submit styleClass="login">Recover now</html:submit> &nbsp;
	<div>
	<span class="error">
		<html:errors property="euserid" />
		<html:errors property="epassword" /><html:errors property="error" /></span>
	  </div>
	</html:form>
	</p> 
</blockquote></blockquote></blockquote>

 