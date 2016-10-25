package jdepend.knowledge.domainanalysis.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdepend.framework.util.BundleUtil;
import jdepend.knowledge.domainanalysis.AbstractDomainAnalysis;
import jdepend.knowledge.domainanalysis.AdviseInfo;
import jdepend.knowledge.domainanalysis.StructureCategory;
import jdepend.model.JDependUnit;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;

public final class DDomainAnalysis extends AbstractDomainAnalysis {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1817433228682769778L;

	public DDomainAnalysis() {
		super("抽象程度合理性分析器", "用于对抽象程度合理性项目进行建议");
	}

	@Override
	protected AdviseInfo doAdvise(String name, AnalysisResult result) {
		List<JDependUnit> units = new ArrayList<JDependUnit>(result.getComponents());
		Collections.sort(units, new JDependUnitByMetricsComparator(MetricsMgr.D, true));

		Iterator<JDependUnit> it = units.iterator();
		JDependUnit unit = null;
		while (it.hasNext()) {
			unit = it.next();
			if (unit.isInner() && (unit.getAfferents().size() != 0 || unit.getEfferents().size() != 0)) {
				break;
			}
		}
		if (unit != null) {
			String advise = null;
			if (unit.getStability() != null && unit.getStability() < 0.5) {
				advise = BundleUtil.getString(BundleUtil.Advise_D_Small);
			} else {
				advise = BundleUtil.getString(BundleUtil.Advise_D_Big);
			}
			AdviseInfo info = new DAdviseInfo();
			info.setDesc(advise);
			info.addComponentName(unit.getName());
			return info;
		} else {
			return null;
		}

	}

	@Override
	public StructureCategory getStructureCategory() {
		return StructureCategory.DDomainAnalysis;
	}
}
