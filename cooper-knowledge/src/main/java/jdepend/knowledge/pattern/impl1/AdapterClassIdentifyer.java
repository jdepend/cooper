package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public final class AdapterClassIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClass> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		Collection<JavaClass> interfaces;
		Collection<JavaClass> supers;
		JavaClass theInterfaceClass;
		JavaClass theSuperClass;

		for (JavaClass javaClass : javaClasses) {
			// 收集接口和抽象类
			interfaces = new HashSet<JavaClass>();
			supers = new HashSet<JavaClass>();
			for (JavaClass superClass : javaClass.getSupers()) {
				if (superClass.isInterface()) {
					interfaces.add(superClass);
				} else {
					supers.add(superClass);
				}
			}

			if (interfaces.size() > 0 && supers.size() > 0) {
				for (Method method : javaClass.getOverrideMethods().keySet()) {
					theInterfaceClass = javaClass.getOverrideMethods().get(method).getJavaClass();

					if (interfaces.contains(theInterfaceClass)) {
						for (InvokeItem item : method.getInvokeItems()) {
							theSuperClass = item.getMethod().getJavaClass();
							if (supers.contains(theSuperClass)) {
								if (!theSuperClass.getSupers().contains(theInterfaceClass)) {
									rtn.add(new PatternInfo(javaClass, javaClass.getName() + "." + method.getName()));
								}
							}
						}
					}
				}
			}

		}
		return rtn;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>适配器Class模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、存在父类，并实现了接口；2、在实现的接口方法中调用了继承的父类方法；3、父类与接口没有实现关系<br><br>");
		return explain.toString();
	}

}
