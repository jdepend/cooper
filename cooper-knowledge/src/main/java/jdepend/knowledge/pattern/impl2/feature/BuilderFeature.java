package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Attribute;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;

public class BuilderFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getHaveSubClassesAbstractAttributes() != null) {
			for (Attribute attribute : context.getHaveSubClassesAbstractAttributes()) {
				for (JavaClass builder : attribute.getTypeClasses()) {
					for (Method method : context.getCurrent().getSelfMethods()) {
						// 识别builderMethod
						if (method.getReturnTypes().size() == 1 && method.getReturnClassTypes().size() == 1) {
							JavaClass productType = method.getReturnClassTypes().iterator().next();
							for (InvokeItem invokeItem : method.getInvokeItems()) {
								Method invokeMethod = invokeItem.getCallee();
								if (invokeMethod.getJavaClass().equals(builder)
										&& invokeMethod.getReturnTypes().size() == 1
										&& invokeMethod.getReturnClassTypes().size() == 1) {
									if (invokeMethod.getReturnClassTypes().iterator().next().equals(productType)) {
										this.setPatternInfo(attribute.getName() + "."
												+ invokeItem.getCallee().getName());
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "类方法中调用了Builder的方法 两个方法返回相同的数据类型";
	}

}
