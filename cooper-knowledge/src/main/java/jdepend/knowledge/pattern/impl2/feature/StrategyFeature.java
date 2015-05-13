package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public class StrategyFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getAbstractMethods() != null && context.getSubClasses() != null) {
			for (JavaClass subClass : context.getSubClasses()) {
				for (Method method : subClass.getSelfMethods()) {
					if (!method.isConstruction() && method.getSelfLineCount() >= 5) {
						for (Method superMethod : context.getAbstractMethods()) {
							if (method.isOverride(superMethod)) {
								this.setPatternInfo(method.getName());
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "覆盖了父类抽象方法的子类方法大于等于5行";
	}

}
