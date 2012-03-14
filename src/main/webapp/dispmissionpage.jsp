<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="org.sgrp.singer.form.MissionForm"%>
<%@page import="org.sgrp.singer.db.MissionManager"%>
<%@page import="org.sgrp.singer.form.GenericForm"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<%@page import="java.util.LinkedHashMap"%>
<%
String query = request.getParameter("search");
if(query!=null && query.trim().length()>0)
{
	
	/*Added by Gautier to create a pagination of Collecting Missions*/
	String rpp = request.getParameter("rpp");
	String rp = request.getParameter("rp");
	int rperPage = 20;
	int rPage = 1;
	try {
		rperPage = Integer.parseInt(rpp);
	} catch (Exception e) {
		rperPage = 20;
	}
	try {
		rPage = Integer.parseInt(rp);
	} catch (Exception e) {
		rPage = 1;
	}
	/*End added by Gautier*/
%>

<div>
<br />
<%
        //System.out.println("Query in timeline is :"+query);
        Map<String,MissionForm> map = SearchResults.getInstance().getMissionMapBySearch(query);
       
        /*Added by Gautier to create a pagination of Collecting Missions*/
        // We count how many pages are needed (same as in disppage.jsp)
        float mapsize = (float) map.size();
        float dpages = (float) (mapsize / rperPage);
		String pageStr = dpages + "";
		int pages = (int) dpages;
		
		/*If the ratio is not an integer we add one page*/
		if (pageStr.indexOf(".") > 0) {
			int num = Integer.parseInt(pageStr.substring(pageStr.indexOf(".") + 1));
			if (num > 0) {
		pages++;
			}
		}
		
		/*If the user request a page number too high, we provide the last page as a result*/
		if (rPage > pages)
			rPage = pages;
		
		/*We set the first and the last mission to be displayed*/
		int rstart = (rPage - 1) * rperPage;
		int rend = rstart + rperPage;
		if (rstart > mapsize) {
			rstart = (int) mapsize - 1;
		}
		rstart = rstart + 1;
		if (rend > mapsize) {
			rend = (int) mapsize;
		}
		
		/*End Added by Gautier*/
%>
<table width="100%">
  <tr>
    <td width="2%"/>
    <td width="95%">
    <b> Collecting Missions Start</b>
    <%String pageUrl = "/paging.jsp?query=" + query + "&qp=dispmissionpage&rpp=" + rperPage + "&tp=" + pages + "&rp=" + rp + "&rsize=" + mapsize; %>
    <jsp:include flush="true" page="<%=pageUrl%>" /> <br />
    <table width="100%">
<tr>
<td class="collHead" width="16%">Mission code</td>
<td class="collHead" width="16%">Institute</td>
<td class="collHead" width="16%">Country</td>
<td class="collHead" width="16%">Start date</td>
<td class="collHead" width="16%">End date</td>
<td class="collHead" width="16%">Accessions Linked</td>
</tr>
      
		
	<%
		
		
       	Map<String,MissionForm> refineMap = SearchResults.getInstance().getMissionMapBySearch(query,rstart-1 ,rend );

        for (Iterator<String> it=refineMap.keySet().iterator(); it.hasNext(); ) 
        {
        String keyid = it.next();
        MissionForm missForm = map.get(keyid);
        if(!missForm.getMisscode().equals("0"))
        {
    %>
    <tr>
<td class="collValue"><a
    href="/index.jsp?page=showmission&missid=<%=keyid%>&<%=AccessionConstants.INSTITUTECODE%>=<%=missForm.getInstcode()%>"><%=missForm.getMisscode()%></a></td>
    <td class="collValue"><%=AccessionServlet.getKeywords().getName(missForm.getInstcode())%></td>
<td class="collValue"><%=missForm.getCtyname()%></td>
<td class="collValue"><%=missForm.getStartdate()%></td>
<td class="collValue"><%=missForm.getEnddate()%></td>
<td class="collValue">
	<%
	/*Added by Gautier to show how many Accessions are linked to a collecting mission*/
	String collQuery = AccessionConstants.INSTITUTECODE+AccessionConstants.SPLIT_KEY+missForm.getInstcode()+" AND misscode"+AccessionConstants.SPLIT_KEY+keyid;
	String tokens [] = AccessionConstants.luceneStrToArray(collQuery);
	String colls[] = AccessionConstants.getCollectionArray(tokens);
	int accessionSize = SearchResults.getInstance().getAccessionCount(collQuery, colls);
	if(accessionSize>0)
	{
%>
<a href="/index.jsp?page=disppage&search=<%=collQuery %>">
View the <%=accessionSize %> linked accession(s)</a>
<%
	}
	else
	{
		%>No Accessions linked to this mission<%
	}
	/*End Added by Gautier*/
	%>
	</td>
</tr>
    <%
        }
    }
    %>
  </table>
    <br/>
    </td>
  </tr>
</table>

<%if(mapsize>0)
      {
      %>
      <br/>
        <jsp:include flush="true" page="<%=pageUrl+\"&opages=yes\"%>" />
      <% 
      }
%>
</div>
<%
}
%>
