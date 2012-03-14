<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<table width="100%">
<tr>
<td width="80%" valign="top">
<br/>
<br/>
<span class="keyTitle"><b>SINGER Distribution by Institutes</b></span>
<br/>
<br/>
<%
		Map<String,String> map = SearchResults.getInstance().getKeywordListBySearch("type"+AccessionConstants.SPLIT_KEY+AccessionConstants.INSTITUTE);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
			String keyid = it.next();
			String keyname = map.get(keyid);
	%>
	<a class="headerlinks"
	href="/index.jsp?page=showdistribution&type=inst&search=<%=AccessionConstants.INSTITUTECODE+AccessionConstants.SPLIT_KEY+keyid%>"><%=keyname%></a><br/>		
	<%
	}
	%>
</td>
</tr>
</table>
				
</div>
