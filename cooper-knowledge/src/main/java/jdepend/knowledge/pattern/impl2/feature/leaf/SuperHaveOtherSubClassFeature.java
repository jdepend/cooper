package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.JavaClassUnit;

public class SuperHaveOtherSubClassFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getAllSupers() == null) {
			return false;
		} else {
			Collection<JavaClassUnit> otherSubClasses = new ArrayList<JavaClassUnit>();
			for (JavaClassUnit superClass : context.getAllSupers()) {
				for (JavaClassUnit subClass : superClass.getSubClasses()) {
					if (!subClass.equals(context.getCurrent()) && !otherSubClasses.contains(subClass)) {
						otherSubClasses.add(subClass);
					}
				}
			}
			if (otherSubClasses.size() > 0) {
				context.setSuperOtherSubClasses(otherSubClasses);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String getName() {
		return "父类存在其他子类";
	}

}
