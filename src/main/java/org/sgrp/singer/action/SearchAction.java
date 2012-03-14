package org.sgrp.singer.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.form.SearchForm;

public class SearchAction extends GenericAction {

	public SearchAction() {
		super();
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setPageParmeters(mapping, form, request, response);
		if (isCancelled(request)) { return getPreviousLinkForward(AccessionConstants.SUCCESS); }
		Map reqMap = request.getParameterMap();
		String page = null;
		if (reqMap.containsKey("page")) {
			page = ((String[]) reqMap.get("page"))[0];
		}
		if ((page == null) || (page.trim().length() == 0)) {
			page = "disppage";
		}
		String squery = "";
		HashMap<String, String> queryMap = new HashMap<String, String>();
		ArrayList<String> alreadyTreatedKey = new ArrayList<String>();
		
		for (Iterator<String> itr = ((SearchForm)form).getFormElements().keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			String[] values = ((SearchForm)form).getElement(key);
			
			if (values != null) {
				StringBuffer sb = new StringBuffer();
				sb.append("(");
				List<String> sList = new ArrayList<String>();
				
				
				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					if ((value != null) && (value.trim().length() > 0)) {
						sList.add(value);
					}
				}
				
				Pattern p = Pattern.compile("bio(\\d{1,2})|alt");
				Matcher m = p.matcher(key);
				
				/*a boolean indicating whether or not we already build the query about
				 * distribution data
				 */
				boolean distributionDone=false;
				
				/*If user is querying on environmental data*/
				if(m.matches())
				{
					/*If we are here data were already validated by the validate
					 * method of the searchForm
					 */
					/*
					 * The idea is to add to the Search Parameters
					 * Field:[000000 to 000200]
					 * for environmental data
					 * This part search on the accession index 
					 */
					
					String envFieldNumber = m.group(1);
					boolean decimalField = false;
					if(envFieldNumber!=null)
					{
						for(int j=1;j<3;j++)
						{
							decimalField = decimalField||envFieldNumber.matches(j+"");
						}
						for(int j=5;j<12;j++)
						{
							decimalField = decimalField||envFieldNumber.matches(j+"");
						}	
					}
					for(int i=0;i<sList.size();i++)
					{
						
						String lowerValue = sList.get(i);
						String upperValue = sList.get(i+1);
						i++;
						if(!decimalField)
						{
							lowerValue = AccessionConstants.encodeEnvVar(lowerValue);
							upperValue = AccessionConstants.encodeEnvVar(upperValue);
						}
						else
						{
							lowerValue= ""+(int)(Double.parseDouble(lowerValue)*10);
							lowerValue = AccessionConstants.encodeEnvVar(lowerValue);
							upperValue= ""+(int)(Double.parseDouble(upperValue)*10);
							upperValue = AccessionConstants.encodeEnvVar(upperValue);
						}
						sb.append(key+":["+lowerValue+" TO "+upperValue+"]");
						
						if (i != values.length - 1) {
							sb.append(" OR ");
						}
					}					
				}
				else if(key.startsWith("distribution")&&!distributionDone)
				{
					String[] countries= null;
					String[] dates = null;
					/*we fill the array with the value*/
					if(key.endsWith("Cty"))
					{
						countries=values;
						dates = ((SearchForm)form).getElement("distributionDates");
					}
					else if(key.endsWith("Date"))
					{
						dates=values;
						countries = ((SearchForm)form).getElement("distributionCty");
					}
					/*We build the query*/
					ArrayList<String> queryParts = new ArrayList<String>();
					if(countries!=null)
					{
						if(dates!=null)
						{
							for(int i=0;i<countries.length;i++)
							{
								for(int j=0;j<dates.length;j++)
								{
									sb.append("dist:["+countries[i]+dates[j]+" TO "+countries[i]+dates[j+1]);
									j++;
									if (j != dates.length - 1)
									{
										sb.append(" OR ");
									}
								}
								if (i != countries.length - 1)
								{
									sb.append(" OR ");
								}
							}
						}
					}
					
					
					
					/*We set the boolean saying that the query about distribution date was done to true*/
					distributionDone = true;
				}
				else
				{
					for (int i = 0; i < sList.size(); i++) {
						String value = sList.get(i);
						if (key.equals("year")) {
							sb.append("sdatekey" + AccessionConstants.SPLIT_KEY + value + "* OR edatekey" + AccessionConstants.SPLIT_KEY + value + "*");
						} else if (key.equals(AccessionConstants.CONTENTS)) {
							sb.append(value);
						}
						else if(key.equals(AccessionConstants.GENUSCODE+AccessionConstants.SPECIESCODE))
						{
								String[] valueSplit = sList.get(i).split("\\|");
								/*substring(4) to remove the gesp*/
								sb.append("("+AccessionConstants.GENUSCODE+AccessionConstants.SPLIT_KEY+"ge"+valueSplit[0].substring(4)+ " AND "+AccessionConstants.SPECIESCODE+AccessionConstants.SPLIT_KEY+"sp"+valueSplit[1]+")");
						}
						
						else {
							sb.append(key + AccessionConstants.SPLIT_KEY + value);
						}
						if (i != values.length - 1) {
							sb.append(" OR ");
						}
					}
				}
				sb.append(")");
				String sbQ = sb.toString();
				sbQ = AccessionConstants.replaceString(sbQ, "()", "", 0);
				if (sbQ.trim().length() > 0) {
					queryMap.put(key, sbQ);
				}
			}
			
		}

		StringBuffer sb = new StringBuffer();
		int size = queryMap.size();
		int pos = 0;
		for (Iterator<String> itr1 = queryMap.keySet().iterator(); itr1.hasNext();) {
			String key = itr1.next();
			sb.append(queryMap.get(key));
			if (pos != size - 1) {
				sb.append(" AND ");
			}
			pos++;
		}

		squery = "?page=" + page + "&search=" + sb.toString();
		//System.out.println("Final Query is :"+squery);
		return getNewActionForward(AccessionConstants.SUCCESS, squery);
	}
}
