package jdepend.metadata.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import jdepend.framework.util.ThreadPool;
import jdepend.metadata.CandidateUtil;
import jdepend.metadata.JavaClass;
import jdepend.metadata.Method;
import jdepend.metadata.relationtype.JavaClassRelationTypes;

public class JavaClassCollection {

	private Collection<JavaClass> javaClasses;
	private Map<String, JavaClass> javaClassesForId;
	private Map<String, JavaClass> javaClassesForName;

	private Map<String, Method> httpMethods;

	private JavaClassRelationTypes javaClassRelationTypes;

	private Map<String, Collection<JavaClass>> unitJavaClasses;

	private final static int unitCount = ThreadPool.ThreadCount;

	public JavaClassCollection(JavaClassRelationTypes javaClassRelationTypes, Collection<JavaClass> javaClasses) {
		super();
		this.javaClassRelationTypes = javaClassRelationTypes;
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

	public JavaClassRelationTypes getJavaClassRelationTypes() {
		return javaClassRelationTypes;
	}

	public static <T> Map<String, Collection<T>> unitTheadClassCollection(Collection<T> javaClasses) {

		Map<String, Collection<T>> unitJavaClasses = new HashMap<String, Collection<T>>();

		for (int unitIndex = 0; unitIndex < unitCount; unitIndex++) {
			unitJavaClasses.put("unit" + unitIndex, new ArrayList<T>());
		}
		int unitIndex = 0;
		for (T javaClass : javaClasses) {
			unitJavaClasses.get("unit" + unitIndex % unitCount).add(javaClass);
			unitIndex++;
		}

		return unitJavaClasses;

	}
}
