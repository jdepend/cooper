package jdepend.util.analyzer.element;

import java.util.HashMap;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public final class JavaClassView extends AbstractAnalyzer {

	private static final long serialVersionUID = -206130595661170832L;

	private transient Map<Object, Object> scales;

	private transient Map<Object, Object> caScales;

	private transient Map<Object, Object> ceScales;

	public JavaClassView() {
		super("JavaClass内外比例浏览", Analyzer.Attention, "JavaClass内外比例浏览");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		this.calRelationScale(result);

		this.printGraphData(this.getGraphData());

	}

	/**
	 * 计算各组件的外关系涉及的Class比例
	 */
	private void calRelationScale(AnalysisResult result) {

		Map<String, Integer> counts = new HashMap<String, Integer>();
		Map<String, Integer> caCounts = new HashMap<String, Integer>();
		Map<String, Integer> ceCounts = new HashMap<String, Integer>();

		Map<String, Integer> totalCounts = new HashMap<String, Integer>();

		Integer count = null;
		boolean found;
		for (JDependUnit unit : result.getComponents()) {
			totalCounts.put(unit.getName(), unit.getClassCount());
			for (JavaClassUnit javaClass : unit.getClasses()) {
				found = false;
				if (javaClass.getAfferents().size() > 0) {
					count = caCounts.get(unit.getName());
					if (count == null) {
						caCounts.put(unit.getName(), 0);
					} else {
						caCounts.put(unit.getName(), count + 1);
					}
					found = true;
				}
				if (javaClass.getEfferents().size() > 0) {
					count = ceCounts.get(unit.getName());
					if (count == null) {
						ceCounts.put(unit.getName(), 0);
					} else {
						ceCounts.put(unit.getName(), count + 1);
					}
					found = true;
				}
				if (found) {
					count = counts.get(unit.getName());
					if (count == null) {
						counts.put(unit.getName(), 0);
					} else {
						counts.put(unit.getName(), count + 1);
					}
				}
			}
		}

		scales = new HashMap<Object, Object>();
		caScales = new HashMap<Object, Object>();
		ceScales = new HashMap<Object, Object>();

		for (String unitName : counts.keySet()) {
			if (counts.get(unitName) != null) {
				scales.put(unitName, counts.get(unitName) * 1F / totalCounts.get(unitName));
			}
			if (caCounts.get(unitName) != null) {
				caScales.put(unitName, caCounts.get(unitName) * 1F / totalCounts.get(unitName));
			}
			if (ceCounts.get(unitName) != null) {
				ceScales.put(unitName, ceCounts.get(unitName) * 1F / totalCounts.get(unitName));
			}
		}

		this.printGraphData(this.getGraphData());
	}

	private GraphData getGraphData() {

		GraphData data = new GraphData();
		data.setColCount(1);

		GraphDataItem item = new GraphDataItem();

		item.setTitle("按外总比例");
		item.setGroup("Graph");
		item.setType(GraphDataItem.BAR);
		item.setLineXName("组件名称");
		item.setLineYName("与其它组件有关系的Class比例");
		item.setDatas(scales);

		data.addItem(item);

		item = new GraphDataItem();

		item.setTitle("按外Ca比例");
		item.setGroup("Graph");
		item.setType(GraphDataItem.BAR);
		item.setLineXName("组件名称");
		item.setLineYName("与其它组件有传入关系的Class比例");
		item.setDatas(caScales);

		data.addItem(item);

		item = new GraphDataItem();

		item.setTitle("按外Ce比例");
		item.setGroup("Graph");
		item.setType(GraphDataItem.BAR);
		item.setLineXName("组件名称");
		item.setLineYName("与其它组件有传出关系的Class比例");
		item.setDatas(ceScales);

		data.addItem(item);

		return data;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("一个组件暴漏给其它组件使用的Class数量占总数量的比例在一定角度上体现了组件的封装性。<br>");
		explain.append("<strong>JavaClass内外比例浏览</strong>从被其它组件使用的类数量的比例、传入数量比例、传出数量比例三个角度上展现了组件暴漏给其它组件使用的类比例。<br>");
		return explain.toString();
	}
}
