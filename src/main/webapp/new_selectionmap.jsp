<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="org.sgrp.singer.ResourceManager"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="Author" content="Kiran Viparthi"/>
<title>Singer Plot MAP</title>
<link rel="stylesheet" type="text/css" href="/css/singer.css" />
<%
String sName =request.getServerName();
if(sName==null){sName="";}
%>

<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<%=ResourceManager.getString(AccessionConstants.GOOGLE_MAP_KEY+sName)%>" type="text/javascript"></script>

<script type="text/javascript">
//<![CDATA[

var earthRadius = 6378137; // in metres

function crossProduct(a, b) {
	return [(a[1] * b[2]) - (a[2] * b[1]), 
			(a[2] * b[0]) - (a[0] * b[2]), 
			(a[0] * b[1]) - (a[1] * b[0])];
}
function dotProduct(a, b) {
	return (a[0] * b[0]) + (a[1] * b[1]) + (a[2] * b[2]);
}
function spherePointAngle(A, B, C) { // returns angle at B
    return Math.atan2(dotProduct(crossProduct(C, B), A), dotProduct(crossProduct(B, A), crossProduct(B, C)));
}
function cartesianCoordinates(latlng) {
    var x = Math.cos(latlng.latRadians()) * Math.sin(latlng.lngRadians());
    var y = Math.cos(latlng.latRadians()) * Math.cos(latlng.lngRadians());
    var z = Math.sin(latlng.latRadians());
	return [x, y, z];
}
function polylineArea(latlngs) {
	var id, sum = 0, pointCount = latlngs.length, cartesians = [];
	if (pointCount < 3) return 0;
	
	for (id in latlngs) {
	    cartesians[id] = cartesianCoordinates(latlngs[id]);
	}
	
	// pad out with the first two elements
	cartesians.push(cartesians[0]);
	cartesians.push(cartesians[1]);
		
	for(id = 0; id < pointCount; id++) {
		var A = cartesians[id];
		var B = cartesians[id + 1];
		var C = cartesians[id + 2];
		sum += spherePointAngle(A, B, C);
	}

	var alpha = Math.abs(sum - (pointCount - 2) * Math.PI);
    alpha -= 2 * Math.PI * Math.floor(alpha / (2 * Math.PI));
    alpha = Math.min(alpha, 4 * Math.PI - alpha);    	
    
    return Math.round(alpha * Math.pow(earthRadius, 2));
}

var icon = new GIcon();
icon.image = "/img/mm_20_green.png";
icon.iconSize = new GSize(12, 20);
icon.shadowSize = new GSize(22, 20);
icon.iconAnchor = new GPoint(6, 20);

var bicon = new GIcon();
bicon.image = "/img/mm_20_blue.png";
bicon.iconSize = new GSize(12, 20);
bicon.shadowSize = new GSize(22, 20);
bicon.iconAnchor = new GPoint(6, 20);


var gmarker=[];
var gpoint=[];
var gpoly=[];
var gshape;
var gshapeshadow;
var gcircleline;
var gcirclelineshadow;
var map;
var zoom = true;
var totalArea=0;
var totalLength= "";
var isSelect = false;
var typeSelect = "square";
function load() {
  if (GBrowserIsCompatible()) {
    map = new GMap2(document.getElementById("map"),{draggableCursor:"crosshair"});
    map.addControl(new GMapTypeControl());
    map.addControl(new GOverviewMapControl());
	map.addControl(new GScaleControl());
	map.addControl(new GLargeMapControl());
    map.setCenter(new GLatLng(0,0),1);
    map.enableContinuousZoom();
    map.enableScrollWheelZoom();
    map.disableDoubleClickZoom();
    GEvent.addListener(map, "click", mapclick);
 }
}

