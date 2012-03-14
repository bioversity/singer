<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.form.MemberForm"%>
<html:html xhtml="true" locale="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Developer" content="Kiran Viparthi"/>
<html:base />
<link rel="stylesheet" type="text/css" href="/css/singer.css" />
<link rel="stylesheet" type="text/css" href="/css/singeralpha.css" />
<link type="image/x-icon" href="/img/singericon.ico" rel="Shortcut Icon"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Member Form</title>
<html:base/>
<link rel="stylesheet" type="text/css" href="/css/form.css" />
</head>
<body>
<center>
<table width="90%" class="toptable" border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody>
<!-- tr>
  <td colspan="3"><img src="/img/spacer.gif" alt="" class="img_deco" border="0" height="5" width="1"/></td>
</tr-->
<!-- Header Section -->
<tr>
  <td colspan="3" class="bborder"><jsp:include flush="true" page="/header.jsp" /></td>
</tr>
<!-- End Header Section -->
<!-- Side/Main bar Section -->
<tr>
  <td class="rborder" width="20%" valign="top">
    <jsp:include flush="true" page="/sidebar.jsp" /> 
  </td>
  <td colspan="2" width="80%" valign="top">
  <table width="100%">
  <tr>
  <td width="5%" align="left">
  &nbsp;
  </td>
  <td width="95%" align="left">
<%
MemberForm membForm = new MemberForm();
String prevlink = request.getParameter(AccessionConstants.PREV_LINK);
String actionName = request.getParameter(AccessionConstants.ACTION);
if(actionName==null || actionName.trim().length()==0)
{
	actionName = AccessionConstants.ACTION_ADD;
}
else if(actionName.equals(AccessionConstants.ACTION_EDIT))
{
  membForm = (MemberForm) request.getAttribute("membForm");
}
String actionLabel = actionName.equals(AccessionConstants.ACTION_ADD)?"Add":"Edit";
%>
<h1><%out.println(actionLabel);%> Member Form</h1>
<span class="error">* - Mandatory Field</span>
<hr noshade="noshade"/>
<html:form action="<%=actionName.equals(AccessionConstants.ACTION_ADD)?"/addMember":"/updateMember"%>">
    <html:hidden property="<%=AccessionConstants.ACTION%>" value="<%=actionName%>"/>
    <html:hidden property="<%=AccessionConstants.PREV_LINK%>" value="<%=prevlink%>"/>
    
	<div>Login Id<span class="error">*</span> <br/><html:text property="nuserid" value="<%=membForm.getNuserid()%>" readonly="<%=actionName.equals(AccessionConstants.ACTION_ADD)?false:true%>" size="40" maxlength="50"/> <html:errors property="euserid"/></div>
	<div>Password <span class="error">*</span><br/><html:password property="npassword" size="40" maxlength="50" value="<%=membForm.getNpassword()%>"/> <html:errors property="epassword"/></div>
	<div>Confirm Password <span class="error">*</span><br/><html:password property="nconfimpass" size="40" maxlength="50" value="<%=membForm.getNpassword()%>"/> <html:errors property="econfirmpass"/></div>
	<div><br/></div>
	<div>First Name <span class="error">*</span><br/><html:text property="nfname" value="<%=membForm.getNfname()%>" size="40" maxlength="50"/> <html:errors property="efname"/></div>
	<div>Last Name <span class="error">*</span><br/><html:text property="nlname" value="<%=membForm.getNlname()%>" size="40" maxlength="50"/> <html:errors property="elname"/></div>
    <div>Institute Name <br/><html:text property="niname" value="<%=membForm.getNiname()%>" size="40" maxlength="50"/> <html:errors property="einame"/></div>
    <div>Email <span class="error">*</span><br/><html:text property="nemail" value="<%=membForm.getNemail()%>" size="40" maxlength="50"/> <html:errors property="eemail"/></div>
    <div><br/></div>
    <div><b>Address</b></div>
    <div>Street <span class="error">*</span><br/><html:text property="nstreet" value="<%=membForm.getNstreet()%>" size="40" maxlength="50"/> <html:errors property="estreet"/></div>
    <div>Zipcode <span class="error">*</span><br/><html:text property="nzip" value="<%=membForm.getNzip()%>" size="40" maxlength="50"/> <html:errors property="ezip"/></div>  
    <div>City <span class="error">*</span><br/><html:text property="ncity" value="<%=membForm.getNcity()%>" size="40" maxlength="50"/> <html:errors property="ecity"/></div>
    <div>Country <span class="error">*</span><br/>
    <html:select property="nctycode" value="<%=membForm.getNctycode()%>" size="1">
      <html:options collection="countries" property="key" labelProperty="value"/>
    </html:select><html:errors property="ectycode"/></div>
    <div>Category <span class="error">*</span><br/>
    <html:select property="nusercode" value="<%=membForm.getNusercode()%>" size="1">
      <html:options collection="users" property="key" labelProperty="value"/>
     </html:select><html:errors property="eusercode"/></div>
    <div>FAO code<br/><html:text property="nfaocode" value="<%=membForm.getNfaocode()%>" size="40" maxlength="50"/> <html:errors property="efaocode"/></div>
    <div>URL <br/><html:text property="nurl" value="<%=membForm.getNurl()%>" size="40" maxlength="50"/> <html:errors property="eurl"/></div>
	<p>
		<html:submit>
			<bean:message key="button.submit" />
		</html:submit>
		<html:reset/>
		<html:cancel/>
	</p>
</html:form>
&nbsp;
        </td></tr></table>
        </td>
</tr>
<!-- End Side/Main bar Section -->
<!-- Footer Section -->
<tr>
  <td colspan="3" class="tbbordergrey" valign="bottom"><jsp:include flush="true" page="/footer.jsp" /></td>
</tr>
<!-- End Footer Section -->
</tbody>
</table>
</center>
</body>
</html:html>