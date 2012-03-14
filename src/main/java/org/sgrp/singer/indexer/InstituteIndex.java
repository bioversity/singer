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

public class InstituteIndex extends BaseIndexer implements IndexerInterface {

	protected static InstituteIndex	mgr	= null;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static InstituteIndex getInstance() {
		if (mgr == null) {
			mgr = new InstituteIndex();
		}
		return mgr;
	}

	public static void indexFromMain(Map<String, String> options)throws Exception
	{
		System.setProperty("disableLuceneLocks", "true");
		
		String sql = options.get("sql");
		
		boolean update = options.get("update") ==null? false : true;
		InstituteIndex iIndex = new InstituteIndex();
		LOG.info("Sql here is :" + sql);
		iIndex.indexData(sql, !update);
		
	}	
	
	public static void main(String args[]) throws Exception {
		System.setProperty("disableLuceneLocks", "true");
		// String codes = args[0];
		String propFile = "C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\singer\\WEB-INF\\singer.properties";
		AccessionServlet.loadInitData(propFile);
		InstituteIndex iIndex = new InstituteIndex();
		//String sql = "select i.*,c.respers, c.address as caddress, c.email as cemail from inst i, (select instcode_ as code_, respers, address, email from col group by instcode_) c where i.code_=c.code_";
		String sql = "SELECT * FROM `SINGER_WAREHOUSE`.`index_inst`";
		LOG.info("Sql here is :" + sql);
		iIndex.indexData(sql, true);
	}

	public String	fullSql		= "";

	public String	INDEX_DIR	= "instindex";

	public String	sql			= "";

	public InstituteIndex() {
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
			indexInstituteData();
		} catch (Exception e) {
			LOG.error("",e);
		}
	}

	public String getIndexName() {
		return "Institute Index";
	}

	public String getShortName() {
		return "Institute(s)";
	}

	public void indexData(String instid) throws Exception {
		String cbStr = " and lower(code_)='" + instid.toLowerCase() + "'";
		indexData(cbStr, false);
	}

	public synchronized void indexData(String sql, boolean fullrefresh) throws Exception {
		Connection conn = null;
		IndexWriter writer = null;
		try {
			//String normSql = "select i.*,c.respers, c.address as caddress, c.email as cemail from inst i, (select instcode_ as code_, respers, address, email from col group by instcode_) c where i.code_=c.code_" + whereSql;
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

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);

				while (rs.next()) {
					Map<String, String> map = new HashMap<String, String>();
					String name = rs.getString("name");
					map.put(ID, mangleKeywordValue(AccessionConstants.INSTITUTE + rs.getString("code_").toLowerCase()));
					map.put(NAME, name);
					map.put("faocode", rs.getString("code"));
					map.put("fullname", rs.getString("fullname"));
					map.put("address", rs.getString("address"));
					map.put("phone", rs.getString("tel"));
					map.put("fax", rs.getString("fax"));
					map.put("email", rs.getString("email"));
					map.put("url", rs.getString("url"));
					map.put("logo", rs.getString("logo"));
					map.put("contact", rs.getString("respers"));
					map.put("caddress", rs.getString("caddress"));
					map.put("cemail", rs.getString("cemail"));

					map.put(AccessionConstants.COUNTRYCODE, mangleKeywordValue(AccessionConstants.COUNTRY + rs.getString("ctycode_")));
					map.put(AccessionConstants.COUNTRYNAME, Keywords.getInstance().getName(AccessionConstants.COUNTRY + rs.getString("ctycode_").toLowerCase()));
					try {
						indexInstitute(writer, map, fullrefresh);
					} catch (Exception e) {
						// e.printStackTrace();
					}

				}
				optimizeLucene(writer);
				// System.gc();
				rs.close();
				stmt.close();
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
						LOG.error("Exception for writer ", e);
					}
				}
			} catch (Exception e) {

			}

			// optimizeLucene(INDEX_DIR);
			setIndexingSemaphore(false, SEMAPHORE_NAME);
		}
	}

	public synchronized void indexInstitute(IndexWriter writer, Map<String, String> map, boolean fullrefresh) throws Exception {
		try {
			if (!fullrefresh) {
				LOG.info(" Writer name :" + writer.toString());
				delete(ID, map.get(ID), writer);
			}
			LOG.info("Indexing Institute :" + map.get(ID));
			try {
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
			} catch (Exception e) {
			}
			map = null;
		} catch (Exception e) {
			LOG.error("Error doing indexing in institute",e);
		}
	}

	public void indexInstituteData() throws Exception {
		if (!isIndexing(SEMAPHORE_NAME)) {
			if (isIndexExists() && isIndexValid()) {
				indexData(sql, false);
			} else {
				indexData(fullSql, true);
			}
		}
	}

}
