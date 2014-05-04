package jdepend.knowledge.pattern.impl2.feature.leaf;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public class ConstructorArgIsSuperFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getSupers() == null) {
			return false;
		} else {
			for (Method method : context.getCurrent().getSelfMethods()) {
				if (method.isConstruction()) {
					for (JavaClass argClass : method.getArgClassTypes()) {
						if (context.getSupers().contains(argClass)) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	@Override
	public String getName() {
		return "构造方法参数有父类";
	}

}
