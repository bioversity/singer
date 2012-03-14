<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<table width="100%">
<tr>
<td width="80%" valign="top">
<br/>
<br/>
<span class="keyTitle"><b>SINGER member collections</b></span>
<br/>
<br/>
<script type="text/javascript">
function showdiv(id)
{
document.getElementById(id).style.visibility="visible";
document.getElementById(id).style.display="inline";
document.getElementById('plus_'+id).style.visibility="hidden";
document.getElementById('plus_'+id).style.display="none";
document.getElementById('minus_'+id).style.visibility="visible";
document.getElementById('minus_'+id).style.display="inline";

}
function hidediv(id)
{
document.getElementById(id).style.visibility="hidden";
document.getElementById(id).style.display="none";
document.getElementById('minus_'+id).style.visibility="hidden";
document.getElementById('minus_'+id).style.display="none";
document.getElementById('plus_'+id).style.visibility="visible";
document.getElementById('plus_'+id).style.display="inline";

}
</script>
<%
		Map<String,String> map = SearchResults.getInstance().getKeywordListBySearch("type="+AccessionConstants.INSTITUTE);
		for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) {
				String keyid = it.next();
				String keyname = map.get(keyid);
		%>
         <div id="coldiv"><b><img src="/img/plus.png" id="plus_<%=keyid%>" onclick="showdiv('<%=keyid%>');"/><img src="/img/minus.png" onclick="hidediv('<%=keyid%>');" id="minus_<%=keyid%>" style="visible:hidden; display:none;"/>&nbsp;<%=keyname%> <a title="Institute data summary" class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.INSTITUTECODE+AccessionConstants.SPLIT_KEY+keyid%>"><img src="/img/search.png" border="0" src="/img/page.png" border="0" width="14" height="14"/></a> </b></div>
			<div id="<%=keyid%>"  style="visible:hidden; display:none;">
			<%
				Map<String,String> colmap = SearchResults.getInstance().getInstituteCollections(keyid);
		for (Iterator<String> colit=colmap.keySet().iterator(); colit.hasNext(); ) {
				String colkeyid = colit.next();
				String colkeyname = colmap.get(colkeyid);
				 %>
				<a title="Key Count data" class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.COLLECTIONCODE+AccessionConstants.SPLIT_KEY+colkeyid%>"><%=colkeyname%></a><br/>				 
			<%
			}
			%>
              <br/>
			</div>
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
