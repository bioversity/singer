<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.sgrp.singer.MySearchHistory"%>
<%@page import="org.sgrp.singer.SearchResults"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.xerces.dom.DocumentImpl"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="java.io.File"%>
<%@page import="org.sgrp.singer.ResourceManager"%>
<%@page import="java.util.Date"%>
<%@page import="org.sgrp.singer.StringComparableValues"%>
<%@ include file="/functions.jsp" %>
<div>
<%
	//String qType= null;
	//String qValue= null;
	String hkeycountlink = "/indexdemotreaty.jsp?page=showkeycountdemo&";
	String hdisppagelink = "/indexdemotreaty.jsp?page=disppagedemo&";
  String stype = request.getParameter("searchtype");
    String query = null;
    if(stype!=null && stype.trim().equals("rangemap"))
    {
      String nw = request.getParameter("nw");
      String ne = request.getParameter("ne");
      String se = request.getParameter("se");
      String sw = request.getParameter("sw");
        query = AccessionConstants.fromPointsToSearch(nw,ne,se,sw);
        //hkeycountlink = "/showkeycount.jsp?";
    }
    else
    {
      query = request.getParameter("search");
    }System.out.println(query);
    
	if (query != null && query.trim().length() > 0) {
		String tokens[] = AccessionConstants.luceneStrToArray(query);
		String colls[] = AccessionConstants.getCollectionArray(tokens);
		//System.out.println("Query is :"+qType+" = "+qValue);
		HashMap<String, HashMap<String, StringComparableValues>> map = SearchResults.getInstance().getAccessionKeyCount(query, colls);
    String queryName = AccessionServlet.getKeywords().getQueryAsText(tokens,query);
		//System.out.println("Value is :"+map.size());
%> <br />
<table width="100%">
<tr>
<td width="100%">
<!-- =query%><br/><span class="keyTitle"><%=queryName%></span>&nbsp;&nbsp;<a title="Save query for future"
          href="<%=hkeycountlink%>search=<%=query%>&saction=add&squery=<%=query%>&squeryName=<%=queryName%>"><img
          src="/img/save.gif" border=0/></a>-->
          <%/*Added by Gautier to sava automatically every queries*/
 				MySearchHistory searchHistory = (MySearchHistory) request.getSession(true).getAttribute("mysearch");
 				searchHistory.addToHistory(query, queryName); 
 				 if( isLoggedIn(request.getSession(true)))
 				{
 					searchHistory.saveToFile(getUserName(request.getSession(true)));
 				}
 				 /*End added by Gautier*/
 				%>
          <br />
          <br />
<%
	if (map.size() > 0) 
    {
			HashMap<String, StringComparableValues> valuesMap = (HashMap<String, StringComparableValues>) map.get(AccessionConstants.ALL_KEY_COUNT);
			StringComparableValues totValues = (StringComparableValues) valuesMap.get(AccessionConstants.ALL_KEY_COUNT);
%> <a class="headerlinks" href="<%=hdisppagelink%>search=<%=query%>">View All <%=totValues.getCount()%> results</a><br />
<%
	//valuesMap = (HashMap<String, StringComparableValues>) map.get("latlongd");
    valuesMap = (HashMap<String, StringComparableValues>) map.get(AccessionConstants.ALL_GIS_MAP_COUNT);
			if (valuesMap != null && valuesMap.size() > 0) {
				totValues = (StringComparableValues) valuesMap.get(AccessionConstants.ALL_GIS_MAP_COUNT);
				/*int count = 0;
				String fName = (new Date()).getTime() + ".xml";
				try {

					String fileName = ResourceManager
							.getString(AccessionConstants.WEB_ROOT)
							+ File.separator
							+ "gmap"
							+ File.separator
							+ "data" + File.separator + fName;

					Document doc = new DocumentImpl();
					Element markers = doc.createElement("markers");

					List values = new Vector(valuesMap.values());
					java.util.Collections.sort(values);
					//java.util.Collections.reverse(values);
					for (int i = 0; i < values.size(); i++) {
						StringComparableValues scompValues = (StringComparableValues) values.get(i);
						String key = scompValues.getId();
						String value = scompValues.getCount();
						count = count + Integer.parseInt(value);
						try {
							//System.out.println("Writing :"+cbForm.getTitle());
							//System.out.println("Date "+cbForm.getlongAsDate(cbForm.getSdate(),"MMM dd yyyy HH:mm:ss Z"));
							//isDuration=\"true\"
							String lat = key.substring(0, key
									.indexOf("#"));
							String lng = key
									.substring(key.indexOf("#") + 1);
							Element marker = doc
									.createElement("marker");
							marker.setAttribute("lat", lat);
							marker.setAttribute("lng", lng);
							marker.setAttribute("txt", value);

							StringBuffer sb = new StringBuffer();
							AccessionServlet.replaceTextInNode(marker,
									sb.toString());
							markers.appendChild(marker);
						} catch (Exception e) {
							System.out.println("Exception "
									+ e.toString());
						}
					}
					doc.appendChild(markers);
					AccessionServlet.saveXML(doc, fileName);
				} catch (Exception e) {
					System.out.println("Exception " + e.toString());
				}*/
				//String mapLink = "/index.jsp?page=plotmap&query="+ query + "&xml=/gmap/data/" + fName;
				String mapLink = "/indexdemotreaty.jsp?";
				String gmapLink = "/indexdemotreaty.jsp?";
%> <a class="headerlinks" href="<%=mapLink%>">View Reference Map (<%=totValues.getCount()%> accession plotted)  (Not available in this demo)</a>
<!-- if(stype!=null && stype.trim().equals("rangemap"))
{
%
<a class="headerlinks" href="<=gmapLink%>">(View in Google Earth)</a><br />
 }
%-->
<br />
<%
      }
 %> <br />
 </td>
 </tr>
  <tr>
    <td>Click <img src="/img/refine.png" border="0" width="10" height="10" /> for further refinement of your selection <br />
    <br />
    <table class="collTable" width="100%">
      <tr>
        <td class="collHead" colspan="2" width="50%" align="center">Collection name</td>
        <td class="collHead" colspan="1" width="50%" align="center">Intrust status</td>
      </tr>

      <tr>
        <td colspan="2" align="left" width="50%" valign="top" class="collTable">
        <div>
        <%
        	valuesMap = (HashMap<String, StringComparableValues>) map
        					.get(AccessionConstants.COLLECTIONCODE);
        			if (valuesMap != null && valuesMap.size() > 0) {
        				List values = new Vector(valuesMap.values());
        				java.util.Collections.sort(values);
        				//java.util.Collections.reverse(values);
        				for (int i = 0; i < values.size(); i++) {
        					StringComparableValues scompValues = (StringComparableValues) values
        							.get(i);
    						String key = scompValues.getId();
    						String value = scompValues.getCount();
        					String keySearch = "(" + query + ") AND "
        							+ AccessionConstants.COLLECTIONCODE
        							+ AccessionConstants.SPLIT_KEY + key;
        %> <%
 	if (values.size() > 1) {
 %> <a class="headerlinks" href="<%=hkeycountlink%>search=<%=keySearch%>"><img src="/img/refine.png" border="0" width="10" height="10" /></a> <%
 	}
 %><a class="headerlinks" href="<%=hdisppagelink%>search=<%=keySearch%>"><%=scompValues.getName()%></a> (<%=value%>)<br />
        <%
        	}
        			}
        %>
        </div>
        </td>
        <td colspan="1" align="left" width="50%" valign="top" class="collTable">
        <div>
        <%
        	valuesMap = (HashMap<String, StringComparableValues>) map.get(AccessionConstants.TRUSTCODE);
        			if (valuesMap != null && valuesMap.size() > 0) {
        				List values = new Vector(valuesMap.values());
        				java.util.Collections.sort(values);
        				//java.util.Collections.reverse(values);
        				for (int i = 0; i < values.size(); i++) {
        					StringComparableValues scompValues = (StringComparableValues) values
							.get(i);
					String key = scompValues.getId();
					String value = scompValues.getCount();
        					String keySearch = query + " AND "
        							+ AccessionConstants.TRUSTCODE
        							+ AccessionConstants.SPLIT_KEY + key;
        %><%
  if (values.size() > 1) {
 %> <a class="headerlinks" href="<%=hkeycountlink%>search=<%=keySearch%>"><img src="/img/refine.png" border="0" width="10" height="10" /></a> <%
  } 
%><a class="headerlinks" href="<%=hdisppagelink%>search=<%=keySearch%>"><%=scompValues.getName()%></a> (<%=value%>)<br/> <%
 	}
 			}
 %>
        </div>

        </td>

      </tr>
      <tr>
        <td class="collHead" colspan="2" width="50%" align="center">Sample Status</td>
        <td class="collHead" colspan="1" width="50%" align="center">Collecting Source</td>
      </tr>
      <tr>
        <td colspan="2" width="50%" valign="top" class="collTable">
        <div>
        <%
        	valuesMap = (HashMap<String, StringComparableValues>) map
        					.get(AccessionConstants.STATUSCODE);
        			if (valuesMap != null && valuesMap.size() > 0) {
        				List values = new Vector(valuesMap.values());
        				java.util.Collections.sort(values);
        				//java.util.Collections.reverse(values);
        				for (int i = 0; i < values.size(); i++) {
        					StringComparableValues scompValues = (StringComparableValues) values
							.get(i);
					         String key = scompValues.getId();
					         String value = scompValues.getCount();
        					String keySearch = query + " AND "
        							+ AccessionConstants.STATUSCODE
        							+ AccessionConstants.SPLIT_KEY + key;
        %> <%
 	if (values.size() > 1) {
 %> <a class="headerlinks" href="<%=hkeycountlink%>search=<%=keySearch%>"><img src="/img/refine.png" border="0" width="10" height="10" /></a> <%
 	}
 %><a class="headerlinks" href="<%=hdisppagelink%>search=<%=keySearch%>"><%=scompValues.getName()%></a> (<%=value%>)<br />
        <%
        	}
        			}
        %>
        </div>
        </td>
        <td colspan="1" width="50%" valign="top" class="collTable" nowrap="nowarp">
        <div>
        <%
          valuesMap = (HashMap<String, StringComparableValues>) map
                  .get(AccessionConstants.SOURCECODE);
              if (valuesMap != null && valuesMap.size() > 0) {
                List values = new Vector(valuesMap.values());
                java.util.Collections.sort(values);
                //java.util.Collections.reverse(values);
                for (int i = 0; i < values.size(); i++) {
                	StringComparableValues scompValues = (StringComparableValues) values
					.get(i);
			String key = scompValues.getId();
			String value = scompValues.getCount();
			String keySearch = query + " AND "
                      + AccessionConstants.SOURCECODE
                      + AccessionConstants.SPLIT_KEY + key;
        %> <%
  if (values.size() > 1) {
 %> <a class="headerlinks" href="<%=hkeycountlink%>search=<%=keySearch%>"><img src="/img/refine.png" border="0" width="10" height="10" /></a> <%
  }
 %><a class="headerlinks" href="<%=hdisppagelink%>search=<%=keySearch%>"><%=scompValues.getName()%></a> (<%=value%>)<br />
        <%
          }
              }
        %>
        </div>
        </td>
        </tr>
      <tr>
        <td class="collHead" width="25%" align="center">Genus</td>
        <td class="collHead" width="25%" align="center">Species</td>
        <td class="collHead" width="50%" align="center">Country Source</td>
      </tr>
      <tr>
        <td width="25%" valign="top" class="collTable">
        <div>
        <%
        	valuesMap = (HashMap<String, StringComparableValues>) map.get(AccessionConstants.GENUSCODE);
        			if (valuesMap != null && valuesMap.size() > 0) {
        				List values = new Vector(valuesMap.values());
        				java.util.Collections.sort(values);
        				//java.util.Collections.reverse(values);
        				for (int i = 0; i < values.size(); i++) {
        					StringComparableValues scompValues = (StringComparableValues) values
							.get(i);
					String key = scompValues.getId();
					String value = scompValues.getCount();
        					String keySearch = query + " AND "
        							+ AccessionConstants.GENUSCODE
        							+ AccessionConstants.SPLIT_KEY + key;
        %> <%
 	if (values.size() > 1) {
 %> <a class="headerlinks" href="<%=hkeycountlink%>search=<%=keySearch%>"><img src="/img/refine.png" border="0" width="10" height="10" /></a> <%
 	}
 %><a class="headerlinks" href="<%=hdisppagelink%>search=<%=keySearch%>"><i><%=AccessionConstants
													.makeProper(scompValues.getName())%></i></a> (<%=value%>)<br />
        <%
        	}
        			}
        %>
        </div>
        </td>
        <td width="25%" valign="top" class="collTable">
        <div>
        <%
        	valuesMap = (HashMap<String, StringComparableValues>) map
        					.get(AccessionConstants.SPECIESCODE);
        			if (valuesMap != null && valuesMap.size() > 0) {
        				List values = new Vector(valuesMap.values());
        				java.util.Collections.sort(values);
        				//java.util.Collections.reverse(values);
        				for (int i = 0; i < values.size(); i++) {
        					StringComparableValues scompValues = (StringComparableValues) values
							.get(i);
					String key = scompValues.getId();
					String value = scompValues.getCount();
        					String keySearch = query + " AND "
        							+ AccessionConstants.SPECIESCODE
        							+ AccessionConstants.SPLIT_KEY + key;
        %> <%
 	if (values.size() > 1) {
 %> <a class="headerlinks" href="<%=hkeycountlink%>search=<%=keySearch%>"><img src="/img/refine.png" border="0" width="10" height="10" /></a> <%
 	}
 %><a class="headerlinks" href="<%=hdisppagelink%>search=<%=keySearch%>"><i><%=scompValues.getName().toLowerCase()%></i></a> (<%=value%>)<br />
        <%
        	}
        			}
        %>
        </div>
        </td>

        

        <td width="50%" valign="top" class="collTable" nowrap="nowarp">
        <div>
        <%
        	valuesMap = (HashMap<String,StringComparableValues>) map
        					.get(AccessionConstants.COUNTRYCODE);
        			if (valuesMap != null && valuesMap.size() > 0) {
        				List values = new Vector(valuesMap.values());
        				java.util.Collections.sort(values);
        				//java.util.Collections.reverse(values);
        				for (int i = 0; i < values.size(); i++) {
        					StringComparableValues scompValues = (StringComparableValues) values
							.get(i);
					String key = scompValues.getId();
					String value = scompValues.getCount();
        					String keySearch = query + " AND "
        							+ AccessionConstants.COUNTRYCODE
        							+ AccessionConstants.SPLIT_KEY + key;
        %> <%
 	if (values.size() > 1) {
 %> <a class="headerlinks" href="<%=hkeycountlink%>search=<%=keySearch%>"><img src="/img/refine.png" border="0" width="10" height="10" /></a> <%
 	}
 %><a class="headerlinks" href="<%=hdisppagelink%>search=<%=keySearch%>"><%=scompValues.getName()%></a> (<%=value%>)<br />
        <%
        	}
        			}
        %>
        </div>
        </td>
      </tr>
    </table>
    </td>
  </tr>
</table>
<%
	} else {
%> <br />
<br />
<center style="color: red; font-weight: bold">No results found for the selected criteria</center>

<%
	}
	} else {
%> <br />
<br />
<center><font color="red"><b>Not a valid query String</b></font></center>
<%
	}
%>
</div>