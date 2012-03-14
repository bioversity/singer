<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ include file="/functions.jsp"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@page import="org.sgrp.singer.PropertiesManager"%>
<%@page import="org.sgrp.singer.form.AccessionForm"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.MyShopCart"%>
<%@page import="org.sgrp.singer.form.RecepientForm"%>
<%@page import="org.sgrp.singer.form.DonorForm"%>
<%@page import="org.sgrp.singer.form.CoopForm"%>
<%@page import="org.sgrp.singer.form.MissionForm"%>
<%@page import="org.sgrp.singer.form.MissionCoopForm"%>
<%@page import="org.sgrp.singer.form.DistributionForm"%>
<%@page import="org.sgrp.singer.form.AdditionalLinksForm"%>
<%@page import=" org.apache.lucene.document.Document" %>

<%
MyShopCart shopCart = (MyShopCart)request.getSession(true).getAttribute("mycart");
	String collcode = request.getParameter(AccessionConstants.COLLECTIONCODE);
	String[] colls = null;
	if(collcode!=null && collcode.trim().length()>0)
	{
		colls = new String[1];
		colls[0] = collcode;
	}
	String accid = request.getParameter(AccessionConstants.ACCESSIONCODE);
	AccessionForm accForm = SearchResults.getInstance().getAccession(accid, colls);
	if (accForm == null) 
	{
%>
<br/>
<span class="error">
<center><b>NO ACCESSION DATA WITH ID <u><%=accid%></u> in <u><%=collcode%></u> EXISTS!!!</b></center>
</span>
<%
	} else {
%>
<br />
<div>
<table width="100%" class="accTable">
<tr>
<td width="90%" valign="top">
<span class="accTitle">Passport information
<% //Add by Gautier %>
<a title="Passport Definitions" href="<%=PropertiesManager.getString(AccessionConstants.DESCRIPTORS_LINK)%>/index.jsp?page=mcpd" target="_blank"><img src="/img/question.gif" border="0"/></a>
<% //End Add by Gautier %>
</span>
<br/>
<table width="100%" align="center">
<tr><td class="accHead" nowrap="nowrap" width="30%">Accession number</td><td class="accValue" width="70%"><%=accForm.getAccenumb()%>
<%
boolean inCart = false;
      if(shopCart!=null)
      {
    	  inCart =shopCart.inCart(accForm.getOrigacceid());
      }
			if(inCart)
		 	{
		%>
			<a href="/index.jsp?page=showaccession&<%=AccessionConstants.ACCESSIONCODE%>=<%=accForm.getAcceid()%>&caction=remove&caccId=<%=accForm.getOrigacceid()%>"><img src="/img/icon_tick.gif"/></a>
		<%
		 	}
		 	else if(shopCart.canAdd())
		 	{
		%>
			<a href="/index.jsp?page=showaccession&<%=AccessionConstants.ACCESSIONCODE%>=<%=accForm.getAcceid()%>&caction=add&caccId=<%=accForm.getOrigacceid()%>&ccollId=<%=accForm.getCollcode()%>"><img src="/img/add_icon.gif"/></a>
		<%
	        } else {
		%> <img src="/img/full_icon.gif" title="Cart already full"/><%
		    }
		%>
</td></tr>
<tr><td class="accHead">Accession name</td><td class="accValue"><%=accForm.getAccename()%></td></tr>
<tr><td class="accHead">Genus</td><td class="accValue"><a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.GENUSCODE+AccessionConstants.SPLIT_KEY+accForm.getGenuscode()%>"><i><%=AccessionConstants.makeProper(accForm.getGenusname())%></i></a></td></tr>
<tr><td class="accHead">Species</td><td class="accValue"><a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.SPECIESCODE+AccessionConstants.SPLIT_KEY+accForm.getSpeciescode()%>"><i><%=accForm.getSpeciesname().toLowerCase()%></i></a></td></tr>
<tr><td class="accHead">Taxon</td><td class="accValue"><a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.TAXONCODE+AccessionConstants.SPLIT_KEY+accForm.getTaxcode()%>"><%=AccessionConstants.makeProper(accForm.getTaxname())%></a></td></tr>
<tr><td class="accHead">Institute</td><td class="accValue"><a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.INSTITUTECODE+AccessionConstants.SPLIT_KEY+accForm.getInstcode()%>"><%=accForm.getInstname()%></a></td></tr>
<tr><td class="accHead">Collection Name</td><td class="accValue"><a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.COLLECTIONCODE+AccessionConstants.SPLIT_KEY+accForm.getCollcode()%>"><%=accForm.getCollname()%></a></td></tr>
<tr><td class="accHead">Country Source</td><td class="accValue"><a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.COUNTRYCODE+AccessionConstants.SPLIT_KEY+accForm.getOrigcode()%>"><%=accForm.getOrigname()%></a></td></tr>
<tr><td class="accHead">Collecting Source</td><td class="accValue"><%=accForm.getSrcname()%> <!-- a class="headerlinks" href="/index.jsp?page=showkeycount&search=<=AccessionConstants.SOURCECODE+AccessionConstants.SPLIT_KEY+accForm.getSrccode()>"><=AccessionServlet.getKeywords().getName(accForm.getSrccode())></a--></td></tr>
<tr><td class="accHead">Sample Status</td><td class="accValue"><a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=AccessionConstants.STATUSCODE+AccessionConstants.SPLIT_KEY+accForm.getStatcode()%>"><%=accForm.getStatname()%></a></td></tr>
<!-- tr><td class="accHead">Donor Code</td><td class="accValue"><=accForm.getDonorcode()%></td></tr-->
<tr><td class="accHead">Donor Institute Type</td><td class="accValue">
<%
DonorForm donorForm = SearchResults.getInstance().getDonor(accForm.getDonorcode());
if(donorForm!=null)
{
%> 
<%=donorForm.getUsername()!=null?"<b>"+donorForm.getUsername()+"</b>"+"<br/>":""%>
<%=donorForm.getDonorname()!=null?donorForm.getDonorname()+"<br/>":""%>
<%}%>
</td></tr>
<tr><td class="accHead">Donor Institute Name</td><td class="accValue">
<%
if(donorForm!=null)
{
%> 
<%=donorForm.getDonororg()!=null?donorForm.getDonororg():""%>
<%}%>
</td></tr>
<tr><td class="accHead">Acquisition Date</td><td class="accValue"><%=accForm.getAcqdate()%></td></tr>
<tr><td class="accHead">Collection number</td><td class="accValue"><%=accForm.getCollnumb()%></td></tr>
<tr><td class="accHead">Collection Date</td><td class="accValue"><%=accForm.getColldate()%></td></tr>
<tr><td class="accHead">Other numbers</td><td class="accValue"><%=accForm.getOthernumb()%></td></tr>
<tr><td class="accHead">Pedigree</td><td class="accValue">
<%if(accForm.getPedigree()!=null && !accForm.getPedigree().equals("null"))
{
  %>
    <%=accForm.getPedigree()%>
  <%
} else if ((accForm.getParentfemale()!=null && !accForm.getParentfemale().equals("null")) && (accForm.getParentmale()!=null&& !accForm.getParentmale().equals("null")))
{
  %>
    <img src="/img/female.png"><%=accForm.getParentfemale()%>&nbsp;<img src="/img/male.png"><%=accForm.getParentmale()%> 
  <%
}
%>&nbsp;
</td></tr>
<tr><td class="accHead">Intrust status</td><td class="accValue"><%=accForm.getTrustname()%><!-- a class="headerlinks" href="/index.jsp?page=showkeycount&search=<=AccessionConstants.TRUSTCODE+AccessionConstants.SPLIT_KEY+accForm.getTrustcode()%>"><=accForm.getTrustname()%></a--></td></tr>
<tr><td class="accHead">Availability</td><td class="accValue"><%
if(accForm.getAvailability().equalsIgnoreCase("n"))
{
%>
Not available
<%
}else if(accForm.getAvailability().equalsIgnoreCase("y"))
{
%>Is available<%
}else
{
%><i>No data available</i><%
}
%></td></tr>
<tr><td class="accHead">Safety-duplicate in svalbard</td><td class="accValue">
<%
if(accForm.getInsvalbard().equalsIgnoreCase("n"))
{
%>
No
<%
}else if(accForm.getInsvalbard().equalsIgnoreCase("y"))
{
%>Yes<%
}else
{
%><i>No data available</i><%
}
%>
</td></tr>
<tr><td class="accHead">Collection site</td><td class="accValue"><%=accForm.getCollsite()%></td></tr>
<tr><td class="accHead">Latitude<sup>o</sup></td><td class="accValue"><i><%=accForm.getLatituded()%></i></td></tr>
<tr><td class="accHead">Longitude<sup>o</sup></td><td class="accValue"><i><%=accForm.getLongituded()%></i></td></tr>
<%
  if(accForm.getLatituded()!=null && !accForm.getLatituded().equals("0") && accForm.getLatituded().trim().length()>0  && accForm.getLongituded()!=null && !accForm.getLongituded().equals("0") && accForm.getLongituded().trim().length()>0)
  {
    String text = "Collecting Site :<b>"+accForm.getCollsite()+"</b><br/>";
    text = text + "Country :<b>"+accForm.getOrigname()+"</b><br/>";
    text = text + "Acession :<b>"+accForm.getAccenumb()+"</b>";
    String mapLink = "/map.jsp?lat="+accForm.getLatituded()+"&lng="+accForm.getLongituded()+"&text="+text;
    String geodataLink = "/index.jsp?page=geodata&lat="+accForm.getLatituded()+"&lng="+accForm.getLongituded()+"&acc="+accForm.getAcceid()+"&col="+accForm.getCollcode();

%>
<tr><td class="accHead">Map</td><td class="accValue">
<p align="right"><a href="<%=geodataLink%>">View enviromental data</a></p><center><jsp:include page="<%=mapLink%>"/></center></td>
</tr>
<%
}
%>
</table>
<%
if(accForm.getMisscode()!=null && !accForm.getMisscode().equals("0"))
{
MissionForm missForm = SearchResults.getInstance().getMission(accForm.getMisscode());
if(missForm!=null && !missForm.getMisscode().equals("0"))
{
%>
<br/>
<span class="accTitle">Collection Mission Information</span>
<br/>
<table width="100%">
<tr>
<td class="collHead" width="20%">Mission code</td>
<td class="collHead" width="40%">Country</td>
<td class="collHead" width="20%">Start date</td>
<td class="collHead" width="20%">End date</td>
</tr>
<tr>
<td class="collValue"><%
if(!missForm.getMisscode().equals("0"))
{
%><a
    href="/index.jsp?page=showmission&missid=<%=accForm.getMisscode()%>&<%=AccessionConstants.INSTITUTECODE%>=<%=missForm.getInstcode()%>"><%=missForm.getMisscode()%></a>
    <%}
else{%>
<%=missForm.getMisscode()%>
<%}%></td>
<td class="collValue"><%=missForm.getCtyname()%></td>
<td class="collValue"><%=missForm.getStartdate()%></td>
<td class="collValue"><%=missForm.getEnddate()%></td>
</tr>
</table>
<!-- 
<
Map<String, MissionCoopForm> misscoopMap = SearchResults.getInstance().getMissCoopMapBySearch("misscode="+accForm.getMisscode());
if(misscoopMap!=null && misscoopMap.size()>0)
{
%>
	
<br/>
<span class="accTitle">Cooperators Information (<=misscoopMap.size()%>)</span>
<br/>
<table width="100%">
<tr>
<td class="collHead" width="20%">Cooperator</td>
<td class="collHead" width="40%">Organization</td>
<td class="collHead" width="25%">Type</td>
<td class="collHead" width="15%">Country</td>
</tr>
	
	<
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
		<=cmissForm.getCoopname()!=null?cmissForm.getCoopname():"&nbsp;"%>
		</td>
		<td class="collValue">
		<=cmissForm.getCooporg()%>
		</td>
		<td class="collValue">
		<=cmissForm.getUsername()%>
		</td>
		<td class="collValue">
		<=cmissForm.getCtyname()%>
		</td>
		</tr>
	
	
	<
	}
	else
	{
	%>
	<tr>
	<td colspan="4" align="left" class="collValue">
	<i style="font-weight:bold;color:red">
	<=misscoopForm.getCoopcode()%> Not a valid coop id</i> 
	</td>
	</tr>
	<
	}
	}
	%>
	</table>
	<
}
misscoopMap=null;
}
%>
 -->
