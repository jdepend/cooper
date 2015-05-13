package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class TemplateMethodIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>模板方法</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、自己是抽象类；2、存在protected的抽象方法；3、存在public的调用这些抽象方法的方法；4、存在子类，并覆盖了抽象方法。<br><br>");
		return explain.toString();
	}

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		Method abstractMethod;
		PatternInfo rtnItem;
		for (JavaClassUnit javaClass : javaClasses) {
			// 判断是否是抽象类
			if (javaClass.getJavaClass().isAbstract()) {
				// 查找抽象方法
				abstractMethod = null;
				for (Method method : javaClass.getJavaClass().getSelfMethods()) {
					if (method.isAbstract() && method.isProtected()) {
						abstractMethod = method;
						// 查找调用抽象方法的公开方法
						for (Method publicMethod : javaClass.getJavaClass().getSelfMethods()) {
							if (!publicMethod.isAbstract() && publicMethod.isPublic()) {
								for (InvokeItem item : publicMethod.getInvokeItems()) {
									if (item.getCallee().equals(abstractMethod)) {
										// 查找是否存在子类，并覆盖了抽象方法
										for (JavaClass subClass : javaClass.getJavaClass().getSubClasses()) {
											if (subClass.getOverridedMethods().contains(abstractMethod)) {
												rtnItem = new PatternInfo(javaClass.getJavaClass(), javaClass.getName() + "."
														+ publicMethod.getName());
												if (!rtn.contains(rtnItem)) {
													rtn.add(rtnItem);
												}
											}
										}

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
