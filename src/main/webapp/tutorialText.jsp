<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<bean:parameter name="file" id="fileName"/>
<b>The tutorial can be downloaded <a href="tutorials/<bean:write name="fileName" />.pdf">here</a>.(PDF version)</b><br />
<b>The tutorial can be downloaded <a href="tutorials/<bean:write name="fileName" />.doc">here</a>.(Doc version)</b>
<br /><br />
<object data="tutorials/<bean:write name="fileName" />.pdf" type="application/pdf" width="900" height="600">
  alt : <a href="tutorials/<bean:write name="fileName" />.pdf"><bean:write name="fileName" />.pdf</a>
</object> 
</body>
</html>