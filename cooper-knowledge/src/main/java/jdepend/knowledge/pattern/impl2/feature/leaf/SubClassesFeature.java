package jdepend.knowledge.pattern.impl2.feature.leaf;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;

public class SubClassesFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getCurrent().getSubClasses().size() > 0) {
			context.setSubClasses(context.getCurrent().getSubClasses());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在多个子类";
	}

}
