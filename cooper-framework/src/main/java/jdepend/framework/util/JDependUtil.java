package jdepend.framework.util;

public final class JDependUtil {

	public static String getArg(String[] args, String key) {
		int i = 0;
		for (String arg : args) {
			if (arg.equals(key)) {
				return args[i + 1];
			}
			i++;
		}
		return null;
	}

}
