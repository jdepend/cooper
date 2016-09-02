package jdepend.knowledge.pattern.impl2.feature.leaf;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.metadata.Attribute;
import jdepend.metadata.JavaClass;

public class CollectionAttributeHaveSubClassesFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getCollectionAttributes() == null) {
			return false;
		} else {
			Collection<Attribute> attributes = new ArrayList<Attribute>();
			for (Attribute attribute : context.getCollectionAttributes()) {
				L: for (JavaClass javaClass : attribute.getTypeClasses()) {
					if (javaClass.getSubClasses().size() > 1) {
						attributes.add(attribute);
						break L;
					}
				}
			}
			if (attributes.size() > 0) {
				context.setHaveSubClassesCollectionAttributes(attributes);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String getName() {
		return "集合属性中存在有子类类型";
	}

}
