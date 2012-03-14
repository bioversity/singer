<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@ page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>

<div>
<html:form action="/doSearch">

<input type="hidden" name="page" value="showkeycount"/>
<table width="100%">
<tr>
<td width="100%" colspan="4" align="left">
<html:submit> <bean:message key="button.submit" /> </html:submit> <html:reset />
</td>
</tr>
<tr>
<td width="45%" valign="top" nowrap="nowrap">
<span class="keyTitle"><b>Collection name</b></span>
<br/>
<%
		Map<String,String> map = SearchResults.getInstance().getKeywordListBySearch("type"+AccessionConstants.SPLIT_KEY+AccessionConstants.COLLECTION);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
				String keyid = it.next();
				String keyname = map.get(keyid);
		%>
		<input type="checkbox" name="<%=AccessionConstants.COLLECTIONCODE%>" value="<%=keyid%>"/>
		<a class="headerlinks"
		href="/indexdemotreaty.jsp?page=showkeycountdemo&search=<%=AccessionConstants.COLLECTIONCODE+AccessionConstants.SPLIT_KEY+keyid%>"><%=keyname%></a><br/>
		<%
		}
		%>
</td>
<td width="25%" valign="top" nowrap="nowrap">
<span class="keyTitle"><b>Collecting Source</b></span>
<br/>
<%
		map = SearchResults.getInstance().getKeywordListBySearch("type"+AccessionConstants.SPLIT_KEY+AccessionConstants.SOURCE);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
				String keyid = it.next();
				String keyname = map.get(keyid);
		%>
		<input type="checkbox" name="<%=AccessionConstants.SOURCECODE%>" value="<%=keyid%>"/>
		<a class="headerlinks"
		href="/indexdemotreaty.jsp?page=showkeycountdemo&search=<%=AccessionConstants.SOURCECODE+AccessionConstants.SPLIT_KEY+keyid%>"><%=keyname%></a><br/>		
		<%
		}
		%>
</td>
<td width="15%" valign="top" nowrap="nowrap">
<span class="keyTitle"><b>Sample Status</b></span>
<br/>
<%
		map = SearchResults.getInstance().getKeywordListBySearch("type"+AccessionConstants.SPLIT_KEY+AccessionConstants.STATUS);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
				String keyid = it.next();
				String keyname = map.get(keyid);
		%>
		<input type="checkbox" name="<%=AccessionConstants.STATUSCODE%>" value="<%=keyid%>"/>
		<a class="headerlinks"
		href="/indexdemotreaty.jsp?page=showkeycountdemo&search=<%=AccessionConstants.STATUSCODE+AccessionConstants.SPLIT_KEY+keyid%>"><%=keyname%></a><br/>
		<%
		}
		%>
</td>
<td width="15%" valign="top" nowrap="nowrap">
<span class="keyTitle"><b>Country Source</b></span>
<br/>
<%
		map = SearchResults.getInstance().getKeywordListBySearch("type"+AccessionConstants.SPLIT_KEY+AccessionConstants.COUNTRY);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
				String keyid = it.next();
				String keyname = map.get(keyid);
		%>
		<input type="checkbox" name="<%=AccessionConstants.COUNTRYCODE%>" value="<%=keyid%>"/>
		<a class="headerlinks"
		href="/indexdemotreaty.jsp?page=showkeycountdemo&search=<%=AccessionConstants.COUNTRYCODE+AccessionConstants.SPLIT_KEY+keyid%>"><%=keyname%></a><br/>
		<%
		}
		%>
</td>
</tr>
</table>
</html:form>
</div>
