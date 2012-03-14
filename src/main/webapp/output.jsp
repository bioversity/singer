<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="org.sgrp.singer.SearchResults"%>
<%@page import="org.sgrp.singer.AccessionServlet"%>
<%@page import="org.sgrp.singer.form.AccessionForm"%>
<%@page import="org.sgrp.singer.ObjectStore"%>
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
<%@page import="org.apache.lucene.document.Document"%>
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
		/*Added by Gautier (copy/Paste from geodata.jsp)*/
		ArrayList<String> tempVar = new ArrayList<String>();
		tempVar.add("bio1");
		tempVar.add("bio2");
		tempVar.add("bio5");
		tempVar.add("bio6");
		tempVar.add("bio7");
		tempVar.add("bio8");
		tempVar.add("bio9");
		tempVar.add("bio10");
		tempVar.add("bio11");
		ArrayList<String> orderVar = new ArrayList<String>();
	    orderVar.add("alt");
	    orderVar.add("bio1");
	    orderVar.add("bio2");
	    orderVar.add("bio3");
	    orderVar.add("bio4");
	    orderVar.add("bio5");
	    orderVar.add("bio6");
	    orderVar.add("bio7");
	    orderVar.add("bio8");
	    orderVar.add("bio9");
	    orderVar.add("bio10");
	    orderVar.add("bio11");
	    orderVar.add("bio12");
	    orderVar.add("bio13");
	    orderVar.add("bio14");
	    orderVar.add("bio15");
	    orderVar.add("bio16");
	    orderVar.add("bio17");
	    orderVar.add("bio18");
	    orderVar.add("bio19");
	    HashMap<String,String> labelMap = new HashMap<String,String>();
		labelMap.put("me", "average temperature (deg C)");
		labelMap.put("min","monthly average of daily minimum temperatures (deg C)");
		labelMap.put("max","monthly average of daily maximum temperatures (deg C)");
		labelMap.put("pr", "precipitation (mm - liter/sqm)");
		labelMap.put("pet", "PET (mm - liter/sqm)");
		labelMap.put("sufr", "sunshine fraction (% of possible)");
		labelMap.put("windsp", "wind speed (m/s)");
		labelMap.put("vap", "water vapor pressure (Hpa)");
	
		labelMap.put("bio1", "Annual Mean Temperature");
		labelMap.put("bio2","Mean Diurnal Range (Mean of monthly (max temp - min temp))");
		labelMap.put("bio3", "Isothermality (P2/P7) (* 100)");
		labelMap.put("bio4","Temperature Seasonality (standard deviation *100)");
		labelMap.put("bio5", "Max Temperature of Warmest Month");
		labelMap.put("bio6", "Min Temperature of Coldest Month");
		labelMap.put("bio7", "Temperature Annual Range (P5-P6)");
		labelMap.put("bio8", "Mean Temperature of Wettest Quarter");
		labelMap.put("bio9", "Mean Temperature of Driest Quarter");
		labelMap.put("bio10", "Mean Temperature of Warmest Quarter");
		labelMap.put("bio11", "Mean Temperature of Coldest Quarter");
		labelMap.put("bio12", "Annual Precipitation");
		labelMap.put("bio13", "Precipitation of Wettest Month");
		labelMap.put("bio14", "Precipitation of Driest Month");
		labelMap.put("bio15","Precipitation Seasonality (Coefficient of Variation)");
		labelMap.put("bio16", "Precipitation of Wettest Quarter");
		labelMap.put("bio17", "Precipitation of Driest Quarter");
		labelMap.put("bio18", "Precipitation of Warmest Quarter");
		labelMap.put("bio19", "Precipitation of Coldest Quarter");
	
		labelMap.put("prec", "Precipitation");
		labelMap.put("tmax", "Max. Temperature");
		labelMap.put("tmin", "Min. Temperature");
	
		labelMap.put("alt", "Altitude");
		/*End added by Gautier*/
