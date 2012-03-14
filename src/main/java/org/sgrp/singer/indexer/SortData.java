package org.sgrp.singer.indexer;

public class SortData implements Comparable<SortData> {
	public String	id		= null;
	public String	value	= null;

	public SortData(String id, String value) {
		this.id = id;
		this.value = value;
	}

	public int compareTo(SortData obj) {
		// SortData sortPair = (SortData) obj;
		return value.compareTo(obj.getValue().toString());
	}

	public Comparable<String> getValue() {
		return value;
	}
}