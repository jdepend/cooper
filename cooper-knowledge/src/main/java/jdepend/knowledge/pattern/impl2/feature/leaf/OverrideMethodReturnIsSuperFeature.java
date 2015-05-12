package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class OverrideMethodReturnIsSuperFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getOverrideMethods() != null) {
			Collection<Method> methods = new ArrayList<Method>();
			for (Method method : context.getOverrideMethods().keySet()) {
				for (JavaClassUnit javaClass : method.getReturnClassTypes()) {
					if (javaClass.getSubClasses().size() > 0) {
						methods.add(method);
					}
				}
			}
			if (methods.size() > 0) {
				context.setReturnIsSuperOverrideMethods(methods);
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "覆盖了父类的方法返回值存在子类";
	}

}
