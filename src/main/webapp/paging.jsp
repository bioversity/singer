<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
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
<%
	String query = request.getParameter("query");
    String queryPage = request.getParameter("qp");
	String rperPageStr = request.getParameter("rpp");
	String rPageStr = request.getParameter("rp");
	String tPagesStr = request.getParameter("tp");
	String rMapSize = request.getParameter("rsize");
    String oPages = request.getParameter("opages");
    String sort = request.getParameter("sort");
   
	int rperPage = 50;
	int rPage = 1;
	float mapsize = 0;
	int pages = 0;
    boolean onlyPages = false;
	try {
		rperPage = Integer.parseInt(rperPageStr);//Results per page
	} catch (Exception e) {
	}
	try {
		rPage = Integer.parseInt(rPageStr);//result Page (Page shown)
	} catch (Exception e) {
	}
	try {
		pages = Integer.parseInt(tPagesStr);//total pages
	} catch (Exception e) {
	}
	try {
		mapsize = Float.parseFloat(rMapSize);//results quantity
	} catch (Exception e) {
	}
	
	//Default query page is "disppage"
    if(queryPage==null || queryPage.trim().length()==0)
    {
    queryPage = "disppage";
	}
    if(oPages!=null && oPages.trim().equalsIgnoreCase("yes"))
    {
    	onlyPages = true;
    }

	int rstart = 1;
    if(rPage>pages)
    {
     rPage=pages; 
    }
	if (rPage > 1)
		rstart = rPage * rperPage;
	if(mapsize>0)
	{
%>
<table width="100%">
	<tr>
		<td align="left">
    <%if(onlyPages)
    {
    %>
    &nbsp;
    <%}
    else
    {%>
    <b>Found <%=(int) mapsize%>
    results viewing <%=rPage%> of <%=pages%> </b>
    <%}%>
    </td>
		<td align="right"> <b>Pages :
          <%
          //pValue = what page are we showing in the row of ten pages
          int pValue = rPage%10;
          if(pValue==0)
          {
           pValue = 10; 
          }
          int minPage = rPage-pValue;
          int maxPage = minPage+10;
          if(pages<maxPage)
          {
           maxPage = pages; 
          }
			int ppaging = rPage;
			if (pages < 10 || rPage <= 10) {
				ppaging = 1;
			}
			
			//if we are not showing the first ten pages
			//We allow the possibility to go to the 10 previous pages
			if (minPage>=1) 
			{
			%>
			<a class="menubarlinks"
			href="/index.jsp?page=<%=queryPage%>&search=<%=query%>&sort=<%=sort%>&rp=<%=minPage%>">&lt; Previous 10</a>  <%
		 	}
			
		 	int i = (minPage+1);
		 	for (int j = (minPage+1); i <= pages && j <= maxPage; i++, j++) {
		 		if (i == rPage) {
				 %> <%=i%> <%
				 } else {
				 %> <a
				href="/index.jsp?page=<%=queryPage%>&search=<%=query%>&sort=<%=sort%>&rp=<%=i%>"><%=i%></a>
				<%
				}
			}
		 	//if there is pages after the 10 we are showing
			if (pages >= i) {
		%> <a class="menubarlinks"
			href="/index.jsp?page=<%=queryPage%>&search=<%=query%>&sort=<%=sort%>&rp=<%=i%>">Next 10 &gt;</a> <%
 }
 %>
		</td>
	</tr>
</table>
<%	}
	else
	{%>
	<table width="100%">
	<tr>
		<td align="left"><b>Found <b style="color:red">0</b> results</b></td>
	</tr>
	</table>
	<%}%>
