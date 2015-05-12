package jdepend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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

	private Collection<JavaClassUnit> classes;

	public static final String Default = "Default";

	public JavaPackage(String place, String name) {
		this.place = place;
		this.name = name;
		this.classes = new ArrayList<JavaClassUnit>();
	}

	public void addClass(JavaClassUnit clazz) {
		classes.add(clazz);
		clazz.setJavaPackage(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.framework.JDependUnit#getClasses()
	 */
	public Collection<JavaClassUnit> getClasses() {
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
		for (JavaClassUnit javaClass : this.getClasses()) {
			if (javaClass.isInner()) {
				return true;
			}
		}
		return false;
	}

	public JavaPackage clone(Map<String, JavaClassUnit> javaClasses) throws JDependException {

		JavaPackage obj = new JavaPackage(place, name);
		for (JavaClassUnit javaClass : this.classes) {
			obj.addClass(javaClasses.get(javaClass.getId()));
		}
		return obj;
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
