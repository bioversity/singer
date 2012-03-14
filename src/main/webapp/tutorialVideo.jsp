<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tutorial Video</title>
</head>
<body>
<bean:parameter name="video" id="videoName"/>
<b>The video can be downloaded <a href="video/<bean:write name="videoName" />.avi">here</a>.</b>
<br /><br />
<OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" 
codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" 
WIDTH="1280" HEIGHT="876" id="<bean:write name="videoName" />.swf" ALIGN=""> 
 <PARAM NAME="movie" VALUE="video/<bean:write name="videoName" />.swf"> 
 <PARAM NAME="quality" VALUE="high"> 
 <PARAM NAME="bgcolor" VALUE="#ffffff"> 
 <EMBED src="video/<bean:write name="videoName" />.swf" quality="high" bgcolor="#500"  WIDTH="1280" HEIGHT="876" NAME="video/<bean:write name="videoName" />.swf" ALIGN="" TYPE="application/x-shockwave-flash" PLUGINSPAGE="http://www.macromedia.com/go/getflashplayer">
</EMBED> 
</OBJECT>

</body>
</html>