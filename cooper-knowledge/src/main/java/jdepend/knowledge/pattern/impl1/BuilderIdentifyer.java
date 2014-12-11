package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.Attribute;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public final class BuilderIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClass> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();

		for (JavaClass javaClass : javaClasses) {
			L: for (Attribute attribute : javaClass.getAttributes()) {
				for (JavaClass builder : attribute.getTypeClasses()) {
					// 识别builder接口
					if (!builder.equals(javaClass) && builder.isAbstract() && builder.getSubClasses().size() > 1) {
						for (Method method : javaClass.getSelfMethods()) {
							// 识别builderMethod
							if (method.getReturnTypes().size() == 1 && method.getReturnClassTypes().size() == 1) {
								JavaClass productType = method.getReturnClassTypes().iterator().next();
								for (InvokeItem invokeItem : method.getInvokeItems()) {
									Method invokeMethod = invokeItem.getMethod();
									if (invokeMethod.getJavaClass().equals(builder)
											&& invokeMethod.getReturnTypes().size() == 1
											&& invokeMethod.getReturnClassTypes().size() == 1) {
										if (invokeMethod.getReturnClassTypes().iterator().next().equals(productType)) {
											rtn.add(new PatternInfo(javaClass, attribute.getName() + "."
													+ invokeItem.getMethod().getName()));
											break L;
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

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>构建模式</strong><br>");
		explain
				.append("&nbsp;&nbsp;&nbsp;&nbsp;1、存在Builder接口（作为属性）；2、存在一个以上实现；3、类方法中调用了Builder的方法；5、两个方法返回相同的数据类型。<br><br>");
		return explain.toString();
	}

}
