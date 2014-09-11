package jdepend.model.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.model.JavaClass;

public class JavaClassCollection {

	private Collection<JavaClass> javaClasses;
	private Map<String, JavaClass> javaClassesForName;

	public JavaClassCollection(Collection<JavaClass> javaClasses) {
		super();
		this.javaClasses = javaClasses;

		javaClassesForName = new HashMap<String, JavaClass>();
		for (JavaClass javaClass : javaClasses) {
			javaClassesForName.put(javaClass.getName(), javaClass);
		}
	}

	public Collection<JavaClass> getJavaClasses() {
		return javaClasses;
	}

	public Map<String, JavaClass> getJavaClassesForName() {
		return javaClassesForName;
	}

	public JavaClass getTheClassByName(String name) {
		return this.javaClassesForName.get(name);
	}
}
