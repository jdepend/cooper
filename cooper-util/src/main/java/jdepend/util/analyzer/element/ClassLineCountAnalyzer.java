package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.domain.notPersistent;
import jdepend.framework.ui.graph.model.GraphData;
import jdepend.framework.ui.graph.model.GraphDataItem;
import jdepend.model.JavaClassUnit;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public final class ClassLineCountAnalyzer extends AbstractAnalyzer {

	private static final long serialVersionUID = -4533305440655861869L;

	private transient Map<Object, Object> data;

	private transient Collection<JavaClassUnit> javaClasses;

	public ClassLineCountAnalyzer() {
		super("类规模分析", Attention, "类规模分析");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws AnalyzerException {

		javaClasses = result.getClasses();

		this.calData(result);

		GraphData graphData = new GraphData();
		GraphDataItem item = new GraphDataItem();
		item.setTitle("按类行数分类比例");
		item.setType(GraphDataItem.PIE);
		item.setDatas(data);
		graphData.addItem(item);

		this.printGraphData(graphData);
		this.printList();

	}

	private void calData(AnalysisResult result) {
		Map<String, Integer> data1 = new HashMap<String, Integer>();
		data1.put("10000~100000", 0);
		data1.put("5000~10000", 0);
		data1.put("1000~5000", 0);
		data1.put("500~1000", 0);
		data1.put("200~500", 0);
		data1.put("0~200", 0);

		int classCount = 0;
		for (JavaClassUnit javaClass : javaClasses) {
			if (javaClass.getLineCount() != 0) {
				if (javaClass.getLineCount() > 10000) {
					data1.put("10000~100000", (Integer) data1.get("10000~100000") + 1);
				} else if (javaClass.getLineCount() > 5000) {
					data1.put("5000~10000", (Integer) data1.get("5000~10000") + 1);
				} else if (javaClass.getLineCount() > 1000) {
					data1.put("1000~5000", (Integer) data1.get("1000~5000") + 1);
				} else if (javaClass.getLineCount() > 500) {
					data1.put("500~1000", (Integer) data1.get("500~1000") + 1);
				} else if (javaClass.getLineCount() > 200) {
					data1.put("200~500", (Integer) data1.get("200~500") + 1);
				} else {
					data1.put("0~200", (Integer) data1.get("0~200") + 1);
				}
				classCount++;
			}
		}
		data = new HashMap<Object, Object>();
		if (data1.get("10000~100000") != 0) {
			data.put("10000~100000", (Integer) data1.get("10000~100000") * 1F / classCount);
		}
		if (data1.get("5000~10000") != 0) {
			data.put("5000~10000", (Integer) data1.get("5000~10000") * 1F / classCount);
		}
		if (data1.get("1000~5000") != 0) {
			data.put("1000~5000", (Integer) data1.get("1000~5000") * 1F / classCount);
		}
		if (data1.get("500~1000") != 0) {
			data.put("500~1000", (Integer) data1.get("500~1000") * 1F / classCount);
		}
		if (data1.get("200~500") != 0) {
			data.put("200~500", (Integer) data1.get("200~500") * 1F / classCount);
		}
		if (data1.get("0~200") != 0) {
			data.put("0~200", (Integer) data1.get("0~200") * 1F / classCount);
		}
	}

	private void printList() {
		List<JavaClassUnit> jces = new ArrayList<JavaClassUnit>(this.javaClasses);
		Collections.sort(jces, new JDependUnitByMetricsComparator(MetricsMgr.LC, false));
		for (JavaClassUnit javaClass : jces) {
			this.printTable("类名", javaClass.getName());
			this.printTable("类行数", javaClass.getLineCount());
			this.printTable("所属组件", javaClass.getComponent().getName());
		}
	}

	@notPersistent
	public Map<Object, Object> getData() {
		return data;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("从Class的代码行数可以对代码的可理解性、逻辑划分的粒度等内容有一个初步判断。<br>");
		explain.append("<strong>类规模分析</strong>展现了类代码行数的区间比例。<br>");
		return explain.toString();
	}
}
