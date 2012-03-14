<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/functions.jsp"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<%@page import="org.sgrp.singer.form.CoopForm"%>
<%@page import="org.sgrp.singer.form.MissionForm"%>
<%@page import="org.sgrp.singer.form.MissionCoopForm"%>
<%@page import="org.sgrp.singer.form.MissionDistributionForm"%>
<%@page import="org.sgrp.singer.form.MissionCollectionForm"%>

<%
	String misscode= request.getParameter("missid");
    String instcode = request.getParameter(AccessionConstants.INSTITUTECODE);
	
MissionForm missForm = SearchResults.getInstance().getMission(misscode);
if(missForm!=null)
{
%>
<span class="accTitle">Collecting Mission </span>
<br/>
<%
if(!missForm.getMisscode().equals("0"))
{
	/*Added by Gautier to show how many Accessions are linked to a collecting mission*/
	String query = AccessionConstants.INSTITUTECODE+AccessionConstants.SPLIT_KEY+instcode+" AND misscode"+AccessionConstants.SPLIT_KEY+misscode;
	String tokens [] = AccessionConstants.luceneStrToArray(query);
	String colls[] = AccessionConstants.getCollectionArray(tokens);
	int mapsize = SearchResults.getInstance().getAccessionCount(query, colls);
	if(mapsize>0)
	{
%>
<a href="/index.jsp?page=disppage&search=<%=query %>">
View the <%=mapsize %> linked accession(s)</a>
<%
	}
	else
	{
		%>No Accessions linked to this mission<%
	}
	/*End Added by Gautier*/
}
%>

<br/>
<br/>
<table width="100%">
<tr>
<td width="50%" valign="top">

<br/>
<span class="accTitle">Information</span>
<br/>

<table width="100%">
<tr>
<td class="collHead">Name</td>
<td class="collHead">Value</td>

<tr>
<td class="collValue">Mission code</td>
<td class="collValue"><%=missForm.getMisscode()%></td>
</tr>

<tr>
<td class="collValue">Institute</td>
<td class="collValue"><a class="headerlinks"
  href="/index.jsp?page=dispmissionpage&search=<%=AccessionConstants.INSTITUTECODE+AccessionConstants.SPLIT_KEY+missForm.getInstcode()%>"><%=AccessionServlet.getKeywords().getName(missForm.getInstcode())%></a></td>
</tr>

<tr>
<td class="collValue">Country</td>
<td class="collValue"><a class="headerlinks"
      href="/index.jsp?page=dispmissionpage&search=<%=AccessionConstants.COUNTRYCODE+AccessionConstants.SPLIT_KEY+missForm.getCtycode()%>"><%=missForm.getCtyname()%></a></td>
</tr>
<tr>

<td class="collValue">Start date</td>
<td class="collValue"><%=missForm.getStartdate()%></td>
</tr>
<tr>
<td class="collValue">End date</td>
<td class="collValue"><%=missForm.getEnddate()%></td>
</tr>
<tr>
</table>
</td>
<td width="50%" valign="top">
<%
Map<String, MissionCollectionForm> misscollMap = SearchResults.getInstance().getMissionCollectionMapBySearch(misscode, instcode);
if(misscollMap!=null && misscollMap.size()>0)
{
%> 
<br/>
<span class="accTitle">Collected Material (<%=misscollMap.size()%>)</span>
<br/>
<table width="100%">
<tr>
<td class="collHead">Genus/Species</td>
<td class="collHead">Samples</td>
</tr>
  
  <%
  for(Iterator<String> itr=misscollMap.keySet().iterator(); itr.hasNext();)
  {
    String misscollid = itr.next();
    MissionCollectionForm misscollForm = misscollMap.get(misscollid);
    if(misscollForm!=null)
    {
  %>
    <tr>
    <td class="collValue" valign="top"><%=misscollForm.getSp()%></td>
    <td class="collValue" valign="top" align="right"><%=misscollForm.getSamp()%></td>
    </tr>
  <%
  }
  else
  {
  %>
  <tr>
  <td colspan="4" align="left" class="collValue">
  <i style="font-weight:bold;color:red">
  <%=misscollForm.getCode()%> Not a valid coop id</i> 
  </td>
  </tr>
  <%
  }
  }
  %>
  </table>
  <%
}
misscollMap=null;
%>
</td>
</tr>
</table>
<%
Map<String, MissionCoopForm> misscoopMap = SearchResults.getInstance().getMissCoopMapBySearch("misscode="+misscode);
if(misscoopMap!=null && misscoopMap.size()>0)
{
%>
	
<br/>
<span class="accTitle">Cooperators Information (<%=misscoopMap.size()%>)</span>
<br/>
<table width="100%">
<tr>
<td class="collHead" width="20%">Cooperator</td>
<td class="collHead" width="40%">Organization</td>
<td class="collHead" width="25%">Type</td>
<td class="collHead" width="15%">Country</td>
</tr>
	
	<%
	for(Iterator<String> itr=misscoopMap.keySet().iterator(); itr.hasNext();)
	{
		String misscoopid = itr.next();
		MissionCoopForm misscoopForm = misscoopMap.get(misscoopid);
		CoopForm cmissForm = misscoopForm.getCoopForm();
		if(cmissForm!=null)
		{
	%>
		<tr>
		<td class="collValue">
		<%=cmissForm.getCoopname()!=null?cmissForm.getCoopname():"&nbsp;"%>
		</td>
		<td class="collValue">
		<%=cmissForm.getCooporg()%>
		</td>
		<td class="collValue">
		<%=cmissForm.getUsername()%>
		</td>
		<td class="collValue">
		<%=cmissForm.getCtyname()%>
		</td>
	<%
	}
	else
	{
	%>
	<tr>
	<td colspan="4" align="left" class="collValue">
	<i style="font-weight:bold;color:red">
	<%=misscoopForm.getCoopcode()%> Not a valid coop id</i> 
	</td>
	</tr>
	<%
	}
	}
	%>
	</table>
	<%
}
misscoopMap=null;
%>

<br/>
<%
Map<String, MissionDistributionForm> missdistMap = SearchResults.getInstance().getMissionDistributionMapBySearch(misscode, instcode);
if(missdistMap!=null && missdistMap.size()>0)
{
%>
  
<br/>
<span class="accTitle">Distribution of Collected Material (<%=missdistMap.size()%>)</span>
<br/>
<table width="100%">
<tr>
<td class="collHead" width="20%">Genus/Species</td>
<td class="collHead" width="5%">Samples</td>
<td class="collHead" width="25%">Institute</td>
<td class="collHead" width="30%">Address</td>
<td class="collHead" width="25%">Details</td>
</tr>
  
  <%
  for(Iterator<String> itr=missdistMap.keySet().iterator(); itr.hasNext();)
  {
    String missdistid = itr.next();
    MissionDistributionForm missdistForm = missdistMap.get(missdistid);
    if(missdistForm!=null)
    {
  %>
    <tr>
    <td class="collValue" valign="top"><%=missdistForm.getSp()%></td>
    <td class="collValue" valign="top"><%=missdistForm.getSamp()%></td>
    <td class="collValue" valign="top"><%=missdistForm.getName()%><br/>FAO Code:  <%=missdistForm.getFaocode()%></td>
    <td class="collValue" valign="top"><%=missdistForm.getStreet()%><br/>
    <%=missdistForm.getCity()%><br/>
    <%=missdistForm.getZip()%>
    </td>
    <td class="collValue" valign="top">Tel: <%=missdistForm.getTlf()%><br/>
    Telex: <%=missdistForm.getTlx()%><br/>
    Fax: <%=missdistForm.getFax()%><br/>
    Email: <%=missdistForm.getEmail()%>
    </td>
    </tr>
  <%
  }
  else
  {
  %>
  <tr>
  <td colspan="4" align="left" class="collValue">
  <i style="font-weight:bold;color:red">
  <%=missdistForm.getCode()%> Not a valid coop id</i> 
  </td>
  </tr>
  <%
  }
  }
  %>
  </table>
  <%
}
missdistMap=null;
}
%>

