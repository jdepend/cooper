package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.JDependUnit;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class CheckFissileObject extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1060879084785500223L;

	private int number;

	public CheckFissileObject() {
		super("易分对象检测", Analyzer.Attention, "检查易分对象是否被其他对象依赖");

		if (this.number == 0) {
			this.number = 5;
		}

	}

	protected void doSearch(AnalysisResult result) throws JDependException {

		List<JDependUnit> units = new ArrayList<JDependUnit>(result.getComponents());
		Collections.sort(units, new JDependUnitByMetricsComparator(MetricsMgr.Ce, false));

		int count = 0;
		for (int i = 0; i < units.size() && count < this.number; i++) {
			if (units.get(i).afferentCoupling() > 0) {
				this.print((count + 1) + "、" + units.get(i).getName() + "(Ce:" + units.get(i).efferentCoupling()
						+ ") Ca（" + units.get(i).afferentCoupling() + "）:\n");
				for (JDependUnit caUnit : units.get(i).getAfferents()) {
					this.printTab();
					this.print(caUnit.getName() + "\n");
				}
				count++;
			}

		}

	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getExplain() {
		return "/culture/关注与反模式/FissileObject.htm";
	}
}
