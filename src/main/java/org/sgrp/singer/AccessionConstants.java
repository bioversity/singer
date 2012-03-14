package org.sgrp.singer;
/*
 * 
 * Comments by Gautier
 * 
 */
import java.io.StringReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

public class AccessionConstants {

	
	/*PID Constants*/
	public static final String  USER_LAST_NAME				= "node_data_field_profile_typeofprovider_field_profile_surname";
	public static final String  USER_FIRST_NAME				= "node_data_field_profile_typeofprovider_field_profile_name_va";
	public static final String  USER_PID					= "node_data_field_profile_typeofprovider_field_profile_pid_val";
	public static final String	USER_TYPE					= "node_data_field_profile_typeofprovider_field_profile_typeofp";
	public static final String  USER_ADDRESS				= "node_data_field_profile_typeofprovider_field_profile_address";
	public static final String  USER_POSTAL_CODE			= "node_data_field_profile_typeofprovider_field_profile_postalc";
	public static final String	USER_CITY					= "node_data_field_profile_typeofprovider_field_profile_city_va";
	public static final String  USER_COUNTRY				= "node_data_field_profile_typeofprovider_field_profile_country";
	public static final String  USER_ADDRESS_TO_USE			= "node_data_field_profile_typeofprovider_field_profile_select_";
	public static final String  USER_SHIPPING_ADDRESS		= "node_data_field_profile_typeofprovider_field_profile_shippin";
	public static final String  USER_SHIPPING_POSTAL_CODE	= "node_data_field_profile_typeofprovider_field_profile_shippin_1";
	public static final String	USER_SHIPPING_CITY			= "node_data_field_profile_typeofprovider_field_profile_shippin_2";
	public static final String  USER_MAIL					= "users_mail";
	
