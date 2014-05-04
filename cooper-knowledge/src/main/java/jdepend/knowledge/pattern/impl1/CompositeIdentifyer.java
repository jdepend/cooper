package jdepend.knowledge.pattern.impl1;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.PatternInfo;
import jdepend.model.Attribute;
import jdepend.model.JavaClass;

public final class CompositeIdentifyer extends AbstractPatternIdentifyer {

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>组合模式</strong><br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;1、存在父类（可能不止一层）；2、属性有父类类型，并且是集合类型；3、父类还有其他子类。<br><br>");
		return explain.toString();
	}

	@Override
	public Collection<PatternInfo> identify(Collection<JavaClass> javaClasses) {
		Collection<JavaClass> superClasses;
		Attribute compositeAttribute;
		Collection<PatternInfo> rtn = new ArrayList<PatternInfo>();
		for (JavaClass javaClass : javaClasses) {
			// 计算存在父类的JavaClasses
			superClasses = javaClass.getSupers();
			if (superClasses != null && superClasses.size() > 0) {
				compositeAttribute = null;
				// 搜索属性
				for (Attribute attribute : javaClass.getAttributes()) {
					if (attribute.existCollectionType()) {
						for (JavaClass superClass : superClasses) {
							if (attribute.getTypes().contains(superClass.getName())) {
								compositeAttribute = attribute;
							}
						}
					}
				}
				// 搜索其他子类
				if (compositeAttribute != null) {
					L: for (JavaClass superClass : superClasses) {
						if (!superClass.getSubClasses().equals(javaClass)) {
							rtn.add(new PatternInfo(javaClass, javaClass.getName() + "[" + compositeAttribute.getName()
									+ "]"));
							break L;
						}
					}
				}
			}
		}
		return rtn;
	}
}
