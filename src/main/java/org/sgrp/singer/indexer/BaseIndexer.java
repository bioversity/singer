package org.sgrp.singer.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ParallelMultiSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.PriorityQueue;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.Main;
import org.sgrp.singer.ResourceManager;
import org.sgrp.singer.action.GenericAction;
import org.sgrp.singer.analyzer.BaseStopAnalyzer;

public class BaseIndexer {

	protected static BaseIndexer	mgr			= null;

	public final static String		SEPERATOR	= File.separator;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	public static void addField(Document doc, String name, String value) {
		addField(doc, name, value, false);
	}

	public static void addField(Document doc, String name, String value[]) {
		addField(doc, name, value, false);
	}

	public static void addField(Document doc, String name, String value, boolean isTermVector) {

		if (name.equals(ID)||name.equals("latlongd")||name.equals(AccessionConstants.TAXONNAME)||name.equals(AccessionConstants.COLLECTIONNAME)||name.equals("accenumbsort")) {
			doc.add(new Field(name, value == null ? "" : value, org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.NOT_ANALYZED_NO_NORMS));
		}
		else if(name.equals("keywords")||name.endsWith("encoded")
				||name.equalsIgnoreCase("dist")||name.equalsIgnoreCase("distdays"))
		{
			/*We index, but doesn't store the keywords field as we have the information in other fields*/
			doc.add(new Field(name, value == null ? "" : value, org.apache.lucene.document.Field.Store.NO, org.apache.lucene.document.Field.Index.ANALYZED_NO_NORMS));
		}
		else if (isTermVector) {
			// doc.add(new Field(name, value == null ? "" : value,
			// true,true,true, true));
			doc.add(new Field(name, value == null ? "" : value, org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.ANALYZED_NO_NORMS, org.apache.lucene.document.Field.TermVector.YES));
		} else {
			doc.add(new Field(name, value == null ? "" : value, org.apache.lucene.document.Field.Store.YES, org.apache.lucene.document.Field.Index.ANALYZED_NO_NORMS));
		}

	}

	public static void addField(Document doc, String name, String value[], boolean isTermVector) {
		if (value != null) {
			for (int i = 0; i < value.length; i++) {
				addField(doc, name, value[i] == null ? "" : value[i], isTermVector);
			}
		}
	}

	public static Map<String, ArrayList> getHighFreqTerms(Directory dir, Hashtable<String, String> junkWords, int numTerms, String[] fields, boolean loadData, boolean sortByFreq) throws Exception {
		if ((dir == null) || (fields == null)) { return null; }
		Map<String, ArrayList> resMap = new HashMap<String, ArrayList>();
		IndexReader reader = IndexReader.open(dir);
		TermEnum terms = reader.terms();
		List<String> fieldsList = null;

		if ((fields != null) && (fields.length > 0)) {
			fieldsList = Arrays.asList(fields);
		}
		while (terms.next()) {
			String field = terms.term().field();
			if (fieldsList != null && fieldsList.size() > 0) {
				if (!fieldsList.contains(field)) {
					continue;
				}
			}
			if ((junkWords != null) && (junkWords.get(terms.term().text()) != null)) {
				continue;
			}
			if (!resMap.containsKey(field)) {
				resMap.put(field, new ArrayList<CompTermInfo>());
			}
			ArrayList<CompTermInfo> cList = resMap.get(field);
			cList.add(new CompTermInfo(field, terms.term().text(), terms.docFreq(), loadData, sortByFreq));
			resMap.put(field, cList);
		}
		reader.close();
		return resMap;
	}

	public static Map<String, ArrayList> getHighFreqTerms(Directory dir, Hashtable<String, String> junkWords, String[] fields, boolean loadData, boolean sortByFreq) throws Exception {
		return getHighFreqTerms(dir, junkWords, 100, fields, loadData, sortByFreq);
	}

	public static Map<String, ArrayList> getHighFreqTerms(String fDir[], String field, boolean sortByFreq) throws Exception {
		String fields[] = new String[1];
		fields[0] = field;
		return getHighFreqTerms(fDir, fields, sortByFreq);
	}

