package org.sgrp.singer.indexer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.FSDirectory;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.Main;
import org.sgrp.singer.db.AscDataManager;
import org.sgrp.singer.form.GenericForm;

public class AccessionIndex extends BaseIndexer implements IndexerInterface {
	public static ArrayList<String>	envVar				= new ArrayList<String>();

	protected static int			MAX_ROWS_IN_SITTING	= 30;
	protected static AccessionIndex	mgr					= null;
	static {
		envVar.add("alt");
		envVar.add("bio1");
		envVar.add("bio2");
		envVar.add("bio3");
		envVar.add("bio4");
		envVar.add("bio5");
		envVar.add("bio6");
		envVar.add("bio7");
		envVar.add("bio8");
		envVar.add("bio9");
		envVar.add("bio10");
		envVar.add("bio11");
		envVar.add("bio12");
		envVar.add("bio13");
		envVar.add("bio14");
		envVar.add("bio15");
		envVar.add("bio16");
		envVar.add("bio17");
		envVar.add("bio18");
		envVar.add("bio19");
	}
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static AccessionIndex getInstance() {
		if (mgr == null) {
			mgr = new AccessionIndex();
		}
		return mgr;
	}
	
	public static void indexFromMain(Map<String, String> options)throws Exception
	{
		System.setProperty("disableLuceneLocks", "true");
		
		String sql = options.get("sql");
		
		boolean update = options.get("update") ==null? false : true;
		AccessionIndex aIndex = new AccessionIndex();
		LOG.info("Sql here is :" + sql);
		aIndex.indexData(sql, !update);
		
	}

	public static void main(String args[]) throws Exception {
		System.setProperty("disableLuceneLocks", "true");
		String codes = args[0];
		String propFile = "C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\singer\\WEB-INF\\singer.properties";
		AccessionServlet.loadInitData(propFile);
		AccessionIndex aIndex = new AccessionIndex();
		String sql = "select * from accdata";
		if (codes.indexOf("all") == -1) {
			sql += "where collcode_ in (" + codes + ")";
		}

		LOG.info("Sql here is :" + sql);
		aIndex.indexData(sql, true);
	}

	public List<String>					arrfieldsList	= new Vector<String>();

	public AscDataManager				ascManger		= AscDataManager.getInstance();

	public List<String>					datefieldList	= new Vector<String>();

	public String						fullSql			= "where collcode_ in ('14','2')";

	public String						INDEX_DIR		= "accindex";

	public HashMap<String, IndexWriter>	indexCollMap	= new HashMap<String, IndexWriter>();

	public List<File>					searchDirList	= new ArrayList<File>();

	public String						sql				= "where collcode_ in ('14','2')";

	public AccessionIndex() {
		super();
		setIndexDir(INDEX_DIR);
	}

	public void closeDown() {
		setIndexingSemaphore(false, SEMAPHORE_NAME);
		LOG.info(this.getClass().getName() + ".closeDown completed");
	}

	@Override
	public synchronized void generateIndex() {
		try {
			indexAccessionData();
		} catch (Exception e) {
			LOG.error("", e);
		}
	}

	public File getCollectionIndexFile(String collId) {
		File indexDir = getRootedFile(INDEX_DIR);
		File colIndexDir = new File(indexDir, collId);
		return colIndexDir;
	}

	public IndexWriter getCollectionWriter(String collId) {
		if (!indexCollMap.containsKey(collId)) {

			File colIndexDir = getCollectionIndexFile(collId);
			try {
				createDirIfNonExistant(colIndexDir);
				IndexWriter writer = getIndexWriter(colIndexDir, false);
				writer.setUseCompoundFile(true);
				indexCollMap.put(collId, writer);
			} catch (Exception e) {
				LOG.error("",e);
			}
		}
		return indexCollMap.get(collId);
	}

