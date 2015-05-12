package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.util.ThreadPool;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;
import jdepend.model.component.modelconf.CandidateUtil;

public class JavaClassCollection {

	private Collection<JavaClassUnit> javaClasses;
	private Map<String, JavaClassUnit> javaClassesForId;
	private Map<String, JavaClassUnit> javaClassesForName;

	private Map<String, Method> httpMethods;

	private Map<String, Collection<JavaClassUnit>> unitJavaClasses;

	private final static int unitCount = ThreadPool.ThreadCount;

	public JavaClassCollection(Collection<JavaClassUnit> javaClasses) {
		super();
		this.javaClasses = javaClasses;

		javaClassesForId = new HashMap<String, JavaClassUnit>();
		javaClassesForName = new HashMap<String, JavaClassUnit>();

		for (JavaClassUnit javaClass : javaClasses) {
			javaClassesForId.put(javaClass.getId(), javaClass);
			javaClassesForName.put(javaClass.getName(), javaClass);
		}

		unitJavaClasses = unitTheadClassCollection(javaClasses);

	}

	public Collection<JavaClassUnit> getJavaClasses() {
		return javaClasses;
	}

	public JavaClassUnit getTheClass(String id) {
		return javaClassesForId.get(id);
	}

	public JavaClassUnit getTheClass(String place, String name) {
		JavaClassUnit javaClass = this.javaClassesForId.get(CandidateUtil.getId(place, name));
		if (javaClass == null) {
			return this.javaClassesForName.get(name);
		} else {
			return javaClass;
		}
	}

	public Map<String, Collection<JavaClassUnit>> getUnitJavaClasses() {
		return unitJavaClasses;
	}

	public synchronized Map<String, Method> getHttpMethod() {
		if (this.httpMethods == null) {
			this.httpMethods = new HashMap<String, Method>();
			for (JavaClassUnit javaClass : javaClasses) {
				if (javaClass.getDetail().getRequestMapping() != null) {
					for (Method method : javaClass.getSelfMethods()) {
						if (method.getRequestMapping() != null) {
							this.httpMethods.put(method.getRequestMappingValueNoVariable(), method);
						}
					}
				}
			}
		}
		return this.httpMethods;
	}

	public static Map<String, Collection<JavaClassUnit>> unitTheadClassCollection(Collection<JavaClassUnit> javaClasses) {

		Map<String, Collection<JavaClassUnit>> unitJavaClasses = new HashMap<String, Collection<JavaClassUnit>>();

		for (int unitIndex = 0; unitIndex < unitCount; unitIndex++) {
			unitJavaClasses.put("unit" + unitIndex, new ArrayList<JavaClassUnit>());
		}
		int unitIndex = 0;
		for (JavaClassUnit javaClass : javaClasses) {
			unitJavaClasses.get("unit" + unitIndex % unitCount).add(javaClass);
			unitIndex++;
		}

		return unitJavaClasses;

	}
}