<%
}
}
%>
<%
Map<String, DistributionForm> distMap = SearchResults.getInstance().getAccDistributionMapBySearch(accForm.getAcceid(), accForm.getCollcode());

if(distMap!=null && distMap.size()>0)
{
%>
	
<br/>
<span class="accTitle">Distribution Information (<%=distMap.size()%>)</span>
<br/>
<table width="100%">
<tr>
<td class="collHead" width="20%">Recepient</td>
<td class="collHead" width="40%">Organization</td>
<td class="collHead" width="25%">Type</td>
<td class="collHead" width="15%">Trans date</td>
<!-- td class="collHead" width="20%">Rep date</td-->
</tr>
	
	<%
	for(Iterator<String> itr=distMap.keySet().iterator(); itr.hasNext();)
	{
		String distid = itr.next();
		DistributionForm distForm = distMap.get(distid);
		RecepientForm rdistForm = distForm.getRecepientForm();
		if(rdistForm!=null)
		{
	%>
		<tr>
		<td class="collValue">
		<%=rdistForm.getRecepientname()!=null?rdistForm.getRecepientname():"&nbsp;"%>
		</td>
		<td class="collValue">
		<%=rdistForm.getRecepientorg()%>
		<br/>
		<%=rdistForm.getCtyname()%>
		</td>
		<td class="collValue">
		<%=rdistForm.getUsername()%>
		</td>
		<td class="collValue">
		<%=distForm.getDatetrans()%>
		</td>
		<!-- td class="collValue">
		<=distForm.getRepdate()%>
		</td-->
		</tr>
	<%
	}
	else
	{
	%>
	<tr>
	<td colspan="4" align="left" class="collValue">
	<i style="font-weight:bold;color:red">
	<%=distForm.getRecptcode()%> Not a valid coop id </i> 
	</td>
	</tr>
	<%
	}
	}
	%>
	</table>
	<%
}
distMap=null;
%>
<!--
	String text = "Collecting Site :<b>"+accForm.getCollsite()+"</b><br/>";
	text = text + "Country :<b>"+accForm.getOrigname()+"</b><br/>";
	text = text + "Acession :<b>"+accForm.getAccenumb()+"</b>";
	String mapLink = "/map.jsp?lat="+accForm.getLatituded()+"&lng="+accForm.getLongituded()+"&text="+text;
	if(accForm.getLatituded()!=null && !accForm.getLatituded().startsWith("0") && accForm.getLatituded().trim().length()>0  && accForm.getLongituded()!=null && !accForm.getLongituded().startsWith("0") && accForm.getLongituded().trim().length()>0)
	{
		request.getSession(true).setAttribute("mapLink",mapLink);
	}
