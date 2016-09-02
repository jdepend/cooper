package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.metadata.JavaClass;
import jdepend.model.JavaClassUnit;

public class HaveSuperFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		Collection<JavaClass> superClasses = context.getCurrent().getSupers();
		if (superClasses != null && superClasses.size() > 0) {
			Collection<JavaClass> interfaces = new ArrayList<JavaClass>();
			Collection<JavaClass> supers = new ArrayList<JavaClass>();
			for (JavaClass superClass : superClasses) {
				if (superClass.isInterface()) {
					interfaces.add(superClass);
				} else {
					supers.add(superClass);
				}
			}
			boolean found = false;
			if (interfaces.size() > 0) {
				context.setInterfaces(interfaces);
				found = true;
			}
			if (supers.size() > 0) {
				context.setSupers(supers);
				found = true;
			}
			return found;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在父类";
	}

}
