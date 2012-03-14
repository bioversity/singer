package org.sgrp.singer.analyzer;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * Filters LetterTokenizer with LowerCaseFilter and StopFilter for all language contents.
 */

public final class DefaultAnalyzer extends Analyzer {
	/*
	 * taken off "as", "at", "be", "by","in","is", "it","no","of", "to", because these are countries code
	 */
	protected static final String[]	ALL_STOP_WORDS	= { "a", "and", "are", "but", "for", "if", "into", "not", "on", "or", "s", "such", "t", "that", "the", "their", "then", "there", "these", "they", "this", "was", "will", "with" };

	private Set						stopSet;

	/** Builds an analyzer which removes words in ENGLISH_STOP_WORDS. */
	public DefaultAnalyzer() {
		stopSet = StopFilter.makeStopSet(ALL_STOP_WORDS);
	}

	/** Builds an analyzer which removes words in the provided array. */
	public DefaultAnalyzer(String[] stopWords) {
		stopSet = StopFilter.makeStopSet(stopWords);
	}

	/** Filters LowerCaseTokenizer with StopFilter. */
	public final TokenStream tokenStream(Reader reader) {
		return new StopFilter(new BaseLowerCaseTokenizer(reader), stopSet);
	}

	@Override
	public TokenStream tokenStream(String arg0, Reader reader) {
		// TODO Auto-generated method stub
		return new StopFilter(new BaseLowerCaseTokenizer(reader), stopSet);
	}
}
