package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class ProxyIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>代理模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、存在父类；2、父类存在其他子类；3、存在覆盖了父类的方法；4、方法使用了其他子类的相同方法。<br><br>");
		return explain.toString();
	}

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {
		Collection<JavaClassUnit> superClasses;
		Collection<JavaClassUnit> otherSubClasses;
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		PatternInfo rtnItem;
		for (JavaClassUnit javaClass : javaClasses) {
			// 计算存在父类的JavaClasses
			superClasses = javaClass.getSupers();
			if (superClasses != null && superClasses.size() > 0) {
				otherSubClasses = new HashSet<JavaClassUnit>();
				M: for (JavaClassUnit superClass : superClasses) {
					for (JavaClassUnit subClass : superClass.getSubClasses()) {
						if (!subClass.equals(javaClass) && !otherSubClasses.contains(subClass)) {
							otherSubClasses.add(subClass);
						}
					}
				}
				if (otherSubClasses.size() > 0) {
					// 搜索代理方法
					L: for (Method method : javaClass.getOverrideMethods()) {
						for (InvokeItem item : method.getInvokeItems()) {
							if (otherSubClasses.contains(item.getCallee().getJavaClass())) {
								for (Method superMethod : javaClass.getOverridedMethods(method)) {
									if (item.math2(superMethod)) {
										rtnItem = new PatternInfo(javaClass, javaClass.getName() + "."
												+ item.getCallee().getName());
										if (!rtn.contains(rtnItem)) {
											rtn.add(rtnItem);
										}
										break L;
									}
								}
							}
						}
					}
				}
			}
		}
		return rtn;
	}

}
