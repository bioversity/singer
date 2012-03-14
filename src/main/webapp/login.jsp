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
<%
	if (isLoggedIn(request.getSession(true))) {
%> 


<span class="login"> 
<html:form action="/logout" method="POST">
<html:hidden property="<%=AccessionConstants.PREV_LINK%>" value="<%=prevlink%>"/>
Hello 
<!-- <a href="/prepareMemberForm.do?<%=AccessionConstants.ACTION%>=<%=AccessionConstants.ACTION_EDIT%>&<%=AccessionConstants.PREV_LINK%>=<%=prevlink%>"><b><%=getUserName(request.getSession(true))%> </b></a>!!! -->
<a href="javascript:window.open('/prepareMemberForm.do?<%=AccessionConstants.ACTION%>=<%=AccessionConstants.ACTION_EDIT%>&<%=AccessionConstants.PREV_LINK%>=<%=prevlink%>', 'memberDetailsPopup', 'width=790,height=550,location=0,toolbar=0,resizable=0,scrollbars=1,menubar=0,status=0').focus();"><b><%=getUserName(request.getSession(true))%> </b></a>!!!
<br/>
<br/>
<html:submit value="Logout" styleClass="login"></html:submit>
</html:form>
</span> 
<%
} else 
{
%>
<span class="login">
<html:form action="/login" method="POST">
<html:hidden property="<%=AccessionConstants.PREV_LINK%>" value="<%=prevlink%>"/>
<center><b>(for ordering only)</b></center>
Login Id <br/><html:text property="userid" maxlength="25" /> <br/>Password <br/><html:password property="upassword" maxlength="25" /><br/>
<html:submit styleClass="login"><bean:message key="button.login" /></html:submit> &nbsp;
<!-- <a href="/prepareMemberForm.do?<%=AccessionConstants.PREV_LINK%>=<%=prevlink%>'">Register Now !!</a> -->
<a href="javascript:window.open('/prepareMemberForm.do?<%=AccessionConstants.PREV_LINK%>=<%=prevlink%>', 'memberDetailsPopup', 'width=790,height=550,location=0,toolbar=0,resizable=0,scrollbars=1,menubar=0,status=0').focus();">Register Now !!</a>
<div>
<span class="error">
	<html:errors property="euserid" />
	<html:errors property="epassword" /><html:errors property="error" /></span>
  </div>
</html:form>
</span> 
<%
 }
 %>
 