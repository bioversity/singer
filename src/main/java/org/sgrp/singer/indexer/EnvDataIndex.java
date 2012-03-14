package org.sgrp.singer.indexer;

import java.io.File;
import java.io.IOException;
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
import org.sgrp.singer.db.AscDataManager;

public class EnvDataIndex extends BaseIndexer implements IndexerInterface {
	protected static int			MAX_ROWS_IN_SITTING	= 30;

	protected static EnvDataIndex	mgr					= null;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static EnvDataIndex getInstance() {
		if (mgr == null) {
			mgr = new EnvDataIndex();
		}
		return mgr;
	}

	public static void main(String args[]) throws Exception {
		System.setProperty("disableLuceneLocks", "true");
		String code = args[0];
		String propFile = "C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\singer\\WEB-INF\\singer.properties";
		AccessionServlet.loadInitData(propFile);
		EnvDataIndex aIndex = new EnvDataIndex();
		String sql = "";
		if (code.indexOf("all") == -1) {
			sql = "co" + code;
		}

		LOG.info("Sql here is :" + sql);
		aIndex.indexData(sql, true);
	}

	public AccessionIndex				accIndex		= AccessionIndex.getInstance();

	public AscDataManager				ascManger		= AscDataManager.getInstance();

	public String						fullSql			= "";

	// public List<String> arrfieldsList = new Vector<String>();

	// public List<String> datefieldList = new Vector<String>();

	public String						INDEX_DIR		= "gisenvindex";

	public HashMap<String, IndexWriter>	indexAscMap		= new HashMap<String, IndexWriter>();

	public List<File>					searchDirList	= new ArrayList<File>();

	public String						sql				= "co69";

