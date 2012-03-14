package org.sgrp.singer.analyzer.en;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.sgrp.singer.analyzer.BaseLowerCaseTokenizer;

/**
 * Filters LetterTokenizer with LowerCaseFilter and StopFilter for English contents.
 */

public final class EnglishAnalyzer extends Analyzer {
	/*
	 * taken off "as", "at", "be", "by","in","is", "it","no","of", "to", because these are countries code
	 */
	protected static final String[]	ENGLISH_STOP_WORDS	= { "and", "are", "but", "for", "if", "into", "not", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "was", "will", "with" };

	private Set						stopSet;

	/** Builds an analyzer which removes words in ENGLISH_STOP_WORDS. */
	public EnglishAnalyzer() {
		stopSet = StopFilter.makeStopSet(ENGLISH_STOP_WORDS);
	}

	/** Builds an analyzer which removes words in the provided array. */
	public EnglishAnalyzer(String[] stopWords) {
		stopSet = StopFilter.makeStopSet(stopWords);
	}

	/** Filters LowerCaseTokenizer with StopFilter. */
	public final TokenStream tokenStream(Reader reader) {
		return new StopFilter(new BaseLowerCaseTokenizer(reader), stopSet);
	}

	@Override
	public TokenStream tokenStream(String arg0, Reader reader) {
		return new StopFilter(new BaseLowerCaseTokenizer(reader), stopSet);
	}
}

