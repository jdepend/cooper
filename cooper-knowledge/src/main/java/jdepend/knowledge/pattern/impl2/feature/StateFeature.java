package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Method;

public class StateFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getAbstractMethods() != null && context.getSubClasses() != null) {
			for (Method method : context.getAbstractMethods()) {
				if (!method.isConstruction() && method.getReturnTypes().size() == 1
						&& method.getReturnClassTypes().size() == 1
						&& method.getReturnClassTypes().iterator().next().equals(context.getCurrent())) {
					this.setPatternInfo(method.getName());
					return true;
				}
			}
			return false;
		}
		return false;
	}

	@Override
	public String getName() {
		return "抽象方法返回值是自己";
	}

}
