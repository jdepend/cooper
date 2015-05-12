package jdepend.parse.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.log.LogUtil;
import jdepend.model.JavaClassUnit;
import jdepend.model.JavaClassDetail;
import jdepend.model.util.ParseUtil;

class IdentifyExtClassesUtil {

	private PackageFilter filter;

	private Map<String, JavaClassUnit> extendJavaClasses;

	private Map<String, JavaClassUnit> parsedClasses;

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
	public Collection<JavaClassUnit> identify(Collection<JavaClassUnit> javaClasses) {

		this.extendJavaClasses = new HashMap<String, JavaClassUnit>();
		this.parsedClasses = new HashMap<String, JavaClassUnit>();
		for (JavaClassUnit javaClass : javaClasses) {
			this.parsedClasses.put(javaClass.getName(), javaClass);
		}

		JavaClassDetail info = null;
		for (JavaClassUnit javaClass : javaClasses) {
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

		Collection<JavaClassUnit> extJavaClasses = this.extendJavaClasses.values();

		LogUtil.getInstance(IdentifyExtClassesUtil.class).systemLog(
				"Identify Ext " + extJavaClasses.size() + " Classes.");

		return extJavaClasses;
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
				JavaClassUnit extendJavaClass = new JavaClassUnit(javaClassName, false);
				extendJavaClass.setPlace("outer");
				extendJavaClass.setPackageName(packageName);
				this.extendJavaClasses.put(extendJavaClass.getName(), extendJavaClass);
			}
		}

	}
}
