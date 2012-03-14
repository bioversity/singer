package org.sgrp.singer;

import java.util.BitSet;

public class Utils {

	private static byte[]	b64dec;

	private static byte[]	b64enc;

	private static BitSet	boundchar	= new BitSet(256);

	static {
		int int1 = 48;
		int int2;
		byte[] byte_1darray3;

		do {
			boundchar.set(int1);
		} while (++int1 <= 57);
		int1 = 65;
		do {
			boundchar.set(int1);
		} while (++int1 <= 90);
		int1 = 97;
		do {
			boundchar.set(int1);
		} while (++int1 <= 122);
		boundchar.set(39);
		boundchar.set(40);
		boundchar.set(41);
		boundchar.set(43);
		boundchar.set(44);
		boundchar.set(45);
		boundchar.set(46);
		boundchar.set(47);
		boundchar.set(58);
		boundchar.set(61);
		boundchar.set(63);
		boundchar.set(95);
		byte_1darray3 = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118,
			119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
		b64enc = byte_1darray3;
		b64dec = new byte[128];
		for (int2 = 0; int2 < b64enc.length; ++int2) {
			b64dec[b64enc[int2]] = (byte) int2;
		}
	}

	public static final byte[] b64decode(byte[] byte_1darray1) {
		if ((byte_1darray1 == null) || (byte_1darray1.length == 0)) {
			return null;
		} else {
			int int2;
			byte[] byte_1darray3;
			int int4;
			int int5;

			for (int2 = byte_1darray1.length; byte_1darray1[int2 - 1] == 61; --int2) {
				;
			}
			byte_1darray3 = new byte[int2 - byte_1darray1.length / 4];
			for (int4 = 0; int4 < byte_1darray1.length; ++int4) {
				byte_1darray1[int4] = b64dec[byte_1darray1[int4]];
			}
			int4 = 0;
			int5 = 0;
			for (int5 = 0; int5 < byte_1darray3.length - 2; int5 += 3) {
				byte_1darray3[int5] = (byte) (byte_1darray1[int4] << 0x2 & 0xFF | byte_1darray1[int4 + 0x1] >>> 0x4 & 0x3);
				byte_1darray3[int5 + 1] = (byte) (byte_1darray1[int4 + 0x1] << 0x4 & 0xFF | byte_1darray1[int4 + 0x2] >>> 0x2 & 0xF);
				byte_1darray3[int5 + 2] = (byte) (byte_1darray1[int4 + 0x2] << 0x6 & 0xFF | byte_1darray1[int4 + 0x3] & 0x3F);
				int4 += 4;
			}
			if (int5 < byte_1darray3.length) {
				byte_1darray3[int5] = (byte) (byte_1darray1[int4] << 0x2 & 0xFF | byte_1darray1[int4 + 0x1] >>> 0x4 & 0x3);
			}
			if (++int5 < byte_1darray3.length) {
				byte_1darray3[int5] = (byte) (byte_1darray1[int4 + 0x1] << 0x4 & 0xFF | byte_1darray1[int4 + 0x2] >>> 0x2 & 0xF);
			}
			return byte_1darray3;
		}
	}

	public static final String b64decode(String String1) {
		if (String1 == null) {
			return null;
		} else {
			// byte[] byte_1darray2 = new byte[String1.length()];
			byte[] byte_1darray2 = String1.getBytes();
			// String1.getBytes( 0, String1.length(), byte_1darray2, 0 );
			byte[] sb = b64decode(byte_1darray2);
			if (sb == null) { return null; }
			return new String(sb);
		}
	}

