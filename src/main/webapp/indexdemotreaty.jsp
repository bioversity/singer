<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.sgrp.singer.MyShopCart"%>
<%@page import="org.sgrp.singer.MySearchHistory"%>
<%@page import="org.sgrp.singer.ObjectStore"%>
<%@ include file="/functions.jsp" %>

<%
	request.getSession(true).removeAttribute("mapLink");
	request.getSession(true).removeAttribute("refinemap");
	request.getSession(true).removeAttribute("refinequery");
	if (request.getSession(true).getAttribute("mycart") == null) {
		//System.out.println("shopping cart found null");
		request.getSession(true).setAttribute("mycart", new MyShopCart());
	}
	MyShopCart shopCart = (MyShopCart) request.getSession(true).getAttribute("mycart");
	String accId = request.getParameter("caccId");
	String action = request.getParameter("caction");

	if (action != null && action.trim().length() > 0) {
		if (action.equals("add")) {
			shopCart.addToCart(accId, request.getParameter("ccollId"));
		}
		if (action.equals("remove")) {
			shopCart.removeFromCart(accId);
		}
		if (action.equals("removeall")) {
			shopCart.removeAll();
		}
	}


  if (request.getSession(true).getAttribute("mysearch") == null) {
    //System.out.println("shopping cart found null");
    request.getSession(true).setAttribute("mysearch", new MySearchHistory());
  }
  MySearchHistory searchHistory = (MySearchHistory) request.getSession(true).getAttribute("mysearch");
  String squery= request.getParameter("squery");
  String saction = request.getParameter("saction");

  String loginAction = (String)request.getAttribute("loginAction");
  if(loginAction!=null && loginAction.equals("login"))
  {
	  MySearchHistory previousSearchHistory = (MySearchHistory)ObjectStore.getObject(AccessionConstants.SEARCHHISTORY,getUserName(request.getSession(true)));
	  
	  if(previousSearchHistory!=null)
	  {
		  previousSearchHistory.load(searchHistory);
		  searchHistory = previousSearchHistory;
		  session.setAttribute("mysearch",searchHistory);
	  }
	  searchHistory.saveToFile(getUserName(request.getSession(true)));
  }
  if(loginAction!=null && loginAction.equals("logout"))
  {
	  searchHistory.removeAll();
  }
  
  if (saction != null && saction.trim().length() > 0) {
    if (saction.equals("add")) {
    	searchHistory.addToHistory(squery, request.getParameter("squeryName"));
    }
    if (saction.equals("remove")) {
    	searchHistory.removeFromHistory(squery);
    }
    if (saction.equals("removeall")) {
    	searchHistory.removeAll();
    }
    /*When actions have been done, if the user is logged in, we save the SearchHistory*/
    if( isLoggedIn(request.getSession(true)))
	{
		searchHistory.saveToFile(getUserName(request.getSession(true)));
	}
  }
%>

<%@page import="org.sgrp.singer.AccessionConstants"%><html:html xhtml="true" locale="true">
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
writeln('<html><head><meta name="Developer" content="Kiran Viparthi"/><title>Loading...</title><style>body{margin:0px;font-family: Verdana, Helvetica, Tahoma, Arial;font-size: 11px;}</style>');writeln('<sc'+'ript>');
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
  <td colspan="3" class="bborder"><jsp:include flush="true" page="/headerdemo.jsp" /></td>
</tr>
<!-- End Header Section -->
<!-- Side/Main bar Section -->
<tr>
  <td class="rborder" width="20%" valign="top">
    <jsp:include flush="true" page="/sidebardemo.jsp" /> 
  </td>
  <td colspan="2" width="80%" valign="top">
  <table width="100%">
  <tr>
  <td width="100%" align="left">
    <%
					String mainPage = (String) request.getParameter("page");
					if (mainPage == null || mainPage.trim().length() == 0) {
						mainPage = "/maindemo.jsp";
					} else if (mainPage.endsWith(".do")) {
						mainPage = "/" + mainPage;
					} else {
						mainPage = mainPage + ".jsp";
					}
					//mainPage="/maindemo.jsp";
				%> <jsp:include flush="true" page="<%=mainPage%>" />
        &nbsp;
        </td></tr></table>
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
</center><!-- 
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? 
"https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-9565684-1"); pageTracker._trackPageview("<%=mainPage%>");
} catch(err) {}</script>-->
</body>
</html:html>
