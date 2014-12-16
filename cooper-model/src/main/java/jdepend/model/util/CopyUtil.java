package jdepend.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;
import jdepend.model.component.modelconf.CandidateUtil;

public final class CopyUtil {

	private List<Component> targets = new ArrayList<Component>();
	private Map<String, JavaPackage> javaPackages = new HashMap<String, JavaPackage>();
	private Map<String, JavaClass> javaClasses = new HashMap<String, JavaClass>();

	public List<Component> copy(List<Component> components) {
		// 创建JavaClass
		for (Component component : components) {
			for (JavaClass javaClass : component.getClasses()) {
				if (javaClasses.get(javaClass.getId()) == null) {
					newJavaClass(javaClass);
				}
			}
		}
		JavaClassCollection jClasses = new JavaClassCollection(javaClasses.values());
		// 补充JavaClassRelationItem的Current和Depend
		JavaClassUtil.supplyJavaClassRelationItem(jClasses);
		// 将JavaClassDetail中的字符串信息填充为对象引用
		JavaClassUtil.supplyJavaClassDetail(jClasses);

		// 创建JavaPackage
		for (Component component : components) {
			for (JavaClass javaClass : component.getClasses()) {
				if (javaPackages.get(javaClass.getJavaPackage().getId()) == null) {
					newJavaPackage(javaClass.getJavaPackage());
				}
			}
		}

		// 关联JavaClass和JavaPackage
		JavaPackage javaPackage;
		for (JavaClass javaClass : jClasses.getJavaClasses()) {
			javaPackage = javaPackages.get(CandidateUtil.getId(javaClass.getPlace(), javaClass.getPackageName()));
			javaClass.setJavaPackage(javaPackage);
			javaPackage.addClass(javaClass);
		}

		// 创建Component
		for (Component component : components) {
			try {
				targets.add(component.clone(javaClasses));
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}
		return targets;

	}

	private JavaPackage newJavaPackage(JavaPackage javaPackage) {

		JavaPackage newjavaPackage = javaPackages.get(javaPackage.getId());
		if (newjavaPackage == null) {
			newjavaPackage = new JavaPackage(javaPackage.getPlace(), javaPackage.getName());
			javaPackages.put(newjavaPackage.getId(), newjavaPackage);
		}
		return newjavaPackage;
	}

	private JavaClass newJavaClass(JavaClass javaClass) {
		JavaClass newJavaClass = javaClasses.get(javaClass.getId());

		if (newJavaClass == null) {
			newJavaClass = javaClass.clone();
			javaClasses.put(newJavaClass.getId(), newJavaClass);
		}
		return newJavaClass;
	}
}
