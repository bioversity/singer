<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.sgrp.singer.SearchResults"%>
<%@ page import="org.sgrp.singer.AccessionServlet"%>
<%@ page import="org.sgrp.singer.form.AccessionForm"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.MySearchHistory"%>
<%@page import="java.util.Enumeration"%>
<%
MySearchHistory shopCart = (MySearchHistory)request.getSession(true).getAttribute("mysearch");
LinkedHashMap<String,String> map = shopCart.getHistoryMap();
%>

<div>
<br/>
<%
String prevlink =request.getRequestURI();
Enumeration enm = request.getParameterNames();
String queryStr = "";
while(enm.hasMoreElements())
{
	String param = (String)enm.nextElement();
	if(!(param.equals("squery") || param.equals("saction") || param.equals("squeryName")))
	{
		queryStr = queryStr+param+"="+request.getParameter(param)+"&";
	}
}
if(queryStr!=null && queryStr.trim().length()>0)
{
	prevlink= prevlink+"?"+queryStr.trim().substring(0,queryStr.trim().length()-1);
}
if(prevlink==null || prevlink.trim().length()==0)
{
 prevlink="?page=main";
}
if(prevlink.indexOf("?")<0)
{
 prevlink=prevlink+"?";
}
%>
<table width="100%">
<tr>
<td class="cartHead" colspan="2"  align="center">
<b>Saved Search(s)</b>
</td>
</tr>
<tr>
	<td class="cartHead" align="center"><img src="/img/save.gif"/></td>
	<td class="cartHead" align="center">Search</td>
</tr>
<%
if(map!=null && map.size()>0)
{
  int posstart = 0;
  String cv = posstart+"";
  
/*Added by Gautier:
This loop reverse the Search History to show it from the most recent to the most ancient*/
String[] keys = new String[map.size()];
  int i=map.size()-1;
for (Iterator<String> it=map.keySet().iterator(); it.hasNext(); ) 
{
	String squery = it.next();
	keys[i]=squery;
	i--;
}

/*End Added by Gautier (the loop configuration below were adapted from the loop above of course)*/
for(i=0;i<keys.length;i++)
{
	cv = (posstart % 2==0)?"0":"1";
String squery = keys[i];
String squeryName = map.get(squery);
%>
<tr>
	<!-- td width="10%"><=(i+1)%></td-->
	<td align="center" class="cartValue<%=cv%>"><a href="<%=prevlink%>&saction=remove&squery=<%=squery%>"><img src="/img/icon_tick.gif"/></a></td>
	<td class="cartValue<%=cv%>"><a class="headerlinks" href="/index.jsp?page=showkeycount&search=<%=squery%>"><%=squeryName%></a></td>
</tr>
<%
}
%>
<tr>
<td colspan="3" align="center" class="cartValue1">
<a class="headerlinks" href="<%=prevlink%>&saction=removeall">Remove All</a>
</td>
</tr>
<%
}
else
{
%>

<tr>
<td colspan="3" align="center" class="cartValue1">
<b>No Search History</b>
</td>
</tr>
<%
}
%>
<tr><td colspan="3" align="left" class="cartValue">
<font color="red"><b>* - Work in Progress</b></font>
</td></tr>
</table>
</div>
