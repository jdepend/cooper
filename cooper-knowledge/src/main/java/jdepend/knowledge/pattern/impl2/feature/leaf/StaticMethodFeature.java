package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Method;

public class StaticMethodFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		Collection<Method> staticMethods = new ArrayList<Method>();
		for (Method method : context.getCurrent().getSelfMethods()) {
			if (method.isStatic()) {
				staticMethods.add(method);
			}
		}
		if (staticMethods.size() > 0) {
			context.setStaticMethods(staticMethods);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在静态方法";
	}

}