function mapclick(marker, point) {
if(isSelect)
{
  if (!marker) {
  if(typeSelect=='polygon' || gmarker.length<2)
  {
    addRoutePoint(point);
    }
  }
 }
}
function showmarkeroptions(marker, pos)
{
  var div = '<div><a href="javascript:removeRoutePoint('+pos+');">Remove me</a><br/>';
  if(typeSelect=='polygon' || gmarker.length<2)
  {
  div+='<a href="javascript:addNewRoutePoint('+pos+');">Add new marker</a></div>';
  }
  map.openInfoWindowHtml(marker.getPoint(), div);
}
function addRoutePoint(point) {
  var dist = 0;
  gpoint.push(point);
  var n = gmarker.length;
gmarker[n] = new GMarker(gpoint[n], {icon:icon,draggable: true});
var marker = gmarker[n];
addMarkerEvents(marker,n);
map.addOverlay(gmarker[n]);
draw();
}

function removeRoutePoint(pos)
{
if(gmarker[pos]!=null)
{
marker = gmarker[pos];
map.closeInfoWindow();
map.removeOverlay(marker);
if(gmarker.length==1)
{
gmarker = [];
gpoint = [];
draw();
}
else
{
gmarker.splice(pos, 1);
gpoint.splice(pos, 1);
draw(true);
}

}
}


function addNewRoutePoint(pos)
{
map.closeInfoWindow();
marker = gmarker[pos];
point = marker.getPoint();
nmarker = new GMarker(point, {icon:icon,draggable: true});
nmarker.setLatLng(new GLatLng(point.lat()+1,point.lng()+1));
map.addOverlay(nmarker);
gmarker.splice(pos, 0, nmarker);
gpoint.splice(pos, 0, nmarker.getPoint());
draw(true);
 
}
function draw(ue_)
{
if(typeSelect=='square')
{
drawSquare(ue_);
}else if(typeSelect=='circle')
{
drawCircle(ue_);
}
else
{
drawPoly(ue_);
}
}

function drawPolyShape(poly)
{
if(gshape){map.removeOverlay(gshape)};
if(gshapeshadow){map.removeOverlay(gshapeshadow)};
gshape=new GPolygon(poly,'#fa7231', 3);
gshapeshadow=new GPolygon(poly,'#fa7231', 3, 0,'#fa9999',0.2);
GEvent.addListener(gshape, "mouseover",function(){changeshapecolor(gshape, gshapeshadow, false);});
GEvent.addListener(gshapeshadow, "mouseout",function(){changeshapecolor(gshape, gshapeshadow, true);});
GEvent.addListener(gshapeshadow, "click", shapeclick);
map.addOverlay(gshape);
map.addOverlay(gshapeshadow);
gshapeshadow.hide();
}

function changeshapecolor(gshape,gshapeshadow, show) {
if(show)
{
gshape.show();
gshapeshadow.hide();
}
else
{
gshapeshadow.show();
gshape.hide();
}
}

function shapeclick(latlng) {
var div = '<div>Testing if it opens</div>';
  map.openInfoWindowHtml(latlng, div);
}

function calculatePolyShape(poly)
{
var id;
var pointCount = poly.length-1;
//alert('PointCount ='+pointCount);
totalLength='Perimeter/Length :0';
totalArea=0;
    if (pointCount >= 2) {
        var length = 0;
        for(id = 0; id < pointCount; id += 1) {
            length += (poly[id]).distanceFrom(poly[id + 1]);
        }
        if (pointCount > 2) {
        	totalLength =  'Perimeter :'+ (Math.round(length) / 1000).toFixed(1);
        } else {
        	totalLength =  'Length :'+(Math.round(length) / 2000).toFixed(1);
        }
    }
    poly.pop();
    if (pointCount >= 3) {
    	totalArea =polylineArea(poly) / 1000000;
	}
	else
	{
		totalArea =0;
	}
	updatePerLen();
	updateArea(); 
}

