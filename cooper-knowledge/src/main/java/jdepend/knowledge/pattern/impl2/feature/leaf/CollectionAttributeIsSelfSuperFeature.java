package jdepend.knowledge.pattern.impl2.feature.leaf;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.metadata.Attribute;
import jdepend.metadata.JavaClass;

public class CollectionAttributeIsSelfSuperFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		if (context.getAllSupers() == null || context.getCollectionAttributes() == null) {
			return false;
		} else {
			for (Attribute attribute : context.getCollectionAttributes()) {
				for (JavaClass javaClass : attribute.getTypeClasses()) {
					if (context.getAllSupers().contains(javaClass)) {
						this.setPatternInfo(attribute.getName());
						return true;
					}
				}
			}
			return false;
		}
	}

	@Override
	public String getName() {
		return "集合属性中存在父类类型";
	}

}