%>
<%
if (type.equals("html")) {
%>

<div>
Use the default search option
<!-- table border="1">
	<tr>
		<td class="collHead" width="10%" align="center">Accession number</td>
		<td class="collHead" width="20%" align="center">Collection name</td>
		<td class="collHead" width="20%" align="center">Genus</td>
		<td class="collHead" width="15%" align="center">Species</td>
		<td class="collHead" width="20%" align="center">Country</td>
        <td class="collHead" width="10%" align="center">Source</td>
        <td class="collHead" width="10%" align="center">Status</td>
	</tr>
	<
			for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String keyid = it.next();
			String text = map.get(keyid);
			Map<String, String> values = AccessionConstants.getRegExValueMap(text, attribList);
	%>
	<tr>
		<td width="10%" nowrap="nowrap"><=values.get("accenumb")%></td>
		<td width="20%" nowrap="nowrap"><=values.get(AccessionConstants.COLLECTIONNAME)%></td>
		<td width="20%"><i><=AccessionConstants.makeProper(values.get(AccessionConstants.GENUSNAME))%></i></td>
		<td width="15%"><=values.get(AccessionConstants.SPECIESNAME).toLowerCase()%></td>
		<td width="20%" nowrap="nowrap"><=values.get(AccessionConstants.COUNTRYNAME)%></td>
        <td width="10%" nowrap="nowrap"><=values.get(AccessionConstants.SOURCENAME)%></td>
        <td width="10%" nowrap="nowrap"><=values.get(AccessionConstants.STATUSNAME)%></td>
	</tr>
	<
	}
	%>
</table-->
</div>
<%
			} else if (type.equals("csv")) {

			String fileName = ObjectStore.getMD5(query) + ".csv";
			String fullFile = ResourceManager.getString(AccessionConstants.WEB_ROOT)+File.separator+"outputs"+File.separator+fileName; 
			File tempFile = new File(fullFile);
			if(!tempFile.exists())
			{
				BufferedWriter bw = null;
				try {
                System.out.println("File doesn't exist");
				bw = new BufferedWriter(new FileWriter(tempFile));
		        String sb="";sb =sb+"\"Accession number\",";
				sb =sb+"\"Accession name\",";
				sb =sb+"\"Genus\",";
				sb =sb+"\"Species\",";
				sb =sb+"\"Taxon\",";
				sb =sb+"\"Institute\",";
				sb =sb+"\"Collection name\",";
				sb =sb+"\"Country source\",";
			    sb =sb+"\"Collecting source\",";
				sb =sb+"\"Sample status\",";
				sb =sb+"\"Aquisition date\",";
				sb =sb+"\"Collection number\",";
				sb =sb+"\"Collection date\",";
				sb =sb+"\"Other numbers\",";
				sb =sb+"\"Pedigree\",";
				sb =sb+"\"Intrust status\",";
				sb =sb+"\"Availability\",";
				sb =sb+"\"Safety-duplicate in svalbard\",";
				sb =sb+"\"Collection site\",";
				sb =sb+"\"Latitude\",";
				sb =sb+"\"Longitude\"";
				/* Added by Gautier, to add environmental Data in the export*/
				for(int i=0;i<orderVar.size();i++)
				{
					String field = orderVar.get(i);
					String fieldName = labelMap.get(field);
					sb =sb+",\""+fieldName+"\"";
				}

				
				/*End Added by Gautier*/
				bw.write(sb);
				bw.newLine();
				/*For each Accession*/
				for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
					String keyid = it.next();
					String doc = map.get(keyid);
					//Map<String, String> values = AccessionConstants.getRegExValueap(text, attribList);
					StringBuffer str = new StringBuffer(); 
		            str.append("\""+AccessionConstants.getRegExValue(doc,"accenumb")+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,"accename")+"\",");
		            str.append("\""+AccessionConstants.makeProper(AccessionConstants.getRegExValue(doc,AccessionConstants.GENUSNAME))+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,AccessionConstants.SPECIESNAME).toLowerCase()+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,AccessionConstants.TAXONNAME)+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,AccessionConstants.INSTITUTENAME)+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,AccessionConstants.COLLECTIONNAME)+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,AccessionConstants.COUNTRYNAME)+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,AccessionConstants.SOURCENAME)+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,AccessionConstants.STATUSNAME)+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,"acqdate")+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,"collnumb")+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,"colldate")+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,"othernumb")+"\",");
		            String pedigree = AccessionConstants.getRegExValue(doc,"pedigree");
		            String parentfemale = AccessionConstants.getRegExValue(doc,"parentfemale");
		            String parentmale = AccessionConstants.getRegExValue(doc,"parentmale");
		            if(pedigree!=null && !pedigree.equals("null"))
		            {
		            	str.append("\""+pedigree+"\",");              
		            } 
		            else if ((parentfemale!=null && !parentfemale.equals("null")) && (parentmale!=null&& !parentmale.equals("null")))
		            {
		            	str.append("\""+parentfemale+" "+parentmale+"\",");
		            }
		            else
		            {
		            	str.append("\"null\",");
		            }
		            str.append("\""+AccessionConstants.getRegExValue(doc,AccessionConstants.TRUSTNAME)+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,"availability")+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,"insvalbard")+"\",");
		            str.append("\""+AccessionConstants.getRegExValue(doc,"collsite")+"\",");
		            String latituded = AccessionConstants.getRegExValue(doc,"latituded");
		            str.append("\""+latituded+"\",");
		            String longituded = AccessionConstants.getRegExValue(doc,"longituded");
		            str.append("\""+longituded+"\"");
		            /* Added by Gautier, to add environmental Data in the export*/
		            /* if coordonates are correct */
		    		//if(latituded!=null && !latituded.startsWith("0") && latituded.trim().length()>0  && longituded!=null && !longituded.startsWith("0") && longituded.trim().length()>0)
		    		//{
		    		//	/*We fill the file with the environmental data*/
		    		//	Map<String,String> latlngData = SearchResults.getInstance().getCleanedLatLng(latituded,longituded);
		    		//    Map<String, Integer[]> ascData = SearchResults.getInstance().getAscDataLatLng(latituded,longituded);
		    		//    boolean calRadius=latlngData.get("radius").equals("Y");
		    		//    for (int i = 0; i <orderVar.size(); i++) {
		    	    //  		String currid = orderVar.get(i);
		    	    //  		Integer[] ascvalue = ascData.get(currid);
		    	    //        int ascVal = ascvalue[0];
		    	    //  		String ascdispValue = ascvalue[0] + "";
		    	    //  		if (ascvalue==null || ascVal == -9999) {
		    	    //  			ascdispValue = "-";
		    	    //  		} else {
		    	    //  			if (tempVar.contains(currid)) {
		    	    //  				ascdispValue = ((double) ascVal / 10) + "";
		    	    //  			}
		    	    //  		}
		    		//    
		    	    //  		str.append(",\""+ascdispValue+"\"");
		    		//    }
		    		//}
		    		//else
		    		//{
		    			/*Else we set all environmental data values to NA (Non available)*/
		    			for (int i = 0; i <orderVar.size(); i++)
		    			{
		    				str.append(",NA");
		    			}
		    		//}
		            String line = str.toString();
		            System.out.println(line);
		    		/*End Added by Gautier*/
					bw.write(line);
					bw.newLine();
				}
				bw.flush();
				bw.close();
				bw = null;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bw != null)
					bw.close();
				}
			}
			//response.setContentType("application/x-msdownload;charset=UTF-8");
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
			AccessionServlet.writetoStream(response, tempFile);
			//tempFile.delete();
