package jdepend.knowledge.pattern.impl2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import jdepend.model.Attribute;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class FeatureCheckContext {

	private JavaClassUnit javaClass;

	private Collection<JavaClassUnit> interfaces;

	private Collection<JavaClassUnit> superClasses;

	private transient Collection<JavaClassUnit> allSuperClasses;

	private Collection<JavaClassUnit> superOtherSubClasses;

	private Collection<JavaClassUnit> subClasses;

	private Collection<Attribute> collectionAttributes;

	private Collection<Attribute> haveSubClassesCollectionAttributes;

	private Collection<Attribute> abstractAttributes;

	private Collection<Attribute> haveSubClassesAbstractAttributes;

	private Collection<Attribute> staticAttributes;

	private Collection<Method> staticMethods;

	private Collection<Method> abstractMethods;

	private Map<Method, Collection<Method>> overrideMethods;

	private Collection<Method> returnIsSuperOverrideMethods;

	public FeatureCheckContext(JavaClassUnit javaClass) {
		super();
		this.javaClass = javaClass;
	}

	public JavaClassUnit getCurrent() {
		return this.javaClass;
	}

	public Collection<JavaClassUnit> getAllSupers() {
		if (this.interfaces == null && this.superClasses == null) {
			return null;
		} else {
			if (allSuperClasses == null) {
				allSuperClasses = new ArrayList<JavaClassUnit>();
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

	public Collection<JavaClassUnit> getSupers() {
		return superClasses;
	}

	public void setSupers(Collection<JavaClassUnit> superClasses) {
		this.superClasses = superClasses;
	}

	public void setInterfaces(Collection<JavaClassUnit> interfaces) {
		this.interfaces = interfaces;
	}

	public Collection<JavaClassUnit> getInterfaces() {
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

	public void setOverrideMethods(Map<Method, Collection<Method>> overrideMethods) {
		this.overrideMethods = overrideMethods;
	}

	public Map<Method, Collection<Method>> getOverrideMethods() {
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

	public void setSuperOtherSubClasses(Collection<JavaClassUnit> otherSubClasses) {
		this.superOtherSubClasses = otherSubClasses;
	}

	public Collection<JavaClassUnit> getSuperOtherSubClasses() {
		return superOtherSubClasses;
	}

	public void setAbstractMethods(Collection<Method> abstractMethods) {
		this.abstractMethods = abstractMethods;
	}

	public Collection<Method> getAbstractMethods() {
		return abstractMethods;
	}

	public void setSubClasses(Collection<JavaClassUnit> subClasses) {
		this.subClasses = subClasses;
	}

	public Collection<JavaClassUnit> getSubClasses() {
		return subClasses;
	}
}
