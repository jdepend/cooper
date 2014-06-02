package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public final class StateIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>状态模式</strong><br>");
		explain
				.append("&nbsp;&nbsp;&nbsp;&nbsp;1、本身是接口或抽象类；2、存在多个子类；3、子类覆盖了父类方法；4、父类方法是抽象方法；5、该方法返回值是父类；6、父类是有状态的。<br><br>");
		return explain.toString();
	}

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClass> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		for (JavaClass javaClass : javaClasses) {
			if (javaClass.isAbstract() && javaClass.isState()) {
				Collection<JavaClass> subClasses = javaClass.getSubClasses();
				if (subClasses.size() > 1) {
					L: for (JavaClass subClass : subClasses) {
						for (Method method : subClass.getSelfMethods()) {
							if (!method.isConstruction() && method.getReturnTypes().size() == 1
									&& method.getReturnClassTypes().size() == 1
									&& method.getReturnClassTypes().iterator().next().equals(javaClass)) {
								for (Method superMethod : javaClass.getSelfMethods()) {
									if (superMethod.isAbstract() && method.isOverride(superMethod)) {
										rtn
												.add(new PatternInfo(javaClass, javaClass.getName() + "."
														+ method.getName()));
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
