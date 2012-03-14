package org.sgrp.singer.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.FieldChecks;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.ErrorConstants;
/**
 * 
 * @author gsarah
 *
 *
 *Common search form for the multiSelect and the queryMaker Search of SINGER
 */
@SuppressWarnings("serial")
public class SearchForm extends GenericForm {
	
	static HashMap<String,String> labelMap = new HashMap<String,String>();
	static{
	labelMap.put("me", "average temperature (deg C)");
	labelMap.put("min","monthly average of daily minimum temperatures (deg C)");
	labelMap.put("max","monthly average of daily maximum temperatures (deg C)");
	labelMap.put("pr", "precipitation (mm - liter/sqm)");
	labelMap.put("pet", "PET (mm - liter/sqm)");
	labelMap.put("sufr", "sunshine fraction (% of possible)");
	labelMap.put("windsp", "wind speed (m/s)");
	labelMap.put("vap", "water vapor pressure (Hpa)");

	labelMap.put("alt", "Altitude");
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
	}
	
	public SearchForm() {
		super();
	}
	
	
	private HashMap<String,String[]> formElements = new HashMap<String, String[]>();
	
	public HashMap<String,String[]> getFormElements()
	{
		return formElements;
	}
	/**
	 * This method allows us to recover the field element easily. It is
	 * most useful for the queryMaker Search as we don't know which field are present,
	 * we just have to parse every parameter of the query and call them by using this
	 * method
	 * 
	 * @param element
	 * @return String[] containing the field value corresponding of the element field
	 */
	public String[] getElement(String element)
	{
		return formElements.get(element);
	}
	
	private String[] all;
	
	public String [] getAll()
	{
		return all;
	}
	
	public void setAll(String[] all)
	{
		this.all = all;
		formElements.put(AccessionConstants.KEYWORDS, all);
	}
	
	private String [] coc;
	
	public String [] getCoc()
	{
		return coc;
	}
	
	public void setCoc(String[] coc)
	{
		this.coc = coc;
		formElements.put("coc", coc);
	}
	
	private String [] stc;
	
	public String [] getStc()
	{
		return stc;
	}
	
	public void setStc(String[] stc)
	{
		this.stc = stc;
		formElements.put("stc", stc);
	}
	
	private String [] soc;
	
	public String [] getSoc()
	{
		return soc;
	}
	
	public void setSoc(String[] soc)
	{
		this.soc = soc;
		formElements.put("soc", soc);
	}
	
	private String [] cuc;
	
	public String [] getCuc()
	{
		return cuc;
	}
	
	public void setCuc(String[] cuc)
	{
		this.cuc = cuc;
		formElements.put("cuc", cuc);
	}
	
	private String [] gecspc;
	
	public String [] getGecspc()
	{
		return gecspc;
	}
	
	public void setGecspc(String[] gecspc)
	{
		this.gecspc = gecspc;
		formElements.put("gecspc", gecspc);
	}
	
	private String [] trc;
	
	public String [] getTrc()
	{
		return trc;
	}
	
	public void setTrc(String[] trc)
	{
		this.trc = trc;
		formElements.put("trc", trc);
	}
	
	public String[] distributionCty;
	
	public void setDistributionCty(String[] distributionCty)
	{
		this.distributionCty = distributionCty;
		formElements.put("distributionCty", distributionCty);
	}
	
	public String[] getDistributionCty()
	{
		return distributionCty;
	}
	
	public String[] distributionDate;
	
	public void setDistributionDate(String[] distributionDate)
	{
		this.distributionDate = distributionDate;
		formElements.put("distributionDate", distributionDate);
	}
	
	public String[] getDistributionDate()
	{
		return distributionDate;
	}
	
	private String[] alt;
	
	public String [] getAlt()
	{
		return alt;
	}
	
	public void setAlt(String[] alt)
	{
		this.alt = alt;
		for(int i=0;i<alt.length;i++)
		{
			
			if(alt[i].equals(""))
			{
				if(i%2==1)
				{
					alt[i]="99999";
				}
				else
				{
					if(alt[i+1].equals(""))
					{
						i++;
					}
					else
					{
						alt[i]="-99999";
					}
					
				}
			}
		}
		formElements.put("alt", alt);
	}
	
	private String[] bio1;
	
	public String [] getBio1()
	{
		return bio1;
	}
	
