package jdepend.knowledge.pattern.impl2.feature.leaf;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;

public class AbstractClassFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {

		if (context.getCurrent().isAbstract()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "本身是接口或抽象类";
	}

}
