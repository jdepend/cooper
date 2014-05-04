package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.Attribute;

public class AbstractAttributeFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		Collection<Attribute> abstractAttributes = new ArrayList<Attribute>();
		for (Attribute attribute : context.getCurrent().getAttributes()) {
			if (attribute.isAbstract() && !attribute.existCollectionType()) {
				abstractAttributes.add(attribute);
			}
		}
		if (abstractAttributes.size() > 0) {
			context.setAbstractAttributes(abstractAttributes);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在抽象属性（不是集合属性）";
	}

}
