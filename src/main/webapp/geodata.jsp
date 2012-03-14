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
	ArrayList<String> tempVar = new ArrayList<String>();
	tempVar.add("bio1");
	tempVar.add("bio2");
	tempVar.add("bio5");
	tempVar.add("bio6");
	tempVar.add("bio7");
	tempVar.add("bio8");
	tempVar.add("bio9");
	tempVar.add("bio10");
	tempVar.add("bio11");
	ArrayList<String> orderVar = new ArrayList<String>();
    orderVar.add("alt");
    orderVar.add("bio1");
    orderVar.add("bio2");
    orderVar.add("bio3");
    orderVar.add("bio4");
    orderVar.add("bio5");
    orderVar.add("bio6");
    orderVar.add("bio7");
    orderVar.add("bio8");
    orderVar.add("bio9");
    orderVar.add("bio10");
    orderVar.add("bio11");
    orderVar.add("bio12");
    orderVar.add("bio13");
    orderVar.add("bio14");
    orderVar.add("bio15");
    orderVar.add("bio16");
    orderVar.add("bio17");
    orderVar.add("bio18");
    orderVar.add("bio19");
    HashMap<String,String> labelMap = new HashMap<String,String>();
	labelMap.put("me", "average temperature (deg C)");
	labelMap.put("min","monthly average of daily minimum temperatures (deg C)");
	labelMap.put("max","monthly average of daily maximum temperatures (deg C)");
	labelMap.put("pr", "precipitation (mm - liter/sqm)");
	labelMap.put("pet", "PET (mm - liter/sqm)");
	labelMap.put("sufr", "sunshine fraction (% of possible)");
	labelMap.put("windsp", "wind speed (m/s)");
	labelMap.put("vap", "water vapor pressure (Hpa)");

	labelMap.put("bio1", "Annual Mean Temperature");
	labelMap.put("bio2","Mean Diurnal Range (Mean of monthly (max temp - min temp))");
	labelMap.put("bio3", "Isothermality (P2/P7) (* 100)");
	labelMap.put("bio4","Temperature Seasonality (standard deviation *100)");
	labelMap.put("bio5", "Max Temperature of Warmest Month");
	labelMap.put("bio6", "Min Temperature of Coldest Month");
	labelMap.put("bio7", "Temperature Annual Range (P5-P6)");
	labelMap.put("bio8", "Mean Temperature of Wettest Quarter");
	labelMap.put("bio9", "Mean Temperature of Driest Quarter");
	labelMap.put("bio10", "Mean Temperature of Warmest Quarter");
	labelMap.put("bio11", "Mean Temperature of Coldest Quarter");
	labelMap.put("bio12", "Annual Precipitation");
	labelMap.put("bio13", "Precipitation of Wettest Month");
	labelMap.put("bio14", "Precipitation of Driest Month");
	labelMap.put("bio15","Precipitation Seasonality (Coefficient of Variation)");
	labelMap.put("bio16", "Precipitation of Wettest Quarter");
	labelMap.put("bio17", "Precipitation of Driest Quarter");
	labelMap.put("bio18", "Precipitation of Warmest Quarter");
	labelMap.put("bio19", "Precipitation of Coldest Quarter");

	labelMap.put("prec", "Precipitation");
	labelMap.put("tmax", "Max. Temperature");
	labelMap.put("tmin", "Min. Temperature");

	labelMap.put("alt", "Altitude");

	
	String lat = request.getParameter("lat");
	String lng = request.getParameter("lng");
    String acc = request.getParameter("acc");
    String col = request.getParameter("col");
	String sName = request.getServerName();
	if (sName == null) {
		sName = "";
	}
	Map<String,String> latlngData = SearchResults.getInstance().getCleanedLatLng(lat,lng);
    Map<String, Integer[]> ascData = SearchResults.getInstance().getAscDataLatLng(lat,lng);
    boolean calRadius=latlngData.get("radius").equals("Y");  
    
 
