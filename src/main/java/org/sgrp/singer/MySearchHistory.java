package org.sgrp.singer;

import java.util.LinkedHashMap;

public class MySearchHistory {

	public LinkedHashMap<String, String>	searchMap	= new LinkedHashMap<String, String>();

	public void addToHistory(String query, String queryName) {
		
		if(!inHistory(query))
		{
			searchMap.put(query, queryName);
		}
		else
		{
			removeFromHistory(query);
			searchMap.put(query, queryName);
		}
		
	}

	public LinkedHashMap<String, String> getHistoryMap() {
		return searchMap;
	}

	public boolean inHistory(String query) {
		// System.out.println("Call in cart with id :"+accId+"
		// "+cartMap.size());
		return searchMap.containsKey(query);
	}

	public void removeAll() {
		searchMap.clear();
	}

	public void removeFromHistory(String query) {
		searchMap.remove(query);
	}
	
	/*Added by Gautier*/
	/** This method save the MySearchHistory Object into a XML file.
	 * 
	 *  return a boolean indicating whether or not the object has been saved
	 */
	public boolean saveToFile(String filename)
	{
		return ObjectStore.storeObject(AccessionConstants.SEARCHHISTORY, filename, this);
	}
	
	/**
	 * This method allows to add the search history of the MySearchHistory object
	 * in parameter.
	 * 
	 * @param history
	 */
	public void load(MySearchHistory history)
	{
		searchMap.putAll(history.getHistoryMap());
	}
}
