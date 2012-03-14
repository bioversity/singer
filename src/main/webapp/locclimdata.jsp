<%@page import="org.sgrp.singer.ResourceManager"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="java.util.Map"%>
<%@page import="org.sgrp.singer.SearchResults"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.sgrp.singer.utils.LocClim"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DecimalFormat"%>
<html>
<head>
<script type="text/javascript" src="/tabs/tabber.js"></script>
<link rel="stylesheet" href="/css/singer.css" TYPE="text/css" MEDIA="screen">
<link rel="stylesheet" href="/tabs/tab.css" TYPE="text/css" MEDIA="screen">
</head>
<body>
<%
    HashMap labelMap = new HashMap();
	labelMap.put("me", "average temperature (deg C)");
	labelMap.put("min","monthly average of daily minimum temperatures (deg C)");
	labelMap.put("max","monthly average of daily maximum temperatures (deg C)");
	labelMap.put("pr", "precipitation (mm - liter/sqm)");
	labelMap.put("pet", "PET (mm - liter/sqm)");
	labelMap.put("sufr", "sunshine fraction (% of possible)");
	labelMap.put("windsp", "wind speed (m/s)");
	labelMap.put("vap", "water vapor pressure (Hpa)");
	
	String lat = request.getParameter("lat");
	String lng = request.getParameter("lng");
    String acc = request.getParameter("acc");
    String col = request.getParameter("col");
	String sName = request.getServerName();
	if (sName == null) {
		sName = "";
	}
	Map<String, Map<String, String>> locMap = LocClim.getMapValues(lat,lng);
%>
<a href="/index.jsp?page=showaccession&<%=AccessionConstants.ACCESSIONCODE%>=<%=acc%>&<%=AccessionConstants.COLLECTIONCODE%>=<%=col%>">View Passport data</a>
<br/>
<a href="/index.jsp?page=geodata&lat=<%=lat%>&lng=<%=lng%>&acc=<%=acc%>&col=<%=col%>">View climate data</a>
<br/>
<h3>LocClim Climate Data @ <b>Latitude:<%=lat%> Longitude:<%=lng%> </b></h3>
<br/>
<b><a href="http://www.fao.org/sd/locclim/srv/en/locclim.home" target="_clim">Climate data from <i>LocClim (FAO)</i></a></b>
<hr/>
<table width="100%">
  <tr>
    <td class="geoHead" align="center">Name</td>
    <td class="geoHead" align="center">Jan</td>
    <td class="geoHead" align="center">Feb</td>
    <td class="geoHead" align="center">Mar</td>
    <td class="geoHead" align="center">Apr</td>
    <td class="geoHead" align="center">May</td>
    <td class="geoHead" align="center">Jun</td>
    <td class="geoHead" align="center">Jul</td>
    <td class="geoHead" align="center">Aug</td>
    <td class="geoHead" align="center">Sep</td>
    <td class="geoHead" align="center">Oct</td>
    <td class="geoHead" align="center">Nov</td>
    <td class="geoHead" align="center">Dec</td>
  </tr>
  <%
  if(locMap!=null)
  {
    for (Iterator<String> iterator = locMap.keySet().iterator(); iterator
        .hasNext();) {
      String type = iterator.next();
      Map<String, String> monMap = locMap.get(type);
      //out.print(monMap);
  %>
  <tr>
    <td class="geoValue1"><%=labelMap.get(type)%></td>
    <%
      for (int i = 1; i <= 12; i++) {
          String val = monMap.get(i + "");
          if(val==null || val.trim().length()==0)
          {
           val = "-"; 
          }
    %>
    <td class="geoValue<%=i%2==0?"0":"1"%>" align="right"><%=val%></td>
    <%
      }
    %>
  </tr>
  <%
    }
    }
  else
  {
  %>
  <td class="geoValue1">No data found</td>
  <%
  }
  %>
</table>

<br />
<br />
<b>* - WORLDCLIM DATA (~1950-2000)</b>

</body>
</html>