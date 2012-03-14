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
boolean fAddingMember = actionName.equals(AccessionConstants.ACTION_ADD);
%>

<center>
<img src="<%= fAddingMember ? "/img/intl_treaty/header.jpg" : "/img/singer.png" %>" alt="" class="imgdisp" border="0" />
<h1><%= actionLabel%> Member Form</h1>
<span class="error">* - Mandatory Field</span>
<html:form action="<%=fAddingMember ? \"/addMember\" : \"/updateMember\"%>">

<table border="0" cellpadding="5" cellspacing="5" width="100%" bgcolor="#eeeeee">
  <tr>
  <td width="5%" align="left">
  &nbsp;
  </td>
  <td width="45%" align="left">
    <html:hidden property="<%=AccessionConstants.ACTION%>" value="<%=actionName%>"/>
    <html:hidden property="<%=AccessionConstants.PREV_LINK%>" value="<%=prevlink%>"/>
    
	<div>Login Id<span class="error">*</span> <br/><html:text property="nuserid" value="<%=membForm.getNuserid()%>" readonly="<%=actionName.equals(AccessionConstants.ACTION_ADD)?false:true%>" size="40" maxlength="50"/><br><html:errors property="euserid"/></div>
	<div>Password <span class="error">*</span><br/><html:password property="npassword" size="40" maxlength="50" value="<%=membForm.getNpassword()%>"/><br><html:errors property="epassword"/></div>
	<div>Confirm Password <span class="error">*</span><br/><html:password property="nconfimpass" size="40" maxlength="50" value="<%=membForm.getNconfimpass()%>"/><br><html:errors property="econfirmpass"/></div>
	<div><br/></div>
	<div>First Name <span class="error">*</span><br/><html:text property="nfname" value="<%=membForm.getNfname()%>" size="40" maxlength="50"/><br><html:errors property="efname"/></div>
	<div>Last Name <span class="error">*</span><br/><html:text property="nlname" value="<%=membForm.getNlname()%>" size="40" maxlength="50"/><br><html:errors property="elname"/></div>
    <div>Institute Name <br/><html:text property="niname" value="<%=membForm.getNiname()%>" size="40" maxlength="50"/><br><html:errors property="einame"/></div>
    <div>Email <span class="error">*</span><br/><html:text property="nemail" value="<%=membForm.getNemail()%>" size="40" maxlength="50"/><br><html:errors property="eemail"/></div>
    
	</td>
	<td width="45%" align="left">
	
    <div><b>Address</b></div>
    <div>Street <span class="error">*</span><br/><html:text property="nstreet" value="<%=membForm.getNstreet()%>" size="40" maxlength="50"/><br><html:errors property="estreet"/></div>
    <div>Zipcode <span class="error">*</span><br/><html:text property="nzip" value="<%=membForm.getNzip()%>" size="40" maxlength="50"/><br><html:errors property="ezip"/></div>  
    <div>City <span class="error">*</span><br/><html:text property="ncity" value="<%=membForm.getNcity()%>" size="40" maxlength="50"/><br><html:errors property="ecity"/></div>
    <div>Country <span class="error">*</span><br/>
    <html:select property="nctycode" value="<%=membForm.getNctycode()%>" size="1">
      <html:options collection="countries" property="key" labelProperty="value"/>
    </html:select><html:errors property="ectycode"/></div>
    <div>Category <span class="error">*</span><br/>
    <html:select property="nusercode" value="<%=membForm.getNusercode()%>" size="1">
      <html:options collection="users" property="key" labelProperty="value"/>
     </html:select><html:errors property="eusercode"/></div>
    <div>FAO code<br/><html:text property="nfaocode" value="<%=membForm.getNfaocode()%>" size="40" maxlength="50"/><br> <html:errors property="efaocode"/></div>
    <div>URL <br/><html:text property="nurl" value="<%=membForm.getNurl()%>" size="40" maxlength="50"/><br><html:errors property="eurl"/></div>

&nbsp;
	</td>
	<td width="5%" align="left">
  	&nbsp;
	</td>
    </tr>
    <tr>
    	<td colspan='4' align='center'>
		<html:submit>
			<bean:message key="button.submit" />
		</html:submit>
		<html:reset/>
		<html:button property="close" value="Cancel" onclick="window.close();"/>
    	</td>
    </tr>
</table>
</html:form>
</center>
<%String trackString= fAddingMember? "RegistrationForm":"MemberEditionForm"; %>

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? 
"https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-9565684-1"); pageTracker._trackPageview("<%=trackString%>");
} catch(err) {}</script>

</body>
</html:html>