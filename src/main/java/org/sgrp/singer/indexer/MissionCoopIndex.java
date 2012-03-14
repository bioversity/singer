package org.sgrp.singer.indexer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.Main;

public class MissionCoopIndex extends BaseIndexer implements IndexerInterface {

	protected static MissionCoopIndex	mgr	= null;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static MissionCoopIndex getInstance() {
		if (mgr == null) {
			mgr = new MissionCoopIndex();
		}
		return mgr;
	}
	
	public static void indexFromMain(Map<String, String> options)throws Exception
	{
		System.setProperty("disableLuceneLocks", "true");
		
		String sql = options.get("sql");
		
		boolean update = options.get("update") ==null? false : true;
		MissionCoopIndex mcIndex = new MissionCoopIndex();
		LOG.info("Sql here is :" + sql);
		mcIndex.indexData(sql, !update);
		
	}

	public static void main(String args[]) throws Exception {
		System.setProperty("disableLuceneLocks", "true");
		// String codes = args[0];
		String propFile = "C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\singer\\WEB-INF\\singer.properties";
		AccessionServlet.loadInitData(propFile);
		MissionCoopIndex aIndex = new MissionCoopIndex();
		String sql = "select * from misscoop";
		LOG.info("Sql here is :" + sql);
		aIndex.indexData(sql, true);
	}

	public String	fullSql		= "";

	public String	INDEX_DIR	= "misscoopindex";

	public String	sql			= "";

	public MissionCoopIndex() {
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
			indexMissionData();
		} catch (Exception e) {
			LOG.error("",e);
		}
	}

	public String getIndexName() {
		return "Mission Coop Link Index";
	}

	public int getRowCount(Connection con, String sql) {
		//String countSql = "select count(*) from misscoop";
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

	public String getShortName() {
		return "MissionCoop(s)";
	}

	public void indexData(String coopid) throws Exception {
		String cbStr = "where lower(code_)='" + coopid.toLowerCase() + "'";
		indexData(cbStr, false);
	}

	public synchronized void indexData(String sql, boolean fullrefresh) throws Exception {
		Connection conn = null;
		IndexWriter writer = null;
		try {
			//String normSql = "select * from misscoop";
			conn = AccessionServlet.getCP().newConnection(this.toString());
			/*
			 * if (fullrefresh) { recursivelyDeleteDirectory(new File(INDEX_DIR)); }
			 */
			setIndexingSemaphore(true, SEMAPHORE_NAME);
			if (!isIndexExists()) {
				createDirIfNonExistant();
			}
			try {

				writer = getIndexWriter(INDEX_DIR, false);
				writer.setUseCompoundFile(true);

				float count = getRowCount(conn, sql);
				LOG.info("Rec Count :" + count);
				int perPage = 5000;
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
					ResultSet rs = stmt.executeQuery(sql+ " LIMIT " + num + "," + perPage);

					while (rs.next()) {
						Map<String, String> map = new HashMap<String, String>();
						String misscode = rs.getString("misscode_").toLowerCase();
						String coopcode = rs.getString("coopcode_").toLowerCase();
						map.put(ID, mangleKeywordValue(misscode + coopcode));
						map.put(NAME, mangleKeywordValue(misscode + coopcode));
						map.put("misscode", mangleKeywordValue(misscode));
						map.put("coopcode", mangleKeywordValue(coopcode));
						map.put("repdate", rs.getDate("repdate") + "");
						try {
							indexMissionCoop(writer, map, fullrefresh);
						} catch (Exception e) {
							// e.printStackTrace();
						}

					}
					optimizeLucene(writer);
					// System.gc();
					rs.close();
					stmt.close();
				}
			} catch (Exception e) {
				LOG.error("",e);
			} finally {
				optimizeLucene(writer);
			}
			// mergeIndexes();
			setIndexingSemaphore(false, SEMAPHORE_NAME);
			conn.commit();
		} catch (SQLException se) {
			LOG.error("",se);
		} finally {
			AccessionServlet.getCP().freeConnection(conn);
			try {
				if (writer != null) {
					try {
						optimizeLucene(writer);
						writer.close();
					} catch (Exception e) {
						LOG.error("Exception for writer ",e);
					}
				}
			} catch (Exception e) {

			}

			// optimizeLucene(INDEX_DIR);
			setIndexingSemaphore(false, SEMAPHORE_NAME);
		}
	}

	public synchronized void indexMissionCoop(IndexWriter writer, Map<String, String> map, boolean fullrefresh) throws Exception {
		try {
			if (!fullrefresh) {
				LOG.info(" Writer name :" + writer.toString());
				delete(ID, map.get(ID), writer);
			}
			LOG.info("Indexing MissionCoop :" + map.get(ID));
			Document doc = new Document();
			StringBuffer sb = new StringBuffer();
			Iterator<String> itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				String value = map.get(key);
				// System.out.println("Out put is :"+map.toString());
				sb.append(AccessionConstants.makeFormattedString(key, value));
				// sb.append("["+key+":"+value+"]");
				if (key.equals(ID) || key.equals(NAME)) {
					addField(doc, key, value);
				}
			}
			// System.out.println("Output is :"+sb.toString());
			doc.add(new Field(AccessionConstants.CONTENTS, sb.toString(), org.apache.lucene.document.Field.Store.COMPRESS, org.apache.lucene.document.Field.Index.TOKENIZED));
			writer.addDocument(doc);

			map = null;
		} catch (Exception e) {
			LOG.error("Error doing indexing select in Coop " ,e);
		}
	}

	public void indexMissionData() throws Exception {
		if (!isIndexing(SEMAPHORE_NAME)) {
			if (isIndexExists() && isIndexValid()) {
				indexData(sql, false);
			} else {
				indexData(fullSql, true);
			}
		}
	}

}