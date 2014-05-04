package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jdepend.model.Attribute;
import jdepend.model.Component;
import jdepend.model.InvokeItem;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.JavaPackage;
import jdepend.model.Method;

public class JavaClassUtil {

	/**
	 * 确定指定Class是否属于某一类型
	 * 
	 * @param theJavaClass
	 * @param superClassNames
	 * @return
	 */
	public static boolean isJavaClassType(JavaClass theJavaClass, List<String> superClassNames) {

		Collection<JavaClass> supers = theJavaClass.getSupers();

		for (JavaClass superClass : supers) {
			if (superClassNames.contains(superClass.getName())) {
				return true;
			}
		}
		return false;
	}

	public static Collection<JavaPackage> getJavaPackages(Collection<Component> units) {
		Collection<JavaPackage> javaPackages = new ArrayList<JavaPackage>();
		for (Component unit : units) {
			for (JavaPackage javaPackage : unit.getJavaPackages()) {
				if (!javaPackages.contains(javaPackage)) {
					javaPackages.add(javaPackage);
				}
			}

		}
		return javaPackages;

	}

	public static Collection<JavaClass> getClasses(Collection<Component> units) {
		Collection<JavaClass> javaClasses = new ArrayList<JavaClass>();
		for (JDependUnit unit : units) {
			javaClasses.addAll(unit.getClasses());
		}
		return javaClasses;
	}

	public static Map<String, JavaClass> getMapClasses(Collection<Component> units) {
		Map<String, JavaClass> javaClasses = new HashMap<String, JavaClass>();
		for (JDependUnit unit : units) {
			for (JavaClass javaClass : unit.getClasses()) {
				javaClasses.put(javaClass.getName(), javaClass);
			}
		}
		return javaClasses;
	}

	public static Collection<JavaClass> getClassesForJavaPackages(Collection<JavaPackage> javaPackages) {
		Collection<JavaClass> javaClasses = new ArrayList<JavaClass>();
		for (JavaPackage javaPackage : javaPackages) {
			javaClasses.addAll(javaPackage.getClasses());
		}
		return javaClasses;
	}

	public static void supplyJavaClassRelationItem(Map<String, JavaClass> classes) {

		Iterator<JavaClassRelationItem> it;
		JavaClassRelationItem relationItem;
		JavaClass dependClass;
		for (JavaClass javaClass : classes.values()) {
			it = javaClass.getCaItems().iterator();
			while (it.hasNext()) {
				relationItem = it.next();
				dependClass = classes.get(relationItem.getDependJavaClass());
				if (dependClass != null) {
					relationItem.setDepend(dependClass);
					relationItem.setCurrent(javaClass);
				} else {
					it.remove();
				}
			}
			it = javaClass.getCeItems().iterator();
			while (it.hasNext()) {
				relationItem = it.next();
				dependClass = classes.get(relationItem.getDependJavaClass());
				if (dependClass != null) {
					relationItem.setDepend(dependClass);
					relationItem.setCurrent(javaClass);
				} else {
					it.remove();
				}
			}
		}

	}

	public static void supplyJavaClassRelationItem(Collection<JavaClass> javaClasses) {
		Map<String, JavaClass> classes = new HashMap<String, JavaClass>();
		for (JavaClass javaClass : javaClasses) {
			classes.put(javaClass.getName(), javaClass);
		}
		supplyJavaClassRelationItem(classes);
	}

	/**
	 * 将JavaClassDetail中的字符串信息填充为对象引用
	 * 
	 * @param javaClasses
	 */
	public static void supplyJavaClassDetail(Map<String, JavaClass> javaClasses) {

		Iterator<InvokeItem> it;
		InvokeItem invokeItem;

		Collection<JavaClass> attributeTypes;
		JavaClass attributeTypeClass;

		Collection<JavaClass> argumentTypes;
		JavaClass argumentTypeClass;

		Collection<JavaClass> returnTypes;
		JavaClass returnTypeClass;

		Collection<JavaClass> classes = javaClasses.values();

		for (JavaClass javaClass : classes) {
			// 填充superClass和interfaces
			JavaClass superClass = javaClasses.get(javaClass.getDetail().getSuperClassName());
			if (superClass != null) {
				javaClass.getDetail().setSuperClass(superClass);
			} else {
				javaClass.getDetail().setSuperClassName(null);
			}
			Collection<JavaClass> interfaces = new HashSet<JavaClass>();
			Collection<String> interfaceNames = new ArrayList<String>();
			for (String interfaceName : javaClass.getDetail().getInterfaceNames()) {
				JavaClass interfaceClass = javaClasses.get(interfaceName);
				if (interfaceClass != null) {
					interfaces.add(interfaceClass);
					interfaceNames.add(interfaceName);
				}
			}
			javaClass.getDetail().setInterfaces(interfaces);
			javaClass.getDetail().setInterfaceNames(interfaceNames);

			// 填充Attribute中的JavaClass
			for (Attribute attribute : javaClass.getAttributes()) {
				attributeTypes = new HashSet<JavaClass>();
				for (String type : attribute.getTypes()) {
					attributeTypeClass = javaClasses.get(type);
					if (attributeTypeClass != null) {
						attributeTypes.add(attributeTypeClass);
					}
				}
				attribute.setTypeClasses(attributeTypes);
			}

			// 填充Method中的JavaClass
			for (Method method : javaClass.getSelfMethods()) {
				// 填充参数
				argumentTypes = new HashSet<JavaClass>();
				for (String type : method.getArgumentTypes()) {
					argumentTypeClass = javaClasses.get(type);
					if (argumentTypeClass != null) {
						argumentTypes.add(argumentTypeClass);
					}
				}
				method.setArgClassTypes(argumentTypes);
				// 填充返回值
				returnTypes = new HashSet<JavaClass>();
				for (String type : method.getReturnTypes()) {
					returnTypeClass = javaClasses.get(type);
					if (returnTypeClass != null) {
						returnTypes.add(returnTypeClass);
					}
				}
				method.setReturnClassTypes(returnTypes);
			}
		}

		// 填充Method中的InvokeItem中的Method
		for (JavaClass javaClass : classes) {
			for (Method method : javaClass.getSelfMethods()) {
				it = method.getInvokeItems().iterator();
				while (it.hasNext()) {
					invokeItem = it.next();
					if (!invokeItem.supplyMethod(javaClasses)) {
						it.remove();
					}
				}
			}
		}
	}

	/**
	 * 将JavaClassDetail中的字符串信息填充为对象引用
	 * 
	 * @param javaClasses
	 */
	public static void supplyJavaClassDetail(Collection<JavaClass> javaClasses) {
		Map<String, JavaClass> javaClassesForName = new HashMap<String, JavaClass>();
		for (JavaClass javaClass : javaClasses) {
			javaClassesForName.put(javaClass.getName(), javaClass);
		}
		supplyJavaClassDetail(javaClassesForName);
	}
}
