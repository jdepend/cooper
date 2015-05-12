package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class PrototypeFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getReturnIsSuperOverrideMethods() != null) {
			for (Method method : context.getReturnIsSuperOverrideMethods()) {
				if (method.getReturnTypes().size() == 1) {
					for (JavaClassUnit javaClass : method.getReturnClassTypes()) {
						if (context.getCurrent().getSupers().contains(javaClass)) {
							this.setPatternInfo(method.getName());
							return true;
						}
					}
				}
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "覆盖了父类的方法返回值类型是自己的父类";
	}

}