	public static final String	SQL_STATEMENTS				= "SQL_STATEMENTS";
	public static final String	SYSUSERROOT					= "sysuserroot";
	public static final String	SYSADMIN					= "sysadmin";
	public static final String	ADMINISTRATOR				= "admin";
	public static final String	CODE						= "c";
	public static final String	NAME						= "n";
	public static final String	ACCESSION					= "ac";
	public static final String	ACCESSIONCODE				= ACCESSION + CODE;
	public static final String	ACTION						= "action";
	public static final String	ACTION_ADD					= "add";
	public static final String	ACTION_EDIT					= "edit";
	public static final String	ALL_GIS_MAP_COUNT			= "all_gis_map_count";
	public static final String	ALL_KEY_COUNT				= "all_key_count";
	public static final String	AUTHORIZATION				= "Authorization";
	public static final String	CACHE_ROOT					= "CACHE_ROOT";
	public static final String	TEMPLATES					= "TEMPLATES";
	public static final String	CHIEFEDITOR					= "chiefeditor";
	public static final String	COLLECTION					= "co";
	public static final String	COLLECTION_ORDER_EMAIL_ACC	= COLLECTION + "ea";
	public static final String	COLLECTIONCODE				= COLLECTION + CODE;
	public static final String	COLLECTIONNAME				= COLLECTION + NAME;
	public static final String	CONTENTS					= "all";
	public static final String	COUNTRY						= "cu";
	public static final String	COUNTRYCODE					= COUNTRY + CODE;
	public static final String	COUNTRYNAME					= COUNTRY + NAME;
	public static final String	DESCRIPTORS_LINK			= "DESCRIPTORS_LINK";
	public static final String	DEVSTAT						= "de";
	public static final String	DEVSTATCODE					= DEVSTAT + CODE;
	public static final String	DISTRIBUTION				= "dist";
	public static final String	ENVDATA_ROOT				= "ENVDATA_ROOT";
	public static final String	FAILURE						= "failure";
	public static final String	FT_INDEX_ROOT				= "FT_INDEX_ROOT";
	public static final String	MAIL_BLOCK_REDIRECT			= "MAIL_BLOCK_REDIRECT";
	public static final String	ORDER_MAIL_HOST				= "ORDER_MAIL_HOST";
	public static final String	ORDER_MAIL_PORT				= "ORDER_MAIL_PORT";
	public static final String	ORDER_MAIL_FROM				= "ORDER_MAIL_FROM";
	public static final String	ORDER_MAIL_CC				= "ORDER_MAIL_CC";
	public static final String	ORDER_MAIL_BCC				= "ORDER_MAIL_BCC";
	public static final String	FULL_NAME					= "name";
	public static final String	GENUS						= "ge";
	public static final String	GENUSCODE					= GENUS + CODE;
	public static final String	GENUSNAME					= GENUS + NAME;
	public static final String	GOOGLE_MAP_KEY				= "GOOGLE_MAP_KEY";
	public static final String	HITS						= "hits";
	public static final String	HOME						= "home";
	public static final String	ID							= "id";
	public static final String	INDEX_LANGUAGES				= "INDEX_LANGUAGES";
	public static final String	INSTITUTE					= "in";
	public static final String	INSTITUTECODE				= INSTITUTE + CODE;
	public static final String	INSTITUTENAME				= INSTITUTE + NAME;
	public static final String	JDBC_CONNECT_STRING			= "JDBC_CONNECT_STRING";
	public static final String	JDBC_DRIVER					= "JDBC_DRIVER";
	public static final String	JDBC_PASSWORD				= "JDBC_PASSWORD";
	public static final String	JDBC_USER					= "JDBC_USER";
	public static final String	KEYCOUNT					= "keycount";
	public static final String	KEYSEARCH					= "keysearch";
	public static final String	KEYWORDS					= "keywords";
	public static final String	LANGUAGES					= "LANGUAGES";
	public static final String	LOCK_POSTFIX				= "_lock";
	public static final String	LOWERNAME					= "l" + NAME;
	public static final String	MARKER						= "marker";
	public static final int		MAX_ENVVAR					= Integer.MAX_VALUE;
	public static final int		MAX_LATLNG					= Integer.MAX_VALUE;
	protected static int		MAX_RESULTS_STORE			= 0;
	protected static int		MAX_ITEMS_PER_ORDER			= 0;
	public static final String	MEMBER						= "member";
	public static final String	OUTPUT_IMAGES				= "OUTPUT_IMAGES";
	public static final String	PICTURE						= "pi";
	public static final String	PICTURECODE					= PICTURE + CODE;
	public static final String	PREV_LINK					= "pl";
	public static final String	REGION						= "re";
	public static final String	REGIONCODE					= REGION + CODE;
	public static final String	SOURCE						= "so";
	public static final String	SOURCECODE					= SOURCE + CODE;
	public static final String	SOURCENAME					= SOURCE + NAME;
	public static final String	SPECIES						= "sp";
	public static final String	SPECIESCODE					= SPECIES + CODE;
	public static final String	SPECIESNAME					= SPECIES + NAME;
	public static final String	SPLIT_KEY					= "=";
	public static final String	STATUS						= "st";
	public static final String	STATUSCODE					= STATUS + CODE;
	public static final String	STATUSNAME					= STATUS + NAME;
	public static final String	STORE_RESULTS_ABOVE			= "STORE_RESULTS_ABOVE";
	public static final String 	ITEMS_PER_ORDER				= "ITEMS_PER_ORDER";
	public static final String	SEARCHHISTORY				= "searchHistory";
	public static final String	SUCCESS						= "success";
	public static final String	SYSUSER						= "sysuser";
	public static final String	SYSUSERID					= "sysuserid";
	public static final String	SYSUSERNAME					= "sysusername";
	public static final String	TAXON						= "ta";
	public static final String	TAXON_LINKS					= TAXON + "l";
	public static final String	TAXONCODE					= TAXON + CODE;
	public static final String	TAXONNAME					= TAXON + NAME;
	public static final String	TRUST						= "tr";
	public static final String	TRUSTCODE					= TRUST + CODE;
	public static final String	TRUSTNAME					= TRUST + NAME;
	public static final String	USER						= "us";
	public static final String	USERCODE					= USER + CODE;
	public static final	String	USER_DATA_ROOT				="USER_DATA_ROOT";
	public static final String	USERNAME					= USER + NAME;
	public static final String	WEB_ROOT					= "WEB_ROOT";
	public static final String	ENC_STR						= "UTF-8";