	public EnvDataIndex() {
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
			indexEnvData();
		} catch (Exception e) {
			LOG.error("",e);
		}
	}

	public File getCollectionIndexFile(String collId) {
		File indexDir = getRootedFile(INDEX_DIR);
		File colIndexDir = new File(indexDir, collId);
		return colIndexDir;
	}

	public IndexWriter getCollectionWriter(String collId) {
		if (!indexAscMap.containsKey(collId)) {
			File colIndexDir = getCollectionIndexFile(collId);
			try {
				createDirIfNonExistant(colIndexDir);
				IndexWriter writer = getIndexWriter(colIndexDir, false);
				writer.setUseCompoundFile(true);
				indexAscMap.put(collId, writer);
			} catch (Exception e) {
				LOG.error("",e);
			}
		}
		return indexAscMap.get(collId);
	}

	public String getIndexName() {
		return "GIS Data Env Index";
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
		// System.out.println("Rooted file here in acc is
		// "+rootFile.getAbsolutePath());
		if (dirs != null) {
			for (int i = 0; i < dirs.length; i++) {
				// System.out.println("Trying to check for dir "+dirs[i]);
				File file = new File(rootFile, dirs[i]);

				if (file.exists() && file.isDirectory()) {
					// System.out.println("Adding col specific
					// dir:"+file.getAbsolutePath());
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
		return "Asc File(s)";
	}

	public void indexData(String collId) throws Exception {
		indexData(collId, false);
	}

	public synchronized void indexData(String collcode, boolean fullrefresh) throws Exception {
		try {

			List<File> file = null;
			if (collcode != null && collcode.trim().length() > 0) {
				file = new ArrayList<File>();
				file.add(accIndex.getCollectionIndexFile(collcode));
			} else {
				file = accIndex.getSearchDirs();
			}

			setIndexingSemaphore(true, SEMAPHORE_NAME);
			if (!isIndexExists()) {
				createDirIfNonExistant(getRootedFile(INDEX_DIR), false);
			}
			try {

				for (int i = 0; i < file.size(); i++) {
					File currFile = file.get(i);
					LOG.info("Indexing file :" + currFile);
					try {
						IndexReader iReader = IndexReader.open(currFile);
						int count = iReader.numDocs();
						int pos = 0;
						for (int j = 0; j < count; j++) {
							Document doc = iReader.document(j);
							String id = doc.getField("id").stringValue();
							String text = doc.getField("all").stringValue();
							String col = AccessionConstants.getRegExValue(text, AccessionConstants.COLLECTIONCODE);
							String lat = AccessionConstants.getRegExValue(text, "latituded");
							String lng = AccessionConstants.getRegExValue(text, "longituded");
							if (lat != null && !lat.equals("0") && !lat.equals("null") && lat.trim().length() > 0 && lng != null && !lng.equals("0") && !lng.equals("null") && lng.trim().length() > 0) {
								// System.out.println(id+" "+lat+" "+lng);
								Map<String, String> map = new HashMap<String, String>();
								map.put(ID, mangleKeywordValue(id));
								map.put(NAME, mangleKeywordValue(id));
								map.put(AccessionConstants.COLLECTIONCODE, col);
								Map<String, Integer[]> ascMap = ascManger.getAscDataLatLng(lat, lng, null);
								// System.out.println(ascMap);
								for (Iterator<String> iterator = ascMap.keySet().iterator(); iterator.hasNext();) {
									String code = iterator.next();
									Integer values[] = ascMap.get(code);
									// System.out.println("Code ="+code+"
									// ="+values[0]);
									map.put(code.toLowerCase(), values[0] + "");
								}

								try {
									indexEnvData(getCollectionWriter(col), map, fullrefresh);
								} catch (Exception e) {
									LOG.error("",e);
								}
								pos++;
								if (pos % 200 == 0) {
									optimizeWriters();
								}
							}

						}
						iReader.close();
					} catch (Exception e) {
						LOG.error("",e);
					}
					optimizeWriters();
				}

				// optimizeWriters();
			} catch (Exception e) {
				LOG.error("",e);
			} finally {
				optimizeWriters();
			}
			// mergeIndexes();
			setIndexingSemaphore(false, SEMAPHORE_NAME);
		} finally {
			try {
				for (Iterator<String> itr = indexAscMap.keySet().iterator(); itr.hasNext();) {
					String collId = itr.next();
					IndexWriter writer = indexAscMap.get(collId);
					try {
						optimizeLucene(writer);
						writer.close();
					} catch (Exception e) {
						LOG.error("Exception for writer ", e);
					}
				}
				indexAscMap.clear();
			} catch (Exception e) {

			}

			// optimizeLucene(INDEX_DIR);
			setIndexingSemaphore(false, SEMAPHORE_NAME);
		}
	}

	public void indexEnvData() throws Exception {
		if (!isIndexing(SEMAPHORE_NAME)) {
			if (isIndexExists() && isIndexValid()) {
				indexData(sql, false);
			} else {
				indexData(fullSql, true);
			}
		}
	}

	public synchronized void indexEnvData(IndexWriter writer, Map<String, String> map, boolean fullrefresh) throws Exception {
		try {
			if (!fullrefresh) {
				LOG.info("Writer name :" + writer.toString());
				delete(ID, map.get(ID), getCollectionIndexFile(map.get(AccessionConstants.COLLECTIONCODE)));
			}
			LOG.info("Indexing EnvId for asc :" + map.get(ID));
			try {
				Document doc = new Document();
				StringBuffer sb = new StringBuffer();
				Iterator<String> itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String key = itr.next();
					String value = map.get(key);
					sb.append(AccessionConstants.makeFormattedString(key, value));
					// if (key.equals(ID) || key.equals(NAME)) {
					addField(doc, key, value);
					// }
				}
				doc.add(new Field(AccessionConstants.CONTENTS, sb.toString(), org.apache.lucene.document.Field.Store.COMPRESS, org.apache.lucene.document.Field.Index.TOKENIZED));
				writer.addDocument(doc);
			} catch (Exception e) {
			}
			map = null;
		} catch (Exception e) {
			LOG.error("Error doing indexing select in Asc " , e);
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
			for (Iterator<String> itr = indexAscMap.keySet().iterator(); itr.hasNext();) {
				String collId = itr.next();
				IndexWriter writer = indexAscMap.get(collId);
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
		indexAscMap.clear();
	}
}