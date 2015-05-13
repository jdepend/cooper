package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public class TemplateMethodFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getAbstractMethods() != null && context.getSubClasses() != null) {
			for (Method abstractMethod : context.getAbstractMethods()) {
				if (abstractMethod.isProtected()) {
					// 存在public的调用抽象方法的方法
					for (Method publicMethod : context.getCurrent().getSelfMethods()) {
						if (!publicMethod.isAbstract() && publicMethod.isPublic() && !publicMethod.isConstruction()) {
							for (InvokeItem item : publicMethod.getInvokeItems()) {
								if (item.getCallee().equals(abstractMethod)) {
									// 子类覆盖了抽象方法
									for (JavaClass subClass : context.getSubClasses()) {
										if (subClass.getOverridedMethods().contains(abstractMethod)) {
											this.setPatternInfo(publicMethod.getName());
											return true;
										}
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
		return "抽象方法是受保护的 存在public的调用抽象方法的方法  子类覆盖了抽象方法";
	}

}