	public static String encodeURL(String url) {

		String eStr = url;
		try {
			eStr = URLEncoder.encode(eStr, ENC_STR);
		} catch (Exception e) {
			eStr = url;
			// TODO: handle exception
		}
		return eStr;
	}

	public static String decodeURL(String url) {
		String dStr = url;
		try {
			dStr = URLDecoder.decode(dStr, ENC_STR);
		} catch (Exception e) {
			dStr = url;
			// TODO: handle exception
		}
		return dStr;
	}

	public static String decodeString(String value) {
		String decodeStr = null;
		if (value != null && value.trim().length() > 0) {
			decodeStr = replaceString(value, "&#39;", "'", 0);
			decodeStr = replaceString(decodeStr, "<br/>", "\n", 0);
		}
		// System.out.println("Value is :"+encodeStr);
		return decodeStr;
	}

	public static String encodeEnvVar(String value) {
		char NEGATIVE_PREFIX = '0';
		// NB: NEGATIVE_PREFIX must be < POSITIVE_PREFIX
		char POSITIVE_PREFIX = '1';

		int MAX_ALLOWED = 99999;
		int MIN_ALLOWED = -100000;
		String FORMAT = "000000";
		String encodedenvVar = null;
		if (value != null && value.trim().length() > 0) {
			int envVar = 0;
			try {
				envVar = Integer.parseInt(value);
				// System.out.println("Integer before conv ="+envVar);
			} catch (Exception e) {
				envVar = 0;
				// TODO: handle exception
			}
			if ((envVar < MIN_ALLOWED) || (envVar > MAX_ALLOWED)) {
				
				throw new IllegalArgumentException(envVar+" out of allowed range");
			}
			char prefix;
			if (envVar < 0) {
				prefix = NEGATIVE_PREFIX;
				envVar = MAX_ALLOWED - envVar +1;
				/* -envVar to have them sorted correctly from -100000 to -1=>0000000 to 0099999*/
				/* positive are in the range 0 to 99999 => 1000000 to 1099999*/
			} else {
				prefix = POSITIVE_PREFIX;
			}
			DecimalFormat fmt = new DecimalFormat(FORMAT);
			encodedenvVar = prefix + fmt.format(envVar);
		}
		return encodedenvVar;
	}

	public static String encodelatlng(String value) {
		String encodedlatlng = null;
		if (value != null && value.trim().length() > 0) {
			double d = 0;
			try {
				d = Double.parseDouble(value);
				// System.out.println("Double before conv ="+d);
			} catch (Exception e) {
				d = 0;
				// TODO: handle exception
			}
			if (d != 0) {
				d = MAX_LATLNG + d;
				// System.out.println("Double after conv ="+d);
				encodedlatlng = d + "";
				encodedlatlng = replaceString(encodedlatlng, ".", "", 0);
			}
		}
		return encodedlatlng;
	}

	public static String encodeString(String value) {
		String encodeStr = null;
		if (value != null && value.trim().length() > 0) {
			encodeStr = replaceString(value, "'", "&#39;", 0);
			encodeStr = replaceString(encodeStr, "\n", "<br/>", 0);
		}
		// System.out.println("Value is :"+encodeStr);
		return encodeStr;
	}

