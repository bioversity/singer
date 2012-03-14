<%@page import="org.sgrp.singer.indexer.BaseIndexer"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.sgrp.singer.SearchResults"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<%@page import="org.sgrp.singer.form.AccessionForm"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.sgrp.singer.AccessionConstants"%>
<%@page import="org.sgrp.singer.MyShopCart"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.sgrp.singer.StringComparableValues"%>
<%@page import="org.sgrp.singer.indexer.Keywords"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.File"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="org.sgrp.singer.ResourceManager"%>
<%
	String type = request.getParameter("type");
	if (type == null || type.trim().length() == 0) {
		type = "xml";
	}

	//String attribList = "accenumb" + "|" + AccessionConstants.COLLECTIONNAME + "|" + AccessionConstants.GENUSNAME + "|" + AccessionConstants.SPECIESNAME + "|" + AccessionConstants.COUNTRYNAME+ "|" + AccessionConstants.SOURCENAME+"|" + AccessionConstants.STATUSNAME;
	String query = request.getParameter("search");
	if (query != null && query.trim().length() > 0) {

		String tokens[] = AccessionConstants.luceneStrToArray(query);
		String colls[] = AccessionConstants.getCollectionArray(tokens);
		Map<String, String> map = SearchResults.getInstance().getAccessionStringBySearch(query, colls);
%>
<%

			String fileName = "map_"+BaseIndexer.mangleKeywordValue(query)+ ".kml";
			String fullFile = ResourceManager.getString(AccessionConstants.WEB_ROOT)+File.separator+"outputs"+File.separator+fileName; 
			File tempFile = new File(fullFile);
			BufferedWriter bw = null;
			try {
		bw = new BufferedWriter(new FileWriter(tempFile));
        String sb = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://www.opengis.net/kml/2.2\"><Document><name>Singer output</name>";
        sb = sb+"<Style id=\"accoutput\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/paddle/wht-blank.png</href></Icon></IconStyle><BalloonStyle><text><![CDATA[ AccNumber $[accnumb] AccName $[name] Lat $[lat] Lng $[lng]]]></text></BalloonStyle></Style>";
        //http://kviparthi-3:8082/img/mm_20_red.png
		bw.write(sb);
		bw.newLine();
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String keyid = it.next();
			String text = map.get(keyid);
            String lat = AccessionConstants.getRegExValue(text,"latituded");
            String lng = AccessionConstants.getRegExValue(text,"longituded");
			if((lat!=null && !lat.startsWith("0") && lat.trim().length()>0)  && (lng!=null && !lng.startsWith("0") && lng.trim().length()>0))
		    {
			//Map<String, String> values = AccessionConstants.getRegExValueap(text, attribList);
			StringBuffer str = new StringBuffer(); 
            str.append("<Placemark>");
            str.append("<name>"+AccessionConstants.getRegExValue(text,"accenumb")+"</name>");
            str.append("<styleUrl>#accoutput</styleUrl>");
            str.append("<ExtendedData>");
            str.append("<Data name=\"accnumb\"><value>"+AccessionConstants.getRegExValue(text,"accenumb")+"</value></Data>");
            str.append("<Data name=\"lat\"><value>"+lat+"</value></Data>");
            str.append("<Data name=\"lng\"><value>"+lng+"</value></Data>");
            str.append("</ExtendedData>");
            str.append("<Point><coordinates>"+lng+","+lat+"</coordinates></Point>");
            str.append("</Placemark>");
			bw.write(str.toString());
			bw.newLine();
		  }
		}
		bw.write("</Document></kml>");
		bw.newLine();
		bw.flush();
		bw.close();
		bw = null;
			} catch (Exception e) {
		e.printStackTrace();
			} finally {
		if (bw != null)
			bw.close();
			}
			response.setContentType("application/vnd.google-earth.kml+xml");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
			AccessionServlet.writetoStream(response, tempFile);
			tempFile.delete();
		}
%>
