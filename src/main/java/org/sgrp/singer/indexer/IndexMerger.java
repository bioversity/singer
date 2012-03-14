/**
 * 
 */
package org.sgrp.singer.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author kviparthi
 */
public class IndexMerger {
	public static void main(String args[]) throws Exception {
		String input = args[0];
		String output = args[1];
		IndexMerger iM = new IndexMerger(input, output);
		iM.mergeIndexes();
	}

	BaseIndexer		bIndexer	= BaseIndexer.getInstance();

	public String	inputLocs;

	public String	outputLoc;

	public IndexMerger(String inputLocs, String outputLoc) {
		this.inputLocs = inputLocs;
		this.outputLoc = outputLoc;
	}

	protected void mergeIndexes() throws Exception {
		List<File> ldirs = new ArrayList<File>();
		StringTokenizer st = new StringTokenizer(inputLocs, ";", false);

		while (st.hasMoreElements()) {
			File file = new File((String) st.nextElement());
			if (file.isDirectory()) {
				ldirs.add(file);
			}
		}
		BaseIndexer.recursivelyDeleteDirectory(new File(outputLoc));
		bIndexer.createDirIfNonExistant(new File(outputLoc));
		Directory[] dirs = new Directory[ldirs.size()];
		try {
			for (int i = 0; i < ldirs.size(); i++) {
				File file = ldirs.get(i);
				dirs[i] = FSDirectory.getDirectory(file);
			}
			IndexWriter writer = null;
			try {
				writer = new IndexWriter(outputLoc, bIndexer.getBaseStopAnalyzer().getInstance("en"), true);
				writer.setUseCompoundFile(true);
				writer.addIndexes(dirs);
				writer.optimize();
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		} catch (IOException e) {
			throw new Exception(e);
		}
	}
}
