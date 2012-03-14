package org.sgrp.singer.indexer;

public class IndexData implements Comparable<IndexData> {
	public String	compValue	= null;

	public int		docCount;

	public String	indexLoc;

	public boolean	isCompressed;

	public boolean	isValid;

	public String	name;

	public String	zero		= "000000000000000";

	public int compareTo(IndexData obj) {
		// IndexData iData = (IndexData) obj;
		return compValue.compareTo(obj.getValue().toString());
	}

	public int getDocCount() {
		return docCount;
	}

	public String getIndexLoc() {
		return indexLoc;
	}

	public String getName() {
		return name;
	}

	public Comparable<String> getValue() {
		return compValue;
	}

	public boolean isCompressed() {
		return isCompressed;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setCompressed(boolean isCompressed) {
		this.isCompressed = isCompressed;
	}

	public void setDocCount(int docCount) {
		this.docCount = docCount;
		this.compValue = zero.substring(0, zero.length() - (docCount + "").length()) + docCount;
	}

	public void setIndexLoc(String indexLoc) {
		this.indexLoc = indexLoc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
