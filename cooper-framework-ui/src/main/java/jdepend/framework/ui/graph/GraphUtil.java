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

import org.jfree.chart.ChartPanel;

public final class GraphUtil {

	private Map<String, ChartCreater> creaters;

	private static GraphUtil util = new GraphUtil();

	private static final int DefaultColCount = 2;

	private ThreadLocal<Boolean> isAddJScrollPane = new ThreadLocal<Boolean>();

	private GraphUtil() {
		this.init();
	}

	public static GraphUtil getInstance() {
		return util;
	}

	public void setAddJScrollPane(Boolean b) {
		this.isAddJScrollPane.set(b);
	}

	private void init() {
		creaters = new HashMap<String, ChartCreater>();

		PieChartCreater pieChartCreater = new PieChartCreater();
		creaters.put(pieChartCreater.getType(), pieChartCreater);

		SplineChartCreater splineChartCreater = new SplineChartCreater();
		creaters.put(splineChartCreater.getType(), splineChartCreater);

		BarCreater barCreater = new BarCreater();
		creaters.put(barCreater.getType(), barCreater);
	}

	public JComponent createGraph(GraphData data) throws JDependException {
		List<String> groups = data.getGroups();
		if (groups.size() == 0 || groups.size() == 1) {
			return createGroupComponent(data.getColCount(), data.getItems());
		} else {
			JTabbedPane tab = new JTabbedPane();
			for (String group : groups) {
				tab.addTab(group, createGroupComponent(data.getColCount(), data.getTheGroupItems(group)));
			}
			return tab;
		}
	}

	private JComponent createGroupComponent(int colCount, List<GraphDataItem> items) throws JDependException {
		int row;
		int cols;
		if (colCount > 0) {
			cols = colCount;
		} else {
			cols = DefaultColCount;
		}
		if (items.size() % cols == 0) {
			row = items.size() / cols;
		} else {
			row = items.size() / cols + 1;
		}
		JPanel content = new JPanel(new GridLayout(row, 2));
		ChartCreater creater;
		ChartPanel chartpanel;
		for (GraphDataItem item : items) {
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
		if (this.isAddJScrollPane.get() == null || this.isAddJScrollPane.get()) {
			JScrollPane pane = new JScrollPane(content);
			return pane;
		} else {
			return content;
		}
	}
}
