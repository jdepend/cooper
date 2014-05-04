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

public class CheckButterflyObject extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1060879084785500223L;

	private int number;

	public CheckButterflyObject() {
		super("蝶形对象检测", Analyzer.Attention, "检查蝶形对象是否依赖了其它对象");

		if (this.number == 0) {
			this.number = 5;
		}

	}

	protected void doSearch(AnalysisResult result) throws JDependException {

		List<JDependUnit> units = new ArrayList<JDependUnit>(result.getComponents());

		Collections.sort(units, new JDependUnitByMetricsComparator(MetricsMgr.Ca, false));

		int count = 0;
		for (int i = 0; i < units.size() && count < this.number; i++) {
			if (units.get(i).efferentCoupling() > 0) {
				this.print((count + 1) + "、" + units.get(i).getName() + "(Ca:" + units.get(i).afferentCoupling()
						+ ") Ce（" + units.get(i).efferentCoupling() + "）:\n");
				for (JDependUnit ceUnit : units.get(i).getEfferents()) {
					this.printTab();
					this.print(ceUnit.getName() + "\n");
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

	@Override
	public String getExplain() {
		return "/culture/关注与反模式/ButterflyObject.htm";
	}

}
