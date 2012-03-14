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
Map<String,String> yMap = new LinkedHashMap<String,String>();
yMap.put("2000-2008", "sdy:[2000 TO 2008]");
yMap.put("1990-2000", "sdy:[1990 TO 2000]");
yMap.put("1980-1990", "sdy:[1980 TO 1990]");
yMap.put("1970-1980", "sdy:[1970 TO 1980]");
yMap.put("1960-1970", "sdy:[1960 TO 1970]");
yMap.put("1950-1960", "sdy:[1950 TO 1960]");
yMap.put("1940-1950", "sdy:[1940 TO 1950]");
yMap.put("Upto 1940", "sdy:[0001 TO 1940]");
yMap.put("Not Specified", "sdy:0000");
%>

<div>
<br />
<table width="100% nowrap="nowrap">
<tr>
<td width="100%" nowrap="nowrap">
<div id="navcontainer">
<ul>
	<%
			String year = request.getParameter("year");
			if (year == null || year.trim().length() == 0)
		     year = "2000-2008";
			
			String month = request.getParameter("month");
			if (month == null || month.trim().length() == 0)
		    month = "12";

            for(Iterator<String> itr = yMap.keySet().iterator();itr.hasNext();)
            {
		String selected = "N";
		String cYear = itr.next();;
		if (year!= null) {
			if (year.equals(cYear))
		selected = "Y";
		}
    if(cYear.equalsIgnoreCase("Not Specified"))
    {
    	%>
      <li id=<%=selected.equals("Y")?"navselected":"navlist"%>><a
    href="/index.jsp?page=missiontimeline&year=<%=cYear%>&month=0"> <%=cYear%>
  </a></li>
      <% 
    }
    else
    {
	%>
	<li id=<%=selected.equals("Y")?"navselected":"navlist"%>><a
		href="/index.jsp?page=missiontimeline&year=<%=cYear%>"> <%=cYear%>
	</a></li>
	<%
    }
	}
	%>
</ul>
</div>
<div id="navcontainer">
<ul>
	<%
			String monthArray[] = new String[] {"Not Specified","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sep", "Oct", "Nov", "Dec"};

			for (int i = monthArray.length-1; i>=0; i--) {
		String selected = "N";
		String dispAlpha = monthArray[i];
		
		String alphabet = (i)+"";
		if (month != null) {
			if (alphabet.equals(month.toLowerCase()))
		      selected = "Y";
		}
	%>
	<li id=<%=selected.equals("Y")?"navselected":"navlist"%>><a
		href="/index.jsp?page=missiontimeline&year=<%=year%>&month=<%=alphabet%>"> <%=dispAlpha%>
	</a></li>
	<%
	}
	%>
</ul>
</div>
</td>
</tr>
</table>

<%
String query = yMap.get(year)+" AND sdm"+AccessionConstants.SPLIT_KEY+month;
String qUrl = "/dispmissionpage.jsp?search="+query;
%>
<jsp:include page="<%=qUrl%>" flush="true"/>

</div>
