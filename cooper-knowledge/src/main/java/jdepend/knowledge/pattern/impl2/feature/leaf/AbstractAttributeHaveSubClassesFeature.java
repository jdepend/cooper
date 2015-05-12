package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Attribute;
import jdepend.model.JavaClassUnit;

public class AbstractAttributeHaveSubClassesFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getAbstractAttributes() == null) {
			return false;
		} else {
			Collection<Attribute> attributes = new ArrayList<Attribute>();
			for (Attribute attribute : context.getAbstractAttributes()) {
				L: for (JavaClassUnit javaClass : attribute.getTypeClasses()) {
					if (javaClass.getSubClasses().size() > 1) {
						attributes.add(attribute);
						break L;
					}
				}
			}
			if (attributes.size() > 0) {
				context.setHaveSubClassesAbstractAttributes(attributes);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String getName() {
		return "存在有多个子类的抽象属性";
	}

}
