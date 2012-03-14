<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.ResourceManager"%>
<%@page import="org.sgrp.singer.ObjectStore"%>
<%
String query = request.getParameter("query");
String xml = ObjectStore.getXMLString("marker",query);
String sName =request.getServerName();
if(sName==null){sName="";}
%>

<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:v="urn:schemas-microsoft-com:vml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Singer Plot MAP</title>
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
        <%
        if(request.getParameter("test")!=null &&request.getParameter("test").equals("test"))
        {
        %>
          tinyIcon.image = "/img/monkey.gif";
          tinyIcon.iconSize = new GSize(21, 18);
          tinyIcon.iconAnchor = new GPoint(2, 5);
        <%
        }
        else
        {
        %>
          tinyIcon.image = "/img/arrow_down.gif";
          tinyIcon.iconSize = new GSize(10, 10);
          tinyIcon.iconAnchor = new GPoint(2, 2);
        <%
        }
        %>
        markerOptions = { icon:tinyIcon };
        loadMarkers(map,markerOptions);
      }
    }
    
    function loadMarkers(map, markerOptions)
    {
        //GDownloadUrl("<=request.getParameter("xml")%>", function(data, responseCode) 
		//{  
		//var xml = GXml.parse(<=xml%>);
    var xml = <%=xml%>;
    var markers= xml.list;
    var markerarr = [];
    //var markerManager = new GMarkerManager(map);

    	//var markers = xml.documentElement.getElementsByTagName("marker");
		for (var i = 0; i < markers.length; i++) 
		{
          var marker = markers[i];
          markerarr.push(createMarker(map, marker.lat, marker.lng, marker.freq, markerOptions));
		}
    var mm = new GMarkerManager(map);
    mm.addMarkers(markerarr,0,17);
    mm.refresh();
		//}
		//);
    }
    
    function createMarker(map,lat,lng, txt, markerOptions) 
    {
      var point = new GLatLng(lat, lng);
      var marker = new GMarker(point,markerOptions);
      GEvent.addListener(marker,"mouseover", function() {
        var myHtml = "<b>" + txt + " accession(s) found</b><br/>";
        myHtml = myHtml +"<a class=&quot;headerlinks&quot; href=\"/index.jsp?page=disppage&search=(<%=query%>)%20AND%20latlongd<%=AccessionConstants.SPLIT_KEY%>"+lat+"%23"+lng+"\">View</a><br/>";
        map.openInfoWindowHtml(point, myHtml);
      });
	  return marker;
    }
    //]]>
    </script>
  </head>
  <body onload="load()" onunload="GUnload()">
    <div id="map" style="width: 800px; height: 500px"></div>
  </body>
</html>