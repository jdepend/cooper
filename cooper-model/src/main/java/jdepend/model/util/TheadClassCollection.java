package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.util.ThreadPool;
import jdepend.model.JavaClass;

public class TheadClassCollection {

	private final static int unitCount = ThreadPool.ThreadCount;

	public static Map<String, Collection<JavaClass>> unitTheadClassCollection(Collection<JavaClass> javaClasses) {

		Map<String, Collection<JavaClass>> unitJavaClasses = new HashMap<String, Collection<JavaClass>>();

		for (int unitIndex = 0; unitIndex < unitCount; unitIndex++) {
			unitJavaClasses.put("unit" + unitIndex, new ArrayList<JavaClass>());
		}
		int unitIndex = 0;
		for (JavaClass javaClass : javaClasses) {
			unitJavaClasses.get("unit" + unitIndex % unitCount).add(javaClass);
			unitIndex++;
		}

		return unitJavaClasses;

	}

}