	public static Map<String, ArrayList> getHighFreqTerms(String fDir, String field, boolean sortByFreq) throws Exception {
		String dirs[] = new String[1];
		dirs[0] = fDir;
		String fields[] = new String[1];
		fields[0] = field;
		return getHighFreqTerms(dirs, fields, sortByFreq);
	}

	public static Map<String, ArrayList> getHighFreqTerms(String fDir, String fields[], boolean sortByFreq) throws Exception {
		String dirs[] = new String[1];
		dirs[0] = fDir;
		return getHighFreqTerms(dirs, fields, sortByFreq);
	}

	public static Map<String, ArrayList> getHighFreqTerms(String fDir[], String fields[], boolean sortByFreq) throws Exception {
		Map<String, ArrayList> resTerm = new HashMap<String, ArrayList>();
		if (fDir.length > 1) {
			// ArrayList<Map<String, ArrayList<TermInfo>>> dList = new
			// ArrayList<Map<String, ArrayList<TermInfo>>>();
			boolean loadData = false;
			for (int i = 0; i < fDir.length; i++) {
				String cDir = fDir[i];
				Directory dir = FSDirectory.getDirectory(cDir);
				Map<String, ArrayList> cresTerm = getHighFreqTerms(dir, null, fields, false, sortByFreq);
				if (i == fDir.length - 1) {
					loadData = true;
				}
				mergeTerms(resTerm, cresTerm, loadData, sortByFreq);
			}

		} else {
			resTerm = getHighFreqTerms(FSDirectory.getDirectory(fDir[0]), null, fields, true, sortByFreq);
		}

		return resTerm;
	}

	public static BaseIndexer getInstance() {
		if (mgr == null) {
			mgr = new BaseIndexer();
		}
		return mgr;
	}

	public static String getLangAttribName(String lang, String attribName) {
		return mangleAttribName(lang + attribName);
	}

	public static File getRootedFile(String dir) {
		String rootDir = AccessionConstants.getDefaultParameter(ResourceManager.getString(AccessionConstants.FT_INDEX_ROOT), ".");
		File f = new File(rootDir, dir);
		return f;
	}

	public static String getSemaphoreName(String indexName) {
		return (indexName + AccessionConstants.LOCK_POSTFIX);
	}

	public static String getStringForFieldArray(Document doc, String name) {
		String values[] = doc.getValues(name);
		StringBuffer sb = new StringBuffer();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				sb.append(values[i]);
				if ((values.length - 1) > i) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}

	public static boolean isIndexing(String semaphoreName) {
		File f = getRootedFile(semaphoreName);
		return f.exists();
	}

	public static boolean isNull(String value) {
		return GenericAction.isNull(value);
	}

	public static String mangleAttribName(String attribName) {
		String value = AccessionConstants.replaceString(attribName, " ", "", 0);
		value = AccessionConstants.replaceString(value, "_", "", 0);
		value = AccessionConstants.replaceString(value, ":", "", 0);
		value = AccessionConstants.replaceString(value, "(", "", 0);
		value = AccessionConstants.replaceString(value, ")", "", 0);
		value = AccessionConstants.replaceString(value, ".", "", 0);
		value = AccessionConstants.replaceString(value, "/", "", 0);
		value = AccessionConstants.replaceString(value, ",", "", 0);
		return value;
	}

	public static String mangleConceptValue(String attribValue) {
		String value = attribValue;
		value = AccessionConstants.replaceString(value, "\"", "", 0);
		value = AccessionConstants.replaceString(value, "(", "", 0);
		value = AccessionConstants.replaceString(value, ")", "", 0);
		return value;

	}

