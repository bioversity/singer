package org.sgrp.singer.analyzer;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;

/**
 * <p>
 * Title: BaseStopAnalyzer
 * </p>
 * <p>
 * Description: Keeps track of all StopAnalyzers
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * </p>
 * 
 * @author Kiran Viparthi
 * @version 1.0
 */
public class BaseStopAnalyzer {
	static {

	}
	public HashMap<String, String>		langMap			= null;

	public HashMap<String, Analyzer>	stopAnalyzerMap	= null;

	public BaseStopAnalyzer() {
		this("en");
	}

	public BaseStopAnalyzer(Hashtable langs) {
		loadDefaults();
		if (langs != null) {
			Enumeration en = langs.keys();
			while (en.hasMoreElements()) {
				String language = (String) en.nextElement();
				loadInstance(language);
			}
		}

	}

	public BaseStopAnalyzer(List<String> langs) {
		loadDefaults();
		if (langs != null) {
			for (int i = 0; i < langs.size(); i++) {
				String language = langs.get(i);
				loadInstance(language);
			}
		}
	}

	public BaseStopAnalyzer(String lang) {
		loadDefaults();
		loadInstance(lang);
	}

	public BaseStopAnalyzer(String langs[]) {
		loadDefaults();
		if (langs != null) {
			for (String language : langs) {
				loadInstance(language);
			}
		}
	}

	public Analyzer getInstance(String language) {
		Analyzer _instAnalyzer = null;
		if (language != null) {
			String languageLowerCase = language.toLowerCase();
			if (!langMap.containsKey(languageLowerCase)) {
				// System.out.println("Found no language setting for :" +
				// language);
				languageLowerCase = "en";
			}
			if (!stopAnalyzerMap.containsKey(languageLowerCase)) {
				loadInstance(languageLowerCase);
			}
			_instAnalyzer = stopAnalyzerMap.get(languageLowerCase);
		}
		return _instAnalyzer;
	}

	public void loadDefaults() {
		langMap = new HashMap<String, String>();
		stopAnalyzerMap = new HashMap<String, Analyzer>();
		langMap.put("en", "org.sgrp.singer.analyzer.en.EnglishAnalyzer");

	}

	public void loadInstance(String language) {
		String languageLowerCase = language.toLowerCase();
		if (!langMap.containsKey(languageLowerCase)) {
			languageLowerCase = "en";
		}
		try {
			if (!stopAnalyzerMap.containsKey(languageLowerCase)) {
				String analyzerClassName = langMap.get(languageLowerCase);
				Analyzer _loadAnalyzer = (Analyzer) Class.forName(analyzerClassName).newInstance();
				stopAnalyzerMap.put(languageLowerCase, _loadAnalyzer);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}