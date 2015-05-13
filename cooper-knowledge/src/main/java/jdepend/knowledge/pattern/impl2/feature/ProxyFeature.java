package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.metadata.InvokeItem;
import jdepend.metadata.Method;

public class ProxyFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getOverrideMethods() == null || context.getSuperOtherSubClasses() == null) {
			return false;
		} else {

			for (Method method : context.getOverrideMethods().keySet()) {
				for (Method superMethod : context.getOverrideMethods().get(method)) {
					for (InvokeItem item : method.getInvokeItems()) {
						if (context.getSuperOtherSubClasses().contains(item.getCallee().getJavaClass())
								&& item.math2(superMethod)) {
							this.setPatternInfo(item.getCallee().getName());
							return true;
						}
					}
				}
			}
			return false;
		}

	}

	@Override
	public String getName() {
		return "在覆盖的方法中调用了其他子类的覆盖方法";
	}

}
