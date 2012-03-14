package org.sgrp.singer.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

public final class BaseLowerCaseTokenizer extends Tokenizer {
	private final static int	IO_BUFFER_SIZE	= 1024;

	private final static int	MAX_WORD_LEN	= 255;

	private final char[]		buffer			= new char[MAX_WORD_LEN];

	private final char[]		ioBuffer		= new char[IO_BUFFER_SIZE];

	private int					offset			= 0, bufferIndex = 0, dataLen = 0;

	public BaseLowerCaseTokenizer(Reader in) {
		input = in;
	}

	@Override
	public final Token next() throws java.io.IOException {
		int length = 0;
		int start = offset;
		while (true) {
			final char c;

			offset++;
			if (bufferIndex >= dataLen) {
				dataLen = input.read(ioBuffer);
				bufferIndex = 0;
			}
			;
			if (dataLen == -1) {
				if (length > 0) {
					break;
				} else {
					return null;
				}
			} else {
				c = ioBuffer[bufferIndex++];
			}

			if (Character.isLetterOrDigit(c) || (c == '_')) { // if it's a
				// letter
				// and "_" too

				if (length == 0) {
					start = offset - 1;
				}

				buffer[length++] = Character.toLowerCase(c);
				// buffer it
				if (length == MAX_WORD_LEN) {
					break;
				}

			} else if (length > 0) {
				break; // return 'em
			}

		}

		return new Token(new String(buffer, 0, length), start, start + length);
	}
}