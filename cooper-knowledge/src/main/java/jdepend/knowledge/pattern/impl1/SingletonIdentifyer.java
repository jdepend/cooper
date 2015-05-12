package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.Attribute;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public final class SingletonIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClassUnit> javaClasses) {
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		boolean isSingleton;
		boolean getter;
		for (JavaClassUnit javaClass : javaClasses) {
			if(javaClass.getName().equals("jdepend.model.MetricsMgr")){
				System.out.print("");
			}
			if (!javaClass.isEnum()) {
				isSingleton = true;
				getter = false;
				// 查找private构造方法和static的返回类型方法
				for (Method method : javaClass.getSelfMethods()) {
					if (method.isConstruction()) {
						if (method.isPublic()) {
							isSingleton = false;
						}
					} else if (method.isStatic()) {
						if (method.getReturnTypes().contains(javaClass.getName())) {
							getter = true;
						}
					}
				}
				// 判断是否存在static的实例
				if (isSingleton && getter) {
					for (Attribute attribute : javaClass.getAttributes()) {
						if (attribute.isStatic()) {
							if (attribute.getTypes().contains(javaClass.getName())) {
								rtn.add(new PatternInfo(javaClass, javaClass.getName()));
								break;
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
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>单例模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、全部为私有构造方法；2、存在静态方法，返回类型为自己；3、存在静态属性，类型为自己。<br><br>");
		return explain.toString();
	}
}