	public static String mangleKeywordValue(String attribValue) {
		String value = mangleAttribName(attribValue);
		value = AccessionConstants.replaceString(value, "[", "", 0);
		value = AccessionConstants.replaceString(value, "]", "", 0);
		value = AccessionConstants.replaceString(value, "<", "", 0);
		value = AccessionConstants.replaceString(value, ">", "", 0);
		value = AccessionConstants.replaceString(value, "%", "", 0);
		value = AccessionConstants.replaceString(value, "&", "", 0);
		value = AccessionConstants.replaceString(value, "-", "", 0);
		value = AccessionConstants.replaceString(value, "#", "", 0);
		value = AccessionConstants.replaceString(value, " ", "", 0);
		value = AccessionConstants.replaceString(value, "\"", "", 0);
		return value;

	}

	public static void mergeTerms(Map<String, ArrayList> resTerm, Map<String, ArrayList> cresTerm, boolean loadData, boolean sortByFreq) throws Exception {
		for (Iterator<String> itr = cresTerm.keySet().iterator(); itr.hasNext();) {
			String field = itr.next();

			ArrayList<CompTermInfo> cTerms = cresTerm.get(field);
			if (!resTerm.containsKey(field)) {
				resTerm.put(field, cTerms);
			} else {
				ArrayList<CompTermInfo> rTerms = resTerm.get(field);
				resTerm.put(field, updateTerms(field, rTerms, cTerms, loadData, sortByFreq));
			}
		}
	}

	public static void recursivelyDeleteDirectory(File dir) throws IOException {
		if ((dir == null) || !dir.isDirectory() || !dir.exists()) { return; }
		boolean del = false;
		final File[] files = dir.listFiles();
		final int size = files.length;
		for (int i = 0; i < size; i++) {
			if (files[i].isDirectory()) {
				recursivelyDeleteDirectory(files[i]);
			} else {
				del = files[i].delete();
			}
		}
		del = dir.delete();
	}

	public static void setIndexingSemaphore(boolean running, String semaphoreName) {
		boolean dirs = false;
		File f = getRootedFile(semaphoreName);
		if (running) {
			dirs = f.mkdirs();
		} else {
			dirs = f.delete();
		}
	}

	public static ArrayList<CompTermInfo> updateTerms(String field, ArrayList<CompTermInfo> rTerms, ArrayList<CompTermInfo> cTerms, boolean loadData, boolean sortByFreq) {
		ArrayList<CompTermInfo> mTerms = null;
		HashMap<String, Integer> tMap = new HashMap<String, Integer>();
		for (int i = 0; i < rTerms.size(); i++) {
			String term = (rTerms.get(i)).getText();
			int freq = (rTerms.get(i)).getDocFreq();
			tMap.put(term, freq);
		}

		for (int j = 0; j < cTerms.size(); j++) {
			String term = (cTerms.get(j)).getText();
			int freq = (cTerms.get(j)).getDocFreq();
			if (tMap.containsKey(term)) {
				freq = freq + tMap.get(term);
			}
			tMap.remove(term);
			tMap.put(term, freq);
		}
		mTerms = new ArrayList<CompTermInfo>();
		for (Iterator<String> itr = tMap.keySet().iterator(); itr.hasNext();) {
			String text = itr.next();
			int cnt = tMap.get(text);
			mTerms.add(new CompTermInfo(field, text, cnt, loadData, sortByFreq));
		}
		return mTerms;

	}

	protected boolean		abortFlag			= false;

	public BaseStopAnalyzer	baseStopAnalyzer	= new BaseStopAnalyzer();

	public static final String			ID					= "id";

	protected String		INDEX_DIR			= "";

	public File				indexLoc;

	public String			LOWERNAME			= "lowername";

	public String			NAME				= AccessionConstants.FULL_NAME;

	public Searcher			searcher			= null;

	public String			SEMAPHORE_NAME		= getSemaphoreName();

	public BaseIndexer() {
	}

	protected boolean createDirIfNonExistant() throws IOException {
		return createDirIfNonExistant(indexLoc, true);
	}

	public boolean createDirIfNonExistant(File dir) throws IOException {
		return createDirIfNonExistant(dir, true);
	}

