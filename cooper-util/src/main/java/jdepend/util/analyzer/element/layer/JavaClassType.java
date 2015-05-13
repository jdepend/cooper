package jdepend.util.analyzer.element.layer;

import java.util.Collection;
import java.util.List;

import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassUnit;

public class JavaClassType {

	private String name;

	private List<String> superClassNames;

	private transient Collection<JavaClassUnit> javaClasses;

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
			if (isJavaClassType(javaClass)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 确定指定Class是否属于某一类型
	 * 
	 * @param theJavaClass
	 * @param superClassNames
	 * @return
	 */
	public boolean isJavaClassType(JavaClass theJavaClass) {

		Collection<JavaClass> supers = theJavaClass.getSupers();

		for (JavaClass superClass : supers) {
			if (superClassNames.contains(superClass.getName())) {
				return true;
			}
		}
		return false;
	}

	protected List<String> getSupers() {
		return superClassNames;
	}

	public void setJavaClasses(Collection<JavaClassUnit> javaClasses) {
		this.javaClasses = javaClasses;
	}

	public Collection<JavaClassUnit> getJavaClasses() {
		if (this.javaClasses == null) {
			this.javaClasses = JDependUnitMgr.getInstance().getResult().getClasses();
		}
		return javaClasses;
	}

}
