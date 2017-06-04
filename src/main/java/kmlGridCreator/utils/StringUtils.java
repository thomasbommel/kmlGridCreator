package main.java.kmlGridCreator.utils;

public class StringUtils {

	public static String toFixedLength2(String s) {
		while (s.length() < 2) {
			s = "0" + s;
		}
		return s;
	}
	
	public static String toFixedLength3(String s) {
		while (s.length() < 3) {
			s = "0" + s;
		}
		return s;
	}
	
}
