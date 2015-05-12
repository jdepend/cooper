package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Attribute;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class FlyweightFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getHaveSubClassesCollectionAttributes() == null) {
			return false;
		} else {
			for (Attribute attribute : context.getHaveSubClassesCollectionAttributes()) {
				for (Method method : context.getCurrent().getSelfMethods()) {
					for (JavaClassUnit rtnClass : method.getReturnClassTypes()) {
						if (attribute.getTypeClasses().contains(rtnClass)) {
							if (method.getArgumentCount() > 0) {
								if (method.getReadFields().contains(attribute.getName())) {
									this.setPatternInfo(attribute.getName());
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在返回集合有子类属性的方法，并且存在参数 方法内部使用了该集合属性";
	}

}