	public static String fromPointsToSearch(String nw, String ne, String se, String sw) {
		String squery = null;
		String north, south, east, west;

		nw = replaceString(nw, "(", "", 0);
		nw = replaceString(nw, ")", "", 0);
		se = replaceString(se, "(", "", 0);
		se = replaceString(se, ")", "", 0);

		north = encodelatlng(nw.substring(0, nw.indexOf(",")));
		west = encodelatlng(nw.substring(nw.indexOf(",") + 1));
		south = encodelatlng(se.substring(0, se.indexOf(",")));
		east = encodelatlng(se.substring(se.indexOf(",") + 1));

		String lat = "latitudedencoded:[" + south + " TO " + north + "]";
		String lng = "longitudedencoded:[" + west + " TO " + east + "]";

		squery = lat + " AND " + lng;
		return squery;

	}

	public static String[] getCollectionArray(String str) {
		return getCollectionArray(luceneStrToArray(str));
	}
	/**
	 * This method recover the collection number defined in the token, either directly
	 * or by recovering every collection of an institute
	 * 
	 * @param tokens
	 * @return An Array of String containing the collections number defined by the tokens
	 */
	public static String[] getCollectionArray(String[] tokens) {
		
		/*arrList will contains every study requested by the different tokens*/
		ArrayList<String> arrList = new ArrayList<String>();
		String[] colls = null;
		if (tokens != null) {
			/*For each tokens*/
			for (int i = 0; i < tokens.length; i++) {
				String token = tokens[i];
				/* If the token means collection = something */
				if (token.startsWith(AccessionConstants.COLLECTIONCODE + AccessionConstants.SPLIT_KEY)) {
					/* coll = the value of the collection requested*/
					String coll = token.substring((AccessionConstants.COLLECTIONCODE + AccessionConstants.SPLIT_KEY).length());
					/*if we did not notice a previous token querying this collection, we add it to arrList*/
					if (!arrList.contains(coll)) {
						arrList.add(coll);
					}
				}
				/*Else if the token means institute = something */
				else if (token.startsWith(AccessionConstants.INSTITUTECODE + AccessionConstants.SPLIT_KEY)) {
					/* inst = the value of the inst requested*/
					String instcode = token.substring((AccessionConstants.INSTITUTECODE + AccessionConstants.SPLIT_KEY).length());
					/*We recover all the collection of this institute */
					Map<String, String> collMap = SearchResults.getInstance().getInstituteCollections(instcode);
					/* We add all this collections to ArrList if they are not in it yet*/
					for (Iterator<String> iter = collMap.keySet().iterator(); iter.hasNext();) {
						String coll = iter.next();
						if (!arrList.contains(coll)) {
							arrList.add(coll);
						}

					}
				}
			}
		}
		
		/* ArrList is transformed in an array*/
		colls = new String[arrList.size()];
		colls = arrList.toArray(colls);
		return colls;
	}

	public static int getDefaultParameter(String value, int newvalue) {
		return isNull(value) ? newvalue : Integer.parseInt(value);
	}

	public static String getDefaultParameter(String value, String newvalue) {
		return isNull(value) ? newvalue : value;
	}

	public static int getMaxResultsStore() {
		try {
			MAX_RESULTS_STORE = Integer.parseInt(ResourceManager.getString(AccessionConstants.STORE_RESULTS_ABOVE));
		} catch (Exception e) {
			MAX_RESULTS_STORE = 5000;
		}
		return MAX_RESULTS_STORE;
	}
	
	public static int getMaxItemsPerOrder() {
		try {
			MAX_ITEMS_PER_ORDER = Integer.parseInt(ResourceManager.getString(AccessionConstants.ITEMS_PER_ORDER));
		} catch (Exception e) {
			MAX_ITEMS_PER_ORDER = 10;
		}
		return MAX_ITEMS_PER_ORDER;
	}

	public static String getPropertiesFile(HttpServlet servlet) {
		String propFile = servlet.getInitParameter("PROP_FILE");

		if (propFile == null) {
			propFile = "singer.properties";
		}

		return propFile;
	}

