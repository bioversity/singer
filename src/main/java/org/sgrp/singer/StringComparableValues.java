package org.sgrp.singer;

import org.sgrp.singer.indexer.BaseIndexer;

public class StringComparableValues implements Comparable<StringComparableValues> {
	public String	compValue	= null;

	public String	count		= null;

	public String	id			= null;
	public String	name		= null;

	public StringComparableValues(String id, String count, String name) {
		this.id = id;
		this.count = count;
		this.name = name;
		this.compValue = name;
		if (compValue == null || compValue.trim().length() == 0) {
			compValue = "null";
			this.name = "null";
		}
		this.compValue = BaseIndexer.mangleKeywordValue(compValue.toLowerCase());
	}

	public int compareTo(StringComparableValues obj) {
		return compValue.compareTo(obj.getCompValue().toString());
	}

	public Comparable<String> getCompValue() {
		return compValue;
	}

	public String getCount() {
		return count;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setCompValue(String compValue) {
		this.compValue = compValue;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public void setId(String id) {
		this.id = id;
	}

}