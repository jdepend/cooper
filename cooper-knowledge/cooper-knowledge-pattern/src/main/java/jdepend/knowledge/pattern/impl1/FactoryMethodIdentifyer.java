package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.metadata.JavaClass;
import jdepend.metadata.Method;
import jdepend.model.JavaClassUnit;

public final class FactoryMethodIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		Collection<JavaClass> superClasses;
		boolean found;
		Method factoryMethod;
		for (JavaClassUnit javaClass : javaClasses) {
			found = false;
			factoryMethod = null;
			// 计算存在父类的JavaClass
			superClasses = javaClass.getJavaClass().getSupers();
			if (superClasses != null && superClasses.size() > 0) {
				// 查找工厂方法
				L: for (Method method : javaClass.getJavaClass().getOverrideMethods()) {
					// 判断返回值是否是某一类型的父类，又不是所在的Class的父类
					if (method.getReturnTypes().size() == 1) {
						for (JavaClass returnType : method.getReturnClassTypes()) {
							if (returnType.getSubClasses().size() > 0
									&& !javaClass.getJavaClass().getSupers().contains(returnType)) {
								found = true;
								factoryMethod = method;
								break L;
							}
						}
					}
				}
			}
			if (found) {
				rtn.add(new PatternInfo(javaClass.getJavaClass(), javaClass.getName() + "." + factoryMethod.getName()));
			}
		}
		return rtn;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>工厂模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、存在父类；2、工厂方法覆盖了父类方法；3、返回类型有子类；4、返回类型只有一个；5、返回类型不是自己的父类。<br><br>");
		return explain.toString();
	}

}
