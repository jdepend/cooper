package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.metadata.Attribute;
import jdepend.metadata.InvokeItem;
import jdepend.metadata.Method;

public class DecoratorFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getOverrideMethods() == null || context.getAbstractAttributes() == null) {
			return false;
		} else {
			for (Method method : context.getOverrideMethods().keySet()) {
				for (Method superMethod : context.getOverrideMethods().get(method)) {
					for (InvokeItem item : method.getInvokeItems()) {
						if (item.getCallee().equals(superMethod)) {
							for (Attribute attribute : context.getAbstractAttributes()) {
								if (method.getReadFields().contains(attribute.getName())) {
									this.setPatternInfo(item.getCallee().getName());
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}

	}

	@Override
	public String getName() {
		return "在覆盖的方法中调用了抽象属性的覆盖方法";
	}

}
