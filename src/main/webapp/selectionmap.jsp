<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-restricted.dtd">
<%@page import="org.sgrp.singer.ResourceManager"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%
String sName =request.getServerName();
if(sName==null){sName="";}
%>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Singer Selection MAP</title>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<%=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY+sName)%>"
      type="text/javascript"></script>
    <script src="/gselect.js" type="text/javascript"></script>
  
    <script type="text/javascript">
    //<![CDATA[
    function load() {
      if (GBrowserIsCompatible()) {
    	var map = new GMap2(document.getElementById("map"));    
        //map.setMapType(G_HYBRID_MAP);
        map.addControl(new GMapTypeControl());
        //map.addControl(new GOverviewMapControl());
		//map.addControl(new GScaleControl());
		//map.addControl(new GLargeMapControl());
        map.addControl(new GSmallMapControl());
        map.setCenter(new GLatLng(0,0),1);
        //var tinyIcon = new GIcon(G_DEFAULT_ICON);
        //tinyIcon.image = "/img/mm_20_red.png";
        //tinyIcon.iconSize = new GSize(12, 20);
        //tinyIcon.shadowSize = new GSize(22, 20);
        //tinyIcon.iconAnchor = new GPoint(6, 20);
        //tinyIcon.infoWindowAnchor = new GPoint(5, 1);
        //markerOptions = { icon:tinyIcon };
        //loadMarkers(map,markerOptions);
        map.addControl(new GSelectControl());        
      }
    }
    
    //function loadMarkers(map, markerOptions)
    //{
     //   GDownloadUrl("<%=request.getParameter("xml")%>", function(data, responseCode) 
	//	{  
	//	var xml = GXml.parse(data);  
	//	var markers = xml.documentElement.getElementsByTagName("marker");
	//	for (var i = 0; i < markers.length; i++) 
	//	{
	//		var point = new GLatLng(markers[i].getAttribute("lat"), markers[i].getAttribute("lng"));
	//		map.addOverlay(createMarker(map, point, markers[i].getAttribute("lat"),markers[i].getAttribute("lng"),markers[i].getAttribute("txt"),markerOptions));
	//	}
	//	}
	//	);
    //}
    
    //function createMarker(map, point,lat,lng, txt, markerOptions) 
    //{
     // var marker = new GMarker(point,markerOptions);
      //GEvent.addListener(marker,"mouseover", function() {
       // var myHtml = "<b>" + txt + " accession(s) found</b><br/>";
        //myHtml = myHtml +"<a class=&quot;headerlinks&quot; href=\"/index.jsp?page=disppage&search=(<=query%>)%20AND%20latlongd<=AccessionConstants.SPLIT_KEY%>"+lat+"%23"+lng+"\">View</a><br/>";
       // map.openInfoWindowHtml(point, myHtml);
      //});
	  //return marker;
   // }
    
    function writeoutput(output)
    {
        var fieldHTML = document.getElementById("goutputfield").innerHTML;
        document.getElementById("goutputfield").innerHTML = fieldHTML+"<br/>"+output;
    }

    function showdata(nw,ne,se,sw)
    {
      var aHTML = 'Coordinates : <img src=\"/img/mm_20_red.png\" width=\"10\" height=\"15\" border=\"0\"/> <a class=\"headerlinks\" target=\"mapoutput\" href=\"/index.jsp?page=showkeycount&searchtype=rangemap&nw='+nw+'&ne='+ne+'&se='+se+'&sw='+sw+'\">'+nw+''+se+'</a>';
        var lastHTML = document.getElementById("lastselect").innerHTML;
        document.getElementById("lastselect").innerHTML = aHTML;
        var fieldHTML = document.getElementById("previousselect").innerHTML;
        document.getElementById("previousselect").innerHTML = lastHTML+"<br/>"+fieldHTML;
    }
    //]]>
    </script>
  </head>
  <body onload="load()" onunload="GUnload()">
    <div id="map" style="height: 350px"></div>
    <div id="goutputfield"></div>
    <br/>
    <br/>
    <b>You have selected the following area.<br/> Click the below link to retrieve all accessions for this area.</b>
    <hr/>
    <br/>
    <b>Last Selection</b>
    <div id="lastselect" style="border:1px solid #eeeeee">
    </div>
    <b>Previous Selection</b>
    <div id="previousselect" style="border:1px solid #eeeeee">
    </div>
  </body>
</html>