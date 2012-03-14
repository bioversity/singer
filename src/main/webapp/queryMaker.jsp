<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html xhtml="true" locale="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="js/singer.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/css/singer.css" />

<title>SINGER - Query Builder</title>
</head>


<body >
<html:form action="doSearch.do" >
	<table width="95%" align="center" class="dispTable" id="fieldTable">
		<tr>
			<td class="dispHead">Output</td>
			<td class="dispValue0">
				<select name="page">
				<option value="disppage">Search Results</option>
				<option value="showkeycount">Search Counts</option>
				</select>
			</td>
		</tr>
		
		<tr id="fieldToAdd">
			<td class="dispHead">Select a field</td>
			<td class="dispValue1">
				<jsp:include flush="true" page="searchableField.jsp" />
			</td>
		</tr>
		</table>
		<table width="95%" align="center" class="dispTable">
		<tr>
			<td class="dispHead" colspan="2" align="center"><html:submit>
				<bean:message key="button.submit" />
			</html:submit> <html:reset /> <html:cancel /></td>
		</tr>	
		<tr>
			<td>
				<div>
					<span class="error">
						<html:errors property="notanumber" />
						<html:errors property="notaninteger" />
					</span>
  
 				</div>
			</td>
		</tr>		
			
	
	</table>
</html:form>
</body>
</html:html>