	public boolean createDirIfNonExistant(File dir, boolean isFSDirectory) throws IOException {
		boolean result = false;
		if (!dir.exists()) {
			result = dir.mkdirs();

			if (isFSDirectory) {
				IndexWriter writer = null;
				try {

					FSDirectory fsDir = FSDirectory.getDirectory(dir);
					writer = new IndexWriter(fsDir, baseStopAnalyzer.getInstance("en"), true);
					writer.setUseCompoundFile(true);
					LOG.info("Created directory " + dir);
				} finally {
					try {
						if (writer != null) {
							writer.close();
						}
					} catch (IOException e) {
						LOG.error("Exception caught in BaseIndexer.createDirIfNonExistant while closing writers");
						throw e;
					}
				}
			}
		}
		return result;
	}

	public void delete(String keyField, String keyValue, File file) throws IOException {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(file, baseStopAnalyzer.getInstance("en"), new MaxFieldLength(IndexWriter.DEFAULT_MAX_FIELD_LENGTH));
			delete(keyField, keyValue, writer);
			writer.close();
		} catch (Exception e) {
			LOG.error("",e);
		}

		finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void delete(String keyField, String keyValue, IndexWriter writer) throws IOException {
		Term t = new Term(keyField, keyValue.toLowerCase());
		try {
			System.setProperty("disableLuceneLocks", "true");
			writer.deleteDocuments(t);
		} catch (Exception e) {
			LOG.error("",e);
		}
	}

	public void delete(String keyField, String keyValue, String dirName) throws IOException {
		delete(keyField, keyValue, getRootedFile(dirName));
	}

	public synchronized void generateIndex() {
	}

	public boolean getAbortFlag() {
		return abortFlag;
	}

	public BaseStopAnalyzer getBaseStopAnalyzer() {
		return baseStopAnalyzer;
	}

	protected TopFieldDocs getHits(Searcher searcher, String field, String[] sortField, boolean[] reverse, String queryString) throws ParseException, IOException {
		
		if(sortField.length>reverse.length)
		{
			boolean[] fullReverse = new boolean[sortField.length];
			for(int i=0; i<reverse.length;i++)
			{
				fullReverse[i]=reverse[i];
			}
			for(int i=reverse.length;i<sortField.length;i++)
			{
				fullReverse[i] = false;
			}
			
			reverse = fullReverse;
		}
		
		QueryParser queryP = new QueryParser(field, baseStopAnalyzer.getInstance("en"));
		queryP.setAllowLeadingWildcard(true);
		BooleanQuery b_query = new BooleanQuery();
		Query query = queryP.parse(queryString.trim());
		//System.out.println("Query: "+queryString);
		b_query.add(query, Occur.MUST);
		BooleanQuery.setMaxClauseCount(b_query.getClauses().length * 100000);
		
		SortField[] sortFields = new SortField[sortField.length];
		
		for(int i=0; i< sortField.length;i++)
		{
			sortFields[i] = new SortField(sortField[i], SortField.STRING, reverse[i]);
		}
		TopFieldDocs topFieldDocs = null;
		
		Term term = new Term("accenumb","g1");
		Term term2 = new Term("accenumb","g2");
		//PrefixFilter filter = new PrefixFilter(term);
		TermQuery p_query = new TermQuery(term);
		TermQuery p_query2 = new TermQuery(term2);
		BooleanQuery b_queryFilter = new BooleanQuery();
		//TopFieldDocs docs = searcher.search(p_query, filter, searcher.maxDoc(),new Sort(sortFields));
		//Filter filter1 = new QueryWrapperFilter(p_query);
		//Filter filter2 = new QueryWrapperFilter(p_query2);
		b_queryFilter.add(p_query, Occur.SHOULD);
		b_queryFilter.add(p_query2, Occur.SHOULD);
		//b_query.add(b_queryFilter, Occur.MUST);
		Filter filter = new QueryWrapperFilter(b_queryFilter);
		//ObjectStore.storeObject("filter", "toto", filter);
		
		//Filter filter = (Filter)ObjectStore.getObject("filter", "toto");
		
		topFieldDocs =  searcher.search(b_query,null,searcher.maxDoc(),new Sort(sortFields));	
	
		
		
		return topFieldDocs;
		
	}

