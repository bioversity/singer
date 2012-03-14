package org.sgrp.singer.indexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.Main;

public class Keywords {

	private static Keywords	_instance	= null;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static Keywords getInstance() {
		if (_instance == null) {
			_instance = new Keywords();
		}
		return _instance;
	}

	public List<String>		invalidKeywords	= new Vector<String>();

	private KeywordIndex	keywordIndex	= new KeywordIndex();

	private Keywords() {
		invalidKeywords.add("latlongd");
		loadKeywords();
	}

	public Map<String, String> getAllColMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.COLLECTION);
	}
	public Map<String, String> getAllUserMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.USER);
	}

	public Map<String, String> getAllGenusMap() {
		return getNameIdMap("type:"+  AccessionConstants.GENUS);
	}
	
	public Map<String, String> getAllGenusSpeciesMap()
	{
		return getNameIdMap("type:"+  AccessionConstants.GENUS+AccessionConstants.SPECIES);
	}

	public Map<String, String> getAllInstMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.INSTITUTE);
	}

	public Map<String, String> getAllOrigMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.COUNTRY);
	}

	public Map<String, String> getAllPicturesMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.PICTURE);
	}

	public Map<String, String> getAllSpeciesMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.SPECIES);
	}

	public Map<String, String> getAllSrcMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.SOURCE);
	}

	public Map<String, String> getAllStatusMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.STATUS);
	}

	public Map<String, String> getAllTaxMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.TAXON);
	}

	public Map<String, String> getAllTrustMap() {
		return getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.TRUST);
	}

	public int getCount(String query) {
		return getNameIdMap(query).size();
	}

	public KeywordIndex getKeywordIndex() {
		return keywordIndex;
	}

	public String getName(String id) throws Exception {
		String label = null;
		if (id != null) {
			label = keywordIndex.getName(id);
		}
		return label;
	}

	public Map<String, String> getNameIdMap(String query) {
		Map<String, String> names = new LinkedHashMap<String, String>();
		if (keywordIndex != null) {
			names = keywordIndex.getNamesAndIds(query, AccessionConstants.CONTENTS, keywordIndex.LOWERNAME);
		}
		return names;
	}

	/**
	 * 
	 * If values is null, this method return the query as it was passed to it.
	 * 
	 * @param values
	 * @param query
	 * @return a String representing the query definition 
	 */
	public String getQueryAsText(String[] values, String query) {
		if (values != null) {
			ArrayList<String> myList = new ArrayList<String>(Arrays.asList(values));
			Collections.sort(myList);
			Collections.reverse(myList);

			for (int i = 0; i < myList.size(); i++) {
				String value = myList.get(i);
				try {
					String fieldName = null;
					String currValue = value;
					if (value.indexOf(AccessionConstants.SPLIT_KEY) > 0) {
						fieldName = value.substring(0, value.indexOf(AccessionConstants.SPLIT_KEY));
						currValue = value.substring(value.indexOf(AccessionConstants.SPLIT_KEY) + 1).toLowerCase();
						// System.out.println("Curr Value is "+currValue);
					}
					if (fieldName != null && fieldName.trim().length() > 0 && !invalidKeywords.contains(fieldName)) {
						String name = getName(currValue);
						if (name != null && name.trim().length() > 0) {
							query = AccessionConstants.replaceString(query, value, "<b>" + name + "</b>", 0);
						}
					}
				} catch (Exception e) {
					LOG.error("",e);				}
			}
		}
		return query;
	}

	public Map<String, String> getTextMap(String query) {
		Map<String, String> textMap = new LinkedHashMap<String, String>();
		if (keywordIndex != null) {
			textMap = keywordIndex.getTextAndIds(query, AccessionConstants.CONTENTS, keywordIndex.LOWERNAME);
		}
		return textMap;
	}

	public void loadKeywords() {
		try {
			if (keywordIndex.isIndexExists()) {
				LOG.info("Skipping Indexing as already found ..");
			} else {
				keywordIndex.indexKeywordData();
			}
			LOG.info("Done");

		} catch (Exception e) {
			LOG.error("",e);
		}
	}
}