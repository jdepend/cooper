package jdepend.parse.impl;

import java.util.HashMap;
import java.util.Map;

import jdepend.framework.util.ParseUtil;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassDetail;

class IdentifyExtClassesUtil {

	private PackageFilter filter;

	private Map<String, JavaClass> extendJavaClasses;

	private Map<String, JavaClass> parsedClasses;

	public IdentifyExtClassesUtil(PackageFilter filter) {
		super();
		this.filter = filter;
	}
	
	/**
	 * 识别外部Classes
	 * 
	 * @param parsedClasses
	 * @return
	 */
	public Map<String, JavaClass> identify(Map<String, JavaClass> parsedClasses) {

		this.extendJavaClasses = new HashMap<String, JavaClass>();
		this.parsedClasses = parsedClasses;

		JavaClassDetail info = null;
		for (JavaClass javaClass : parsedClasses.values()) {
			if (javaClass.isInner()) {
				info = javaClass.getDetail();
				// 处理父类
				if (info.getSuperClassName() != null) {
					appendExtJavaClass(info.getSuperClassName());
				}

				// 处理接口
				for (String interfaceName : info.getInterfaceNames()) {
					appendExtJavaClass(interfaceName);
				}

				// 处理属性
				for (String attributeType : info.getAttributeTypes()) {
					appendExtJavaClass(attributeType);
				}

				// 处理参数
				for (String paramType : info.getParamTypes()) {
					appendExtJavaClass(paramType);
				}

				// 处理变量
				for (String variableType : info.getVariableTypes()) {
					appendExtJavaClass(variableType);
				}
			}
		}

		return this.extendJavaClasses;
	}

	private void appendExtJavaClass(String javaClassName) {
		if (this.parsedClasses.containsKey(javaClassName)) {
			return;
		}
		if (this.extendJavaClasses.containsKey(javaClassName)) {
			return;
		}
		// 收集外部JavaClass信息
		if (javaClassName.indexOf('$') == -1) {// 不是内部类
			String packageName = ParseUtil.getPackageName(javaClassName);
			if (this.filter.accept(packageName)) {
				JavaClass extendJavaClass = new JavaClass(javaClassName, false);
				extendJavaClass.setPackageName(packageName);
				this.extendJavaClasses.put(extendJavaClass.getName(), extendJavaClass);
			}
		}

	}
}
