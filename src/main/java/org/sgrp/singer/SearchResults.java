package org.sgrp.singer;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.sgrp.singer.action.GenericAction;
import org.sgrp.singer.db.AccessionManager;
import org.sgrp.singer.db.AdditionalLinksManager;
import org.sgrp.singer.db.AscDataManager;
import org.sgrp.singer.db.CoopManager;
import org.sgrp.singer.db.DistributionManager;
import org.sgrp.singer.db.DonorManager;
import org.sgrp.singer.db.EnvDataManager;
import org.sgrp.singer.db.InstituteManager;
import org.sgrp.singer.db.MissionCollectionManager;
import org.sgrp.singer.db.MissionCoopManager;
import org.sgrp.singer.db.MissionDistributionManager;
import org.sgrp.singer.db.MissionManager;
import org.sgrp.singer.db.RecepientManager;
import org.sgrp.singer.form.AccessionForm;
import org.sgrp.singer.form.AdditionalLinksForm;
import org.sgrp.singer.form.CoopForm;
import org.sgrp.singer.form.DistributionForm;
import org.sgrp.singer.form.DonorForm;
import org.sgrp.singer.form.InstituteForm;
import org.sgrp.singer.form.MissionCollectionForm;
import org.sgrp.singer.form.MissionCoopForm;
import org.sgrp.singer.form.MissionDistributionForm;
import org.sgrp.singer.form.MissionForm;
import org.sgrp.singer.form.RecepientForm;
import org.sgrp.singer.indexer.Keywords;

public class SearchResults {

	protected static SearchResults	searchResults;

	public static SearchResults getInstance() {
		if (searchResults == null) {
			searchResults = new SearchResults();
		}
		return searchResults;
	}

	public static boolean isNull(String value) {
		return GenericAction.isNull(value);
	}

	/* Used for Searchs */