%>
<%
			} else if (type.equals("xml")) {
			String fileName = ObjectStore.getMD5(query) + ".xml";
			String fullFile = ResourceManager.getString(AccessionConstants.WEB_ROOT)+File.separator+"outputs"+File.separator+fileName; 
			File tempFile = new File(fullFile);
			if(!tempFile.exists())
			{
				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new FileWriter(tempFile));
					String sb = "<data><titles>";
			        int i=1;
					sb =sb+"<f"+i+">Accession number</f"+i+++">";
					sb =sb+"<f"+i+">Accession name</f"+i+++">";
					sb =sb+"<f"+i+">Genus</f"+i+++">";
					sb =sb+"<f"+i+">Species</f"+i+++">";
					sb =sb+"<f"+i+">Taxon</f"+i+++">";
					sb =sb+"<f"+i+">Institute</f"+i+++">";
					sb =sb+"<f"+i+">Collection name</f"+i+++">";
					sb =sb+"<f"+i+">Country source</f"+i+++">";
				    sb =sb+"<f"+i+">Collecting source</f"+i+++">";
					sb =sb+"<f"+i+">Sample status</f"+i+++">";
					sb =sb+"<f"+i+">Aquisition date</f"+i+++">";
					sb =sb+"<f"+i+">Collection number</f"+i+++">";
					sb =sb+"<f"+i+">Collection date</f"+i+++">";
					sb =sb+"<f"+i+">Other numbers</f"+i+++">";
					sb =sb+"<f"+i+">Pedigree</f"+i+++">";
					sb =sb+"<f"+i+">Intrust status</f"+i+++">";
					sb =sb+"<f"+i+">Availability</f"+i+++">";
					sb =sb+"<f"+i+">Safety-duplicate in svalbard</f"+i+++">";
					sb =sb+"<f"+i+">Collection site</f"+i+++">";
					sb =sb+"<f"+i+">Latitude</f"+i+++">";
					sb =sb+"<f"+i+">Longitude</f"+i+++">";
					/* Added by Gautier, to add environmental Data in the export*/
					for(int j=0;j<orderVar.size();j++)
					{
						String field = orderVar.get(j);
						String fieldName = labelMap.get(field);
						sb =sb+"<f"+i+">"+fieldName+"</f"+i+++">";
					}
					
					/*End Added by Gautier*/
					sb =sb+"</titles>";
					sb =sb+"<records>";
					bw.write(sb);
					bw.newLine();
					//For each Accessions
					for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			            i =1;
						String keyid = it.next();
						String doc = map.get(keyid);
						//Map<String, String> values = AccessionConstants.getRegExValueap(text, attribList);
						StringBuffer str = new StringBuffer(); 
			            str.append("<record>");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"accenumb")+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"accename")+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.makeProper(AccessionConstants.getRegExValue(doc,AccessionConstants.GENUSNAME))+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,AccessionConstants.SPECIESNAME).toLowerCase()+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,AccessionConstants.TAXONNAME)+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,AccessionConstants.INSTITUTENAME)+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,AccessionConstants.COLLECTIONNAME)+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,AccessionConstants.COUNTRYNAME)+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,AccessionConstants.SOURCENAME)+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,AccessionConstants.STATUSNAME)+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"acqdate")+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"collnumb")+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"colldate")+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"othernumb")+"</f"+i+++">");
			            String pedigree = AccessionConstants.getRegExValue(doc,"pedigree");
			            String parentfemale = AccessionConstants.getRegExValue(doc,"parentfemale");
			            String parentmale = AccessionConstants.getRegExValue(doc,"parentmale");
			            if(pedigree!=null && !pedigree.equals("null"))
			            {
			            	str.append("<f"+i+">"+pedigree+"</f"+i+++">");              
			            } 
			            else if ((parentfemale!=null && !parentfemale.equals("null")) && (parentmale!=null&& !parentmale.equals("null")))
			            {
			            	str.append("<f"+i+">"+parentfemale+" "+parentmale+"</f"+i+++">");
			            }
			            else
			            {
			            	str.append("<f"+i+">null</f"+i+++">");
			            }
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,AccessionConstants.TRUSTNAME)+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"availability")+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"insvalbard")+"</f"+i+++">");
			            str.append("<f"+i+">"+AccessionConstants.getRegExValue(doc,"collsite")+"</f"+i+++">");
			            String latituded=AccessionConstants.getRegExValue(doc,"latituded");
			            str.append("<f"+i+">"+latituded+"</f"+i+++">");
			            String longituded = AccessionConstants.getRegExValue(doc,"longituded");
			            str.append("<f"+i+">"+longituded+"</f"+i+++">");
			            /* Added by Gautier, to add environmental Data in the export*/
			            /* if coordonates are correct */
			    		if(latituded!=null && !latituded.startsWith("0") && latituded.trim().length()>0  && longituded!=null && !longituded.startsWith("0") && longituded.trim().length()>0)
			    		{
			    			/*We fill the file with the environmental data*/
			    			Map<String,String> latlngData = SearchResults.getInstance().getCleanedLatLng(latituded,longituded);
			    		    Map<String, Integer[]> ascData = SearchResults.getInstance().getAscDataLatLng(latituded,longituded);
			    		    boolean calRadius=latlngData.get("radius").equals("Y");
			    		    for (int j = 0; j <orderVar.size(); j++) {
			    	      		String currid = orderVar.get(j);
			    	      		Integer[] ascvalue = ascData.get(currid);
			    	            int ascVal = ascvalue[0];
			    	      		String ascdispValue = ascvalue[0] + "";
			    	      		if (ascvalue==null || ascVal == -9999) {
			    	      			ascdispValue = "-";
			    	      		} else {
			    	      			if (tempVar.contains(currid)) {
			    	      				ascdispValue = ((double) ascVal / 10) + "";
			    	      			}
			    	      		}
			    	      		
			    	      		str.append("<f"+i+">"+ascdispValue+"</f"+i+++">");
			    		    }
			    		}
			    		else
			    		{
			    			/*Else we set all environmental data values to NA (Non available)*/
			    			for (int j = 0; j <orderVar.size(); j++)
			    			{
			    				str.append("<f"+i+">NA</f"+i+++">");;
			    			}
			    		}
			    		/*End Added by Gautier*/
			            
			            str.append("</record>");
			            String xmlStr = AccessionConstants.replaceString(str.toString(),"&amp;","&",0);
			            xmlStr= AccessionConstants.replaceString(xmlStr,"&","&amp;",0);
						bw.write(xmlStr);
						bw.newLine();
					}
					bw.write("</records></data>");
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
			}
			response.setContentType("text/xml");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
			AccessionServlet.writetoStream(response,tempFile);
			//tempFile.delete();
		}
%>
<%
}
%>
