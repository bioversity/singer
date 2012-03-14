<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<div><br />
<table width="100% nowrap="nowrap">
	<tr>
		<td width="100%" nowrap="nowrap">
		<div id="navcontainer">
		<ul>

			<!-- "0-9"-->
			<%
	String alphaArray[] = new String[] { "A", "B", "C", "D", "E", "F",
	"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
	"S", "T", "U", "V", "W", "X", "Y", "Z" };

		String letter = request.getParameter("l");
		if (letter == null || letter.trim().length() == 0)
			letter = "a";
		String alphabet = null;
		for (int i = 0; i < alphaArray.length; i++) {
			String selected = "N";
			String dispAlpha = alphaArray[i];
			alphabet = alphaArray[i].toLowerCase();
			if (letter != null) {
				if (alphabet.equals(letter.toLowerCase()))
			selected = "Y";
			}
			
	%>
			<li id=<%=selected.equals("Y")?"navselected":"navlist"%>><a
				href="/index.jsp?page=taxatoz&l=<%=alphabet%>"> <%=dispAlpha%> </a></li>
			<%
	}
	%>
		</ul>
		</div>
		</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td width="5%" />
		<td width="95%" valign="top" nowrap=nowrap>
		<div>
		<%
		Map<String,String> map = SearchResults.getInstance().getTaxonListByAlphabet(letter);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
				String id = it.next();
				String name = map.get(id);
		%> <a class="headerlinks"
			href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.TAXONCODE+AccessionConstants.SPLIT_KEY+id%>"><%=AccessionConstants.makeProper(name)%></a>
        <br />
		<%
		}
		%>
		</div>
		<br />
		<br />
		</td>
	</tr>
</table>
</div>