	public Map<String, DistributionForm> getAccDistributionMapBySearch(String acccode, String collcode) {
		try {
			// System.out.println("Query is "+acccode);
			return DistributionManager.getInstance().searchDistribution("accenumb" + AccessionConstants.SPLIT_KEY + acccode, collcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}

	public Map<String, MissionCollectionForm> getMissionCollectionMapBySearch(String misscode, String instcode) {
		try {
			// System.out.println("Query is "+acccode);
			return MissionCollectionManager.getInstance().searchMissionCollection("misscode" + AccessionConstants.SPLIT_KEY + misscode, instcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}

	public Map<String, MissionDistributionForm> getMissionDistributionMapBySearch(String misscode, String instcode) {
		try {
			// System.out.println("Query is "+acccode);
			return MissionDistributionManager.getInstance().searchMissionDistribution("misscode" + AccessionConstants.SPLIT_KEY + misscode, instcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}

	/* Data part */
	public AccessionForm getAccession(String id, String[] colls) {
		try {
			return AccessionManager.getInstance().getAccession(id, colls);
		} catch (Exception e) {

		}
		return null;
	}

	public int getAccessionCount(String query, String[] colls) {
		try {
			// System.out.println("Query is "+query);
			return AccessionManager.getInstance().getAccessionCount(query, colls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public HashMap getAccessionKeyCount(String query, String colls[]) {
		try {
			return AccessionManager.getInstance().getAccessionKeyCount(query, colls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public Map<String, AccessionForm> getAccessionMapBySearch(String query, String[] colls) {
		try {
			// System.out.println("Query is "+query);
			return AccessionManager.getInstance().searchAccession(query, colls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}

	public Map<String, String> getAccessionStringBySearch(String query, String[] colls) {
		try {
			Map<String, String> resultsMap = (Map<String, String>) ObjectStore.getObject("download", query);
			if (resultsMap == null) {
				resultsMap = AccessionManager.getInstance().searchAccessionString(query, true, colls);
				if (resultsMap != null && resultsMap.size() > AccessionConstants.getMaxResultsStore()) {
					ObjectStore.storeObject("download", query, resultsMap);
				}
			}
			// System.out.println("Query is "+query);
			return resultsMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}
	
	public Map<String, String> getAccessionStringBySearch(String query, String[] sortField, boolean[] reverse, String[] colls, int from, int to) {
		try {
			// System.out.println("Query is "+query);
			return AccessionManager.getInstance().searchAccessionString(query, true, sortField, reverse, colls, from, to);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}
	
	public Map<String, String> getAccessionStringBySearch(String query, String[] sortField, String[] colls, int from, int to) {
		try {
			// System.out.println("Query is "+query);
			return AccessionManager.getInstance().searchAccessionString(query, true, sortField, colls, from, to);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}
	
	public Map<String, String> getAccessionStringBySearch(String query, String sortField, String[] colls, int from, int to) {
		try {
			// System.out.println("Query is "+query);
			return AccessionManager.getInstance().searchAccessionString(query, true, sortField, colls, from, to);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}
	

	public Map<String, String> getAccessionStringBySearch(String query, String[] colls, int from, int to) {
		try {
			// System.out.println("Query is "+query);
			return AccessionManager.getInstance().searchAccessionString(query, true, colls, from, to);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}

	public Map<String, AdditionalLinksForm> getAdditionalLinksMapBySearch(String acccode, String collcode) {
		try {
			// System.out.println("Query is "+acccode);
			return AdditionalLinksManager.getInstance().searchAdditionalLinks("accenumb" + AccessionConstants.SPLIT_KEY + acccode, collcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}

	public Map<String, Integer[]> getAscDataLatLng(String lat, String lng) {
		try {
			return AscDataManager.getInstance().getAscDataLatLng(lat, lng, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Integer[]> getAscDataLatLng(String lat, String lng, boolean calcRadius) {
		try {
			return AscDataManager.getInstance().getAscDataLatLng(lat, lng, null, calcRadius);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getCleanedLatLng(String lat, String lng) {
		try {
			return AscDataManager.cleanLatLng(lat, lng);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public CoopForm getCoop(String id) {
		try {
			return CoopManager.getInstance().getCoop(id);
		} catch (Exception e) {

		}
		return null;
	}

	public Map<String, String> getCountryListByAlphabet(String alpha) {
		return getCountryListBySearch(" AND startswith" + AccessionConstants.SPLIT_KEY + alpha);
	}

	public Map<String, String> getCountryListBySearch(String query) {
		return Keywords.getInstance().getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.COUNTRY + query);
	}

	public DonorForm getDonor(String id) {
		try {
			return DonorManager.getInstance().getDonor(id);
		} catch (Exception e) {

		}
		return null;
	}

	public HashMap<String, Integer> getIntrustCollectionMap() {
		try {
			return AccessionManager.getInstance().getIntrustCollectionCount();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getAccEnvDataMapBySearch(String query, String[] colls) {
		try {
			// System.out.println("Query is "+query);
			return AccessionManager.getInstance().searchAccessionString(query, true, colls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getEnvDataMapBySearch(String query, String[] colls) {
		try {
			// System.out.println("Query is "+query);
			return EnvDataManager.getInstance().searchEnvDataString(query, false, colls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public Map<String, AccessionForm> getAcessionMapFromTo(String query, int pageNum, int pageSize) { try { return AccessionManager.getInstance().getAccessions(query, pageNum, pageSize); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); } return null; }
	 */

	public Map<String, String> getGenusListByAlphabet(String alpha) {
		return getGenusListBySearch(" AND startswith" + AccessionConstants.SPLIT_KEY + alpha);
	}

	public Map<String, String> getGenusListBySearch(String query) {
		return Keywords.getInstance().getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.GENUS + query);
	}

	public InstituteForm getInstitute(String id) {
		try {
			return InstituteManager.getInstance().getInstitute(id);
		} catch (Exception e) {

		}
		return null;
	}

	public Map<String, String> getInstituteCollections(String instcode) {
		if ((instcode != null) && instcode.startsWith(AccessionConstants.INSTITUTE)) {
			instcode = instcode.substring(AccessionConstants.INSTITUTE.length());
		}
		// System.out.println("Institute is "+instcode);
		return Keywords.getInstance().getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.COLLECTION + " AND parent" + AccessionConstants.SPLIT_KEY + instcode);
	}

	public Map<String, String> getKeywordListBySearch(String query) {
		return AccessionServlet.getKeywords().getNameIdMap(query);
	}

	public Map<String, MissionCoopForm> getMissCoopMapBySearch(String missCode) {
		try {
			return MissionCoopManager.getInstance().searchMissionCoop(missCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		// return AccessionServlet.getKeywords().getNameIdMap("type:build" +
		// query);
	}

	public MissionForm getMission(String id) {
		try {
			return MissionManager.getInstance().getMission(id);
		} catch (Exception e) {

		}
		return null;
	}

	public Map<String, MissionForm> getMissionMapBySearch(String query) {
		try {
			return MissionManager.getInstance().searchMission(query);
		} catch (Exception e) {

		}
		return null;
	}
	
	/*Added by Gautier to create a pagination of Collecting Missions*/
	public Map<String, MissionForm> getMissionMapBySearch(String query, int from, int to)
	{
		try {
			return MissionManager.getInstance().searchMission(query, from, to);
		} catch (Exception e) {

		}
		return null;
	}

	public Map<String, String> getPicturesListForGenusSpecies(String genus, String species) {
		return Keywords.getInstance().getTextMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.PICTURE + " AND parent" + AccessionConstants.SPLIT_KEY + genus.toLowerCase() + species.toLowerCase());
	}

	public RecepientForm getRecepient(String id) {
		try {
			return RecepientManager.getInstance().getRecepient(id);
		} catch (Exception e) {

		}
		return null;
	}

	/*
	 * public Map<String, String> getLinksForTaxon(String taxcode) { return Keywords.getInstance().getTextMap( "type" + AccessionConstants.SPLIT_KEY + AccessionConstants.TAXON_LINKS+ " AND parent" +
	 * AccessionConstants.SPLIT_KEY + taxcode); }
	 */

	public Map<String, String> getGenusSpeciesListBySearch (String query)
	{
		return Keywords.getInstance().getNameIdMap("type:"+AccessionConstants.GENUS+AccessionConstants.SPECIES+ query);
	}
	
	public Map<String, String> getSpeciesListByAlphabet(String alpha, String genus) {
		if ((genus != null) && genus.equals("*")) {
			return getSpeciesListBySearch(" AND startswith" + AccessionConstants.SPLIT_KEY + alpha);
		} else {
			return getSpeciesListBySearch(" AND parent" + AccessionConstants.SPLIT_KEY + genus + " AND startswith" + AccessionConstants.SPLIT_KEY + alpha);
		}
	}

	public Map<String, String> getSpeciesListBySearch(String query) {
		return Keywords.getInstance().getNameIdMap("type:" + AccessionConstants.SPECIES + query);
	}

	public Map<String, String> getTaxonListByAlphabet(String alpha) {
		return getTaxonListBySearch(" AND startswith" + AccessionConstants.SPLIT_KEY + alpha);
	}

	public Map<String, String> getTaxonListBySearch(String query) {
		return Keywords.getInstance().getNameIdMap("type" + AccessionConstants.SPLIT_KEY + AccessionConstants.TAXON + query);
	}

	/* search form */
	public Map<String, AccessionForm> searchAccession(String query, String[] colls) {
		try {
			return AccessionManager.getInstance().searchAccession(query, colls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
