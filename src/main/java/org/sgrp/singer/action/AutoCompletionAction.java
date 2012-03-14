package org.sgrp.singer.action;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.SearchResults;
import org.sgrp.singer.indexer.Keywords;

public class AutoCompletionAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/xml");
		response.setCharacterEncoding("iso-8859-1");
		String subString = request.getParameter("auto");
		String query ="";
		if(subString!=null && subString.trim().length()>0)
		{
			String[] split = subString.split("\\s");
			for(int i=0;i<split.length;i++)
			{
				query += " AND ("+AccessionConstants.FULL_NAME+":*"+split[i]+"*)";
			}
			
		}
		
		Map<String,String> genusSpecies = SearchResults.getInstance().getGenusSpeciesListBySearch(query);
		byte[] bytes = null;
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='ISO-8859-1' ?>");
		xml.append("<allgenusspecies>\n");
		for(Iterator<String> i = genusSpecies.keySet().iterator();i.hasNext();)
		{
			String key = i.next();
			String value = genusSpecies.get(key);
			/* We escape the & character to create a valid XML*/
			key = AccessionConstants.replaceString(key,"&", "&amp;", 0);
			value = AccessionConstants.replaceString(value,"&", "&amp;", 0);
			/* We escape the < character to create a valid XML*/
			key = AccessionConstants.replaceString(key,"<", "&lt;", 0);
			value = AccessionConstants.replaceString(value,"<", "&lt;", 0);
			/* We escape the > character to create a valid XML*/
			key = AccessionConstants.replaceString(key,">", "&gt;", 0);
			value = AccessionConstants.replaceString(value,">", "&gt;", 0);
			xml.append("<genusspecies>\n");
			xml.append("<id>"+key+"</id>\n");
			xml.append("<name>"+value+"</name>\n");
			xml.append("</genusspecies>\n");
		}
		xml.append("</allgenusspecies>");
		bytes = xml.toString().getBytes();
	 
		OutputStream os = response.getOutputStream();
		os.write(bytes);
		os.close();
	 
		return null;
	}

}
