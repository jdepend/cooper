package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.Collection;
import java.util.Map;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Method;

public class OverrideMethodFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getAllSupers() == null) {
			return false;
		} else {
			Map<Method, Collection<Method>> overrideMethods = context.getCurrent().calOverrideMethods();
			if (overrideMethods != null && overrideMethods.size() > 0) {
				context.setOverrideMethods(overrideMethods);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String getName() {
		return "覆盖了父类方法";
	}

}
