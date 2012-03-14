<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.sgrp.singer.SearchResults"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<%@page import="org.sgrp.singer.form.AccessionForm"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.MyShopCart"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.sgrp.singer.StringComparableValues"%>
<%@page import="org.sgrp.singer.indexer.Keywords"%>
<%@page import="org.sgrp.singer.db.EnvDataManager"%>
<%@page import=" org.apache.lucene.document.Document" %>

<div>
<!-- a href="/envsearch.jsp">Back to search page</a-->
<%
//Map map = request.getParameterMap();
//String query = EnvDataManager.makeSearchQuery(map);
String query = request.getParameter("search");
if (query != null && query.trim().length() > 0) 
{
  //query = "coc=co69 AND ("+query+")";
 //String attribList = "accenumb" + "|" + AccessionConstants.COLLECTIONCODE + "|" + AccessionConstants.COLLECTIONNAME + "|" + AccessionConstants.TAXONNAME +  "|" + AccessionConstants.COUNTRYNAME;
 //Map<String,String> resMap = SearchResults.getInstance().getEnvDataMapBySearch(query,null);
 Map<String,Document> resMap = SearchResults.getInstance().getAccEnvDataMapBySearch(query,null);
%>
Results based on Search <b><%=query%></b> <br/>found <%=resMap.size()%> results
<br/>
<div>
<p>
<%
  for(Iterator<String> itr=resMap.keySet().iterator(); itr.hasNext();)
  {
   String id = itr.next();
%>
   <%=id%>, 
<%
  }
%>
</p>
</div>
<%
} else {
%> <br />
<br />
<center><font color="red"><b>Not a valid query <%=query%> String</b></font></center>
<%
}
%>
</div>
