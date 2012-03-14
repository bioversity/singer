<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.ResourceManager"%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Google Maps JavaScript API Example</title>
    <link rel="stylesheet" type="text/css" href="/css/pest.css" />
	<link rel="stylesheet" type="text/css" href="/css/pestalpha.css" />
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<%=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY)%>"
      type="text/javascript"></script>
    <script type="text/javascript">
    //<![CDATA[
    var map;
    var gdir;
    function load() {
      if (GBrowserIsCompatible()) {
        map = new GMap2(document.getElementById("map"));
        //map.setMapType(G_SATELLITE_MAP);
        map.addControl(new GMapTypeControl());
        var marker = new GMarker(new GLatLng(<%=request.getParameter("lat")%>,<%=request.getParameter("lng")%>));
        map.setCenter(new GLatLng(<%=request.getParameter("lat")%>, <%=request.getParameter("lng")%>), 9);
        map.addControl(new GLargeMapControl());
        map.addOverlay(marker);
        var html = "<%=request.getParameter("text")%>";
        marker.openInfoWindowHtml(html);
        GEvent.addListener(marker, "click", function() {
	marker.openInfoWindowHtml(html);
	});
        gdir = new GDirections(map, document.getElementById("directions"));
        var home = new GLatLng("<%=request.getParameter("lat")%>","<%=request.getParameter("lng")%>");
        //var ahome =  new GLatLng("41.85779934552825", "12.456436157");
        //var bhome =  new GLatLng("41.82608370627639", "12.568359375");
                                var arrLocation = new Array(2); 
                                arrLocation[0] = home; 
                                arrLocation[1] = home; 
        
        
        GEvent.addListener(gdir, "load", onGDirectionsLoad);
        GEvent.addListener(gdir, "error", handleErrors);
        //gdir.load("from: 42.10076980332238, -87.7712345123291 to: 38.299, -122.2836");
        //gdir.loadFromWaypoints([("<=request.getParameter("lat")%>,<=request.getParameter("lng")%>"),("<=request.getParameter("lat")%>,<=request.getParameter("lng")%>")],{"locale":"en_US"});
        var latlng = "from: "+<%=request.getParameter("lat")%>+", "+<%=request.getParameter("lng")%>+" to: "+<%=request.getParameter("lat")%>+", "+<%=request.getParameter("lng")%>;
        alert(latlng);
        gdir.load(latlng);
        //gdir.loadFromWaypoints(arrLocation,{"locale":"en_US"});
      }
    }
    function handleErrors(){
     if (gdir.getStatus().code == G_GEO_UNKNOWN_ADDRESS)
       alert("No corresponding geographic location could be found for one of the specified addresses. This may be due to the fact that the address is relatively new, or it may be incorrect.\nError code: " + gdir.getStatus().code);
     else if (gdir.getStatus().code == G_GEO_SERVER_ERROR)
       alert("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.\n Error code: " + gdir.getStatus().code);
     
     else if (gdir.getStatus().code == G_GEO_MISSING_QUERY)
       alert("The HTTP q parameter was either missing or had no value. For geocoder requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input.\n Error code: " + gdir.getStatus().code);

  //   else if (gdir.getStatus().code == G_UNAVAILABLE_ADDRESS)  <--- Doc bug... this is either not defined, or Doc is wrong
  //     alert("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.\n Error code: " + gdir.getStatus().code);
       
     else if (gdir.getStatus().code == G_GEO_BAD_KEY)
       alert("The given key is either invalid or does not match the domain for which it was given. \n Error code: " + gdir.getStatus().code);

     else if (gdir.getStatus().code == G_GEO_BAD_REQUEST)
       alert("A directions request could not be successfully parsed.\n Error code: " + gdir.getStatus().code);
      
     else alert("An unknown error occurred."+gdir.getStatus().code);
     
  }

  function onGDirectionsLoad(){ 
      // Use this function to access information about the latest load()
      // results.

      // e.g.
       document.getElementById("getStatus").innerHTML = gdir.getStatus().code;
       document.getElementById("getStatus").innerHTML = gdir.getNumRoutes();
       
       alert("Code is :"+gdir.getStauts().code);
    // and yada yada yada...
  }
    //]]>
    </script>
  </head>
  <body onload="load()" onunload="GUnload()">
    <div id="map" style="width:400px; height: 290px"></div>
    <br/>
    <div id="directions" style="width: 275px"></div>
    <div id="getStatus" style="width: 275px"></div>
  </body>
</html>
<!-- Using this static image for quick rendering -->
<!-- html>
<body>
<div id="map" style="width:400px; height: 290px">
<img src="http://maps.google.com/staticmap?center=<=request.getParameter("lat")%>,<=request.getParameter("lng")%>&markers=<=request.getParameter("lat")%>,<=request.getParameter("lng")%>,red&zoom=9&size=400x290&key=ABQIAAAA166IDnF-TusBuyEMxy4V8RSRFT-O3iKxI1FM2gdgFRiQbZmT0xSPO8xEw62JXnb9yVAZjOy_LMMhaQ">
</div>
</body>
</html-->