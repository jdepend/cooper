package jdepend.knowledge.pattern.impl2.feature;

import jdepend.knowledge.pattern.impl2.AbstractFeature;
import jdepend.knowledge.pattern.impl2.FeatureCheckContext;
import jdepend.model.util.JavaClassUnitUtil;

public class FacadeFeature extends AbstractFeature {

	@Override
	protected boolean have(FeatureCheckContext context) {
		Float extCaCallScale;
		Float innerCellScale;
		int innerCellCount;
		if (context.getCurrent().getCaList().size() > 0) {
			extCaCallScale = JavaClassUnitUtil.getJavaClassUnit(context.getCurrent()).getAfferents().size()
					/ context.getCurrent().getCaList().size() * 1F;
			if (extCaCallScale >= 0.9) {
				if (context.getCurrent().getCeList().size() > 0) {
					innerCellCount = context.getCurrent().getCeList().size()
							- JavaClassUnitUtil.getJavaClassUnit(context.getCurrent()).getEfferents().size();
					innerCellScale = innerCellCount / context.getCurrent().getCeList().size() * 1F;
					if (innerCellScale >= 0.8) {
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public String getName() {
		return "被组件外的类调用的比例超过九成 调用的类是所属组件内的比较超过八成";
	}
}