	public void setBio1(String[] bio1)
	{
		this.bio1 = bio1;
		for(int i=0;i<bio1.length;i++)
		{
			if(bio1[i].equals(""))
			{
				if(i%2==1)
				{
					bio1[i]="9999";
				}
				else
				{
					if(bio1[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio1[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio1", bio1);
	}
	
private String[] bio2;
	
	public String [] getBio2()
	{
		return bio2;
	}
	
	public void setBio2(String[] bio2)
	{
		this.bio2 = bio2;
		for(int i=0;i<bio2.length;i++)
		{
			if(bio2[i].equals(""))
			{
				if(i%2==1)
				{
					bio2[i]="9999";
				}
				else
				{
					if(bio2[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio2[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio2", bio2);
	}
	
private String[] bio3;
	
	public String [] getBio3()
	{
		return bio3;
	}
	
	public void setBio3(String[] bio3)
	{
		this.bio3 = bio3;
		for(int i=0;i<bio3.length;i++)
		{
			if(bio3[i].equals(""))
			{
				if(i%2==1)
				{
					bio3[i]="99999";
				}
				else
				{
					if(bio3[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio3[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio3", bio3);
	}
	
private String[] bio4;
	
	public String [] getBio4()
	{
		return bio4;
	}
	
	public void setBio4(String[] bio4)
	{
		this.bio4 = bio4;
		for(int i=0;i<bio4.length;i++)
		{
			if(bio4[i].equals(""))
			{
				if(i%2==1)
				{
					bio4[i]="99999";
				}
				else
				{
					if(bio4[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio4[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio4", bio4);
	}
	
private String[] bio5;
	
	public String [] getBio5()
	{
		return bio5;
	}
	
	public void setBio5(String[] bio5)
	{
		this.bio5 = bio5;
		for(int i=0;i<bio5.length;i++)
		{
			if(bio5[i].equals(""))
			{
				if(i%2==1)
				{
					bio5[i]="9999";
				}
				else
				{
					if(bio5[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio5[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio5", bio5);
	}
	
private String[] bio6;
	
	public String [] getBio6()
	{
		return bio6;
	}
	
	public void setBio6(String[] bio6)
	{
		this.bio6 = bio6;
		for(int i=0;i<bio6.length;i++)
		{
			if(bio6[i].equals(""))
			{
				if(i%2==1)
				{
					bio6[i]="9999";
				}
				else
				{
					if(bio6[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio6[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio6", bio6);
	}
	
	private String[] bio7;
	
	public String [] getBio7()
	{
		return bio7;
	}
	
	public void setBio7(String[] bio7)
	{
		this.bio7 = bio7;
		for(int i=0;i<bio7.length;i++)
		{
			if(bio7[i].equals(""))
			{
				if(i%2==1)
				{
					bio7[i]="9999";
				}
				else
				{
					if(bio7[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio7[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio7", bio7);
	}
	
	private String[] bio8;
	
	public String [] getBio8()
	{
		return bio8;
	}
	
	public void setBio8(String[] bio8)
	{
		this.bio8 = bio8;
		for(int i=0;i<bio8.length;i++)
		{
			if(bio8[i].equals(""))
			{
				if(i%2==1)
				{
					bio8[i]="9999";
				}
				else
				{
					if(bio8[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio8[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio8", bio8);
	}
	
private String[] bio9;
	
	public String [] getBio9()
	{
		return bio9;
	}
	
	public void setBio9(String[] bio9)
	{
		this.bio9 = bio9;
		for(int i=0;i<bio9.length;i++)
		{
			if(bio9[i].equals(""))
			{
				if(i%2==1)
				{
					bio9[i]="9999";
				}
				else
				{
					if(bio9[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio9[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio9", bio9);
	}
	
	private String[] bio10;
	
	public String [] getBio10()
	{
		return bio10;
	}
	
	public void setBio10(String[] bio10)
	{
		this.bio10 = bio10;
		for(int i=0;i<bio10.length;i++)
		{
			if(bio10[i].equals(""))
			{
				if(i%2==1)
				{
					bio10[i]="9999";
				}
				else
				{
					if(bio10[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio10[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio10", bio10);
	}
	
	private String[] bio11;
	
	public String [] getBio11()
	{
		return bio11;
	}
	
	public void setBio11(String[] bio11)
	{
		this.bio11 = bio11;
		for(int i=0;i<bio11.length;i++)
		{
			if(bio11[i].equals(""))
			{
				if(i%2==1)
				{
					bio11[i]="9999";
				}
				else
				{
					if(bio11[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio11[i]="-9999";
					}
				}
			}
		}
		formElements.put("bio11", bio11);
	}
	
	private String[] bio12;
	
	public String [] getBio12()
	{
		return bio12;
	}
	
	public void setBio12(String[] bio12)
	{
		this.bio12 = bio12;
		for(int i=0;i<bio12.length;i++)
		{
			if(bio12[i].equals(""))
			{
				if(i%2==1)
				{
					bio12[i]="99999";
				}
				else
				{
					if(bio12[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio12[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio12", bio12);
	}
	
	private String[] bio13;
	
	public String [] getBio13()
	{
		return bio13;
	}
	
	public void setBio13(String[] bio13)
	{
		this.bio13 = bio13;
		for(int i=0;i<bio13.length;i++)
		{
			if(bio13[i].equals(""))
			{
				if(i%2==1)
				{
					bio13[i]="99999";
				}
				else
				{
					if(bio13[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio13[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio13", bio13);
	}
	
	private String[] bio14;
	
	public String [] getBio14()
	{
		return bio14;
	}
	
	public void setBio14(String[] bio14)
	{
		this.bio14 = bio14;
		for(int i=0;i<bio14.length;i++)
		{
			if(bio14[i].equals(""))
			{
				if(i%2==1)
				{
					bio14[i]="99999";
				}
				else
				{
					if(bio14[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio14[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio14", bio14);
	}
	
	private String[] bio15;
	
	public String [] getBio15()
	{
		return bio15;
	}
	
	public void setBio15(String[] bio15)
	{
		this.bio15 = bio15;
		for(int i=0;i<bio15.length;i++)
		{
			if(bio15[i].equals(""))
			{
				if(i%2==1)
				{
					bio15[i]="99999";
				}
				else
				{
					if(bio15[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio15[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio15", bio15);
	}
	
	private String[] bio16;
	
	public String [] getBio16()
	{
		return bio16;
	}
	
	public void setBio16(String[] bio16)
	{
		this.bio16 = bio16;
		for(int i=0;i<bio16.length;i++)
		{
			if(bio16[i].equals(""))
			{
				if(i%2==1)
				{
					bio16[i]="99999";
				}
				else
				{
					if(bio16[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio16[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio16", bio16);
	}
	
	private String[] bio17;
	
	public String [] getBio17()
	{
		return bio17;
	}
	
	public void setBio17(String[] bio17)
	{
		this.bio17 = bio17;
		for(int i=0;i<bio17.length;i++)
		{
			if(bio17[i].equals(""))
			{
				if(i%2==1)
				{
					bio17[i]="99999";
				}
				else
				{
					if(bio17[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio17[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio17", bio17);
	}
	
	private String[] bio18;
	
	public String [] getBio18()
	{
		return bio18;
	}
	
	public void setBio18(String[] bio18)
	{
		this.bio18 = bio18;
		for(int i=0;i<bio18.length;i++)
		{
			if(bio18[i].equals(""))
			{
				if(i%2==1)
				{
					bio18[i]="99999";
				}
				else
				{
					if(bio18[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio18[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio18", bio18);
	}
	
	private String[] bio19;
	
	public String [] getBio19()
	{
		return bio19;
	}
	
	public void setBio19(String[] bio19)
	{
		this.bio19 = bio19;
		for(int i=0;i<bio19.length;i++)
		{
			if(bio19[i].equals(""))
			{
				if(i%2==1)
				{
					bio19[i]="99999";
				}
				else
				{
					if(bio19[i+1].equals(""))
					{
						i++;
					}
					else
					{
						bio19[i]="-99999";
					}
				}
			}
		}
		formElements.put("bio19", bio19);
	}
	
	
	@Override
	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
		
		ActionErrors errors = new ActionErrors();
		
		Iterator<String> ite = formElements.keySet().iterator();
		while(ite.hasNext())
		{
			String key = ite.next();
			
			Pattern envPattern = Pattern.compile("(bio(\\d{1,2}))|alt");
			Matcher envM = envPattern.matcher(key);
			
			if(envM.matches())
			{
				ValidatorAction va = new ValidatorAction();
				va.setName("");
				Field field=new Field();
				
				field.addVar("max", "100000", null);
				String envFieldNumber = envM.group(2);
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
				
				String[] values = formElements.get(key);
				for(int i=0;i<values.length;i++)
				{
					if(!values.equals(""))
					{
						boolean fieldOk = false;
						if(i%2==1)
						{
							field.addVar("min", values[i-1], null);
						}
						else
						{
							field.addVar("min", "-99999", null);
						}
						if(decimalField)
						{
							fieldOk = FieldChecks.validateDoubleRange(values[i], va, field, errors, request);
							if(!fieldOk)
							{
								errors.add("notanumber", ErrorConstants.getActionMessage(ErrorConstants.ERROR_NOT_A_NUMBER, labelMap.get(key)));
							}
							
						}
						else
						{
							fieldOk = FieldChecks.validateIntRange(values[i], va, field, errors, request);
							if(!fieldOk)
							{
								errors.add("notaninteger", ErrorConstants.getActionMessage(ErrorConstants.ERROR_NOT_AN_INTEGER,labelMap.get(key)));
							}
						}
						
					}
				}				
			}
			if(key.equalsIgnoreCase("distributionDays"))
			{
				for(int i=0;i<formElements.get(key).length;i++)
				{
					if(formElements.get(key)[i].compareTo(formElements.get(key)[i+1])==1)
					{
						errors.add("inverseddates", ErrorConstants.getActionMessage(ErrorConstants.ERROR_INVERSED_DATES,labelMap.get(key)));
					}
				}
			}
		}
		
		return errors;
	}

}
