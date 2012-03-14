<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.sgrp.singer.MySearchHistory"%>
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
<%@page import=" org.apache.lucene.document.Document" %>
<%@ include file="/functions.jsp" %>
<div>
<%
	boolean showRefine = false;
	HashMap<String, String> refineAttr = null;
	if (showRefine) {
		refineAttr = new HashMap<String, String>();
		refineAttr.put(AccessionConstants.COLLECTIONCODE, AccessionConstants.COLLECTIONNAME);
		refineAttr.put(AccessionConstants.GENUSCODE, AccessionConstants.GENUSNAME);
		refineAttr.put(AccessionConstants.SPECIESCODE, AccessionConstants.SPECIESNAME);
		refineAttr.put(AccessionConstants.COUNTRYCODE, AccessionConstants.COUNTRYNAME);
	}
	String attribFullList = "accenumb" + "|" + AccessionConstants.COLLECTIONCODE + "|" + AccessionConstants.COLLECTIONNAME + "|" + AccessionConstants.TAXONCODE + "|"+ AccessionConstants.TAXONNAME + "|" + AccessionConstants.COUNTRYCODE + "|"+ AccessionConstants.COUNTRYNAME;
	String attribList = "origaccenumb|accenumb" + "|" + AccessionConstants.COLLECTIONCODE + "|" + AccessionConstants.COLLECTIONNAME + "|" + AccessionConstants.TAXONNAME +  "|" + AccessionConstants.COUNTRYNAME;
	MyShopCart shopCart = (MyShopCart) request.getSession(true).getAttribute("mycart");
	String stype = request.getParameter("searchtype");
    String query = null;
    if(stype!=null && stype.trim().equals("rangemap"))
    {
    	String nw = request.getParameter("nw");
    	String ne = request.getParameter("ne");
    	String se = request.getParameter("se");
    	String sw = request.getParameter("sw");
        query = AccessionConstants.fromPointsToSearch(nw,ne,se,sw);
    }
    else
    {
      query = request.getParameter("search");
    }
    
  
	if (query != null && query.trim().length() > 0) 
	{
		//System.out.println("Query is :"+query);

		String rpp = request.getParameter("rpp");
		String rp = request.getParameter("rp");
		int rperPage = 100;
		int rPage = 1;
		try {
			rperPage = Integer.parseInt(rpp);
		} catch (Exception e) {
			rperPage = 100;
		}
		try {
			rPage = Integer.parseInt(rp);
		} catch (Exception e) {
			rPage = 1;
		}
		String tokens [] = AccessionConstants.luceneStrToArray(query);
		String colls[] = AccessionConstants.getCollectionArray(tokens);
		float mapsize = SearchResults.getInstance().getAccessionCount(query, colls);
		float dpages = (float) (mapsize / rperPage);
		String pageStr = dpages + "";
		int pages = (int) dpages;
		if (pageStr.indexOf(".") > 0) {
			int num = Integer.parseInt(pageStr.substring(pageStr.indexOf(".") + 1));
			if (num > 0) {
		pages++;
			}
		}
		if (rPage > pages)
			rPage = pages;

		int rstart = (rPage - 1) * rperPage;
		int rend = rstart + rperPage;
		if (rstart > mapsize) {
			rstart = (int) mapsize - 1;
		}

		rstart = rstart + 1;
		if (rend > mapsize) {
			rend = (int) mapsize;
		}
		String pageUrl = "/paging.jsp?query=" + query + "&qp=disppagedemo&rpp=" + rperPage + "&tp=" + pages + "&rp=" + rp + "&rsize=" + mapsize;
    String queryName = Keywords.getInstance().getQueryAsText(tokens, query);
		//out.println("Page Url ="+pageUrl);
%>
<table width="100%">
	<tr>
		<td width="100%">
		<b>Your Search </b>: <%=queryName%>
		<jsp:include flush="true" page="<%=pageUrl%>" /> <br />
		<br />
		<table width="100%">
			<tr>
                
				<td class="dispHead" colspan="5" width="100%" align="right">Download all results in 
				<!--  a class="headerlinks" target="output" href="/output.jsp?type=html&search=<=query%>">html</a-->  
				<a target="output" href="/output.jsp?type=xml&search=<%=AccessionConstants.replaceString(query,"#","%23",0)%>">xml</a>
				<a target="output" href="/output.jsp?type=csv&search=<%=AccessionConstants.replaceString(query,"#","%23",0)%>">csv</a> format(s)
				&nbsp;
				<!-- Disabled by Gautier to save automatically every queries
                <a title="Save query for future"
          href="/index.jsp?page=disppage&search=<%=query%>&saction=add&squery=<%=query%>&squeryName=<%=queryName%>"><img
          src="/img/save.gif" /></a>
 				-->
 				<%/*Added by Gautier to save automatically every queries*/
 				MySearchHistory searchHistory = (MySearchHistory) request.getSession(true).getAttribute("mysearch");
 				searchHistory.addToHistory(query, queryName); 
 				 if( isLoggedIn(request.getSession(true)))
 				{
 					searchHistory.saveToFile(getUserName(request.getSession(true)));
 				}
 				 /*End added by Gautier*/
 				%>
          </td>
			</tr>
			<tr>
				<!-- td class="dispHead" width="1%" align="center">&nbsp;</td-->
				<td class="dispHead" width="10%" align="center">Accession</td>
				<td class="dispHead" width="30%" align="center">Collection name</td>
				<td class="dispHead" width="35%" align="center">Taxon</td>
				<td class="dispHead" width="25%" align="center">Country Source</td>
				<td class="dispHead" width="1%" align="center"><img src="/img/add_icon.gif" /></td>
			</tr>
			<%
			if(mapsize>0)
			{
					HashMap<String, HashMap> refineMap = new HashMap<String, HashMap>();
					Map<String, Document> map = SearchResults.getInstance().getAccessionStringBySearch(query,/*AccessionConstants.COUNTRYNAME,*/ colls, rstart - 1, rend);
					int posstart = rstart;
					//System.out.println("From ="+(rstart-1)+" to="+(rend-1)+" mapsize="+map.size());
					//ArrayList<String> aList = new ArrayList<String>(map.keySet());
                    String cv = "0";
					for (Iterator<String> it = map.keySet().iterator(); it.hasNext();)
					//for (int it = rstart-1; it<rend;it++)
					{
						
                        cv = (posstart % 2==0)?"0":"1";
                        
                      
						String keyid = it.next();
						//String keyid = aList.get(it);
						Document doc = map.get(keyid);
						Map<String, String> values = AccessionConstants.getValueMap(doc, attribList);
                        String okeyid = values.get("origaccenumb");
						/*if (mapsize > 5 && showRefine) {
							values = AccessionConstants.getRegExValueMap(text, attribFullList);
					for (Iterator<String> rit = refineAttr.keySet().iterator(); rit.hasNext();) {
						String cattr = rit.next();
						HashMap<String, StringComparableValues> valuesMap = null;
						if (refineMap.containsKey(cattr)) {
							valuesMap = refineMap.get(cattr);
						} else {
							valuesMap = new HashMap<String, StringComparableValues>();
						}
						String cattrValue = values.get(cattr);
						if (!valuesMap.containsKey(cattrValue)) {
							String cattrname = refineAttr.get(cattr);
							String cattrnameValue = values.get(cattrname);
							valuesMap.put(cattrValue, new StringComparableValues(cattrValue, cattrnameValue));
							refineMap.remove(cattr);
							refineMap.put(cattr, valuesMap);
						}
					}
						}
						else
						{
							values = AccessionConstants.getRegExValueMap(text, attribList);
						}*/
						//AccessionForm accForm = map.get(keyid);
			%>
			<tr>
				<!-- td class="dispValue<%=cv%>" width="1%"><=posstart%></td-->
				<td class="dispValue<%=cv%>" width="10%" nowrap="nowrap"><a href="/indexdemotreaty.jsp?page=showaccessiondemo&<%=AccessionConstants.ACCESSIONCODE%>=<%=keyid%>&<%=AccessionConstants.COLLECTIONCODE%>=<%=values.get(AccessionConstants.COLLECTIONCODE)%>"><%=values.get("accenumb")%></a></td>
				<td class="dispValue<%=cv%>" width="30%" nowrap="nowrap"><%=values.get(AccessionConstants.COLLECTIONNAME)%></td>
				<td class="dispValue<%=cv%>" width="35%" nowrap="nowrap"><%=AccessionConstants.makeProper(values.get(AccessionConstants.TAXONNAME))%></td>
				<td class="dispValue<%=cv%>" width="25%" nowrap="nowrap"><%=values.get(AccessionConstants.COUNTRYNAME)%></td>
				<td class="dispValue<%=cv%>" width="1%" align="center">
				<%
        				boolean inCart = false;
        			      if(shopCart!=null)
        			      {
        			    	  inCart =shopCart.inCart(okeyid);
        			      }
						if (inCart) {
				%> <a
					href="/indexdemotreaty.jsp?page=disppagedemo&search=<%=query%>&caction=remove&caccId=<%=okeyid%>"><img
					src="/img/icon_tick.gif" /></a> <%
 } else if(shopCart.canAdd()){
 %> <a
					href="/indexdemotreaty.jsp?page=disppagedemo&search=<%=query%>&caction=add&caccId=<%=okeyid%>&ccollId=<%=values.get(AccessionConstants.COLLECTIONCODE)%>"><img
					src="/img/add_icon.gif" /></a> <%
 } else {
 %> <img src="/img/full_icon.gif" title="Cart already full"/><%
 }
 %>
				</td>
			</tr>
			<%
			posstart++;
					}
					if (map.size() > 5 && showRefine) {
						HashMap<String, HashMap> refineRes = new HashMap<String, HashMap>();
						for (Iterator<String> rit = refineAttr.keySet().iterator(); rit.hasNext();) {
					String cattr = rit.next();
					HashMap valuesMap = refineMap.get(cattr);
					if (valuesMap.size() > 1) {
						refineRes.put(cattr, valuesMap);
					}
						}
						//System.out.println("Adding refine map & query");
						if (refineRes.size() > 0) {
					request.getSession(true).setAttribute("refinemap", refineMap);
					request.getSession(true).setAttribute("refinequery", query);
						}
					}
			}
			%>
		</table>
    <%
    if(mapsize>0)
      {
      %>
      <br/>
        <jsp:include flush="true" page="<%=pageUrl+\"&opages=yes\"%>" />
      <% 
      }
    	 %> 
      
		</td>
	</tr>
</table>
<%
} else {
%> <br />
<br />
<center><font color="red"><b>Not a valid query
String</b></font></center>
<%
}
%>
</div>
