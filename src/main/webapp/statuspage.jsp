<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
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
<div>

<table class="collTable" width="100%">
<%
HashMap<String,String> kMap  = new HashMap<String,String>();
kMap.put(AccessionConstants.TAXON,"Taxon");
kMap.put(AccessionConstants.SPECIES,"Species");
kMap.put(AccessionConstants.GENUS,"Genus");
kMap.put(AccessionConstants.COUNTRY,"Country");
kMap.put(AccessionConstants.INSTITUTE,"Institute");
kMap.put(AccessionConstants.REGION,"Region");
kMap.put(AccessionConstants.DEVSTAT,"Developing Status");
kMap.put(AccessionConstants.SOURCE,"Source");
kMap.put(AccessionConstants.USER,"User");
kMap.put(AccessionConstants.STATUS,"Status");
kMap.put(AccessionConstants.TRUST,"Trust");
kMap.put(AccessionConstants.PICTURE,"Pictures");
kMap.put(AccessionConstants.COLLECTION,"Collection");

Runtime rt = Runtime.getRuntime();
System.gc();
%>
<tr>
	<td class="collHead" colspan="5" align="center">System Information</td>
</tr>
<tr>
	<td class="collHead" colspan="1">Java Version</td>
	<td colspan="4"><%=System.getProperty("java.version")%></td>
</tr>
<tr>
	<td class="collHead" colspan="1">Free Memory</td>
	<td colspan="4"><%=String.valueOf((rt.freeMemory()/1024)/1024)%> MB</td>
</tr>
<tr>
	<td class="collHead" colspan="1">Total Memory</td>
	<td colspan="4"><%=String.valueOf(((rt.totalMemory()/1024)/1024))%> MB</td>
</tr>
<tr>
  <td class="collHead" colspan="1">Redirect Requests</td>
  <td colspan="4"><%=(SingerLoginFilter.getRedirctMCount()*SingerLoginFilter.refreshGCPer)+SingerLoginFilter.getRedirectCount()%></td>
</tr>

<%
List<Object> indexClasses = StatusPage.getInstance().getIndexClass();
if(indexClasses!=null)
{
	%>
	<tr>
		<td colspan="5" class="collHead" align="center">Index Information</td>
	</tr>
	<tr>
		<td class="collHead">Type</td>
		<td class="collHead">Name</td>
		<td class="collHead">Valid</td>
		<td class="collHead">Optimized</td>
		<td class="collHead">DocCount</td>
	</tr>
	<%
	IndexerInterface obj=null;
	for(int i=0;i<indexClasses.size(); i++)
	{
	obj = (IndexerInterface)indexClasses.get(i);
	List<IndexData> data = obj.getIndexList();
	if(data.size()>0)
	{
		int totCount = 0;
	%>
	<%
	for(int j=0;j<data.size(); j++)
	{
		IndexData iData = data.get(j);
		totCount = totCount+iData.getDocCount();
	%>
		<tr>
			<td class="collValue"><%=j==0?obj.getIndexName():""%></td>
			<td class="collValue"><%=obj.getClass().getName().endsWith("AccessionIndex")||obj.getClass().getName().endsWith("DistributionIndex")||obj.getClass().getName().endsWith("AdditionalLinksIndex")?"("+Keywords.getInstance().getName(iData.getName())+") - ":""%><%=iData.getName()%></td>
			<td class="collValue" style="color:<%=iData.isValid()?"green":"red"%>"><%=iData.isValid()%></td>
			<td class="collValue"><%=iData.isCompressed()%></td>
			<td class="collValue" align="right"><%=iData.getDocCount()+""%></td>
		</tr>
		
		<%if(obj.getClass().getName().endsWith("KeywordIndex"))
		{
			ArrayList<CompTermInfo> tinfo = BaseIndexer.getHighFreqTerms(iData.getIndexLoc(),"type", false).get("type");
			Collections.sort(tinfo);
			Collections.reverse(tinfo);
      
			for(int t = 0; t<tinfo.size(); t++)
			{
				CompTermInfo termInfo = tinfo.get(t);
		%>
		<tr>
			<td class="collValue"></td>
			<td class="collValue" colspan="2"><%=kMap.get(termInfo.getText())%> </td>
			<td class="collValue" align="right"><%=termInfo.getDocFreq()%></td>
			<td class="collValue"></td>
		</tr>
		
		<%	
			}
		}%>
		
	<%
	}
	%>
		<tr>
			<td colspan="4" class="collHead" >Total <%=obj.getIndexName()%> Count</td>
			<td class="collHead" align="right"><b><%=totCount%></b></td>
			</tr>
	<%
	}
}
	%>
	<%
}
%>
</table>

</div>