	public static String getRegExDecodeValue(String text, String name) {
		return Utils.b64decode(getRegExValueMap(text, name).get(name));
	}
	
	public static String getValue(Document doc, String field)
	{
		return getValueMap(doc,field).get(field);
	}

	public static String getRegExValue(String text, String name) {
		return getRegExValueMap(text, name).get(name);
	}

	public static Map<String, String> getRegExValueMap(String text, String search) {
		if (search.equals("*")) {
			search = ".*?";
		}
		Map<String, String> values = new HashMap<String, String>();
		if ((text != null) && (text.trim().length() > 0)) {
			String regEx = "'(" + search + ")" + SPLIT_KEY + "(.*?)'";
			// System.out.println("Regexp is "+regEx);
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(text);

			while (matcher.find()) {
				if (matcher.groupCount() >= 2) {
					values.put(matcher.group(1), decodeString(matcher.group(2)));
				}
			}
		}
		// System.out.println("Map is :"+values);
		return values;
	}
	
	public static Map<String, String> getValueMap(Document doc, String fields) {
		
		List<Fieldable> fieldsList = new ArrayList<Fieldable>();
		Map<String, String> values = new HashMap<String, String>();
		
		if (fields.equals("*")) {
			fieldsList =doc.getFields();
		}
		else
		{
			String[] split = fields.split("\\|");
			for(int i=0;i<split.length;i++)
			{
				fieldsList.add(doc.getFieldable(split[i]));
			}
		}
			
			
		if (doc != null) {

			for(int i=0;i<fieldsList.size();i++) {
				if(fieldsList.get(i)!=null)
				{
					values.put(fieldsList.get(i).name(), doc.get(fieldsList.get(i).name()));
				}
				
			}
		}
		// System.out.println("Map is :"+values);
		return values;

	}

