<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.indexer.BaseIndexer"%>
<%@page import=" org.apache.lucene.document.Document" %>
<div>
<table width="100%">
<tr>
<td width="100%">
<span class="keyTitle">Species under <i><%=AccessionConstants.makeProper(request.getParameter(AccessionConstants.GENUS))%></i></span>
<br/>
<br/>
	<%
    String genuscode= request.getParameter(AccessionConstants.GENUSCODE);
	String genus = request.getParameter(AccessionConstants.GENUS);
				String alphaArray[] = new String[] { "A", "B", "C", "D", "E", "F",
				"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };

		for (int i = 0; i < alphaArray.length; i++) {
			String dispAlpha = alphaArray[i];
			String alphabet = alphaArray[i].toLowerCase();
			
			Map<String,String> map = SearchResults.getInstance().getSpeciesListByAlphabet(alphabet, genus);
			if(map.size()>0)
			{
	%>
	<b><%=dispAlpha%></b>
	<hr/>
	<%
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
				String id = it.next();
				String name = map.get(id);
		%>
		<a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.GENUSCODE+AccessionConstants.SPLIT_KEY+genuscode%>%20AND%20<%=AccessionConstants.SPECIESCODE+AccessionConstants.SPLIT_KEY+id%>"><i><%=name%></i></a>
		<%
		Map<String,Document> subMap = SearchResults.getInstance().getPicturesListForGenusSpecies(genus, name);
			if(subMap.size()>0)
			{
				%>
				<%
				for (Iterator<String> sit=subMap.keySet().iterator(); sit.hasNext(); ) {
				String sid = sit.next();
                Document doc = subMap.get(sid);
				String pname = AccessionConstants.getValueMap(doc, AccessionConstants.FULL_NAME).get(AccessionConstants.FULL_NAME);
				String other = AccessionConstants.getValueMap(doc, "other").get("other");
		%>
		<a title="<%=other%>" class="headerlinks" href="javascript:void(0);"><img alt="<%=other%>" src="/img<%=pname%>" height="25" width="25" onmouseover="popImage('/img<%=pname%>','<%=genus%> <%=name%>',escape('<%=other%>'));"></a>
		<%
		}
		%>
		<%
		}
		%>	
		<br/>
		<%
		}
		%>
		<br/>
	<%
		}
	}
	%>
</td>
</tr>
</table>
</div>
