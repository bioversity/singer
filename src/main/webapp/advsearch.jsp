<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@ page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>

<html:html xhtml="true" locale="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="js/singer.js" type="text/javascript"></script>
<title>Search Form</title>

<html:base />
<link rel="stylesheet" type="text/css" href="/css/singer.css" />
</head>
<body>
<br />
<html:form action="/doSearch">
	<table width="95%" align="center" class="dispTable">
		<tr>
			<td class="dispHead">Output</td>
			<td class="dispValue0">
				<select name="page">
				<option value="disppage">Search Results</option>
				<option value="showkeycount">Search Counts</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="dispHead">Free Text</td>
			<td class="dispValue1"><html:text
				property="<%=AccessionConstants.CONTENTS%>" size="80" maxlength="80"
				value="" /><br />
			<i>Separate keywords using AND, OR, NOT</i></td>
		</tr>
		<tr>
			<td class="dispHead">Collection</td>
			<td class="dispValue0"><select
				name="<%=AccessionConstants.COLLECTIONCODE%>" multiple="true"
				rows="3">
				<logic:iterate id="key" name="collections">
					<option value="<bean:write name="key" property="key" />"><bean:write
						name="key" property="value" /></option>
				</logic:iterate>
			</select></td>
		</tr>
		<tr>
			<td class="dispHead">Sample Status</td>
			<td class="dispValue1"><select
				name="<%=AccessionConstants.STATUSCODE%>" multiple="true" rows="3">
				<logic:iterate id="key" name="statuses">
					<option value="<bean:write name="key" property="key" />"><bean:write
						name="key" property="value" /></option>
				</logic:iterate>
			</select></td>
		</tr>
		<tr>
			<td class="dispHead">Collecting Source</td>
			<td class="dispValue0"><select
				name="<%=AccessionConstants.SOURCECODE%>" multiple="true" rows="3">
				<logic:iterate id="key" name="sources">
					<option value="<bean:write name="key" property="key" />"><bean:write
						name="key" property="value" /></option>
				</logic:iterate>
			</select></td>
		</tr>
		<tr>
			<td class="dispHead">Country Source</td>
			<td class="dispValue1"><select
				name="<%=AccessionConstants.COUNTRYCODE%>" multiple="true" rows="3">
				<logic:iterate id="key" name="countries">
					<option value="<bean:write name="key" property="key" />"><bean:write
						name="key" property="value" /></option>
				</logic:iterate>
			</select></td>
		</tr>
		<tr>
			<td class="dispHead">Look for taxon:</td>
			<td class="dispValue1"><html:text
				property="" size="80" maxlength="80"
				value="" onkeyup="autoCompleteTaxon(this, document.getElementById('SelectGenusSpecies'));" styleId="auto"/><br /></td>
		</tr>
        <tr>
          <td class="dispHead">Taxon</td>
          <td class="dispValue0" >
          <select
            name="<%=AccessionConstants.GENUSCODE+AccessionConstants.SPECIESCODE%>" multiple="true" rows="3" id="SelectGenusSpecies">
             <logic:iterate id="key" name="taxons">
              <option value="<bean:write name="key" property="key" />"><bean:write
                name="key" property="value" /></option>
            </logic:iterate>
          </select></td>
        </tr>
		<tr>
			<td class="dispHead">Intrust Status</td>
			<td class="dispValue1"><select
				name="<%=AccessionConstants.TRUSTCODE%>" multiple="true" rows="2">
				<logic:iterate id="key" name="trusts">
					<option value="<bean:write name="key" property="key" />"><bean:write
						name="key" property="value" /></option>
				</logic:iterate>
			</select></td>
		</tr>
		<tr>
			<td class="dispHead" colspan="2" align="center"><html:submit>
				<bean:message key="button.submit" />
			</html:submit> <html:reset /> <html:cancel /></td>
		</tr>
	</table>
</html:form>
</body>
</html:html>