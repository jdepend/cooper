package jdepend.knowledge.pattern.impl2.feature.leaf;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;

public class NotEnumTypeFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getCurrent().isEnum()) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public String getName() {
		return "不是枚举类型";
	}

}
