package org.sgrp.singer.indexer;

import org.apache.log4j.Logger;
import org.sgrp.singer.AccessionConstants;
import org.sgrp.singer.AccessionServlet;
import org.sgrp.singer.Main;

public class CompTermInfo implements Comparable<CompTermInfo> {
	public String	compValue	= null;
	int				docFreq;
	public String	field		= null;
	public boolean	loadForSort	= true;
	public String	name		= null;
	public boolean	sortByFreq	= true;
	public String	text		= null;
	
	private static Logger LOG = Logger.getLogger(Main.class);

	// Term term;

	public CompTermInfo(String field, String text, int df, boolean loadForSort) {
		setValues(field, text, df, loadForSort, true);
	}

	public CompTermInfo(String field, String text, int df, boolean loadForSort, boolean sortByFreq) {
		setValues(field, text, df, loadForSort, sortByFreq);
	}

	public int compareTo(CompTermInfo tInfo) {
		if (sortByFreq) {
			return docFreq < tInfo.getDocFreq() ? 0 : 1;
		} else {
			return compValue.compareTo(tInfo.getCompValue().toString());
		}
	}

	public Comparable<String> getCompValue() {
		return compValue;
	}

	public int getDocFreq() {
		return docFreq;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public boolean isSortByFreq() {
		return sortByFreq;
	}

	public void setCompValue(String compValue) {
		this.compValue = compValue;
	}

	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSortByFreq(boolean sortByFreq) {
		this.sortByFreq = sortByFreq;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setValues(String field, String text, int df, boolean loadForSort, boolean sortByFreq) {
		this.text = text;
		this.docFreq = df;
		this.field = field;
		this.loadForSort = loadForSort;
		this.sortByFreq = sortByFreq;
		if (loadForSort) {
			this.name = text.trim();
			this.compValue = name;
			try {
				if (field.equals("usercode")) {
					name = AccessionConstants.USER + name;
				} else if (field.equals("devstat")) {
					name = AccessionConstants.DEVSTAT + name;
				} else if (field.equals("region")) {
					name = AccessionConstants.REGION + name;
				} else if (field.equals("ctycode")) {
					name = AccessionConstants.COUNTRY + name;
				}
				if (!field.equals("year")) {
					name = AccessionServlet.getKeywords().getName(name);
					if (name == null || name.trim().equalsIgnoreCase("null")) {
						name = "Unspecified";
					}
				}
				this.compValue = name;
			} catch (Exception e) {
				LOG.error("",e);
				compValue = name;
			}
			this.compValue = BaseIndexer.mangleKeywordValue(compValue.toLowerCase());
		}
	}

}
