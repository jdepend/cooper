package jdepend.metadata.util;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.Method;

public class MethodUtil {

	private static Collection<String> commFuns;

	static {
		commFuns = new HashSet<String>();

		commFuns.add("toString");
		commFuns.add("equals");
		commFuns.add("hashCode");
	}

	public static boolean isBusinessMethod(Method method) {
		String name = method.getName();
		return !method.isConstruction() && !name.startsWith("get") && !name.startsWith("set")
				&& !commFuns.contains(name);
	}

}
