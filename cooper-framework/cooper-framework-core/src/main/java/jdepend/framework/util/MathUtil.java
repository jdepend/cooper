package jdepend.framework.util;

public final class MathUtil {

	private static float zero = 0.00000001F;

	public static boolean isEquals(float obj1, float obj2) {
		return Math.abs(obj1 - obj2) < zero;
	}

	public static boolean isEquals(Comparable obj1, Comparable obj2) {
		if (obj1 instanceof Float && obj2 instanceof Float) {
			return isEquals(((Float) obj1).floatValue(), ((Float) obj2).floatValue());
		} else {
			return obj1.compareTo(obj2) == 0;
		}
	}

	public static boolean isZero(Float obj) {
		return Math.abs(obj) < zero;
	}

	public static int compare(Float arg1, Float arg2) {
		if (arg1 == null || arg2 == null) {
			return 0;
		} else if (Math.abs(arg1 - arg2) < 0.0000001) {
			return 0;
		} else if (arg1 < arg2) {
			return -1;
		} else {
			return 1;
		}
	}

	public static int compare(Integer arg1, Integer arg2) {
		if (arg1 == null || arg2 == null) {
			return 0;
		} else if (Math.abs(arg1 - arg2) < 0.0000001) {
			return 0;
		} else if (arg1 < arg2) {
			return -1;
		} else {
			return 1;
		}
	}

	public static int compare(String arg1, String arg2) {
		if (arg1 == null || arg2 == null) {
			return 0;
		} else {
			return arg1.compareTo(arg2);
		}
	}

}
