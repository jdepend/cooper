package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.util.ThreadPool;
import jdepend.model.JavaClass;
import jdepend.model.Method;
import jdepend.model.component.modelconf.CandidateUtil;

public class JavaClassCollection {

	private Collection<JavaClass> javaClasses;
	private Map<String, JavaClass> javaClassesForId;
	private Map<String, JavaClass> javaClassesForName;

	private Map<String, Method> httpMethods;

	private Map<String, Collection<JavaClass>> unitJavaClasses;

	private final static int unitCount = ThreadPool.ThreadCount;

	public JavaClassCollection(Collection<JavaClass> javaClasses) {
		super();
		this.javaClasses = javaClasses;

		javaClassesForId = new HashMap<String, JavaClass>();
		javaClassesForName = new HashMap<String, JavaClass>();

		for (JavaClass javaClass : javaClasses) {
			javaClassesForId.put(javaClass.getId(), javaClass);
			javaClassesForName.put(javaClass.getName(), javaClass);
		}

		unitJavaClasses = unitTheadClassCollection(javaClasses);

	}

	public Collection<JavaClass> getJavaClasses() {
		return javaClasses;
	}

	public JavaClass getTheClass(String id) {
		return javaClassesForId.get(id);
	}

	public JavaClass getTheClass(String place, String name) {
		JavaClass javaClass = this.javaClassesForId.get(CandidateUtil.getId(place, name));
		if (javaClass == null) {
			return this.javaClassesForName.get(name);
		} else {
			return javaClass;
		}
	}

	public Map<String, Collection<JavaClass>> getUnitJavaClasses() {
		return unitJavaClasses;
	}

	public synchronized Map<String, Method> getHttpMethod() {
		if (this.httpMethods == null) {
			this.httpMethods = new HashMap<String, Method>();
			for (JavaClass javaClass : javaClasses) {
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
