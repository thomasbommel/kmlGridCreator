package main.java.kmlGridCreator.utils;

public class MathUtils {

	private static final double EPSILON = 0.0000000001;

	public static final boolean equals(Double a, Double b) {
		if (a == null && b == null) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}
		return Math.abs(a - b) < EPSILON;
	}

	public static boolean equals(int a, int b) { // NO_UCD (test only)
		return equals((double) a, (double) b);
	}

	public static boolean equals(double a, int b) { // NO_UCD (test only)
		return equals(a, (double) b);
	}

	public static boolean equals(int a, double b) { // NO_UCD (test only)
		return equals((double) a, b);
	}

}
