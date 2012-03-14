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
<span class="keyTitle"><b>SINGER Collecting source types</b></span>
<br/>
<br/>
<%
		Map<String,String> map = SearchResults.getInstance().getKeywordListBySearch("type"+AccessionConstants.SPLIT_KEY+AccessionConstants.SOURCE);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
			String keyid = it.next();
			String keyname = map.get(keyid);
	%>
	<a class="headerlinks"
	href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.SOURCECODE+AccessionConstants.SPLIT_KEY+keyid%>"><%=keyname%></a><br/>		
	<%
	}
	%>
</td>
<td width="20%" valign="top" class="lborder" align="center">
<jsp:include page="/logos.jsp" flush="true"/>
</td>
</tr>
</table>
				
</div>
