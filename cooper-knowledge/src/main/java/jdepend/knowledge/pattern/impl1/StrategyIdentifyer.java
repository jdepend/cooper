package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public final class StrategyIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>策略模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、本身是接口或抽象类；2、存在多个子类；3、子类覆盖了父类方法；4、父类方法是抽象公开方法；5、子类方法大于等于5行。<br><br>");
		return explain.toString();
	}

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		for (JavaClassUnit javaClass : javaClasses) {
			if (javaClass.isAbstract()) {
				Collection<JavaClassUnit> subClasses = javaClass.getSubClasses();
				if (subClasses.size() > 1) {
					L: for (JavaClassUnit subClass : subClasses) {
						for (Method method : subClass.getSelfMethods()) {
							if (!method.isConstruction() && method.getSelfLineCount() >= 5) {
								for (Method superMethod : javaClass.getSelfMethods()) {
									if (superMethod.isAbstract() && superMethod.isPublic()
											&& method.isOverride(superMethod)) {
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