	public static String getStrArrAsString(String values[], String del) {
		if (del == null) {
			del = ",";
		}
		StringBuffer sb = new StringBuffer();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				sb.append(values[i]);
				if ((values.length - 1) > i) {
					sb.append(del);
				}
			}
		}
		return sb.toString();
	}

	public static String[] getValuesAsStrArr(String value) {
		if (value != null) {
			value = replaceString(value, ",", ";", 0);
			StringTokenizer st = new StringTokenizer(value, ";", false);
			String[] arr = new String[st.countTokens()];
			int i = 0;
			while (st.hasMoreElements()) {
				arr[i] = (String) st.nextElement();
				i++;
			}
			return arr;
		} else {
			return null;
		}
	}

	public static boolean isNull(String value) {
		boolean isNull = true;
		if ((value != null) && (value.trim().length() > 0)) {
			isNull = false;
		}
		return isNull;
	}

	public static String[] luceneStrToArray(String str) {
		String[] tokens = null;
		/*Replacement of the characters +-()/~\ by a space which will be 
		 * our delimitation character for the tokenizer*/
		str = replaceString(str, "+", " ", 0);
		str = replaceString(str, "-", " ", 0);
		str = replaceString(str, "(", " ", 0);
		str = replaceString(str, ")", " ", 0);
		str = replaceString(str, "/", " ", 0);
		str = replaceString(str, "~", " ", 0);
		str = replaceString(str, "\"", " ", 0);

		StringTokenizer st = new StringTokenizer(str, " ");
		StringBuffer sb = new StringBuffer();

		/*For each Element of the Tokenizer*/
		while (st.hasMoreElements()) {
			String val = st.nextToken();
			/*If the token is not an operator*/
			if (!(val.equals("AND") || val.equals("OR") || val.equals("NOT"))) {
				/*if the token contains ":" (I don't know what is that for)*/
				if (val.indexOf(":") > -1) {
					sb.append("_");
					sb.append(val);
					sb.append(" ");
				} else {
					sb.append(val);
					sb.append("_");
				}
			}
		}

		String newstring = sb.toString();
		st = new StringTokenizer(newstring, "_");
		tokens = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreElements()) {
			tokens[i] = st.nextToken();
			// System.out.println("****** ADDED TOKENS :"+tokens[i]);
			i++;
		}
		
		return tokens;

	}

	public static String makeEncodedFormattedString(String name, String value) {
		return "'" + name + SPLIT_KEY + Utils.b64encode(encodeString(value)) + "'";
	}

	public static String makeFormattedString(String name, String value) {
		return "'" + name + SPLIT_KEY + encodeString(value) + "'";
	}

	public static String makeProper(String theString) {
		StringBuffer properCase = new StringBuffer();
		try {
			if ((theString != null) && (theString.trim().length() > 0)) {
				StringReader in = new StringReader(theString.toLowerCase());
				boolean precededBySpace = true;

				while (true) {
					int i = in.read();
					if (i == -1) {
						break;
					}
					char c = (char) i;
					if ((c == ' ') || (c == '"') || (c == '(') || (c == '.') || (c == '/') || (c == '\\') || (c == ',')) {
						properCase.append(c);
						//precededBySpace = true;
					} else {
						if (precededBySpace) {
							properCase.append(Character.toUpperCase(c));
						} else {
							properCase.append(c);
						}
						precededBySpace = false;
					}
				}
			} else {
				properCase.append(theString);
			}
		} catch (Exception e) {

			return theString;
		}
		return properCase.toString();
	}

	public static String replaceString(String s, String pattern, String newPattern, int span) {
		String res = s;

		if ((s != null) && (s.length() > 0) && (pattern != null) && (pattern.length() > 0) && (newPattern != null) && (span >= 0)) {
			StringBuffer rs = new StringBuffer();
			int last = 0;
			int i = s.indexOf(pattern, 0);
			while (i != -1) {
				rs.append(s.substring(last, i) + newPattern);
				last = i + pattern.length() + span;
				i = s.indexOf(pattern, last);
			}
			rs.append(s.substring(Math.min(last, s.length())));
			res = rs.toString();
		}
		return res;
	}
	
	public static String[] getSortParametersAndImage(String field, String oldSort, ArrayList<String> sortField, boolean[] oldReverse)
	{
		boolean[] reverse = new boolean[oldReverse.length];
		for(int i=0;i<oldReverse.length;i++)
		{
			reverse[i]=oldReverse[i];
		}
		String[] sortParametersAndImage = new String[2];
		String sortParameters ="";
		String img="";
		if(oldSort==null)
		{
			oldSort="";
		}
		
		/*If we already sort by accession full name*/
		if(sortField.contains(field))
		{/*If the accession full name sort is reversed*/
			if(reverse[sortField.indexOf(field)])
			{
				sortParameters="sort=";
				/*then we remove the sorting from the sort Parameters*/
				if(!sortField.get(0).equals(field))
				{
					sortParameters+=sortField.get(0);
				}
				for(int i=1;i<sortField.size();i++)
				{							
					if(!sortField.get(i).equals(field))
					{
						sortParameters+=sortField.get(i);
					}
				}
				img="<img src='/img/arrow-up.jpg' />";
				/*We set the reverse to false be sure it doesn't appear*/
				reverse[sortField.indexOf(field)]=false;
			}
			else
			{	
				img="<img src='/img/arrow-down.jpg' />";
				sortParameters="sort="+oldSort;
				reverse[sortField.indexOf(field)]=true;
			}
		}
		else
		{
			if(oldSort==null||oldSort.trim().length()==0)
			{
				sortParameters = "sort="+field;
			}
			else
			{
				sortParameters = "sort="+oldSort+","+field;
			}
			
			
		}
		/*We write the reverse parameters*/
		for(int i=0;i<reverse.length;i++)
		{
			if(reverse[i])
			{
				sortParameters+="&rev"+i+"=true";
			}
		}
		sortParametersAndImage[0]=sortParameters;
		sortParametersAndImage[1]=img;
		
		return sortParametersAndImage;
	}

}
