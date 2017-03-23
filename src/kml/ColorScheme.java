package kml;

public class ColorScheme {

	private static final String low = "FF0711FA";
	private static final String normal = "FF0AFCF5";
	private static final String high = "FFA1FA42";

	public static String getColor(int count) {
		if (count < 10) {
			return low;
		} else if (count < 20) {
			return normal;
		} else {
			return high;
		}
	}
}
