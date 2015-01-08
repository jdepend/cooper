package jdepend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import jdepend.framework.exception.JDependException;
import jdepend.model.component.modelconf.Candidate;
import jdepend.model.component.modelconf.CandidateUtil;

/**
 * The <code>JavaPackage</code> class represents a Java package.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class JavaPackage implements Serializable, Candidate, Comparable<JavaPackage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2047744159854799747L;

	private String name;

	private String place;

	private Collection<JavaClass> classes;

	public static final String Default = "Default";

	public JavaPackage(String place, String name) {
		this.place = place;
		this.name = name;
		this.classes = new ArrayList<JavaClass>();
	}

	public void addClass(JavaClass clazz) {
		classes.add(clazz);
		clazz.setJavaPackage(this);
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

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Override
	public String getId() {
		return CandidateUtil.getId(this);
	}

	@Override
	public int size() {
		return this.getClassCount();
	}

	@Override
	public String toString() {
		return "JavaPackage [classes.size()=" + classes.size() + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;
		JavaPackage other = (JavaPackage) obj;
		if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public int compareTo(JavaPackage o) {
		return this.getId().compareTo(o.getId());
	}
}
