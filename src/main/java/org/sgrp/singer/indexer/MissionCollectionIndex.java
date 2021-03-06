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
import org.sgrp.singer.form.GenericForm;

public class MissionCollectionIndex extends BaseIndexer implements IndexerInterface {
	protected static MissionCollectionIndex	mgr	= null;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static MissionCollectionIndex getInstance() {
		if (mgr == null) {
			mgr = new MissionCollectionIndex();
		}
		return mgr;
	}

	public static void indexFromMain(Map<String, String> options)throws Exception
	{
		System.setProperty("disableLuceneLocks", "true");
		
		String sql = options.get("sql");
		
		boolean update = options.get("update") ==null? false : true;
		MissionCollectionIndex mcIndex = new MissionCollectionIndex();
		LOG.info("Sql here is :" + sql);
		mcIndex.indexData(sql, !update);
		
	}
	
	public static void main(String args[]) throws Exception {
		System.setProperty("disableLuceneLocks", "true");
		String codes = args[0];
		String propFile = "C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\singer\\WEB-INF\\singer.properties";
		AccessionServlet.loadInitData(propFile);
		MissionCollectionIndex dIndex = new MissionCollectionIndex();
		String sql = "select * from cmcollect";
		if (codes.indexOf("all") == -1) {
			sql += "where instcode_ in (" + codes + ")";
		}

		LOG.info("Sql here is :" + sql);
		dIndex.indexData(sql, true);
	}

	public String						fullSql			= "where instcode_ in ('14','2')";

	public String						INDEX_DIR		= "misscollindex";

	public HashMap<String, IndexWriter>	indexInstMap	= new HashMap<String, IndexWriter>();

	public List<File>					searchDirList	= new ArrayList<File>();

	public String						sql				= "where instcode_ in ('14','2')";