	static final byte[] b64encode(byte[] byte_1darray1) {
		if (byte_1darray1 == null) {
			return null;
		} else {
			byte[] byte_1darray4 = new byte[(byte_1darray1.length + 2) / 3 * 4];
			int int2 = 0;
			int int3 = 0;

			int3 = 0;
			while (int2 < byte_1darray1.length - 2) {
				byte_1darray4[int3++] = b64enc[byte_1darray1[int2] >>> 0x2 & 0x3F];
				byte_1darray4[int3++] = b64enc[byte_1darray1[int2 + 0x1] >>> 0x4 & 0xF | byte_1darray1[int2] << 0x4 & 0x3F];
				byte_1darray4[int3++] = b64enc[byte_1darray1[int2 + 0x2] >>> 0x6 & 0x3 | byte_1darray1[int2 + 0x1] << 0x2 & 0x3F];
				byte_1darray4[int3++] = b64enc[byte_1darray1[int2 + 0x2] & 0x3F];
				int2 += 3;
			}
			if (int2 < byte_1darray1.length) {
				byte_1darray4[int3++] = b64enc[byte_1darray1[int2] >>> 0x2 & 0x3F];
				if (int2 < byte_1darray1.length - 1) {
					byte_1darray4[int3++] = b64enc[byte_1darray1[int2 + 0x1] >>> 0x4 & 0xF | byte_1darray1[int2] << 0x4 & 0x3F];
					byte_1darray4[int3++] = b64enc[byte_1darray1[int2 + 0x1] << 0x2 & 0x3F];
				} else {
					byte_1darray4[int3++] = b64enc[byte_1darray1[int2] << 0x4 & 0x3F];
				}
			}
			while (int3 < byte_1darray4.length) {
				byte_1darray4[int3] = (byte) 61;
				++int3;
			}
			return byte_1darray4;
		}
	}

	public static final String b64encode(String String1) {
		if (String1 == null) {
			return null;
		} else {
			byte[] byte_1darray2 = String1.getBytes();
			return new String(b64encode(byte_1darray2));
		}
	}

	public static String getcookie(String String1, String String2) {
		if (String1 == null) {
			return null;
		} else {
			int int3 = String1.indexOf(String2 + '=');
			int int4;
			String String5;

			if (int3 == -1) { return null; }
			int4 = String1.indexOf(59, int3);
			if (int4 == -1) {
				int4 = String1.length();
			}
			String5 = String1.substring(int3 + String2.length() + 1, int4);
			return String5;
		}
	}

	private static boolean ishex(char char1) {
		String String2 = "0123456789abcdefABCDEF";

		if (String2.indexOf(char1) == -1) {
			return false;
		} else {
			return true;
		}
	}

	public static final String qsdecode(String String1) {
		if ((String1 == null) || String1.equals("")) {
			return "";
		} else {
			StringBuffer StringBuffer2 = new StringBuffer();
			int int3 = String1.length();
			int int4;

			for (int4 = 0; int4 < int3; ++int4) {
				char char5 = String1.charAt(int4);

				if ((char5 == 37) && (int4 + 2 < int3)) {
					char char6 = String1.charAt(int4 + 1);
					char char7 = String1.charAt(int4 + 2);

					if (ishex(char6) && ishex(char7)) {
						StringBuffer2.append(String.valueOf((char) (Character.digit(char6, 16) * 16 + Character.digit(char7, 16))));
						int4 += 2;
					} else {
						StringBuffer2.append(String.valueOf(char5));
					}
				} else if (char5 == 43) {
					StringBuffer2.append(" ");
				} else {
					StringBuffer2.append(String.valueOf(char5));
				}
			}
			return StringBuffer2.toString();
		}
	}

	public static final String qsencode(String String1) {
		if ((String1 == null) || String1.equals("")) {
			return "";
		} else {
			StringBuffer StringBuffer2 = new StringBuffer();
			int int3 = String1.length();
			int int4;

			for (int4 = 0; int4 < int3; ++int4) {
				char char5 = String1.charAt(int4);

				if (char5 == 37) {
					StringBuffer2.append("%25");
				} else if (char5 == 61) {
					StringBuffer2.append("%3D");
				} else if (char5 == 59) {
					StringBuffer2.append("%3B");
				} else if (char5 == 10) {
					StringBuffer2.append("%0A");
				} else if (char5 == 13) {
					StringBuffer2.append("%0D");
				} else if (char5 == 32) {
					StringBuffer2.append("+");
				} else {
					StringBuffer2.append(String.valueOf(char5));
				}
			}
			return StringBuffer2.toString();
		}
	}

	public static byte[] strtobytearr(String String1) {
		int int2 = String1.length();
		byte[] byte_1darray3 = new byte[int2];
		int int4;

		for (int4 = 0; int4 < int2; ++int4) {
			byte_1darray3[int4] = (byte) String1.charAt(int4);
		}
		return byte_1darray3;
	}
}
