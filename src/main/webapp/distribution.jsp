<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.db.DistributionManager"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<%@page import="org.sgrp.singer.DistributionStatistics"%>
<br/>
<div>
<span class="keyTitle"><b>View distribution data by member collections</b></span>
<br/>
<br/>
<table width="100%">
  <%
  	String nil = "-";
  	DistributionStatistics distStat = DistributionStatistics.getInstance();

  	Map<String, Integer> countMap = distStat.getYearDistribution();
  	List values = new Vector(countMap.keySet());
  	java.util.Collections.sort(values);
  	java.util.Collections.reverse(values);
  %>
  <tr>
    <td nowrap="nowrap" class="distHead" align="center">Collection Name / Year</td>
    <td class="distHead" align="center"><a class="headerlinks" href="/index.jsp?page=fulldistribution&search=all">Distributions Total</a></td>
    <%
    	for (int i = 0; i < values.size(); i++) {
    %>
    <td class="distHead" align="center"><%=values.get(i).equals("0000")?"Not specified":values.get(i)%></td>
    <%
    	}
    %>
  </tr>
  <tr>
    <td nowrap="nowrap" align="right" class="distHead">Yearly Total</td>
    <td align="right" class="distTotValue"><b><%=distStat.getTotDistCount()%></b></td>
    <%
    	for (int i = 0; i < values.size(); i++) {
    %>
    <td align="right" class="distTotValue"><b><%=countMap.get(values.get(i))%></b></td>
    <%
    	}
    %>
    
  </tr>

  <%
  int posstart = 0;
  String cv = posstart+"";
  	Map<String, String> colmap = AccessionServlet.getKeywords().getAllColMap();
  	for (Iterator<String> colit = colmap.keySet().iterator(); colit.hasNext();) {
  		cv = (posstart % 2==0)?"0":"1";

  		String colkeyid = colit.next();
  		String colkeyname = colmap.get(colkeyid);
  %>
  <tr>
    <td nowrap="nowrap" class="distHead">
    <%
    	if (DistributionManager.getInstance()
    				.isDistributionAvailableForColl(colkeyid)) {
    %> <a title="Distribution data" class="headerlinks" href="/index.jsp?page=showdistribution&search=<%=AccessionConstants.COLLECTIONCODE+AccessionConstants.SPLIT_KEY+colkeyid%>"><%=colkeyname%></a><br />
    <%
    	} else {
    %> <%=colkeyname%><br />
    <%
    	}
    %>
    </td>
    <td align="right" class="distTotValue<%=cv%>"><b><%=distStat.getCollectiontotal(colkeyid)!= 0 ? distStat.getCollectiontotal(colkeyid) : nil%></td>
    <%
    
    	Map<String, Integer> collyearmap = distStat.getCollectionYearDistributionMap(colkeyid);
    		for (int i = 0; i < values.size(); i++) 
            {
              String year = (String)values.get(i);
    			int collValue=0;
    			if (collyearmap != null && collyearmap.containsKey(year)) {
    				collValue = collyearmap.get(values.get(i));
    			}
    %>
    <td align="right" class="distValue<%=cv%>" title="<%=colkeyname+" x "+ year%>"><%=collValue != 0 ? collValue : nil%></td>
    <%
  	}
    %>
    
  </tr>
  <%
    posstart++;
  	}
  %>
</table>
</div>
</div>
