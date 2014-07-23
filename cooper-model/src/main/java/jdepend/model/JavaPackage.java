package jdepend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import jdepend.framework.exception.JDependException;

/**
 * The <code>JavaPackage</code> class represents a Java package.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class JavaPackage implements Serializable, Named, Comparable<JavaPackage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2047744159854799747L;

	private String name;

	private Collection<JavaClass> classes;

	public static final String Default = "Default";

	public JavaPackage(String name) {
		this.name = name;
		this.classes = new ArrayList<JavaClass>();
	}

	public void addClass(JavaClass clazz) {
		classes.add(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.framework.JDependUnit#getClasses()
	 */
	public Collection<JavaClass> getClasses() {
		return classes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.framework.JDependUnit#getClassCount()
	 */
	public int getClassCount() {
		return classes.size();
	}

	public boolean isInner() {
		for (JavaClass javaClass : this.getClasses()) {
			if (javaClass.isInner()) {
				return true;
			}
		}
		return false;
	}

	public JavaPackage clone(Collection<JavaClass> javaClasses) throws JDependException {
		for (JavaClass javaClass : javaClasses) {
			if (javaClass.getJavaPackage().equals(this)) {
				return javaClass.getJavaPackage();
			}
		}
		throw new JDependException("克隆" + this.getName() + "出错");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "JavaPackage [classes.size()=" + classes.size() + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JavaPackage other = (JavaPackage) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(JavaPackage o) {
		return this.name.compareTo(o.name);
	}
}
