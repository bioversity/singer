<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.form.MemberForm"%>
<%@page import="org.sgrp.singer.MySearchHistory" %>
<%@ include file="/functions.jsp" %>
<html:html xhtml="true" locale="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Developer" content="Guilhem SEMPERE"/>
<html:base />
<link rel="stylesheet" type="text/css" href="/css/singer.css" />
<link rel="stylesheet" type="text/css" href="/css/singeralpha.css" />
<link type="image/x-icon" href="/img/singericon.ico" rel="Shortcut Icon"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Reloading</title>
<html:base/>
<link rel="stylesheet" type="text/css" href="/css/form.css" />
<%


String action = request.getParameter("action"); 
String trackString = action.equals("add") ? "newMember":"memberEdited"; %>
<!-- 
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? 
"https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-9565684-1"); pageTracker._trackPageview("<%=trackString%>");
} catch(err) {}</script>
 -->
<%//End Added by Gautier %>
</head>

<body onload="opener.location.href='<%= request.getParameter("pl") %>'; window.close();">
</body>
</html:html>