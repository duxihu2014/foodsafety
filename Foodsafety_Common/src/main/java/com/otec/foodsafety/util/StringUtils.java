package com.otec.foodsafety.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {
	static Logger log = LoggerFactory.getLogger(StringUtils.class);

	public static String toUpperCase(String str) {
		return trim(str).toUpperCase();
	}

	public static String toLowerCase(String str) {
		return trim(str).toLowerCase();
	}

	public static String trim(String s) {
		return getNotNullString(s);
	}

	public static String None2Null(String s) {
		if (s != null) {
			if ("None".equals(s)) {
				return "";
			}
			return s.trim();
		}

		return "";
	}

	public static String NullString2Null(String s) throws Exception {
		if (s != null) {
			if ("null".equalsIgnoreCase(s)) {
				return "";
			}
			return s.trim();
		}

		return "";
	}

	public static String blankToBracket(String str) {
		if ((str == null) || (str.equals("")))
			return "{}";
		return str;
	}

	public static String blankToZero(String str) {
		if ((str == null) || (str.equals("")))
			return "0";
		return str;
	}

	public static String getTrimString(String src, int n, String var) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < n; i++) {
			buf.append(var);
		}
		buf.append(src);

		return buf.toString();
	}

	public static String getTrimString(String conzt, String var) {
		String ret = conzt + var;
		return ret.substring(var.length());
	}

	public static String getTrimRight(String src, int n) {
		StringBuilder buf = new StringBuilder();

		int lngth = src != null ? src.length() : 0;
		buf.append(src);
		for (int i = 0; i < n - lngth; i++) {
			buf.append(" ");
		}

		return buf.toString();
	}

	public static String getTrimString(String conzt, int var) {
		String ret = conzt + var;
		return ret.substring(ret.length() - conzt.length());
	}

	public static String getNotNullString(String s, String sdefault) {
		return s != null ? s.trim() : sdefault;
	}

	public static String getNotNullString(String s) {
		return s != null ? s.trim() : "";
	}
	
	/**
	 * 鍒ゆ柇瀛楃涓叉槸鍚︿负绌?
	 * @param value	String
	 * @return boolean
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.equals(""));
	}
	
	public static boolean isBlankString(String in) {
		
		if (isEmpty(in))
			return true;
		//鍔犲叆None鐨勫垽鏂?
		if ((in.trim().equals("")) || (in.trim().equalsIgnoreCase("null")))
			return true;
		return false;
	}
	
	public static String format(String intval, int length) {
		char[] c = new char[length];
		for (int i = 0; i < c.length; i++)
			c[i] = '0';
		StringBuffer buf = new StringBuffer(String.valueOf(c));
		buf.replace(length - intval.length(), length, intval);
		return buf.toString();
	}
	
	
	public static String replace(String strSource, String strFrom, String strTo) {
		StringBuffer strDest = new StringBuffer();
		int intFromLen = strFrom.length();
		int intPos;
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			strDest = strDest.append(strSource.substring(0, intPos)).append(
					strTo);
			strSource = strSource.substring(intPos + intFromLen);
		}
		strDest = strDest.append(strSource);

		return strDest.toString();
	}
	
	// / 杞叏瑙掔殑鍑芥暟(SBC case) ///
		// /浠绘剰瀛楃涓?/// 鍏ㄨ瀛楃涓?///
		// /鍏ㄨ绌烘牸涓?2288,鍗婅绌烘牸涓?2
		// /鍏朵粬瀛楃鍗婅(33-126)涓庡叏瑙?65281-65374)鐨勫搴斿叧绯绘槸锛氬潎鐩稿樊65248 ///
		public static String toSBC(String input) {
			// 鍗婅杞叏瑙掞細
			char[] c = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == 32) {
					c[i] = (char) 12288;
					continue;
				}
				if (c[i] < 127)
					c[i] = (char) (c[i] + 65248);
			}
			return new String(c);
		}

		// / /// 杞崐瑙掔殑鍑芥暟(DBC case) ///
		// /浠绘剰瀛楃涓?
		// / 鍗婅瀛楃涓?///
		// /鍏ㄨ绌烘牸涓?2288锛屽崐瑙掔┖鏍间负32
		// /鍏朵粬瀛楃鍗婅(33-126)涓庡叏瑙?65281-65374)鐨勫搴斿叧绯绘槸锛氬潎鐩稿樊65248 ///
		public static String toDBC(String input) {
			char[] c = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if (c[i] == 12288) {
					c[i] = (char) 32;
					continue;
				}
				if (c[i] > 65280 && c[i] < 65375)
					c[i] = (char) (c[i] - 65248);
			}
			return new String(c);
		}
}