	public String getIndexName() {
		return "Accession Index";
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @return how many rows are returning by the sql query in param
	 */
	public int getRowCount(Connection con, String sql) {
		sql=sql.toLowerCase();
		int indexFirstFrom = sql.indexOf("from");
		sql=sql.substring(indexFirstFrom);
		sql = "select count(*) "+sql;
		int count = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			LOG.error("",e);
			// TODO: handle exception
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return count;
	}

	@Override
	public List<File> getSearchDirs() {
		if ((searchDirList == null) || (searchDirList.size() == 0)) {
			File rootFile = getRootedFile(INDEX_DIR);
			// System.out.println("Rooted file here in acc is
			// "+rootFile.getAbsolutePath());
			File[] files = rootFile.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory()) {
						// System.out.println("Adding col dir
						// :"+file.getAbsolutePath());
						searchDirList.add(file);
					}
				}
			}
		}
		return searchDirList;
	}

	public List<File> getSearchDirs(String[] dirs) {
		List<File> searchDirs = new ArrayList<File>();
		File rootFile = getRootedFile(INDEX_DIR);
		if (dirs != null) {
			for (int i = 0; i < dirs.length; i++) {
				File file = new File(rootFile, dirs[i]);

				if (file.exists() && file.isDirectory()) {
					searchDirs.add(file);
				}
			}
		}
		return searchDirs;
	}

	@Override
	public Searcher[] getSearcherArray(String[] colls) throws IOException {
		// System.out.println("In Search Array");

		if (searchDirList.size() == 0) {
			getSearchDirs();
		}

		List<File> listDirs = null;
		if ((colls != null) && (colls.length > 0)) {
			// System.out.println("Using collections specific "+colls);
			listDirs = getSearchDirs(colls);
		} else {
			listDirs = searchDirList;
		}
		Searcher[] sRet = null;

		if (listDirs.size() > 0) {
			sRet = new Searcher[listDirs.size()];
			for (int s = 0; s < listDirs.size(); s++) {
				File fio = listDirs.get(s);
				FSDirectory index = FSDirectory.getDirectory(fio);
				sRet[s] = new IndexSearcher(index);
				fio = null;
			}
		}
		listDirs = null;
		return sRet;
	}

	public String getShortName() {
		return "Accession(s)";
	}

	public synchronized void indexAccession(IndexWriter writer, Map<String, String> map, boolean fullrefresh) throws Exception {
		try {
			if (!fullrefresh) {
				LOG.info(" Writer name :" + writer.toString());
				delete(ID, map.get(ID), writer);
			}
			LOG.info("Indexing Accession :" + map.get(ID));
			try {
				Document doc = new Document();
				StringBuffer sb = new StringBuffer();
				Iterator<String> itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String key = itr.next();
					String value = map.get(key);
					
					/*We do not need to store in the all field informations of the field in this if*/
					if(!key.equalsIgnoreCase(AccessionConstants.KEYWORDS)&&!key.equalsIgnoreCase("accenumbsort")
							&&!key.equalsIgnoreCase("dist") && !key.equalsIgnoreCase("distdays"))
					{
						sb.append(AccessionConstants.makeFormattedString(key, value));
					}
					
					
					if (envVar.contains(key)) {
						// System.out.println("Env var :"+key+" = "+value);
						if (!value.equals("-9999")) {
							value = AccessionConstants.encodeEnvVar(value);
							// System.out.println("Env var :"+key+" = "+value);
							addField(doc, key, value);
						}
					}

					else {
						addField(doc, key, value);
					}
					/*This encoding allows to search in a range for the long and lat
					 * this is used for the Map search
					 */
					if ((key.equals("latituded") || key.equals("longituded"))&& value!=null && value.trim().length()>0 && !value.equals("0")) {
						value = AccessionConstants.encodelatlng(value);
						addField(doc, key+"encoded", value);
					}
					
				}
				doc.add(new Field(AccessionConstants.CONTENTS, sb.toString(), org.apache.lucene.document.Field.Store.COMPRESS, org.apache.lucene.document.Field.Index.NO));

				writer.addDocument(doc);
			} catch (NullPointerException e) {
				LOG.error("",e);
				//System.out.println("null ;)");
			}
			catch (Exception e)
			{
				LOG.error("",e);
			}
			map = null;
		} catch (Exception e) {
			LOG.error("Error doing indexing select in Accession " , e);
		}
	}

	public void indexAccessionData() throws Exception {
		if (!isIndexing(SEMAPHORE_NAME)) {
			if (isIndexExists() && isIndexValid()) {
				indexData(sql, false);
			} else {
				indexData(fullSql, true);
			}
		}
	}

	public void indexData(String cbid) throws Exception {
		String cbStr = " where lower(accenumb_)='" + cbid.toLowerCase() + "'";
		indexData(cbStr, false);
	}

	public synchronized void indexData(String sql, boolean fullrefresh) throws Exception {
		Connection conn = null;
		try {
			//String normSql ="select * from accdata";
			conn = AccessionServlet.getCP().newConnection(this.toString());
			setIndexingSemaphore(true, SEMAPHORE_NAME);
			if (!isIndexExists()) {
				createDirIfNonExistant(getRootedFile(INDEX_DIR), false);
			}
			try {

				float count = getRowCount(conn, sql);
				LOG.info("Rec Count :" + count);
				int perPage = 25000;
				if (count < perPage) {
					perPage = (int) count;
				}
				float dpages = (count / perPage);
				LOG.info("Pages are " + dpages);
				String pageStr = dpages + "";
				int pages = (int) dpages;
				if (pageStr.indexOf(".") > 0) {
					int num = Integer.parseInt(pageStr.substring(pageStr.indexOf(".") + 1));
					if (num > 0) {
						pages++;
					}
				}
				LOG.info("Total pages are " + pages);

				for (int i = 0; i < pages; i++) {
					int num = i * perPage;
					LOG.info("From record " + num + " of " + count);
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql+" LIMIT " + num + "," + perPage);

					while (rs.next()) {
						/* We get the date and destination country of the distribution for each accession*/
						/*String distSQL = "select * from transdata where accenumb_ = '"+rs.getString("accenumb_")+"'";
						Statement stmtDist = conn.createStatement();
						ResultSet rsDist = stmtDist.executeQuery(distSQL);
						String distDays = "";
						String dist = "";
						Data in the field distDays will be coded:
						 * YYYYMMDD It will be indexed, NoStored and analyzed
						 * This will allow simple Query and RangeQuery on it.
						 * 
						 * Date in theField dist will be coded:
						 * three letter country code+YYYYMMDD example: pak19720325
						 * 
						 * This will allow query on :
						 *  Country example: "dist:pak*"
						 *  Country+Date example: "dist:pak19720325"
						 *  Country+DateRange example: "dist:[pak19720325 TO pak19831231]
						 
						while(rsDist.next())
						{
							Timestamp transdate=null;
							try
							{
								transdate = rsDist.getTimestamp("dateTrans");
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							if(transdate!=null)
							{
								String dateStr = GenericForm.getlongAsDate(transdate.getTime(), "yyyyMMdd");
								distDays+=dateStr+" ";
								String distCty = AccessionConstants.COUNTRYCODE+rsDist.getString("ctycode_");
								dist+=distCty+dateStr+" ";
							}
							if there is no date for this distribution we just add the country
							else
							{
								String distCty = AccessionConstants.COUNTRYCODE+rsDist.getString("ctycode_");
								dist+=distCty+" ";
							}
							
						}
						rsDist.close();
						stmtDist.close();*/
						Map<String, String> map = new HashMap<String, String>();
						//map.put("distDays", distDays);
						//map.put("dist",dist);
						map.put(ID, mangleKeywordValue(rs.getString("accenumb_").toLowerCase()));
						map.put(NAME, mangleKeywordValue(rs.getString("accenumb_").toLowerCase()));
						map.put("origaccenumb", rs.getString("accenumb_").toLowerCase());
						map.put(AccessionConstants.ACCESSIONCODE, mangleKeywordValue(rs.getString("accenumb_").toLowerCase()));
						map.put(AccessionConstants.INSTITUTECODE, mangleKeywordValue(AccessionConstants.INSTITUTE + rs.getString("instcode_")));
						map.put("accenumb", rs.getString("accenumb"));
						map.put(AccessionConstants.INSTITUTENAME, rs.getString("instname"));
						map.put(AccessionConstants.COLLECTIONCODE, mangleKeywordValue(AccessionConstants.COLLECTION + rs.getString("collcode_")));
						map.put(AccessionConstants.COLLECTIONNAME, rs.getString("collname"));
						map.put(AccessionConstants.TAXONCODE, mangleKeywordValue(AccessionConstants.TAXON + rs.getString("taxcode_")));
						map.put(AccessionConstants.TAXONNAME, rs.getString("taxname"));
						map.put(AccessionConstants.GENUSCODE, mangleKeywordValue(AccessionConstants.GENUS + rs.getString("genus")));
						map.put(AccessionConstants.GENUSNAME, rs.getString("genus"));
						map.put(AccessionConstants.SPECIESCODE, mangleKeywordValue(AccessionConstants.SPECIES + rs.getString("species")));
						map.put(AccessionConstants.SPECIESNAME, rs.getString("species"));
						map.put(AccessionConstants.STATUSCODE, mangleKeywordValue(AccessionConstants.STATUS + rs.getString("statcode_")));
						map.put(AccessionConstants.STATUSNAME, rs.getString("statname"));
						map.put(AccessionConstants.SOURCECODE, mangleKeywordValue(AccessionConstants.SOURCE + rs.getString("srccode_")));
						map.put(AccessionConstants.SOURCENAME, rs.getString("srcname"));
						map.put(AccessionConstants.TRUSTCODE, mangleKeywordValue(AccessionConstants.TRUST + rs.getString("trustcode_").toLowerCase()));
						map.put(AccessionConstants.TRUSTNAME, rs.getString("trustname"));
						map.put(AccessionConstants.COUNTRYCODE, mangleKeywordValue(AccessionConstants.COUNTRY + rs.getString("origcode_").toLowerCase()));
						map.put(AccessionConstants.COUNTRYNAME, rs.getString("origname"));

                        map.put("misscode", mangleKeywordValue((rs.getString("misscode_") == null) ? "" : rs.getString("misscode_")).toLowerCase());
						map.put("donorcode", mangleKeywordValue((rs.getString("donorcode_") == null) ? "" : rs.getString("donorcode_")).toLowerCase());
						map.put("accename", rs.getString("accename"));

                        // Luca did this, need to build the date for each part of the date (year, month and day)
                        String acqDate = null;
                        if(rs.getString("acqdate") != null) {
                            String acq_year = rs.getString("acqdate_year"),
                                   acq_month = rs.getString("acqdate_month"),
                                   acq_day = rs.getString("acqdate_day");

                            acqDate = "";

                            if(acq_year != null && !acq_year.equals("0"))
                                acqDate += acq_year;

                            if(acq_month != null && !acq_month.equals("0"))
                                acqDate += "-" + acq_month;

                            if(acq_day != null && !acq_day.equals("0"))
                                acqDate += "-" + acq_day;
                        }

                        // do the same for colldate
                        String collDate = null;
                        if(rs.getString("colldate") != null) {
                            String coll_year = rs.getString("colldate_year"),
                                   coll_month = rs.getString("colldate_month"),
                                   coll_day = rs.getString("colldate_day");

                            collDate = "";

                            if(coll_year != null && !coll_year.equals("0"))
                                collDate += coll_year;

                            if(coll_month != null && !coll_month.equals("0"))
                                collDate += "-" + coll_month;

                            if(coll_day != null && !coll_day.equals("0"))
                                collDate += "-" + coll_day;
                        }
                        
                    
                        // now add the dates to the INDEX
						map.put("acqdate", acqDate);
						map.put("colldate", collDate);


						map.put("collnumb", rs.getString("collnumb"));
						map.put("latitude", rs.getString("latitude"));
						map.put("longitude", rs.getString("longitude"));
						map.put("latres", rs.getString("latres"));
						map.put("lonres", rs.getString("lonres"));
						map.put("elevation", rs.getString("elevation"));
						map.put("latituded", rs.getString("latituded"));
						map.put("longituded", rs.getString("longituded"));
						String lat = map.get("latituded");
						String lng = map.get("longituded");
						if ((lat != null) && !lat.equals("0") && (lat.trim().length() > 0) && (lng != null) && !lng.equals("0") && (lng.trim().length() > 0)) {
							map.put("latlongd", lat + "#" + lng);

                            // man dunno why this was causing some issues with indexing
                            /*
							Map<String, Integer[]> ascMap = ascManger.getAscDataLatLng(lat, lng, null);
							for (Iterator<String> iterator = ascMap.keySet().iterator(); iterator.hasNext();) {
								String code = iterator.next();
								Integer values[] = ascMap.get(code);
								map.put(code.toLowerCase(), values[0] + "");
							}
                            */

						}
						map.put("availability", rs.getString("availability"));
						map.put("insvalbard", rs.getString("insvalbard"));
						map.put("collsite", rs.getString("collsite"));
						map.put("othernumb", rs.getString("othernumb"));
						map.put("pedigree", rs.getString("pedigree"));
						map.put("parentfemale", rs.getString("parentfemale"));
						map.put("parentmale", rs.getString("parentmale"));
						
						/*Definition of keywords for the text Search*/
						String keywords=map.get("accenumb");
						keywords+=" "+map.get(AccessionConstants.INSTITUTENAME);
						keywords+=" "+map.get(AccessionConstants.COLLECTIONNAME);
						keywords+=" "+map.get(AccessionConstants.TAXONNAME);
						keywords+=" "+map.get(AccessionConstants.GENUSNAME);
						keywords+=" "+map.get(AccessionConstants.SPECIESNAME);
						keywords+=" "+map.get(AccessionConstants.STATUSNAME);
						keywords+=" "+map.get(AccessionConstants.SOURCENAME);
						keywords+=" "+map.get(AccessionConstants.TRUSTNAME);
						keywords+=" "+map.get(AccessionConstants.COUNTRYNAME);
						keywords+=" "+map.get("accename");
						keywords+=" "+map.get("collsite");
						keywords+=" "+map.get("othernumb");
						keywords+=" "+map.get("parentmale");
						keywords+=" "+map.get("parentfemale");
						
						map.put(AccessionConstants.KEYWORDS,keywords);
						
						/*Adding 0 at the beginning of the accenumb which are number
						 * in order to be able to sort them as String (Problems with
						 * the SortField.AUTO parameter, same exception, same stackTrace,
						 * not always caught)
						 */
						
						String accenumbsort = new String(map.get("accenumb"));
						/*If the accession number contains a number*/
						Pattern p = Pattern.compile("(\\D*)(\\d+\\.?\\d*)(\\D*)");
						Matcher m = p.matcher(accenumbsort);
						if(m.matches())
						{/*The length of the number will be 12 digits, so we add
						*12-(numberLength) 0 before the number
						*/
							String number=m.group(2);
							while(number.length()<12)
							{
								number="0"+number;
							}
							accenumbsort=m.group(1)+number+m.group(3);
						}
						
						map.put("accenumbsort", accenumbsort);
						
						try {
							indexAccession(getCollectionWriter(map.get(AccessionConstants.COLLECTIONCODE)), map, fullrefresh);
						} catch (Exception e) {
							LOG.error("",e);
						}
					}
					optimizeWriters();
					rs.close();
					stmt.close();
				}
			} catch (Exception e) {
				LOG.error("",e);
			} finally {
				optimizeWriters();
			}
			setIndexingSemaphore(false, SEMAPHORE_NAME);
			conn.commit();
		} catch (SQLException se) {
			LOG.error("",se);
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
			try {
				for (Iterator<String> itr = indexCollMap.keySet().iterator(); itr.hasNext();) {
					String collId = itr.next();
					IndexWriter writer = indexCollMap.get(collId);
					try {
						optimizeLucene(writer);
						writer.close();
					} catch (Exception e) {
						LOG.error("Exception for writer ",e);
					}
				}
				indexCollMap.clear();
			} catch (Exception e) {

			}
			setIndexingSemaphore(false, SEMAPHORE_NAME);
		}
	}

	@Override
	public boolean isIndexExists(String indexLoc) {
		File f = new File(indexLoc);
		boolean valid = true;
		File[] files = f.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					try {
						valid = IndexReader.indexExists(file);
						if (!valid) {
							break;
						}
					} catch (Exception e) {
						valid = false;
						break;
					}
				}
			}
		} else {
			valid = false;
		}
		return (valid);
	}

	@Override
	public boolean isIndexValid(String indexLoc) {
		File f = new File(indexLoc);
		boolean valid = true;
		File[] files = f.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isDirectory()) {
					try {
						IndexReader reader = IndexReader.open(file);
						IndexReader.indexExists(file);
						reader.close();
					} catch (Exception e) {
						valid = false;
						break;
					}

				}
			}
		} else {
			valid = false;
		}

		return (valid);
	}

	public void optimizeWriters() {
		try {
			for (Iterator<String> itr = indexCollMap.keySet().iterator(); itr.hasNext();) {
				String collId = itr.next();
				IndexWriter writer = indexCollMap.get(collId);
				try {
					LOG.info("Optimizing Writer :" + collId);
					optimizeLucene(writer);
					writer.close();
				} catch (Exception e) {
					LOG.error("Exception for writer optimizing " , e);
				}
			}
		} catch (Exception e) {

		}
		indexCollMap.clear();
	}
	
	/*Overwritten method to adapt to the new Index. 
	 * To Remove once all Index are constructed the same way.
	 * (non-Javadoc)
	 * @see org.sgrp.singer.indexer.BaseIndexer#search(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], boolean, int, int)
	 */
	public synchronized Map<String, String> search(String queryString, String fieldName, String[] sortField, boolean[] reverse, String colls[], boolean addContents, int from, int to) throws Exception 
	{
		queryString = AccessionConstants.replaceString(queryString, AccessionConstants.SPLIT_KEY, ":", 0);
		return super.search(queryString, fieldName, sortField, reverse, colls, addContents, from, to);
	}
	
	public synchronized Map<String, String> search(String queryString, String fieldName, String[] sortField, String colls[], boolean addContents, int from, int to) throws Exception 
	{
		queryString = AccessionConstants.replaceString(queryString, AccessionConstants.SPLIT_KEY, ":", 0);
		return super.search(queryString, fieldName, sortField, colls, addContents, from, to);
	}
	
	public synchronized Map<String, String> search(String queryString, String fieldName, String sortField, String colls[], boolean addContents, int from, int to) throws Exception 
	{
		queryString = AccessionConstants.replaceString(queryString, AccessionConstants.SPLIT_KEY, ":", 0);
		return super.search(queryString, fieldName, sortField, colls, addContents, from, to);
	}
	
	public Map<String, String> search(String queryString, String fieldName, String sortField, String colls[], boolean addContents) throws Exception {
		return search(queryString, fieldName, sortField, colls, addContents, 0, -1);
	}
	
	public int searchCount (String queryString, String fieldName, String sortField, String[] colls)
	{
		queryString = AccessionConstants.replaceString(queryString, AccessionConstants.SPLIT_KEY, ":", 0);
		return super.searchCount(queryString, fieldName, sortField, colls);
	}
}
