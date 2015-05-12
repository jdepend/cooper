package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class PrototypeIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		Collection<JavaClassUnit> superClasses;
		boolean found;
		Method cloneMethod;
		Collection<String> returns;
		for (JavaClassUnit javaClass : javaClasses) {
			found = false;
			cloneMethod = null;
			// 计算存在父类的JavaClass
			superClasses = javaClass.getSupers();
			if (superClasses != null && superClasses.size() > 0) {
				// 查找克隆方法
				L: for (Method method : javaClass.getOverrideMethods()) {
					// 判断返回值是否是所在的Class的父类
					returns = method.getReturnTypes();
					if (returns != null && returns.size() > 0) {
						for (String returnT : returns) {
							for (JavaClassUnit returnType : javaClasses) {
								if (returnType.getName().equals(returnT)) {
									if (javaClass.getSupers().contains(returnType)) {
										found = true;
										cloneMethod = method;
										break L;
									}
								}
							}
						}
					}
				}
			}
			if (found) {
				rtn.add(new PatternInfo(javaClass, javaClass.getName() + "." + cloneMethod.getName()));
			}
		}
		return rtn;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>克隆模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、存在父类；2、克隆方法覆盖了父类方法；3、返回类型是自己的父类。<br><br>");

		return explain.toString();
	}

}
