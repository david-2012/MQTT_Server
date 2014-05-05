package com.luxurite.mqtt;

import java.io.UnsupportedEncodingException;

public class FormatTransfer {
	/**
	 * * å‚è?http://blog.csdn.net/jiangxinyu/article/details/8165064 *
	 * å°†intè½¬ä¸ºä½å­—èŠ‚åœ¨å‰ï¼Œé«˜å­—èŠ‚åœ¨åçš„byteæ•°ç»„ * @param n int * @return byte[]
	 */
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	// /** * å°†intè½¬ä¸ºé«˜å­—èŠ‚åœ¨å‰ï¼Œä½å­—èŠ‚åœ¨åçš„byteæ•°ç»„ * @param n int * @return byte[] */
	// public static byte[] toHH(int n) {
	// byte[] b = new byte[4];
	// b[3] = (byte) (n & 0xff);
	// b[2] = (byte) (n >> 8 & 0xff);
	// b[1] = (byte) (n >> 16 &
	//
	// 0xff);
	// b[0] = (byte) (n >> 24 & 0xff);
	// return b;
	// }

	/** * å°†é«˜å­—èŠ‚æ•°ç»„è½¬æ¢ä¸ºint * @param b byte[] * @return int */
	// public static int hBytesToInt(byte[] b) {
	// int s =0;
	// for (int i = 0; i < 3; i++) {
	// if ( b>=0 ) {
	// s = s + b;
	// } else {
	// s = s + 256 + b;
	// }
	// s = s * 256;
	// }
	// if (b[3] >= 0) {
	// s = s + b[3];
	// } else {
	// s = s + 256 + b[3];
	// }
	// return s;
	// }

	/** * å°†ä½å­—èŠ‚æ•°ç»„è½¬æ¢ä¸ºint * @param b byte[] * @return int */
	public static int lBytesToInt(byte[] b) {
		int s =

		0;
		for (int i = 0; i < 3; i++) {
			if (b[3 - i] >= 0) {
				s = s + b[3 - i];
			} else {
				s = s + 256 + b[3 - i];
			}

			s = s * 256;
		}
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		return s;
	}

	/** * åˆå¹¶ä¸¤ä¸ªå­—èŠ‚æ•°ç»„ */
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	/**
	 * ½«×Ö·û±àÂë×ª»»³ÉUTF-8Âë
	 */
	public static String toUTF_8(String str) throws UnsupportedEncodingException {
		return changeCharset(str, "UTF-8");
	}

	public static String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			// ÓÃÄ¬ÈÏ×Ö·û±àÂë½âÂë×Ö·û´®¡£
			byte[] bs = str.getBytes();
			// ÓÃĞÂµÄ×Ö·û±àÂëÉú³É×Ö·û´®
			return new String(bs, newCharset);
		}
		return null;
	}

}
