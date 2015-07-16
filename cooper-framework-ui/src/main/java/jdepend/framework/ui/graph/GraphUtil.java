package jdepend.framework.ui.graph;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.creater.BarCreater;
import jdepend.framework.ui.graph.creater.ChartCreater;
import jdepend.framework.ui.graph.creater.PieChartCreater;
import jdepend.framework.ui.graph.creater.SplineChartCreater;
import jdepend.framework.ui.graph.model.GraphData;
import jdepend.framework.ui.graph.model.GraphDataItem;

import org.jfree.chart.ChartPanel;

public final class GraphUtil {

	private static final int DefaultColCount = 2;

	public static JComponent createGraph(GraphData data) throws JDependException {
		List<String> groups = data.getGroups();
		if (groups.size() == 0 || groups.size() == 1) {
			return createGroupComponent(data);
		} else {
			JTabbedPane tab = new JTabbedPane();
			for (String group : groups) {
				tab.addTab(group, createGroupComponent(data));
			}
			return tab;
		}
	}

	private static JComponent createGroupComponent(GraphData data) throws JDependException {
		int row;
		int cols;
		if (data.getColCount() > 0) {
			cols = data.getColCount();
		} else {
			cols = DefaultColCount;
		}
		if (data.getItems().size() % cols == 0) {
			row = data.getItems().size() / cols;
		} else {
			row = data.getItems().size() / cols + 1;
		}

		Map<String, ChartCreater> creaters = getCreaters();

		JPanel content = new JPanel(new GridLayout(row, 2));
		ChartCreater creater;
		ChartPanel chartpanel;
		for (GraphDataItem item : data.getItems()) {
			creater = creaters.get(item.getType());
			if (creater != null) {
				chartpanel = new ChartPanel(creater.create(item));
				if (item.getType().equals(GraphDataItem.SPLINE)) {
					chartpanel.setMouseWheelEnabled(true);
				}
				content.add(chartpanel);
			} else {
				throw new JDependException("没有类型为[" + item.getType() + "]的图形创建器");
			}
		}
		if (data.isAddJScrollPane()) {
			JScrollPane pane = new JScrollPane(content);
			return pane;
		} else {
			return content;
		}
	}

	private static Map<String, ChartCreater> getCreaters() {

		Map<String, ChartCreater> creaters = new HashMap<String, ChartCreater>();

		PieChartCreater pieChartCreater = new PieChartCreater();
		creaters.put(pieChartCreater.getType(), pieChartCreater);

		SplineChartCreater splineChartCreater = new SplineChartCreater();
		creaters.put(splineChartCreater.getType(), splineChartCreater);

		BarCreater barCreater = new BarCreater();
		creaters.put(barCreater.getType(), barCreater);

		return creaters;
	}
}
