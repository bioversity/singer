package org.sgrp.singer;

public class ComparableValues implements Comparable<ComparableValues> {
	public static final String	zero		= "000000000000000";

	public String				compValue	= null;

	public String				count		= null;

	public String				uri			= null;

	public ComparableValues(String uri, String count) {
		this.uri = uri;
		this.count = count;
		this.compValue = zero.substring(0, zero.length() - count.length()) + count;
	}

	public int compareTo(ComparableValues obj) {
		// ComparableValues compareValuePair = (ComparableValues) obj;
		return compValue.compareTo(obj.getValue().toString());
	}

	public String getValue() {
		return compValue;
	}
}