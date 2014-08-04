package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;
import jdepend.model.Method;

public final class CopyUtil {

	private List<Component> targets = new ArrayList<Component>();
	private Map<String, JavaPackage> javaPackages = new HashMap<String, JavaPackage>();
	private Map<String, JavaClass> javaClasses = new HashMap<String, JavaClass>();

	public List<Component> copy(List<Component> components) {
		// 创建JavaClass
		for (Component component : components) {
			for (JavaClass javaClass : component.getClasses()) {
				if (javaClasses.get(javaClass.getName()) == null) {
					newJavaClass(javaClass);
				}
			}
		}
		// 补充JavaClassRelationItem的Current和Depend
		JavaClassUtil.supplyJavaClassRelationItem(javaClasses);
		// 将JavaClassDetail中的字符串信息填充为对象引用
		JavaClassUtil.supplyJavaClassDetail(javaClasses);

		// 创建JavaPackage
		for (Component component : components) {
			for (JavaClass javaClass : component.getClasses()) {
				if (javaPackages.get(javaClass.getJavaPackage().getName()) == null) {
					newJavaPackage(javaClass.getJavaPackage());
				}
			}
		}

		// 关联JavaClass和JavaPackage
		JavaPackage javaPackage;
		for (JavaClass javaClass : javaClasses.values()) {
			javaPackage = javaPackages.get(javaClass.getPackageName());
			javaClass.setJavaPackage(javaPackage);
			javaPackage.addClass(javaClass);
		}

		// 创建Component
		Collection<JavaClass> classes = javaClasses.values();
		for (Component component : components) {
			try {
				targets.add(component.clone(classes));
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}
		return targets;

	}

	private JavaPackage newJavaPackage(JavaPackage javaPackage) {

		JavaPackage newjavaPackage = javaPackages.get(javaPackage.getName());
		if (newjavaPackage == null) {
			newjavaPackage = new JavaPackage(javaPackage.getName());
			javaPackages.put(newjavaPackage.getName(), newjavaPackage);
		}
		return newjavaPackage;
	}

	private JavaClass newJavaClass(JavaClass javaClass) {
		JavaClass newJavaClass = javaClasses.get(javaClass.getName());

		if (newJavaClass == null) {
			newJavaClass = javaClass.clone();
			javaClasses.put(newJavaClass.getName(), newJavaClass);

			for (Method name : javaClass.getSelfMethods()) {
				newJavaClass.addSelfMethod(newMethod(name));
			}
		}
		return newJavaClass;
	}

	private Method newMethod(Method method) {
		return new Method(newJavaClass(method.getJavaClass()), method);

	}
}