-->
</td>
<td width="10%" valign="top">
<table width="100%">
<%
    Map<String,AdditionalLinksForm> linksMap = SearchResults.getInstance().getAdditionalLinksMapBySearch(accForm.getAcceid(), accForm.getCollcode());
      if(linksMap.size()>0)
      {
%>
<tr>
<td>
<center class="collHead"><b>Links</b></center>
<br/>
<%
    	 for (Iterator<String> sit=linksMap.keySet().iterator(); sit.hasNext(); ) {
        String sid = sit.next();
        AdditionalLinksForm linkForm = linksMap.get(sid);
    %>
    <a title="<%=linkForm.syslinkid%>" class="headerlinks" target="_olinks" href="<%=linkForm.getSyslink()%>"><%=linkForm.getType()%></a><br/>
    <%
    }
%>
<br/>
</td>
</tr>
<%
  }
  linksMap = null;
%>
<tr>
<td>
<center class="collHead"><b>Images</b></center>
<br/>
<center>
<%
    Map<String,String> subMap = SearchResults.getInstance().getPicturesListForGenusSpecies(accForm.getGenusname(), accForm.getSpeciesname());
      if(subMap.size()>0)
      {
        %>
<%
				int size = 50;
				int dsize = subMap.size()/2;
				if(dsize>size)
				{
					dsize = 40;
				}
				size = size - dsize;
				for (Iterator<String> sit=subMap.keySet().iterator(); sit.hasNext(); ) {
				String sid = sit.next();
				String doc = subMap.get(sid);
				String pname = AccessionConstants.getRegExValueMap(doc, AccessionConstants.FULL_NAME).get(AccessionConstants.FULL_NAME);
				String other = AccessionConstants.getRegExValueMap(doc, "other").get("other");
		%>
		<a title="<%=other%>" href="javascript:void(0);"><img src="/img/<%=pname%>" height="<%=size%>" width="<%=size%>" onmouseover="popImage('/img/<%=pname%>','<%=accForm.getGenusname()%> <%=accForm.getSpeciesname()%>',escape('<%=other%>'));"></a>		
		<%
		}
        %>
<%
    }else
    {
     %>
     <i>No image(s) found</i>
     <%
    }
      subMap = null;
    %>        
</center>
</td>
</tr>
</table>
</td>
</tr>
</table>
</div>
<br/>
<%
}
%>