%>
<a href="/index.jsp?page=showaccession&<%=AccessionConstants.ACCESSIONCODE%>=<%=acc%>&<%=AccessionConstants.COLLECTIONCODE%>=<%=col%>">View Passport data</a>
<br />
<h3>Climate Data @ <b>Latitude:<%=lat%> Longitude:<%=lng%> </b></h3>
<table>
  <tr>
    <td valign="top" width="50%"><b><a href="http://maps.google.com" target="_clim">Google Maps</a></b>
    <hr/>
    <div id="map" style="width: 450px; height: 350px">
    <div>
    <div class="tabber">
    <div class="tabbertab" title="Overview Map"><img
      src="http://maps.google.com/staticmap?center=<%=lat%>,<%=lng%>&markers=<%=lat%>,<%=lng%>,red&span=20,20&size=400x290&key=<%=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY+sName)%>">
    </div>
    <div class="tabbertab" title="Close-up View"><img
      src="http://maps.google.com/staticmap?center=<%=lat%>,<%=lng%>&markers=<%=lat%>,<%=lng%>,red&span=2,2&zoom=8&size=400x290&key=<%=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY+sName)%>">
    </div>
    </div>
    </div>
    </div>
    </td>
    <td valign="top" width="50%"><b><a href="http://www.worldclim.org" target="_clim">BioClim data from <i>WORLDCLIM</i> @ 10 arc-mins</a></b>
    <hr/>
    <b>Latitude:<%=latlngData.get("lat")%> Longitude:<%=latlngData.get("lng")%></b> are used for data extraction.<br/>
    <%if(calRadius)
    {%>
    <i>Data viewed are estimated calculated values</i><br/>
    <%}%>
    <br/>
    <table>
      <tr>
        <td class="geoHead" nowrap="nowrap">Code</td>
        <td class="geoHead" nowrap="nowrap">Variable</td>
        <td class="geoHead" nowrap="nowrap">Value</td>
        <td class="geoHead">+/- Err Value</td>
      </tr>
      <%
      	for (int i = 0; i <orderVar.size(); i++) {
      		String currid = orderVar.get(i);
      		Integer[] ascvalue = ascData.get(currid);
            int ascVal = ascvalue[0];
      		String ascdispValue = ascvalue[0] + "";
      		if (ascvalue==null || ascVal == -9999) {
      			ascdispValue = "-";
      		} else {
      			if (tempVar.contains(currid)) {
      				ascdispValue = ((double) ascVal / 10) + "";
      			}
      		}
      %>
      <tr>
        <td class="geoValue<%=i%2==0?"0":"1"%>" align="left"><%=currid.toUpperCase()%></td>
        <td class="geoValue<%=i%2==0?"0":"1"%>" align="left"><%=labelMap.get(currid)%></td>
        <td class="geoValue<%=i%2==0?"0":"1"%>" align="right"><%=ascdispValue%></td>
         <%if(calRadius && ascVal!=-9999)
        {
        	int minVal = ascvalue[1];
            //if(minVal<0)minVal = -(minVal);
            int maxVal = ascvalue[2];
            //if(maxVal<0)maxVal = -(maxVal);
            int errVal = maxVal-minVal;
            //int errVal = 0;
            //if(ascVal!=-9999)
            //{
             // errVal = difVal - ascVal;
            //}
            //if(errVal<0)errVal=-(errVal);
            String errdispVal = errVal+"";
            if (tempVar.contains(currid)) {
            	errdispVal = ((double) errVal / 10) + "";
  			}
        %>
        <td class="geoValue<%=i%2==0?"0":"1"%>" align="right">+/- <%=errdispVal%></td>
        <%
        }
         else
         {
        %>
        <td class="geoValue<%=i%2==0?"0":"1"%>" align="right">-</td>
        <%} %>
      </tr>
      <%
      	}
      %>
    </table>
    </td>
  </tr>
</table>
<br />
<!-- 
<b><a href="http://www.worldclim.org" target="_clim">Climate data from WORLDCLIM @ 2.5 arc-mins</a></b>
<br />
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
  <
  	String types[] = new String[] { "prec", "tmax", "tmin" };
  	for (int i = 0; i < types.length; i++) {
  		String type = types[i];
  %>
  <tr>
    <td class="geoValue1"><=labelMap.get(type)%></td>
    <
    	for (int j = 1; j <= 12; j++) {
    			String varid = type + j;
    			Integer value = bilData.get(varid);
    			String dispValue = value + "";
    			if (value==null || value == -9999) {
    				dispValue = "-";
    			} else {
    				if (tempVar.contains(varid)) {
    					dispValue = ((double) value / 10) + "";
    				}
    			}
    %>
    <td class="geoValue<=i%2==0?"0":"1"%>" align="right"><=dispValue%></td>
    <
    	}
    %>
  </tr>
  <
  	}
  %>
</table>
<br/>
 -->
<br/>
<b><a href="/index.jsp?page=locclimdata&lat=<%=lat%>&lng=<%=lng%>&acc=<%=acc%>&col=<%=col%>">Click here to view climate data from <i>LocClim (FAO)</i></a></b>
<br/>
<i>This link might take a while to load because the data is being loaded directly from an FAO web service.</i>
<br />
<br />
<b>* - WORLDCLIM DATA (~1950-2000)</b>

</body>
</html>