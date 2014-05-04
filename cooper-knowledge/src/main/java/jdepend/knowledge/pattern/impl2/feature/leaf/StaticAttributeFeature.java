package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Attribute;

public class StaticAttributeFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		Collection<Attribute> staticAttributes = new ArrayList<Attribute>();
		for (Attribute attribute : context.getCurrent().getAttributes()) {
			if (attribute.isStatic()) {
				staticAttributes.add(attribute);
			}
		}
		if (staticAttributes.size() > 0) {
			context.setStaticAttributes(staticAttributes);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在静态属性";
	}

}
