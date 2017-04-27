package net.ojava.openkit.drawnumbers.util;

public class StrUtil {
	public static boolean isEmpty(String str) {
		if(str == null) {
			return true;
		}
		
		return str.trim().length() == 0;
	}
}