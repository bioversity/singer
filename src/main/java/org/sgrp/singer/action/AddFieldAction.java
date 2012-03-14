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
import org.sgrp.singer.form.AccessionForm;

public class AddFieldAction extends Action {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AccessionForm.setSearchAttributes(request);
		response.setContentType("text/xml");
		response.setCharacterEncoding("iso-8859-1");
		String fieldName = request.getParameter("field");	
		int n = Integer.parseInt(request.getParameter("n"));
		String text=request.getParameter("text");
		Map<String,String> select = (Map<String,String>)request.getAttribute(fieldName);
		if(fieldName.equals("distributionCty"))
		{
			select = (Map<String,String>)request.getAttribute("countries");
		}
		
		
		byte[] bytes = null;
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='ISO-8859-1' ?>");
		xml.append("<result>");
		xml.append("<head>");
		xml.append(text);
		xml.append("</head>");
		xml.append("<value class='dispValue"+n%2+"'>");
		
		if(select!=null)
		{
			String selectName =null;
			if(fieldName.equals("collections"))
			{
				selectName = AccessionConstants.COLLECTIONCODE;
			}
			else if(fieldName.equals("statuses"))
			{
				selectName=AccessionConstants.STATUSCODE;
			}
			else if(fieldName.equals("sources"))
			{
				selectName=AccessionConstants.SOURCECODE;
			}
			else if(fieldName.equals("countries"))
			{
				selectName=AccessionConstants.COUNTRYCODE;
			}
			else if(fieldName.equals("taxons"))
			{
				selectName=AccessionConstants.GENUSCODE+AccessionConstants.SPECIESCODE;
			}
			else if(fieldName.equals("trusts"))
			{
				selectName=AccessionConstants.TRUSTCODE;
			}
			else if(fieldName.equals("distributionCty"))
			{
				selectName="distributionCty";
			}
			xml.append("<select name='"+selectName+"' >");
			for(Iterator<String> i = select.keySet().iterator();i.hasNext();)
			{
				String key=i.next();
				String value = select.get(key);
				/* We escape the & character to create a valid XML*/
				key = AccessionConstants.replaceString(key,"&", "&amp;", 0);
				value = AccessionConstants.replaceString(value,"&", "&amp;", 0);
				/* We escape the < character to create a valid XML*/
				key = AccessionConstants.replaceString(key,"<", "&lt;", 0);
				value = AccessionConstants.replaceString(value,"<", "&lt;", 0);
				/* We escape the > character to create a valid XML*/
				key = AccessionConstants.replaceString(key,">", "&gt;", 0);
				value = AccessionConstants.replaceString(value,">", "&gt;", 0);
				
				xml.append("<option value=\""+key+"\">");
				xml.append(value);
				xml.append("</option>");				
			}
			xml.append("</select>");
		}
		else if(fieldName.matches("^bio\\d+$") || fieldName.equals("alt"))
		{
			xml.append("<text name='"+fieldName+"' />");
		}
		/*else if(fieldName.equals("distribution"))
		{
			xml.append("<distribution>");
			xml.append("<country>");
			Map<String,String> country = (Map<String,String>)request.getAttribute("countries");
			for(Iterator<String> i = country.keySet().iterator();i.hasNext();)
			{
				String key=i.next();
				String value = country.get(key);
				 We escape the & character to create a valid XML
				key = AccessionConstants.replaceString(key,"&", "&amp;", 0);
				value = AccessionConstants.replaceString(value,"&", "&amp;", 0);
				 We escape the < character to create a valid XML
				key = AccessionConstants.replaceString(key,"<", "&lt;", 0);
				value = AccessionConstants.replaceString(value,"<", "&lt;", 0);
				 We escape the > character to create a valid XML
				key = AccessionConstants.replaceString(key,">", "&gt;", 0);
				value = AccessionConstants.replaceString(value,">", "&gt;", 0);
				key=key.substring(2);
				
				if(key.length()==3 && !key.equals("cip"))
				{
					xml.append("<option value=\""+key+"\">");
					xml.append(value);
					xml.append("</option>");
				}								
			}
			xml.append("</country>");
			xml.append("</distribution>");			
			
		}*/
		xml.append("<a><img title='Click here to remove this field ' src='/img/remove_icon.gif' onclick=\"removeField(document.getElementById('field"+n+"'));\" style='position:absolute;' /></a>");
		xml.append("</value>");
		xml.append("</result>");
		bytes = xml.toString().getBytes();
		OutputStream os = response.getOutputStream();
		os.write(bytes);
		os.close();
	 
		return null;
	}

}
