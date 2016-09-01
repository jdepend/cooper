package jdepend.model.util;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;

public class JavaClassUnitUtil {

	public static Collection<JavaPackage> getJavaPackages(Collection<Component> components) {
		Collection<JavaPackage> javaPackages = new HashSet<JavaPackage>();
		for (Component component : components) {
			for (JavaPackage javaPackage : component.getJavaPackages()) {
				javaPackages.add(javaPackage);
			}
		}
		return javaPackages;

	}

	public static Collection<JavaClassUnit> getClasses(Collection<Component> components) {
		Collection<JavaClassUnit> javaClasses = new HashSet<JavaClassUnit>();
		for (Component component : components) {
			javaClasses.addAll(component.getClasses());
		}
		return javaClasses;
	}

	public static Collection<JavaClass> getAllClasses(Collection<Component> components) {
		Collection<JavaClass> javaClasses = new HashSet<JavaClass>();
		for (Component component : components) {
			for (JavaClassUnit javaClass : component.getClasses()) {
				javaClasses.add(javaClass.getJavaClass());
				javaClasses.addAll(javaClass.getJavaClass().getInnerClasses());
			}
		}
		return javaClasses;
	}

	public static Collection<JavaClass> getJavaClasses(Collection<JavaClassUnit> javaClassUnits) {
		Collection<JavaClass> classes = new HashSet<JavaClass>();
		for (JavaClassUnit javaClassUnit : javaClassUnits) {
			classes.add(javaClassUnit.getJavaClass());
		}
		return classes;
	}

	/**
	 * 该关系是同一个组件内的关系
	 * 
	 * @return
	 */

	public static boolean isInner(JavaClassRelationItem item, AnalysisResult result) {
		return result.getTheClass(item.getSource().getId()).getComponent()
				.equals(result.getTheClass(item.getTarget().getId()).getComponent());
	}

	/**
	 * 判断该关系是否是组件间的类关系
	 * 
	 * @return
	 */
	public static boolean crossComponent(JavaClassRelationItem item, AnalysisResult result) {
		return !result.getTheClass(item.getSource().getId()).getComponent()
				.containsClass(result.getTheClass(item.getTarget().getId()));
	}
}
