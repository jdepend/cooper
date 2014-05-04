package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.TableCallBack;
import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public final class JavaClassBalanceSort extends AbstractAnalyzer {

	private static final long serialVersionUID = -4568932984457081003L;

	public JavaClassBalanceSort() {
		super("JavaClass内聚性指数排序", Analyzer.Attention, "JavaClass内聚性指数排序");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		List<BalanceJavaClassWrapper> wrappers = new ArrayList<BalanceJavaClassWrapper>();

		for (JavaClass javaClass : result.getClasses()) {
			wrappers.add(new BalanceJavaClassWrapper(javaClass));
		}
		Collections.sort(wrappers);
		for (BalanceJavaClassWrapper wrapper : wrappers) {
			this.printTable("类名称", wrapper.javaClass.getName());
			this.printTable("行数", wrapper.javaClass.getLineCount());
			this.printTable("内聚性指数", wrapper.balance);
			this.printTable("内聚值", wrapper.cohesion);
			this.printTable("分组耦合最大顺序差值", wrapper.gcmd);
		}
		// 增加回调函数
		this.addTableCallBack(new TableCallBack("类名称", "jdepend.report.ui.JavaClassDetailDialog"));
		this.addTableCallBack(new TableCallBack("内聚性指数", "jdepend.report.ui.BalanceDialog"));
		this.addTableCallBack(new TableCallBack("内聚值", "jdepend.report.ui.CohesionDialog"));
		this.addTableCallBack(new TableCallBack("分组耦合最大顺序差值", "jdepend.report.ui.CouplingDialog"));

	}

	@Override
	public String getExplain() {
		StringBuilder info = new StringBuilder();
		info
				.append("JavaClass内聚性指数表现了在指定的组件划分下，与所属组件其他JavaClass的关系占总关系(其中耦合关系为与其他组件耦合关系的最大差值，用以考虑复用)的比例，值越小表示该JavaClass与所属组件关系越弱，有移动的可能性。");
		return info.toString();
	}

	class BalanceJavaClassWrapper implements Comparable<BalanceJavaClassWrapper> {

		public JavaClass javaClass;

		public float balance;

		public float cohesion;

		public float gcmd;

		public BalanceJavaClassWrapper(JavaClass javaClass) {
			this.javaClass = javaClass;
			this.balance = this.javaClass.balance();
			this.cohesion = this.javaClass.cohesion();
			this.gcmd = this.javaClass.getGroupCouplingInfo().getMaxDifference();
		}

		@Override
		public int compareTo(BalanceJavaClassWrapper o) {
			int result = new Float(this.balance).compareTo(o.balance);
			if (result == 0) {
				result = new Float(this.cohesion).compareTo(o.cohesion);
			}
			if (result == 0) {
				result = new Float(o.gcmd).compareTo(this.gcmd);
			}
			return result;
		}

	}

}
