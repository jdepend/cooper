package jdepend.metadata.relationtype;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassDetail;
import jdepend.metadata.Method;
import jdepend.metadata.util.JavaClassCollection;

public class VariableRelation extends BaseJavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 491333186837514572L;

	public VariableRelation(float intensity) {
		super(JavaClassRelationTypes.Variable, intensity);
	}

	@Override
	public boolean canAbstraction() {
		return false;
	}

	@Override
	public boolean invokeRelated() {
		return true;
	}

	@Override
	public boolean create(JavaClass javaClass, JavaClassCollection javaClasses) {

		boolean isCreate = false;
		JavaClassDetail info = javaClass.getDetail();

		if (info.getAttributeClasses().size() != 0) {
			for (JavaClass attributeClass : info.getAttributeClasses()) {
				// 分析该属性是包含关系还是调用关系
				if (!info.getReturnTypes().contains(attributeClass.getName())) {
					if (setDependInfo(javaClass, attributeClass, this)) {
						isCreate = true;
					}
				}
			}
		}

		if (info.getVariableTypes().size() != 0) {
			for (String variableType : info.getVariableTypes()) {
				JavaClass dependJavaClass = javaClasses.getTheClass(javaClass.getPlace(), variableType);
				if (setDependInfo(javaClass, dependJavaClass, this)) {
					isCreate = true;
				}
			}
		}

		return isCreate;
	}
}
