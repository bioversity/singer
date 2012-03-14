
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="org.sgrp.singer.db.EnvDataManager"%>
<html><head>
<title>Kiran's Environment search</title>
<style>
  .sliderInput {
  	height:20;
    width:45;
  	font-family : Tahoma,Arial, Helvetica, sans-serif;
  	font-size : 12px;
  }
  .smallTxt {
    font-size: 12px;
  }
</style>
<script type="text/javascript" src="/slider/lib/LibCrossBrowser.js"></script>
<script type="text/javascript" src="/slider/lib/EventHandler.js"></script>
<script type="text/javascript" src="/slider/core/form/Bs_FormUtil.lib.js"></script>
<script type="text/javascript" src="/slider/core/gfx/Bs_ColorUtil.lib.js"></script>
<script type="text/javascript" src="/slider/Bs_Slider.class.js"></script>
<link rel="stylesheet" type="text/css" href="/css/singer.css" />

<%
LinkedHashMap<String,String[]> lmap = EnvDataManager.getEnvMap();
Map map = request.getParameterMap();
String link = EnvDataManager.makeSearchQuery(map);
%>

<script type="text/javascript"><!--
function init() {

<%
for(Iterator<String> iter=lmap.keySet().iterator(); iter.hasNext();)
{
  String key = iter.next();
  String val[] = lmap.get(key);
  %>
  <%=key%> = new Bs_Slider();
  <%=key%>.loadSkin('osx-horizontal');
  <%=key%>.width         = 520;
  <%=key%>.imgDir = '/slider/img/osx/';
  <%=key%>.fieldName     = '<%=key%>_start';
  <%=key%>.minVal        = <%=val[2]%>;
  <%=key%>.maxVal        = <%=val[3]%>;
  <%=key%>.valueInterval = <%=val[4]%>;
  <%=key%>.valueDefault  = <%=val[5]%>;
  <%=key%>.setSliderIcon('horizontal_knob_blue.gif', 13, 18);
  <%=key%>.useInputField = 3;
  <%=key%>.styleValueFieldClass = 'sliderInput';
  <%=key%>.colorbar = new Object({ color:'blue', height:5, widthDifference:-34, offsetLeft:-4, offsetTop:9 });
  <%=key%>.useSecondKnob        = true;
  <%=key%>.preventValueCrossing = true;
  <%=key%>.wheelAmount        = 0;
  <%=key%>.fieldName2     = '<%=key%>_end';
  <%=key%>.minVal2        = <%=val[2]%>;
  <%=key%>.maxVal2        = <%=val[3]%>;
  <%=key%>.valueInterval2 = <%=val[4]%>;
  <%=key%>.valueDefault2  = <%=val[6]%>;
  <%=key%>.setSliderIcon2('horizontal_knob_red.gif', 13, 18);
  <%=key%>.useInputField2 = 3;
  <%=key%>.styleValueFieldClass2 = 'sliderInput';
  <%=key%>.colorbar2 = new Object({ color:'orange', height:5, widthDifference:-34, offsetLeft:45, offsetTop:9 });
  <%=key%>.drawInto('<%=key%>Div');
<%  
}
%>

}
// --></script>
</head>
<body onLoad="init();">

<h3>Environmental Data Search</h3>
<br/>
<br/>
<table width="100%">
<tr>
<td width="70%" valign="top">
<form name="envdata" id="test" action="/envsearch.jsp" method="post">
<table border="1" >
<tr>
<td align="center" class="smallTxt">Search</td>
<td align="center" class="smallTxt">Range Search</td>
<td align="center" class="smallTxt">Single / Range Selection</td>
</tr>
<%
for(Iterator<String> iter1=lmap.keySet().iterator(); iter1.hasNext();)
{
  String key = iter1.next();
  String val[] = lmap.get(key);
  %>
  <tr>
  <td class="smallTxt"><input type="checkbox" name="<%=key%>_check"/><%=key.toUpperCase()%></td>
  <td class="smallTxt"><input type="checkbox" name="<%=key%>_range"/>[<%=val[2]%> - <%=val[3]%>]</td>
  <td class="smallTxt"><div id="<%=key%>Div" style="width:600px; height:25px; border:1px solid gray;"></div></td>
  </tr>  
  <%
  }
  %>
  
</table>
<br>
<input type="submit" name="btnSubmit" value="Find"> 
</form>
</td>
<td width="30%" valign="top">
<%
if(link!=null && link.trim().length()>0)
{
  String queryPage = "/envsearchpage.jsp?search="+link;
%>
<a href="<%=queryPage%>" target="_new"/>View in separate Page</a>
<br/>
<br/>
<div class="smallTxt">
<jsp:include page="<%=queryPage%>" flush="true"/>
</div>
<%
}
%>
</td>
</tr>
</table>
</body></html>
