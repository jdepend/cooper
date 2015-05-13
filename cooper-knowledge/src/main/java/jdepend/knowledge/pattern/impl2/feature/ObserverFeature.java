package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Attribute;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public class ObserverFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getAbstractAttributes() != null) {
			boolean attributeHaveSubClass;
			for (Attribute attribute : context.getAbstractAttributes()) {
				attributeHaveSubClass = false;
				if (attribute.isInterface()) {
					L: for (JavaClass attributeClass : attribute.getTypeClasses()) {
						if (attributeClass.getSubClasses().size() > 0) {
							attributeHaveSubClass = true;
							break L;
						}
					}
				}
				if (attributeHaveSubClass) {
					for (Method method : context.getCurrent().getSelfMethods()) {
						for (InvokeItem item : method.getInvokeItems()) {
							if (item.getCallee().getArgClassTypes().contains(context.getCurrent())
									&& attribute.getTypeClasses().contains(item.getCallee().getJavaClass())) {
								this.setPatternInfo(method.getName());
								return true;
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
		return "在方法中调用抽象属性的方法，并将自身作为参数 该抽象属性有实现";
	}

}
