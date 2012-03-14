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
<%@page import="java.io.File"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.sgrp.singer.DistributionStatistics"%>
<link rel="stylesheet" type="text/css" href="/tabs/tab.css" media="screen">
<script type="text/javascript" src="/tabs/tabber.js"></script>
<%
		Map<String, ArrayList> tMap = DistributionStatistics.getInstance().getGlobalDistributionMap();
		//List<File> colls = DistributionManager.getInstance().getDistributionDirs();
      
%>
<br />
<span class="keyTitle"><b>Global Distribution</b></span>
<br /><br />
IMPORTANT NOTICE: The SINGER database is in the process of being updated. Work is currently underway with Centres to update their distribution figures through 2008. Data currently displayed on the graph after 2006 do not yet accurately reflect the total amount of germplasm exchanged by the international genebanks of the CGIAR.
<br/>
<br/>
<div>
<div class="tabber">
<%
	if (tMap != null && tMap.size()>0) 
    {
    
			//String dLoc = colls.get(0).toString();
             
			ArrayList<CompTermInfo> tinfo = tMap.get("year"); 
			if (tinfo != null && tinfo.size() > 0) {
				//List<TermInfo> tlist = Arrays.asList(tinfo);
 	            Collections.sort(tinfo);
%>

<div class="tabbertab">
<h2>Yearly</h2>
<table width="100%">

  <tr>
    <td width="20%" align="left" valign="top">
    <table class="distTable">
      <tr>
        <td align="center" class="distHead" nowrap="nowrap"><b>Year(s)</b></td>
        <td align="center" class="distHead" nowrap="nowrap"><b>Accession(s)</b></td>
      </tr>
      <%
      	int totCount = 0;
      for (int t = 0; t < tinfo.size(); t++) {
    	  CompTermInfo termInfo = tinfo.get(t);
      					totCount = totCount + termInfo.getDocFreq();
      %>
      <tr class="distTable">
        <td class="distValue<%=(t%2==0)?"0":"1"%>" nowrap="nowrap"><%=termInfo.getText().equals(
													"0000") ? "Not Specified"
											: termInfo.getName()%></td>
        <td align="right" class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
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
    						.getYearlyDistributionChart("all", tinfo);
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
        <td class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
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
                .getRecipientChart("all", tinfo);
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
        <td class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
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
    						.getDevStatusChart("all", tinfo);
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
        <td class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
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
    						.getRegionChart("all", tinfo);
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
        <td class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getName()%></td>
        <td align="right" class="distValue<%=(t%2==0)?"0":"1"%>"><%=termInfo.getDocFreq()%></td>
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
%>
</div>
</div>
