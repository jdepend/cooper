package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.metadata.Attribute;
import jdepend.metadata.Method;

public class SingletonFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getStaticAttributes() != null && context.getStaticMethods() != null) {
			boolean foundAttribute;
			foundAttribute = false;
			L: for (Attribute attribute : context.getStaticAttributes()) {
				if (attribute.getTypeClasses().contains(context.getCurrent())) {
					foundAttribute = true;
					break L;
				}
			}
			boolean foundMethod;
			foundMethod = false;
			M: for (Method method : context.getStaticMethods()) {
				if (method.getReturnClassTypes().contains(context.getCurrent())) {
					foundMethod = true;
					break M;
				}
			}
			if (foundAttribute && foundMethod) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "静态属性类型为自己 静态方法返回值为自己";
	}

}
