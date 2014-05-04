package jdepend.model;

import java.util.Collection;
import java.util.List;

import jdepend.model.util.JavaClassUtil;

public class JavaClassType {

	private String name;

	private List<String> superClassNames;

	private transient Collection<JavaClass> javaClasses;

	private String endWith;

	public JavaClassType() {
		super();
	}

	public JavaClassType(String name, List<String> superClassNames) {
		this.name = name;
		this.superClassNames = superClassNames;
	}

	public JavaClassType(String name, String endWith) {
		this.name = name;
		this.endWith = endWith;
	}

	public JavaClassType(String name, List<String> superClassNames, String endWith) {
		this.name = name;
		this.superClassNames = superClassNames;
		this.endWith = endWith;
	}

	public String getName() {
		return name;
	}

	public boolean isMember(JavaClass javaClass) {
		if (this.endWith != null && this.endWith.length() != 0) {
			if (javaClass.getName().endsWith(this.endWith)) {
				return true;
			}
		}

		if (this.superClassNames != null) {
			if (JavaClassUtil.isJavaClassType(javaClass, superClassNames)) {
				return true;
			}
		}

		return false;
	}

	protected List<String> getSupers() {
		return superClassNames;
	}

	public void setJavaClasses(Collection<JavaClass> javaClasses) {
		this.javaClasses = javaClasses;
	}

	public Collection<JavaClass> getJavaClasses() {
		if (this.javaClasses == null) {
			this.javaClasses = JDependUnitMgr.getInstance().getClasses();
		}
		return javaClasses;
	}

}