function drawCircle(ue_)
{
gpoly.length=0;
var circlepoints=[];
for(var i=0;i<gmarker.length;i++)
{
var marker = gmarker[i];
if(ue_)
{
addMarkerEvents(marker, i);
}
cpoint = gmarker[i].getPoint();
if(i==1)
{
ppoint = gmarker[i-1].getPoint();
var normalProj = G_NORMAL_MAP.getProjection();	
var zoom = map.getZoom();
	var centerPt = normalProj.fromLatLngToPixel(ppoint, zoom);
	var radiusPt = normalProj.fromLatLngToPixel(cpoint, zoom);
	with (Math) {
		var radius = floor(sqrt(pow((centerPt.x-radiusPt.x),2) + pow((centerPt.y-radiusPt.y),2)));

		for (var a = 0 ; a < 361 ; a+=10 ) {
			var aRad = a*(PI/180);
			y = centerPt.y + radius * sin(aRad)
			x = centerPt.x + radius * cos(aRad)
			var p = new GPoint(x,y);
			gpoly.push(normalProj.fromPixelToLatLng(p, zoom));
		}
	}
}
circlepoints[i]=cpoint;
}
//updateDebug(circlepoints);
if(gcircleline){map.removeOverlay(gcircleline)};
gcircleline=new GPolygon(circlepoints,'#000000', 3, 1,'#000000',0.2);
GEvent.addListener(gcircleline, "click", shapeclick);
map.addOverlay(gcircleline);
//updateDebug(gpoly);
drawPolyShape(gpoly);
calculatePolyShape(gpoly);

if(gmarker.length>0)
{
locktypeSelect(true);
}
else
{
updateMarkerCoord(null);
locktypeSelect(false);
}
}

function drawSquare(ue_)
{
gpoly.length=0;
for(var i=0;i<gmarker.length;i++)
{
var marker = gmarker[i];
if(ue_)
{
addMarkerEvents(marker, i);
}
cpoint = marker.getPoint();
if(i==1)
{
ppoint = gmarker[i-1].getPoint();

rpoint = new GLatLng(ppoint.lat(),cpoint.lng());
lpoint = new GLatLng(cpoint.lat(),ppoint.lng());
gpoly.push(rpoint);
gpoly.push(cpoint);
gpoly.push(lpoint);
}
else
{
gpoly.push(gmarker[i].getPoint());
}
}
if(gmarker.length>1)
{
gpoly.push(gmarker[0].getPoint());
}
//updateDebug(gpoly);
drawPolyShape(gpoly);
calculatePolyShape(gpoly);
if(gmarker.length>0)
{
locktypeSelect(true);
}
else
{
updateMarkerCoord(null);
locktypeSelect(false);
}
}

function drawPoly(ue_)
{
gpoly.length=0;
for(var i=0;i<gmarker.length;i++)
{
var marker = gmarker[i];
if(ue_)
{
addMarkerEvents(marker, i);
}
gpoly.push(gmarker[i].getPoint());
}
if(gmarker.length>1)
{
gpoly.push(gmarker[0].getPoint());
}
drawPolyShape(gpoly);
calculatePolyShape(gpoly);
if(gmarker.length>0)
{
locktypeSelect(true);
}
else
{
updateMarkerCoord(null);
locktypeSelect(false);
}
}


function addMarkerEvents(marker, pos)
{
marker.enableDragging();
GEvent.clearListeners(marker);
GEvent.addListener(marker,'drag',function()
{
updateMarkerCoord(marker);
});
GEvent.addListener(marker,'dragend',function()
{
color('green');
draw();
//fit();
});
GEvent.addListener(marker,'mouseover',function()
{
updateMarkerCoord(marker);
}
);
GEvent.addListener(marker,'click',function()
{
showmarkeroptions(marker, pos);
}
);
}


////////change color of the line

function color(rgb)
{
if(gshape){
gshape.color=rgb;
gshape.redraw(true);
}}



function clearAll() {
map.closeInfoWindow();
if(gmarker.length>0)
{
for(var i=0;i<gmarker.length;i++)
{
marker = gmarker[i];
map.removeOverlay(marker);
}
gmarker.length=0;
gpoint.length=0;
draw();
}
updateMarkerCoord(null);
locktypeSelect(false);
}