	@SuppressWarnings("unchecked")
	public List<IndexData> getIndexList() {
		List<IndexData> indexData = null;

		List<File> fileList = getSearchDirs();
		if (fileList.size() > 0) {
			indexData = new ArrayList<IndexData>();
			for (int i = 0; i < fileList.size(); i++) {
				File file = fileList.get(i);
				IndexData iData = new IndexData();
				iData.setName(file.getName());
				iData.setIndexLoc(file.getAbsolutePath());
				iData.setValid(isIndexValid(file.getAbsolutePath()));
				IndexReader reader = null;
				try {
					reader = getIndexReader(file);
					iData.setCompressed(reader.isOptimized());
					iData.setDocCount(reader.numDocs());
					reader.close();
				} catch (Exception e) {
					LOG.error("Found error :" , e);
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (Exception e) {
						}
					}
				}
				indexData.add(iData);
			}
			if (indexData.size() > 1) {
				java.util.Collections.sort(indexData);
				java.util.Collections.reverse(indexData);
			}
		}
		return indexData;

	}

	public IndexReader getIndexReader(File file) throws IOException {
		IndexReader indexReader = null;
		try {
			indexReader = IndexReader.open(file);
		} catch (IOException ie) {
			LOG.error("",ie);
		}
		return indexReader;
	}

	public IndexReader getIndexReader(String file) throws IOException {
		return getIndexReader(new File(file));
	}

	public IndexWriter getIndexWriter(File dir, boolean readOnly) throws IOException {
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(dir, baseStopAnalyzer.getInstance("en"), readOnly,IndexWriter.MaxFieldLength.LIMITED);
		} catch (IOException ie) {
			LOG.error("",ie);
		}
		return indexWriter;
	}

	public IndexWriter getIndexWriter(String dirName, boolean readOnly) throws IOException {
		return getIndexWriter(getRootedFile(dirName), readOnly);
	}

	public String getName(String id) {
		return getName(id, ID);
	}

	/**
	 * This method return the value in the NAME Field of the Document matching the ID
	 * @param id
	 * @param fieldName
	 * @return
	 */
	public String getName(String id, String fieldName) {
		String langLabel = null;
		try {
			Map<String, String> list = search(ID + ":" + BaseIndexer.mangleKeywordValue(id).toLowerCase(), fieldName, NAME, null, true);
			if (list.size() > 0) {
				langLabel = AccessionConstants.getRegExValue(list.get(id), NAME);
			}
		} catch (Exception e) {
			LOG.error("",e);
		}
		return langLabel;
	}

	public Map<String, String> getNamesAndIds(String queryString, String fieldName, String sortField) {
		Map<String, String> names = new LinkedHashMap<String, String>();
		try {
			Map<String, String> list = getTextAndIds(queryString, fieldName, sortField);
			for (Iterator<String> iter = list.keySet().iterator(); iter.hasNext();) {
				String id = iter.next();
				names.put(id,AccessionConstants.getRegExValue(list.get(id), NAME));
			}
		} catch (Exception e) {
			LOG.error("",e);
		}
		return names;
	}

	public List<File> getSearchDirs() {
		List<File> searchDirs = new ArrayList<File>();
		searchDirs.add(indexLoc);
		return searchDirs;
	}

	public Searcher[] getSearcherArray(String[] colls) throws IOException {
		Searcher[] sRet = new Searcher[1];
		FSDirectory index = FSDirectory.getDirectory(indexLoc);
		sRet[0] = new IndexSearcher(index);
		return sRet;
	}

	public String getSemaphoreName() {
		return getSemaphoreName(INDEX_DIR);
	}

	public Map<String, String> getTextAndIds(String queryString, String fieldName, String sortField) {
		Map<String, String> names = null;
		try {
			names = search(queryString, fieldName, sortField, null, true);
		} catch (Exception e) {
			LOG.error("",e);
		}
		return names;
	}

	public boolean isIndexExists() {
		return isIndexExists(indexLoc.getAbsolutePath());
	}

	public boolean isIndexExists(String indexLoc) {
		return IndexReader.indexExists(indexLoc);
	}

	public boolean isIndexValid() {
		return isIndexValid(indexLoc.getAbsolutePath());
	}

	public boolean isIndexValid(String indexLoc) {
		File f = new File(indexLoc);
		boolean valid = true;
		try {
			IndexReader reader = IndexReader.open(f);
			IndexReader.indexExists(f);
			reader.close();
		} catch (Exception e) {
			valid = false;
		}
		return (valid);
	}

	protected void optimizeLucene(IndexWriter writer) throws Exception {
		try {
			writer.optimize();
		} catch (IOException e) {
			throw new Exception(e);
		}
	}

	protected void optimizeLucene(String dirName) throws Exception {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(getRootedFile(dirName), baseStopAnalyzer.getInstance("en"), false);
			writer.setUseCompoundFile(true);
			optimizeLucene(writer);
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

	}
	
	public synchronized int searchCount(String queryString, String fieldName, String sortField, String[] colls)
	{
		LOG.info("Search Counts ->Field: "+fieldName+" queryString: "+queryString);
		int counts=0;
		String [] sortFields = new String[]{sortField};
		boolean[] reverse = new boolean[]{false};
		Searcher[] searchers_arr = null;
		/* Some fix added but need a better solution*/
		queryString = AccessionConstants.replaceString(queryString, "#", "?", 0);
		queryString = AccessionConstants.replaceString(queryString, ":-", ":\\-", 0);
		//System.out.println("query "+queryString);
		try {
			if (!isIndexExists() || !isIndexValid()) {
				generateIndex();
			}
			if (isIndexExists() && isIndexValid()) {
				searchers_arr = getSearcherArray(null);
				if ((searchers_arr != null) && (searchers_arr.length > 0)) {
					if(searcher==null)
					{
						searcher = new ParallelMultiSearcher(searchers_arr);
					}	
					TopFieldDocs hits = getHits(searcher, fieldName, sortFields, reverse, queryString);
					counts=hits.totalHits;
				}
			}
		}
		catch(Exception e)
		{
			LOG.error("",e);
		}
		finally
		{
			try {
				//searcher.close();
			} catch (Exception e) {
				LOG.error("",e);
				//searcher = null;
			}
		}
		
		return counts;				
	}

	public Map<String, String> search(String queryString, String fieldName, String sortField, String colls[], boolean addContents) throws Exception {
		return search(queryString, fieldName, sortField, colls, addContents, 0, -1);
	}
	
	public synchronized Map<String, String> search(String queryString, String fieldName, String[] sortField, String colls[], boolean addContents, int from, int to) throws Exception {
		
		boolean[] reverse = new boolean[sortField.length];
		
		for(int i=0;i<sortField.length;i++)
		{
			reverse[i]=false;
		}
		
		return search(queryString, fieldName, sortField, reverse, colls, addContents, from, to);
	}
	
	public synchronized Map<String, String> search(String queryString, String fieldName, String sortField, String colls[], boolean addContents, int from, int to) throws Exception {
		
		return search(queryString, fieldName, new String[]{sortField}, colls, addContents, from, to);
	}

	public synchronized Map<String, String> search(String queryString, String fieldName, String[] sortField, boolean[] reverse, String colls[], boolean addContents, int from, int to) throws Exception {
		//queryString = AccessionConstants.replaceString(queryString, AccessionConstants.SPLIT_KEY, ":", 0);
		Searcher[] searchers_arr = null;
		long start = Calendar.getInstance().getTimeInMillis();
		LOG.info("Field: "+fieldName+" queryString: "+queryString);
		/* Some fix added but need a better solution*/
		queryString = AccessionConstants.replaceString(queryString, "#", "?", 0);
		queryString = AccessionConstants.replaceString(queryString, ":-", ":\\-", 0);
		//System.out.println("query "+queryString);
		Map<String, String> results = new LinkedHashMap<String, String>();
		long startquery = 0;
		long endquery=0;
		try {
			if (!isIndexExists() || !isIndexValid()) {
				generateIndex();
			}
			if (isIndexExists() && isIndexValid()) {
				{
					searchers_arr = getSearcherArray(null);
					if ((searchers_arr != null) && (searchers_arr.length > 0)) {
						if(searcher==null)
						{
							searcher = new ParallelMultiSearcher(searchers_arr);
						}
													
						startquery=Calendar.getInstance().getTimeInMillis();
						TopFieldDocs hits = getHits(searcher, fieldName, sortField, reverse, queryString);
						endquery=Calendar.getInstance().getTimeInMillis();
						//System.out.println("To: "+to+" totalHits: "+hits.totalHits);
						if (to == -1) {
							to = hits.totalHits;
						} else if (to > hits.totalHits) {
							to = hits.totalHits;
						}
						if (from > hits.totalHits) {
							from = hits.totalHits - 1;
						}
						if (from > to) {
							from = to;
						}
						if (from<0)
						{
							from = 0;
						}
						
						//FieldSelector selector = new MapFieldSelector(new String[]{ID,AccessionConstants.CONTENTS});
						//System.out.println("length"+hits.scoreDocs.length);
						for (int i = from; i < to; i++) {
							String id = searcher.doc(hits.scoreDocs[i].doc).getField(ID).stringValue();
							//System.out.println(i+" "+searcher.doc(hits.scoreDocs[i].doc));
							
							if (!results.containsKey(id)) {
								results.put(id, addContents ? searcher.doc(hits.scoreDocs[i].doc).getField(AccessionConstants.CONTENTS).stringValue():"");
								//System.out.println(results.get(id));
							}
							
						}//System.out.println(Runtime.getRuntime().freeMemory());
					}
				}
			}
		} catch (Exception e) {
			LOG.error("",e);
		} /*finally {
			try {
				if (searcher != null) {
					//searcher.close();
				}
				for (int i = 0; (searchers_arr != null) && (i < searchers_arr.length); i++) {
					try {
						if (searchers_arr[i] != null) {
							//searchers_arr[i].close();
							//searchers_arr[i] = null;
						}
					} catch (Exception e) {
						e.printStackTrace(System.out);
					}
				}

			} catch (Exception e) {
			}
		}*/
		long end = Calendar.getInstance().getTimeInMillis();
		//searcher = null;
		/*System.out.println("Total "+(end-start));
		System.out.println("Pre requete: "+(startquery-start));
		System.out.println("Requete: "+(endquery-startquery));
		System.out.println("Post requete: "+(end-endquery));*/
		return results;
	}

	public void setAbortFlag(boolean abortFlag) {
		this.abortFlag = abortFlag;
	}

	public void setBaseStopAnalyzer(BaseStopAnalyzer baseStopAnalyzer) {
		this.baseStopAnalyzer = baseStopAnalyzer;
	}

	public void setIndexDir(String indexDir) {
		INDEX_DIR = indexDir;
		indexLoc = new File(ResourceManager.getString(AccessionConstants.FT_INDEX_ROOT), INDEX_DIR);
		SEMAPHORE_NAME = getSemaphoreName(INDEX_DIR);
	}

	public void writeToIndex(String dirName, Document doc, String language) throws IOException {
		IndexWriter writer = null;

		try {
			writer = new IndexWriter(getRootedFile(dirName), baseStopAnalyzer.getInstance(language), false);
			writer.setUseCompoundFile(true);
			writer.addDocument(doc);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}

final class TermInfoQueue extends PriorityQueue {
	TermInfoQueue(int size) {
		initialize(size);
	}

	@Override
	protected final boolean lessThan(Object a, Object b) {
		CompTermInfo termInfoA = (CompTermInfo) a;
		CompTermInfo termInfoB = (CompTermInfo) b;
		return termInfoA.docFreq < termInfoB.docFreq;
	}
}