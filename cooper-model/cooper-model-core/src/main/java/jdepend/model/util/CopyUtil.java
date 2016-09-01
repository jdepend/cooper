package jdepend.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaPackage;
import jdepend.metadata.util.JavaClassCollection;
import jdepend.metadata.util.JavaClassUtil;
import jdepend.model.Component;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisRunningContext;

public final class CopyUtil {

	private List<Component> targets = new ArrayList<Component>();
	private Map<String, JavaClassUnit> javaClasses = new HashMap<String, JavaClassUnit>();

	public List<Component> copy(AnalysisRunningContext runningContext, List<Component> components) {
		// 创建JavaClassUnit
		for (Component component : components) {
			for (JavaClassUnit javaClass : component.getClasses()) {
				if (javaClasses.get(javaClass.getId()) == null) {
					newJavaClass(javaClass);
				}
			}
		}
		// 生成JavaClasses
		Map<String, JavaClass> newJavaClasses = new HashMap<String, JavaClass>();
		for (String id : javaClasses.keySet()) {
			newJavaClasses.put(id, javaClasses.get(id).getJavaClass());
		}

		JavaClassCollection jClasses = new JavaClassCollection(runningContext.getProfileFacade()
				.getJavaClassRelationItemProfile().getJavaClassRelationTypes(), newJavaClasses.values());
		// 补充JavaClassRelationItem的Current和Depend
		JavaClassUtil.supplyJavaClassRelationItem(jClasses);
		// 将JavaClassDetail中的字符串信息填充为对象引用
		JavaClassUtil.supplyJavaClassDetail(jClasses);

		// 创建JavaPackage
		for (Component component : components) {
			for (JavaPackage javaPackage : component.getJavaPackages()) {
				try {
					javaPackage.clone(newJavaClasses);
				} catch (JDependException e) {
					e.printStackTrace();
				}
			}
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

	private JavaClassUnit newJavaClass(JavaClassUnit javaClass) {
		JavaClassUnit newJavaClass = javaClasses.get(javaClass.getId());

		if (newJavaClass == null) {
			newJavaClass = javaClass.clone();
			javaClasses.put(newJavaClass.getId(), newJavaClass);
			// 添加内部类
			for (JavaClassUnit innerClass : newJavaClass.getInnerClassUnits()) {
				javaClasses.put(innerClass.getId(), innerClass);
			}
		}
		return newJavaClass;
	}
}
