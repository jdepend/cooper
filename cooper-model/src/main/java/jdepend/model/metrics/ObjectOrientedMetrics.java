package jdepend.model.metrics;

import jdepend.metadata.Attribute;
import jdepend.metadata.Method;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClassUnit;
import jdepend.model.MetricsInfo;

public final class ObjectOrientedMetrics extends EmptyMetrics {

	@Override
	public MetricsInfo getMetrics(JDependUnit unit) {
		MetricsInfo info = new MetricsInfo();

		if (unit instanceof JavaClassUnit) {
			info.setMetrics(getObjectOriented((JavaClassUnit) unit));
		} else if (unit instanceof Component) {
			info.setMetrics(getObjectOriented((Component) unit));
		}

		return info;
	}

	public float getObjectOriented(JavaClassUnit javaClass) {
		Float objectOriented = null;
		int attributeCount = countAttributes(javaClass);
		int methodCount = countMethods(javaClass);
		if (methodCount != 0) {
			objectOriented = new Float(attributeCount) / new Float(methodCount);
		} else {
			objectOriented = 0F;
		}

		return objectOriented;
	}

	public float getObjectOriented(Component component) {
		if (component.getClassCount() == 0) {
			return 0.0F;
		}
		Float oo = 0.0F;
		for (JavaClassUnit javaClass : component.getClasses()) {
			oo += getObjectOriented(javaClass);
		}
		return oo / component.getClassCount();
	}

	/**
	 * 计算非公开属性
	 * 
	 * @return
	 */
	private int countAttributes(JavaClassUnit javaClass) {
		int count = 0;
		for (Attribute attribute : javaClass.getJavaClass().getAttributes()) {
			if (!attribute.isPublic() && !attribute.isStatic()) {
				count += 1;
			}
		}
		return count;
	}

	/**
	 * 计算公开方法
	 * 
	 * @return
	 */
	private int countMethods(JavaClassUnit javaClass) {
		int count = 0;
		for (Method method : javaClass.getJavaClass().getSelfMethods()) {
			if (method.isPublic() && !method.isConstruction()) {
				count += 1;
			}
		}
		return count;
	}
}
