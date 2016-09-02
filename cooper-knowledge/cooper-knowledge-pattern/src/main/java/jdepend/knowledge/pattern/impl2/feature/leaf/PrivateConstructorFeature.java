package jdepend.knowledge.pattern.impl2.feature.leaf;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.metadata.Method;

public class PrivateConstructorFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		// 查找public构造方法
		for (Method method : context.getCurrent().getSelfMethods()) {
			if (method.isConstruction()) {
				if (!method.isPrivate()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getName() {
		return "全部为私有构造方法";
	}

}
