<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!-- html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Google Maps JavaScript API Example</title>
    <link rel="stylesheet" type="text/css" href="/css/pest.css" />
	<link rel="stylesheet" type="text/css" href="/css/pestalpha.css" />
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY)%>"
      type="text/javascript"></script>
    <script type="text/javascript">
    //<![CDATA[
    var map;
    function load() {
      if (GBrowserIsCompatible()) {
        map = new GMap2(document.getElementById("map"));
        //map.setMapType(G_SATELLITE_MAP);
        map.addControl(new GMapTypeControl());
        var marker = new GMarker(new GLatLng(<=request.getParameter("lat")%>,<=request.getParameter("lng")%>));
        map.setCenter(new GLatLng(<=request.getParameter("lat")%>, <=request.getParameter("lng")%>), 9);
        map.addControl(new GLargeMapControl());
        map.addOverlay(marker);
        var html = "<=request.getParameter("text")%>";
        marker.openInfoWindowHtml(html);
        GEvent.addListener(marker, "click", function() {
	marker.openInfoWindowHtml(html);
	});
        }
    }
    //]]>
    </script>
  </head>
  <body onload="load()" onunload="GUnload()">
    <div id="map" style="width:400px; height: 290px"></div>
  </body>
</html-->
<!-- Using this static image for quick rendering -->
<%@page import="org.sgrp.singer.ResourceManager"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<script type="text/javascript" src="/tabs/tabber.js"></script>
<link rel="stylesheet" href="/tabs/tab.css" TYPE="text/css" MEDIA="screen">
<!-- html xmlns="http://www.w3.org/1999/xhtml">
<body-->
<%
String lat = request.getParameter("lat");
String lng = request.getParameter("lng");
String sName =request.getServerName();
if(sName==null){sName="";}
%>
<div class="tabber">
     <div class="tabbertab" title="Overview Map">
      <img src="http://maps.google.com/staticmap?center=<%=lat%>,<%=lng%>&markers=<%=lat%>,<%=lng%>,red&span=20,20&size=400x290&key=<%=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY+sName)%>">
     </div>
     <div class="tabbertab" title="Close-up View">
      <img src="http://maps.google.com/staticmap?center=<%=lat%>,<%=lng%>&markers=<%=lat%>,<%=lng%>,red&span=2,2&zoom=8&size=400x290&key=<%=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY+sName)%>">
     </div>
    </div>
<!-- /td>
</td>
</tr>
</table-->

<!-- /body>
</html-->