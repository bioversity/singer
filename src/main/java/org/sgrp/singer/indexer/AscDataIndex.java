package org.sgrp.singer.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
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
import org.sgrp.singer.ResourceManager;

public class AscDataIndex extends BaseIndexer implements IndexerInterface {
	// protected static String currNum = "2.5m";
	protected static String			currNum				= "10m";

	protected static int			MAX_ROWS_IN_SITTING	= 30;
	protected static AscDataIndex	mgr					= null;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static AscDataIndex getInstance() {
		if (mgr == null) {
			mgr = new AscDataIndex();
		}
		return mgr;
	}

	public static void main(String args[]) throws Exception {
		System.setProperty("disableLuceneLocks", "true");
		String codes = args[0];
		String propFile = "C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\singer\\WEB-INF\\singer.properties";
		AccessionServlet.loadInitData(propFile);
		AscDataIndex aIndex = new AscDataIndex();
		String sql = "";
		if (codes.indexOf("all") == -1) {
			sql = codes + ".asc";
		}

		LOG.info("Sql here is :" + sql);
		aIndex.indexData(sql, true);
	}

	public String						fullSql			= "";

	// public List<String> arrfieldsList = new Vector<String>();

	// public List<String> datefieldList = new Vector<String>();

	public String						INDEX_DIR		= currNum + "ascworldclimindex";

	public HashMap<String, IndexWriter>	indexAscMap		= new HashMap<String, IndexWriter>();

	public List<File>					searchDirList	= new ArrayList<File>();

	public String						sql				= "bio_14.asc";

	public AscDataIndex() {
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
			indexAscData();
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
		return "Asc Index";
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

	public void indexAscData() throws Exception {
		if (!isIndexing(SEMAPHORE_NAME)) {
			if (isIndexExists() && isIndexValid()) {
				indexData(sql, false);
			} else {
				indexData(fullSql, true);
			}
		}
	}

	public synchronized void indexAscData(IndexWriter writer, Map<String, String> map, boolean fullrefresh) throws Exception {
		try {
			if (!fullrefresh) {
				LOG.info(" Writer name :" + writer.toString());
				delete(ID, map.get(ID), getCollectionIndexFile(map.get(AccessionConstants.COLLECTIONCODE)));
			}
			LOG.info("Indexing AscId :" + map.get(ID));
			try {
				Document doc = new Document();
				StringBuffer sb = new StringBuffer();
				Iterator<String> itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String key = itr.next();
					String value = map.get(key);
					sb.append(AccessionConstants.makeFormattedString(key, value));
					if (key.equals(ID) || key.equals(NAME) || key.equals("row")) {
						addField(doc, key, value);
					}
				}
				doc.add(new Field(AccessionConstants.CONTENTS, sb.toString(), org.apache.lucene.document.Field.Store.COMPRESS, org.apache.lucene.document.Field.Index.TOKENIZED));
				writer.addDocument(doc);
			} catch (Exception e) {
			}
			map = null;
		} catch (Exception e) {
			LOG.error("Error doing indexing select in Asc " ,e);
		}
	}

	public void indexData(String ascFile) throws Exception {
		indexData(ascFile, false);
	}

	public synchronized void indexData(String ascFile, boolean fullrefresh) throws Exception {
		try {

			String rootDir = AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.ENVDATA_ROOT), ".");
			File f = new File(rootDir, currNum + "ascworldclim");
			File file[] = f.listFiles();
			if (ascFile != null && ascFile.trim().length() > 0) {
				file = new File[1];
				file[0] = new File(f, ascFile);
			}
			setIndexingSemaphore(true, SEMAPHORE_NAME);
			if (!isIndexExists()) {
				createDirIfNonExistant(getRootedFile(INDEX_DIR), false);
			}
			try {

				for (int i = 0; i < file.length; i++) {
					File currFile = file[i];
					LOG.info("Indexing file :" + currFile);
					if (currFile.exists() && currFile.getName().endsWith(".asc")) {
						String line = ""; // line read from the file
						// String[] fields; // fields in the line
						// int startLine = 6; // number of line
						// int startLine = 145; // number of line
						String fileName = currFile.getName();
						fileName = fileName.substring(0, fileName.indexOf("."));
						fileName = mangleKeywordValue(fileName);
						LOG.info("File Name is " + fileName);
						// Opening of the file
						try {
							FileInputStream fis = new FileInputStream(currFile);
							InputStreamReader isr = new InputStreamReader(fis);
							LineNumberReader lnr = new LineNumberReader(isr);
							int currLine = 0;
							int row = -1;
							while (true) {
								line = lnr.readLine();
								// detection of EOF
								if (line == null) {
									break;
								}
								if (row == -1) {
									String fields[] = line.split("\\s+");
									if (fields.length > 10) {
										row = 0;
									}
								}
								if (row >= 0) {
									// System.out.println("Line is "+line);
									Map<String, String> map = new HashMap<String, String>();
									String id = fileName + row;
									map.put(ID, mangleKeywordValue(id));
									map.put(NAME, mangleKeywordValue(id));
									map.put("row", row + "");
									map.put("type", fileName);
									map.put("val", line);
									try {
										indexAscData(getCollectionWriter(map.get("type")), map, fullrefresh);
									} catch (Exception e) {
										LOG.error("",e);
									}
									// fields = line.split("\\s+");
									// vector.addElement(fields[col]);
									row++;
								}
								if (currLine % 500 == 0) {
									optimizeWriters();
								}
								currLine++;

							}
						} catch (Exception e) {
							LOG.error("",e);
						}
						optimizeWriters();
					}
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
						LOG.error("Exception for writer " , e);
					}
				}
				indexAscMap.clear();
			} catch (Exception e) {
				
			}

			// optimizeLucene(INDEX_DIR);
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
			for (Iterator<String> itr = indexAscMap.keySet().iterator(); itr.hasNext();) {
				String collId = itr.next();
				IndexWriter writer = indexAscMap.get(collId);
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
		indexAscMap.clear();
	}
}