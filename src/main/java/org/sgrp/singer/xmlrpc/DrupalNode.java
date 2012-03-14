package org.sgrp.singer.xmlrpc;

import java.util.HashMap;
import java.util.Map;

/**
 * A drupal Node.
 * It sets and gets all its variables as MAP entries.
 * @author sauermann
 *
 */
public class DrupalNode extends HashMap<String, Object> {
	
	/**
	 * types
	 */
	public static final String TYPE_STORY = "story";
	
	/**
	 * Properties of a Node.
	 * Taken from 
	 */
	public static String NID = "nid";
	public static String TYPE = "type";
	public static String LANGUAGE = "language";
	public static String UID = "uid";
	public static String STATUS = "status";
	public static String CREATED = "created";
	/**
	 * Set changed when saving existing nodes (=update)
	 */
	public static String CHANGED = "changed";
	public static String TITLE = "title";
	public static String BODY = "body";
/*	more for future
 * public static String NID = "comment";
	public static String NID = "promote";
	public static String NID = "moderate";
	public static String NID = "sticky";
	public static String NID = "tnid";
	public static String NID = "translate";
	public static String NID = "vid";
	public static String NID = "revision_uid";
	public static String NID = "teaser";
	public static String NID = "log";
	public static String NID = "revision_timestamp";
	public static String NID = "format";
	public static String NID = "name";
	public static String NID = "picture";
	public static String NID = "data";
	public static String NID = "rdf";
	public static String NID = "last_comment_timestamp";
	public static String NID = "last_comment_name";
	public static String NID = "comment_count";
	public static String NID = "taxonomy";
	public static String NID = "build_mode";
	public static String NID = "readmore";
	public static String NID = "content";
	*/

	public DrupalNode() {
		super();
	}

	public DrupalNode(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public DrupalNode(int initialCapacity) {
		super(initialCapacity);
	}

	public DrupalNode(Map m) {
		super(m);
	}
	
	public long getNid() {
		Object nid = get(NID);
		if (nid == null)
			throw new RuntimeException("nid not set (null)");
		if (nid instanceof String) {
			long result = Long.parseLong((String)nid);
			put(NID, result); // replace, for future
			return result;
		} else
			return (Long)nid;
	}
	
	public void setNid(long nid) {
		put(NID, nid);
	}
	
	public String getTitle() {
		Object o = get(TITLE);
		if (o==null)
			return null;
		else
			return o.toString();
	}
	
	public void setTitle(String o) {
		put(TITLE, o);
	}
	
	public String getBody() {
		Object o = get(BODY);
		if (o==null)
			return null;
		else
			return o.toString();
	}
	
	public void setBody(String o) {
		put(BODY, o);
	}

	public void setType(String o) {
		put(TYPE, o);
	}
	
	
	
}