	public MissionCollectionIndex() {
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
			indexCMDistributionData();
		} catch (Exception e) {
			LOG.error("",e);
		}
	}

	public String getIndexName() {
		return "Mission Distribution Index";
	}

	public File getInstituteIndexFile(String instId) {
		File indexDir = getRootedFile(INDEX_DIR);
		File colIndexDir = new File(indexDir, instId);
		return colIndexDir;
	}

	public IndexWriter getInstituteWriter(String instId) {
		if (!indexInstMap.containsKey(instId)) {

			File instIndexDir = getInstituteIndexFile(instId);
			try {
				createDirIfNonExistant(instIndexDir);
				IndexWriter writer = getIndexWriter(instIndexDir, false);
				writer.setUseCompoundFile(true);
				indexInstMap.put(instId, writer);
			} catch (Exception e) {
				LOG.error("",e);
			}
		}
		return indexInstMap.get(instId);
	}

	public int getRowCount(Connection con, String sql) {
		//String countSql = "select count(*) from cmcollect";
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

	public File getSearchDir(String dir) {
		File rootFile = getRootedFile(INDEX_DIR);
		return new File(rootFile, dir);

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

		// System.out.println("Rooted file here in acc is
		// "+rootFile.getAbsolutePath());
		if (dirs != null) {
			for (int i = 0; i < dirs.length; i++) {
				// System.out.println("Trying to check for dir "+dirs[i]);
				File file = getSearchDir(dirs[i]);
				if (file.exists() && file.isDirectory()) {
					// System.out.println("Adding distribution col specific dir
					// :"+file.getAbsolutePath());
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
		return "Collecting Mission Collections(s)";
	}

	public void indexData(String cmid) throws Exception {
		String cbStr = "where lower(code_)='" + cmid.toLowerCase() + "'";
		indexData(cbStr, false);
	}

	public synchronized void indexData(String sql, boolean fullrefresh) throws Exception {
		Connection conn = null;
		try {
			//String normSql = "select * from cmcollect";
			conn = AccessionServlet.getCP().newConnection(this.toString());
			/*
			 * if (fullrefresh) { recursivelyDeleteDirectory(new File(INDEX_DIR)); }
			 */
			setIndexingSemaphore(true, SEMAPHORE_NAME);
			if (!isIndexExists()) {
				createDirIfNonExistant(getRootedFile(INDEX_DIR), false);
			}
			// HashMap<String, String> keyValues = new HashMap<String,
			// String>();
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
						Map<String, String> map = new HashMap<String, String>();
						String code = rs.getString("code_");
						String instcode = rs.getString("instcode_");
						String sp = rs.getString("sp");
						String id = code + sp;
						map.put(ID, mangleKeywordValue(id.toLowerCase()));
						map.put(NAME, mangleKeywordValue(id.toLowerCase()));
						map.put("misscode", mangleKeywordValue(code.toLowerCase()));
						map.put("spcode", mangleKeywordValue(sp));
						map.put("cn", rs.getString("cn"));
						map.put("sp", sp);
						map.put("samp", rs.getString("samp"));
						map.put(AccessionConstants.INSTITUTECODE, mangleKeywordValue(AccessionConstants.INSTITUTE + instcode));
						indexCMDistribution(getInstituteWriter(AccessionConstants.INSTITUTE + instcode), map, fullrefresh);
						/*
						 * count++; int per = fullrefresh ? 5000 : 20; if (count % per == 0) { optimizeWriters(); System.gc(); } if (count == 5000) { count = 0; }
						 */
					}
					optimizeWriters();
					// System.gc();
					// writeColMap(AccessionConstants.COLLECTION+collId,keyValues);
					rs.close();
					stmt.close();
				}
			} catch (Exception e) {
				LOG.error("",e);
			} finally {
				optimizeWriters();
			}
			// mergeIndexes();
			setIndexingSemaphore(false, SEMAPHORE_NAME);
			conn.commit();
		} catch (SQLException se) {
			LOG.error("",se);
		} catch (Exception e) {
			LOG.error("",e);
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
			try {
				for (Iterator<String> itr = indexInstMap.keySet().iterator(); itr.hasNext();) {
					String collId = itr.next();
					IndexWriter writer = indexInstMap.get(collId);
					try {
						optimizeLucene(writer);
						writer.close();
					} catch (Exception e) {
						LOG.error("Exception for writer ", e);
					}
				}
				indexInstMap.clear();
			} catch (Exception e) {

			}

			// optimizeLucene(INDEX_DIR);
			setIndexingSemaphore(false, SEMAPHORE_NAME);
		}
	}

	/*
	 * public Searcher[] getSearcherArray() throws IOException { Searcher[] sRet=new Searcher[1]; FSDirectory index = FSDirectory.getDirectory(getRootedFile(INDEX_DIR_FULL), false); sRet[0]=new
	 * IndexSearcher(index); return sRet; }
	 */

	public synchronized void indexCMDistribution(IndexWriter writer, Map<String, String> map, boolean fullrefresh) throws Exception {
		try {
			if (!fullrefresh) {
				LOG.info("Writer name :" + writer.toString());
				delete(ID, map.get(ID), writer);
			}
			LOG.info("Indexing Mission Collection:" + map.get(ID));
			Document doc = new Document();
			// doc.add(new Field("f", "a",
			// org.apache.lucene.document.Field.Store.YES,
			// org.apache.lucene.document.Field.Index.UN_TOKENIZED));
			StringBuffer sb = new StringBuffer();
			Iterator<String> itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				String value = map.get(key);
				sb.append(AccessionConstants.makeFormattedString(key, value));
				if (key.equals(ID) || key.equals(NAME)) {
					addField(doc, key, value);
				}
			}
			// System.out.println("Output is :"+sb.toString());
			doc.add(new Field(AccessionConstants.CONTENTS, sb.toString(), org.apache.lucene.document.Field.Store.COMPRESS, org.apache.lucene.document.Field.Index.ANALYZED));
			writer.addDocument(doc);
			map = null;
		} catch (Exception e) {
			LOG.error("Error doing indexing select in CM Distribution ",e);
		}
	}

	public void indexCMDistributionData() throws Exception {
		if (!isIndexing(SEMAPHORE_NAME)) {
			if (isIndexExists() && isIndexValid()) {
				indexData(sql, false);
			} else {
				indexData(fullSql, true);
			}
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
			for (Iterator<String> itr = indexInstMap.keySet().iterator(); itr.hasNext();) {
				String collId = itr.next();
				IndexWriter writer = indexInstMap.get(collId);
				try {
					LOG.info("Optimizing Writer :" + collId);
					optimizeLucene(writer);
					writer.close();
				} catch (Exception e) {
					LOG.error("Exception for writer optimizing ", e);
				}
			}
		} catch (Exception e) {

		}
		indexInstMap.clear();
	}
}