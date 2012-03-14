<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.HashMap"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page import="org.sgrp.singer.StringComparableValues"%>
<%@page import="org.sgrp.singer.StatusPage"%>
<%@page import="org.sgrp.singer.indexer.IndexerInterface"%>
<%@page import="org.sgrp.singer.indexer.IndexData"%>
<%@page import="org.sgrp.singer.indexer.Keywords"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.lucene.index.IndexReader"%>
<%@page import="org.apache.lucene.index.Term"%>
<%@page import="org.sgrp.singer.indexer.CompTermInfo"%>
<%@page import="org.sgrp.singer.indexer.BaseIndexer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="org.sgrp.singer.filters.SingerLoginFilter"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<table width="100%">
  <tr>
    <td align="left" width="50%" class="cartValue1"><b>Institute(s) </b></td>
    <td align="right" width="50%" class="cartValue0"><%=AccessionServlet.getKeywords().getAllInstMap().size()%></td>
  </tr>
  <tr>
    <td align="left" class="cartValue1"><b>Collection(s)</b></td>
    <td align="right" class="cartValue0"><%=AccessionServlet.getKeywords().getAllColMap()
									.size()%></td>
  </tr>
  <tr>
    <td align="left" class="cartValue1"><b>Picture(s)</b></td>
    <td align="right" class="cartValue0"><%=AccessionServlet.getKeywords().getAllPicturesMap()
                  .size()%></td>
  </tr>
  <tr>
    <td align="left" class="cartValue1"><b>Pedigree</b></td>
    <td align="right" class="cartValue0">10498</td>
  </tr>

  <%
  	List<Object> indexClasses = StatusPage.getInstance()
  			.getIndexClass();
  	if (indexClasses != null) {
  %>
  <%
  	IndexerInterface obj = null;
  		for (int i = 0; i < indexClasses.size(); i++) {
  			obj = (IndexerInterface) indexClasses.get(i);
  			if (obj.getClass().getName().endsWith("AccessionIndex")
  					|| obj.getClass().getName().endsWith(
  							"DistributionIndex")|| obj.getClass().getName().endsWith(
                "MissionIndex")|| obj.getClass().getName().endsWith(
          "AdditionalLinksIndex")) {
  				List<IndexData> data = obj.getIndexList();
  				if (data.size() > 0) {
  					int totCount = 0;
  					for (int j = 0; j < data.size(); j++) {
  						IndexData iData = data.get(j);
  						totCount = totCount + iData.getDocCount();
  					}
  %>
  <tr>
    <td align="left" nowrap="nowrap" class="cartValue1"><b><%=obj.getShortName()%></b></td>
    <td align="right" nowrap="nowrap" class="cartValue0"><%=totCount%></td>
  </tr>
  <%
  	}
  			}
  		}
  	}
  %>
</table>
