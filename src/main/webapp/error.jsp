<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html xhtml="true" locale="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="Developer" content="Kiran Viparthi"/>
<title>Singer</title>
<html:base />
<link rel="stylesheet" type="text/css" href="/css/singer.css" />
<link rel="stylesheet" type="text/css" href="/css/singeralpha.css" />
<link type="image/x-icon" href="/img/singericon.ico" rel="Shortcut Icon"/>
<!-- script src="/tablescroll.js" type="text/javascript"/-->
<script>
// Script Source: CodeLifter.com
// Copyright 2003
// Do not remove this notice.
// SETUPS:
// ===============================
// Set the horizontal and vertical position for the popup
PositionX = 100;
PositionY = 100;
// Set these value approximately 20 pixels greater than the
// size of the largest image to be used (needed for Netscape)
defaultWidth  = 500;
defaultHeight = 500;
// Set autoclose true to have the window close automatically
// Set autoclose false to allow multiple popup windows
var AutoClose = true;
// Do not edit below this line...
// ================================
if (parseInt(navigator.appVersion.charAt(0))>=4){
var isNN=(navigator.appName=="Netscape")?1:0;
var isIE=(navigator.appName.indexOf("Microsoft")!=-1)?1:0;}
var optNN='scrollbars=no,width='+defaultWidth+',height='+defaultHeight+',left='+PositionX+',top='+PositionY;
var optIE='scrollbars=no,width=150,height=100,left='+PositionX+',top='+PositionY;
function popImage(imageURL,imageTitle, imageDesc){
if (isNN){imgWin=window.open('about:blank','',optNN);}
if (isIE){imgWin=window.open('about:blank','',optIE);}
with (imgWin.document){
writeln('<html><head><title>Loading...</title><style>body{margin:0px;font-family: Verdana, Helvetica, Tahoma, Arial;font-size: 11px;}</style>');writeln('<sc'+'ript>');
writeln('var isNN,isIE;');writeln('if (parseInt(navigator.appVersion.charAt(0))>=4){');
writeln('isNN=(navigator.appName=="Netscape")?1:0;');writeln('isIE=(navigator.appName.indexOf("Microsoft")!=-1)?1:0;}');
writeln('function reSizeToImage(){');writeln('if (isIE){');writeln('window.resizeTo(300,350);');
writeln('width=300-(document.body.clientWidth-document.images[0].width);');
writeln('height=350-(document.body.clientHeight-document.images[0].height);');
writeln('window.resizeTo(width,height);}');writeln('if (isNN){');       
writeln('window.innerWidth=document.images["fullimage"].width;');writeln('window.innerHeight=document.images["fullimage"].height;}}');
writeln('function doTitle(){document.title="'+imageTitle+'";}');writeln('</sc'+'ript>');
if (!AutoClose) writeln('</head><body bgcolor=000000 scroll="no" onload="reSizeToImage();doTitle();self.focus()">')
else writeln('</head><body bgcolor=000000 scroll="no" onload="reSizeToImage();doTitle();self.focus()" onblur="self.close()">');
writeln('<div style="background-color:#FFFFFF"><span style="keyTitle">'+imageTitle+'</span><br/><span style="keyTitle"><i>'+unescape(imageDesc)+'</i></span></div><img name="fullimage" src='+imageURL+' style="display:block"></body></html>');
close();		
}}

function openWin(url)
{
window.open(url,'','scrollbars=yes,menubar=no,height=600,width=800,resizable=yes,toolbar=no,location=no,status=no');
}
</script>
</head>
<body>
<center>
<table width="90%" class="toptable" border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody>
<!-- tr>
  <td colspan="3"><img src="/img/spacer.gif" alt="" class="img_deco" border="0" height="5" width="1"/></td>
</tr-->
<!-- Header Section -->
<tr>
  <td colspan="3" class="bborder"><jsp:include flush="true" page="/header.jsp" /></td>
</tr>
<!-- End Header Section -->
<!-- Side/Main bar Section -->
<tr>
  <td class="rborder" width="20%" valign="top">
  <jsp:include flush="true" page="/sidebar.jsp" />
  </td>
  <td class="rborder" width="80%" valign="top" align="center">
  <table width="90%">
  <tr>
  <td align="left">
   <div>
   <br/>
   <br/>
   <h2 class="keyTitle">Page Not Found</h2>

  <div class="content">
   <p><strong>Sorry, but we couldn't find the page you're looking for. <a href="/index.jsp">SINGER</a> has recently been reorganized, so this page may have been moved or removed. </strong> </p>
   
   <p>There are several ways you can find the information you need: </p><p><strong>Search:</strong> <br />Enter search term(s) in the above search box to search the entire site for accessions. <br/>
   You could also use the links available in the sidebar to start your navigation. </p>  
   <p><strong><a href="mailto:SINGER@CGIAR.ORG">Contact Us</a>:</strong> <br />Email us with the page you're trying to find, and we can help locate it for you.</p>
   </div>
   
   </div>
   </td>
   </tr>
   </table>
   
   
   </td>
</tr>
<!-- End Side/Main bar Section -->
<!-- Footer Section -->
<tr>
  <td colspan="3" class="tbbordergrey" valign="bottom"><jsp:include flush="true" page="/footer.jsp" /></td>
</tr>
<!-- End Footer Section -->
</tbody>
</table>
</center>
</body>
</html:html>
