package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.Method;

public class AdapterClassFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getInterfaces() != null && context.getSupers() != null) {
			JavaClass theInterfaceClass;
			JavaClass theSuperClass;
			for (Method method : context.getCurrent().getOverrideMethods()) {
				for (Method overrideMethod : context.getCurrent().getOverridedMethods(method)) {
					theInterfaceClass = overrideMethod.getJavaClass();
					if (context.getInterfaces().contains(theInterfaceClass)) {
						for (InvokeItem item : method.getInvokeItems()) {
							theSuperClass = item.getMethod().getJavaClass();
							if (context.getSupers().contains(theSuperClass)) {
								if (!theSuperClass.getSupers().contains(theInterfaceClass)) {
									this.setPatternInfo(method.getName());
									return true;
								}
							}
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
		return "在实现的接口方法中调用了继承的父类方法 父类与接口没有实现关系";
	}

}
