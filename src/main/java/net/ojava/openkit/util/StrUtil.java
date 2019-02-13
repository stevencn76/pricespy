package net.ojava.openkit.util;

public class StrUtil {
	public static boolean isEmpty(String str) {
		if(str == null) {
			return true;
		}
		
		return str.trim().length() == 0;
	}
	
	public static String subString(String str, String startStr, String endStr) {
		if (str == null)
			return null;
		
		int pos1 = str.indexOf(startStr);
		if (pos1 == -1)
			return null;
		
		if (endStr == null)
			return str.substring(pos1 + startStr.length());
		
		int pos2 = str.indexOf(endStr, pos1);
		if (pos2 == -1) 
			return null;
		
		return str.substring(pos1 + startStr.length(), pos2);
	}
	
	public static String prefixLength(String str, int length, char prefix) {
		if (str == null) {
			str = "";
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<length - str.length(); i++) {
			sb.append(prefix);
		}
		sb.append(str);
		
		return sb.toString();
	}
}