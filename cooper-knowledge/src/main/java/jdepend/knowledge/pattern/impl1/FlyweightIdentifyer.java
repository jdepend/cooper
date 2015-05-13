package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.metadata.Attribute;
import jdepend.metadata.JavaClass;
import jdepend.metadata.Method;
import jdepend.model.JavaClassUnit;

public class FlyweightIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>享元模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、存在集合属性；2、属性存在子类；3、存在返回该属性的方法，并且存在参数；4、方法内部使用了集合属性。<br><br>");
		return explain.toString();
	}

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();

		for (JavaClassUnit javaClass : javaClasses) {
			// 是否存在集合属性
			M: for (Attribute attribute : javaClass.getJavaClass().getAttributes()) {
				if (attribute.existCollectionType()) {
					// 是否存在子类
					for (JavaClass attributeClass : attribute.getTypeClasses()) {
						if (attributeClass.getSubClasses().size() > 1) {
							// 是否存在返回属性的方法，并且存在参数;方法内部使用了集合属性
							for (Method method : javaClass.getJavaClass().getSelfMethods()) {
								for (JavaClass rtnClass : method.getReturnClassTypes()) {
									if (attribute.getTypeClasses().contains(rtnClass)) {
										if (method.getArgumentCount() > 0) {
											if (method.getReadFields().contains(attribute)) {
												rtn.add(new PatternInfo(javaClass.getJavaClass(), javaClass.getName() + "."
														+ attribute.getName()));
												break M;
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
