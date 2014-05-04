package jdepend.knowledge.pattern.impl2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import jdepend.model.Attribute;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public class FeatureCheckContext {

	private JavaClass javaClass;

	private Collection<JavaClass> interfaces;

	private Collection<JavaClass> superClasses;

	private transient Collection<JavaClass> allSuperClasses;

	private Collection<JavaClass> superOtherSubClasses;

	private Collection<JavaClass> subClasses;

	private Collection<Attribute> collectionAttributes;

	private Collection<Attribute> haveSubClassesCollectionAttributes;

	private Collection<Attribute> abstractAttributes;

	private Collection<Attribute> haveSubClassesAbstractAttributes;

	private Collection<Attribute> staticAttributes;

	private Collection<Method> staticMethods;

	private Collection<Method> abstractMethods;

	private Map<Method, Method> overrideMethods;

	private Collection<Method> returnIsSuperOverrideMethods;

	public FeatureCheckContext(JavaClass javaClass) {
		super();
		this.javaClass = javaClass;
	}

	public JavaClass getCurrent() {
		return this.javaClass;
	}

	public Collection<JavaClass> getAllSupers() {
		if (this.interfaces == null && this.superClasses == null) {
			return null;
		} else {
			if (allSuperClasses == null) {
				allSuperClasses = new ArrayList<JavaClass>();
				if (this.interfaces != null) {
					allSuperClasses.addAll(this.interfaces);
				}
				if (this.superClasses != null) {
					allSuperClasses.addAll(superClasses);
				}
			}
			return allSuperClasses;
		}
	}

	public Collection<JavaClass> getSupers() {
		return superClasses;
	}

	public void setSupers(Collection<JavaClass> superClasses) {
		this.superClasses = superClasses;
	}

	public void setInterfaces(Collection<JavaClass> interfaces) {
		this.interfaces = interfaces;
	}

	public Collection<JavaClass> getInterfaces() {
		return interfaces;
	}

	public Collection<Attribute> getCollectionAttributes() {
		return collectionAttributes;
	}

	public void setCollectionAttributes(Collection<Attribute> collectionAttributes) {
		this.collectionAttributes = collectionAttributes;
	}

	public void setStaticAttributes(Collection<Attribute> staticAttributes) {
		this.staticAttributes = staticAttributes;
	}

	public Collection<Attribute> getStaticAttributes() {
		return staticAttributes;
	}

	public void setStaticMethods(Collection<Method> staticMethods) {
		this.staticMethods = staticMethods;
	}

	public Collection<Method> getStaticMethods() {
		return staticMethods;
	}

	public void setOverrideMethods(Map<Method, Method> overrideMethods) {
		this.overrideMethods = overrideMethods;
	}

	public Map<Method, Method> getOverrideMethods() {
		return overrideMethods;
	}

	public void setReturnIsSuperOverrideMethods(Collection<Method> methods) {
		this.returnIsSuperOverrideMethods = methods;
	}

	public Collection<Method> getReturnIsSuperOverrideMethods() {
		return returnIsSuperOverrideMethods;
	}

	public void setAbstractAttributes(Collection<Attribute> abstractAttributes) {
		this.abstractAttributes = abstractAttributes;
	}

	public Collection<Attribute> getAbstractAttributes() {
		return abstractAttributes;
	}

	public void setHaveSubClassesAbstractAttributes(Collection<Attribute> attributes) {
		this.haveSubClassesAbstractAttributes = attributes;
	}

	public Collection<Attribute> getHaveSubClassesAbstractAttributes() {
		return haveSubClassesAbstractAttributes;
	}

	public Collection<Attribute> getHaveSubClassesCollectionAttributes() {
		return haveSubClassesCollectionAttributes;
	}

	public void setHaveSubClassesCollectionAttributes(Collection<Attribute> haveSubClassesCollectionAttributes) {
		this.haveSubClassesCollectionAttributes = haveSubClassesCollectionAttributes;
	}

	public void setSuperOtherSubClasses(Collection<JavaClass> otherSubClasses) {
		this.superOtherSubClasses = otherSubClasses;
	}

	public Collection<JavaClass> getSuperOtherSubClasses() {
		return superOtherSubClasses;
	}

	public void setAbstractMethods(Collection<Method> abstractMethods) {
		this.abstractMethods = abstractMethods;
	}

	public Collection<Method> getAbstractMethods() {
		return abstractMethods;
	}

	public void setSubClasses(Collection<JavaClass> subClasses) {
		this.subClasses = subClasses;
	}

	public Collection<JavaClass> getSubClasses() {
		return subClasses;
	}
}
