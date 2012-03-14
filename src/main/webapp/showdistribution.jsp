<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="org.sgrp.singer.ComparableValues"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="org.sgrp.singer.AccessionConstants"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>

<%@page import="org.sgrp.singer.db.DistributionManager"%>
<%@page import="org.sgrp.singer.indexer.CompTermInfo"%>
<%@page import="org.sgrp.singer.indexer.BaseIndexer"%>
<%@page import="org.sgrp.singer.DistributionChart"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.sgrp.singer.DistributionStatistics"%>
<link rel="stylesheet" type="text/css" href="/tabs/tab.css" media="screen">
<script type="text/javascript" src="/tabs/tabber.js"></script>
<%
  String fields[] = new String[]{"year", "usercode","devstat","region","ctycode"};    
	String query = request.getParameter("search");
	if (query != null && query.trim().length() > 0) {
      String type = request.getParameter("type");
      if (type == null || type.trim().length() == 0) {
       type="col"; 
      }
      
		String tokens[] = AccessionConstants.luceneStrToArray(query);
		String colls[] = AccessionConstants.getCollectionArray(tokens);
		String queryName = AccessionServlet.getKeywords().getQueryAsText(tokens, query);
%>
<br />
<span class="keyTitle"><b><%=queryName%></b></span>
<br/>
<div>
<div class="tabber">
<%
String fileName = colls[0];
if(colls.length>0)
{
  fileName = BaseIndexer.mangleKeywordValue(queryName);
}
  Map<String, ArrayList> tMap =null;
  if(type.equals("inst"))
  {
	  tMap= DistributionStatistics.getInstance().getCollArrayDistributionMap(colls,true,fileName);
  }
  else
  {
 	  tMap= DistributionStatistics.getInstance().getCollArrayDistributionMap(colls);
  }
  if(tMap!= null && tMap.size()> 0) {
		ArrayList<CompTermInfo> tinfo = tMap.get("year"); 
		if (tinfo != null && tinfo.size() > 0) {
          Collections.sort(tinfo);
%>

<div class="tabbertab">
<h2>Yearly</h2>
<table width="100%">

  <tr>
    <td width="20%" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead"><b>Year(s)</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
      	int totCount = 0;
      				for (int t = 0; t < tinfo.size(); t++) {
      					CompTermInfo termInfo = tinfo.get(t);
      					totCount = totCount + termInfo.getDocFreq();
      %>
      <tr class="distTable">
        <td class="distsValue<%=(t%2==0)?"0":"1"%>" nowrap="nowrap"><%=termInfo.getText().equals(
													"0000") ? "Not Specified"
											: termInfo.getText()%></td>
        <td align="right" class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
      </tr>
      <%
      	}
      %>
      <tr>
        <td><b><%=tinfo.size()%></b></td>
        <td align="right"><b><%=totCount%></b></td>
      </tr>
    </table>
    </td>
    <td width="80%" valign="top">
    <%
    	String link = DistributionChart.getInstance()
    						.getYearlyDistributionChart(fileName, tinfo);
    %> <img src="<%=link%>" border="0" /></td>
  </tr>
</table>
</div>

<%
	}
%> <%
  tinfo = tMap.get("usercode");
if (tinfo != null && tinfo.size() > 0) {
    Collections.sort(tinfo);
 %>
<div class="tabbertab">
<h2>Recipient</h2>

<table width="100%">
  <tr>
    <td width="40%" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead" nowrap="nowrap"><b>Type</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
        int totCount = 0;
             for (int t = 0; t < tinfo.size(); t++) {
            	 CompTermInfo termInfo = tinfo.get(t);
                totCount = totCount + termInfo.getDocFreq();
      %>

      <tr>
        <td class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
      </tr>
      <%
        }
      %>
      <tr>
        <td><b><%=tinfo.size()%></b></td>
        <td align="right"><b><%=totCount%></b></td>
      </tr>
    </table>
    </td>
    <td width="60%" valign="top">
    <%
      String link = DistributionChart.getInstance()
                .getRecipientChart(fileName, tinfo);
    %> <img src="<%=link%>" border="0" /></td>
  </tr>
</table>
</div>
<%
  }
%> <%
 	tinfo = tMap.get("devstat");
if (tinfo != null && tinfo.size() > 0) {
    Collections.sort(tinfo);
 %>
<div class="tabbertab">
<h2>Status</h2>

<table width="100%">
  <tr>
    <td width="40%" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead" nowrap="nowrap"><b>Status</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
      	int totCount = 0;
                  for (int t = 0; t < tinfo.size(); t++) {
                	  CompTermInfo termInfo = tinfo.get(t);
      					totCount = totCount + termInfo.getDocFreq();
      %>

      <tr>
        <td class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
      </tr>
      <%
      	}
      %>
      <tr>
        <td><b><%=tinfo.size()%></b></td>
        <td align="right"><b><%=totCount%></b></td>
      </tr>
    </table>
    </td>
    <td width="60%" valign="top">
    <%
    	String link = DistributionChart.getInstance()
    						.getDevStatusChart(fileName, tinfo);
    %> <img src="<%=link%>" border="0" /></td>
  </tr>
</table>
</div>
<%
	}
%> <%
 	tinfo = tMap.get("region");
if (tinfo != null && tinfo.size() > 0) {
    Collections.sort(tinfo);
 %>
<div class="tabbertab">
<h2>Region</h2>

<table width="100%">
  <tr>
    <td width="40%" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead" nowrap="nowrap"><b>Regions</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
      	int totCount = 0;
                    for (int t = 0; t < tinfo.size(); t++) {
                    	CompTermInfo termInfo = tinfo.get(t);
      					totCount = totCount + termInfo.getDocFreq();
      %>

      <tr>
        <td class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
      </tr>
      <%
      	}
      %>
      <tr>
        <td><b><%=tinfo.size()%></b></td>
        <td align="right"><b><%=totCount%></b></td>
      </tr>
    </table>
    </td>
    <td width="60%" valign="top">
    <%
    	String link = DistributionChart.getInstance()
    						.getRegionChart(fileName, tinfo);
    %> <img src="<%=link%>" border="0" /></td>
  </tr>
</table>
</div>
<%
}
%> <%
 	tinfo = tMap.get("ctycode");
if (tinfo != null && tinfo.size() > 0) {
    Collections.sort(tinfo);
 %>
<div class="tabbertab">
<h2>Country</h2>

<table width="100%">
  <tr>
    <td width="100%" colspan="2" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead" nowrap="nowrap"><b>Countries</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
      	int totCount = 0;
      				for (int t = 0; t < tinfo.size(); t++) {
      					CompTermInfo termInfo = tinfo.get(t);
      					totCount = totCount + termInfo.getDocFreq();
      %>

      <tr>
        <td class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
      </tr>
      <%
      	}
      %>
      <tr>
        <td><b><%=tinfo.size()%></b></td>
        <td align="right"><b><%=totCount%></b></td>
      </tr>
    </table>
    </td>
    <!-- td width="80%" valign="top">
    <
    	String link = DistributionChart.getInstance().getCountryChart(colls[0], tinfo);
    %> <img src="<=link%>" border="0" /></td-->
  </tr>
</table>
</div>
<%
	}
%><%
  tinfo = tMap.get(AccessionConstants.TAXONCODE);
if (tinfo != null && tinfo.size() > 0) {
    Collections.sort(tinfo);
 %>
<div class="tabbertab">
<h2>Taxon</h2>

<table width="100%">
  <tr>
    <td width="100%" colspan="2" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead" nowrap="nowrap"><b>Taxon</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
        int totCount = 0;
              for (int t = 0; t < tinfo.size(); t++) {
                CompTermInfo termInfo = tinfo.get(t);
                totCount = totCount + termInfo.getDocFreq();
      %>

      <tr>
        <td class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
      </tr>
      <%
        }
      %>
      <tr>
        <td><b><%=tinfo.size()%></b></td>
        <td align="right"><b><%=totCount%></b></td>
      </tr>
    </table>
    </td>
    <!-- td width="80%" valign="top">
    <
      String link = DistributionChart.getInstance().getCountryChart(colls[0], tinfo);
    %> <img src="<=link%>" border="0" /></td-->
  </tr>
</table>
</div>
<%
  }
%><%
  tinfo = tMap.get(AccessionConstants.GENUSCODE);
if (tinfo != null && tinfo.size() > 0) {
    Collections.sort(tinfo);
 %>
<div class="tabbertab">
<h2>Genus</h2>

<table width="100%">
  <tr>
    <td width="100%" colspan="2" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead" nowrap="nowrap"><b>Genus</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
        int totCount = 0;
              for (int t = 0; t < tinfo.size(); t++) {
                CompTermInfo termInfo = tinfo.get(t);
                totCount = totCount + termInfo.getDocFreq();
      %>

      <tr>
        <td class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
      </tr>
      <%
        }
      %>
      <tr>
        <td><b><%=tinfo.size()%></b></td>
        <td align="right"><b><%=totCount%></b></td>
      </tr>
    </table>
    </td>
    <!-- td width="80%" valign="top">
    <
      String link = DistributionChart.getInstance().getCountryChart(colls[0], tinfo);
    %> <img src="<=link%>" border="0" /></td-->
  </tr>
</table>
</div>
<%
  }
%><%
  tinfo = tMap.get(AccessionConstants.SPECIESCODE);
if (tinfo != null && tinfo.size() > 0) {
    Collections.sort(tinfo);
 %>
<div class="tabbertab">
<h2>Species</h2>

<table width="100%">
  <tr>
    <td width="100%" colspan="2" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead" nowrap="nowrap"><b>Species</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
        int totCount = 0;
              for (int t = 0; t < tinfo.size(); t++) {
                CompTermInfo termInfo = tinfo.get(t);
                totCount = totCount + termInfo.getDocFreq();
      %>

      <tr>
        <td class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distsValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
      </tr>
      <%
        }
      %>
      <tr>
        <td><b><%=tinfo.size()%></b></td>
        <td align="right"><b><%=totCount%></b></td>
      </tr>
    </table>
    </td>
    <!-- td width="80%" valign="top">
    <
      String link = DistributionChart.getInstance().getCountryChart(colls[0], tinfo);
    %> <img src="<=link%>" border="0" /></td-->
  </tr>
</table>
</div>
<%
  }
%>
 <%
 	} else {
 %>
<table width="100%">
  <tr>
    <td colspan="2">
    <center><font color="red"><b>No distribution data is available for this collection</b></font></center>
    </td>
  </tr>
</table>
<%
	}
	} else {
%> <br />
<br />
<table width="100%">
  <tr>
    <td colspan="2">
    <center><font color="red"><b>Not a valid query String</b></font></center>
    </td>
  </tr>
</table>
<%
	}
%>
</div>
</div>
