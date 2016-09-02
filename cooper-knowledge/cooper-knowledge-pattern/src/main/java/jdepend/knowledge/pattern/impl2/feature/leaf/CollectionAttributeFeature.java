package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.metadata.Attribute;

public class CollectionAttributeFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		Collection<Attribute> collectionAttributes = new ArrayList<Attribute>();
		for (Attribute attribute : context.getCurrent().getAttributes()) {
			if (attribute.existCollectionType()) {
				collectionAttributes.add(attribute);
			}
		}
		if (collectionAttributes.size() > 0) {
			context.setCollectionAttributes(collectionAttributes);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return "存在集合属性";
	}

}