function showPoints(xml) {
  var html = '';
  if (xml) {
    html = '<?xml version=\"1.0\" encoding=\"utf-8\"?>\n';
    html += '<points type="'+typeSelect+'">\n';
    for (var i = 0 ; i < gmarker.length ; i++ ) {
      marker = gmarker[i];
        html += '    <p lat="' + marker.getPoint().y.toFixed(8) + '" lon="' + marker.getPoint().x.toFixed(8) + '"';
        html += ' />\n';
    }
    html += '</points>\n';
  }
  else {
  html+='(';
     for (var i = 0 ; i < gmarker.length ; i++ ) {
      marker = gmarker[i];
        html += marker.getPoint().y.toFixed(8)  + ' ' + marker.getPoint().x.toFixed(8);
        if(i<gmarker.length-1)
        {
        html+= ',';
        }
    }
    html+=')';
  }
  if (html == '') {
    html += 'No point are selected\n\n';
  }
	html += '\n';
  	html += '\n\n';
//  html += encodePolyline();

  var nWin = window.open('','nWin','width=780,height=500,left=50,top=50,resizable=1,scrollbars=yes,menubar=no,status=no');
  nWin.focus();
  nWin.document.open('text/xml\n\n');
  nWin.document.write(html);
  nWin.document.close();
}
function updateMarkerCoord(marker)
{
  if(marker!=null)
  {
  document.getElementById("coords").innerHTML = 'Selected Marker: Lat :' + marker.getPoint().y.toFixed(8)+ ' Lng :' + marker.getPoint().x.toFixed(8);
  }
  else
  {
  document.getElementById("coords").innerHTML = 'Selected Marker:';
  }
}

function updatePerLen()
{
document.getElementById("perimeter").innerHTML = totalLength + ' km';
}
function updateArea()
{
  document.getElementById("area").innerHTML = 'Total Area: '+ totalArea.toFixed(1) + ' km<sup>2</sup>';
}

function updateDebug(txt)
{
  document.getElementById("debug").innerHTML = txt;
}

function locktypeSelect(lvalue)
{
	document.getElementById("s"+typeSelect).checked=isSelect;
  if(gmarker.length>0)
  {
   lvalue = true;
  }
  document.getElementById("spolygon").disabled= lvalue;
  document.getElementById("ssquare").disabled= lvalue;  
  document.getElementById("scircle").disabled= lvalue;
  
}
function updatetypeSelect(svalue)
{
typeSelect = svalue;
}


function updateIsSelect(value)
{
isSelect = value;
//dragObj =map.getDragObject(); 
//if(isSelect)
//{
//dragObj.setDraggableCursor('crosshair');
//}
//else
//{
//dragObj.setDraggableCursor('default');
//}
document.getElementById("isselect").checked= isSelect;
locktypeSelect(!isSelect);
}

//]]>
</script>
</head>
<body onload="load()" onunload="GUnload()">
<br/>
<span class="keyTitle"><b>Kiran New Map Selection tool - *</b></span>
<br/>
<br/>
<table cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td valign="top">
    &nbsp;&nbsp;
    <span class="dtools">
    <span class="dtool" align="left"><input type="checkbox" id="isselect" onclick="updateIsSelect(this.checked)" checked="false" />Draw &nbsp;
    <input type="radio" name="typeselect" id="ssquare" onclick="updatetypeSelect('square')"/><img src="/img/square.png" border="0" />
    <input type="radio" name="typeselect" id="spolygon" onclick="updatetypeSelect('polygon')" checked="checked"/><img src="/img/polygon.png" border="0" />
    <input type="radio" name="typeselect" id="scircle" onclick="updatetypeSelect('circle')"/><img src="/img/circle.png" border="0" />
    </span></span>&nbsp;&nbsp;&nbsp;
    <span class="buttons">
    <span class="buttonB" onclick="clearAll()">Clear all</span>&nbsp;
    <span class="buttonB" onclick="showPoints()">View Points as TXT</span>
    <span class="buttonB" onclick="showPoints(1)">View Points as XML</span>&nbsp;
    </span>&nbsp;
    <br/>
    <br/>
    <div id="map" style="height: 300px"></div>
    <div>
    <span>
    <span class="wText" id="coords">Selected Marker:</span>
    <span class="wText" id="perimeter">Perimeter/Length: 0 km</span>
    <span class="wText" id="area">Total Area: 0 km<sup>2</sup></span>
    </span>
    </div>
    <br/>
    <div class="dText" id="debug">Debug Area</div>
    </td>
  </tr>
</table>
<script>
updateIsSelect(false);
</script>
</body>
</html>