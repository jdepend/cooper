package jdepend.knowledge.pattern.impl2.feature.leaf;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;

public class SelfHaveStateFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getCurrent().haveState()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在状态";
	}

}
