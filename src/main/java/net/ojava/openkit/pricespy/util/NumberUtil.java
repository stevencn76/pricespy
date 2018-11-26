package net.ojava.openkit.pricespy.util;

import java.text.NumberFormat;
import java.util.Set;

public class NumberUtil {
	public static Set<Integer> parseNumbers(String numberSource) {
		return null;
	}
	
	public static String double2String(double value, int decimalLength) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(decimalLength);
		return nf.format(value);
	}
}
