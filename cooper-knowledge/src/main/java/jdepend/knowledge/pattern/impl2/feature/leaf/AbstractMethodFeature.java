package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Method;

public class AbstractMethodFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {

		Collection<Method> abstractMethods = new ArrayList<Method>();
		for (Method method : context.getCurrent().getSelfMethods()) {
			if (method.isAbstract()) {
				abstractMethods.add(method);
			}
		}
		if (abstractMethods.size() > 0) {
			context.setAbstractMethods(abstractMethods);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在抽象方法";
	}

}
