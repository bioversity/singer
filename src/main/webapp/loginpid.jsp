<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/functions.jsp" %>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%
String prevlink =  request.getParameter(AccessionConstants.PREV_LINK);;
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
<b><%=getUserName(request.getSession(true))%> </b>!!!
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
<html:form action="/loginpid" method="POST">
<html:hidden property="<%=AccessionConstants.PREV_LINK%>" value="<%=prevlink%>"/>
<center><b>(for ordering only)</b></center>
Login Id <br/><html:text property="userid" maxlength="25" /> <br/>Password <br/><html:password property="upassword" maxlength="25" /><br/>
<html:submit styleClass="login"><bean:message key="button.login" /></html:submit> &nbsp;
<a href="javascript:window.open('/registration.jsp', 'LoginInformationPopup', 'width=1028,height=600,location=0,toolbar=0,resizable=0,scrollbars=1,menubar=0,status=0').focus();">Register Now!</a>
<br>
<div align='right'><a href="javascript:window.open('http://mls.planttreaty.org/itt/user/password', 'LoginInformationPopup', 'width=1028,height=600,location=0,toolbar=0,resizable=0,scrollbars=1,menubar=0,status=0').focus();">Lost your password?</a></div>
<div>
<span class="error" style='color:red;'>
	<html:errors property="euserid" />
	<html:errors property="epassword" />
	<html:errors property="error" />
	<html:errors property="oldLogin" />
	</span>
  </div>
</html:form>
</span> 
<%
 }
 %>
 