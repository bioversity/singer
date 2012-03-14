<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.ResourceManager"%>
<%@page import="org.sgrp.singer.ObjectStore"%>
<%
String sName =request.getServerName();
if(sName==null){sName="";}
%>

<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:v="urn:schemas-microsoft-com:vml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="Author" content="Kiran Viparthi"/>
<link rel="stylesheet" type="text/css" href="/css/singer.css" />
<link rel="stylesheet" type="text/css" href="/css/singeralpha.css" />
<link type="image/x-icon" href="/img/singericon.ico" rel="Shortcut Icon"/>
    
    <title>Singer Map Plot tool</title>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<%=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY+sName)%>"
      type="text/javascript"></script>
    <script type="text/javascript">
    //<![CDATA[
    function load() {
      if (GBrowserIsCompatible()) {
    	var map = new GMap2(document.getElementById("map"));    
        //map.setMapType(G_HYBRID_MAP);
        map.addControl(new GMapTypeControl());
        map.addControl(new GOverviewMapControl());
		map.addControl(new GScaleControl());
		map.addControl(new GLargeMapControl());
        map.setCenter(new GLatLng(0,0),1);
        map.enableContinuousZoom();
        map.enableScrollWheelZoom();
        map.disableDoubleClickZoom();
        var tinyIcon = new GIcon();
        tinyIcon.image = "/img/arrow_down.gif";
        tinyIcon.iconSize = new GSize(10, 10);
        tinyIcon.iconAnchor = new GPoint(2, 2);
        markerOptions = { icon:tinyIcon };
        loadMarkers(map,markerOptions);
      }
    }
    
    function loadMarkers(map, markerOptions)
    {
    <%
    String xml =request.getParameter("xml"); 
    if(xml!=null && xml.trim().length()>0)
    {
    %>
      GDownloadUrl("<%=xml%>", function(data, responseCode) 
	   {
        if(data!=null && data.length>0)
        {
		var xml = GXml.parse(data);
        var markerarr = [];
    	var markers = xml.documentElement.getElementsByTagName("marker");
		for (var i = 0; i < markers.length; i++) 
		{
          var marker = markers[i];
          markerarr.push(createMarker(map, marker.getAttribute("lat"), marker.getAttribute("lng"), marker.getAttribute("id"), marker.getAttribute("genus"), markerOptions));
		}
        var mm = new GMarkerManager(map);
        mm.addMarkers(markerarr,0,17);
        mm.refresh();
        }
    	}
		);
    <%
    }
    %>
    }
    
    function createMarker(map,lat,lng, id, name, markerOptions) 
    {
      var point = new GLatLng(lat, lng);
      var marker = new GMarker(point,markerOptions);
      GEvent.addListener(marker,"mouseover", function() {
        var myHtml = "<b>"+id+ " : " + name+ "</b><br/>";
        map.openInfoWindowHtml(point, myHtml);
      });
	  return marker;
    }
    //]]>
    </script>
  </head>
  <body onload="load()" onunload="GUnload()">
  <h4>SINGER plotting tool of data on Google Maps</h4>
    <div id="map" style="width: 800px; height: 500px"></div>
  </body>
</html>