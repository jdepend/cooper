package jdepend.ui.result.panel;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.component.TextViewer;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.JDependUnit;
import jdepend.model.util.MetricsTool;

public class InstabilityDialog extends CooperDialog {

	public InstabilityDialog(jdepend.model.Component component) {

		super(component.getName() + " 不稳定性");

		getContentPane().setLayout(new BorderLayout());

		JTabbedPane pane = new JTabbedPane();

		StringBuilder instabilityText = getText(component);

		final TextViewer resultViewer = new TextViewer();
		resultViewer.setText(instabilityText.toString());
		resultViewer.setCaretPosition(0);

		JScrollPane textPane = new JScrollPane(resultViewer);
		resultViewer.setScrollPane(textPane);

		pane.addTab("图形", this.createGraph(component));
		pane.addTab("文本", textPane);

		this.add(pane);

	}

	private JComponent createGraph(JDependUnit unit) {
		GraphData graph = new GraphData();

		Float instabilityWithCount = MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithCount(unit));
		Float instabilityWithIntensity = MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithIntensity(unit));

		GraphDataItem item = null;
		Map<Object, Object> datas = null;
		item = new GraphDataItem();
		item.setTitle("数量稳定性");
		item.setType(GraphDataItem.PIE);
		datas = new HashMap<Object, Object>();
		datas.put("传出", instabilityWithCount);
		datas.put("传入", 1 - instabilityWithCount);
		item.setDatas(datas);
		graph.addItem(item);

		item = new GraphDataItem();
		item.setTitle("强度稳定性");
		item.setType(GraphDataItem.PIE);
		datas = new HashMap<Object, Object>();
		datas.put("传出", instabilityWithIntensity);
		datas.put("传入", 1 - instabilityWithIntensity);
		item.setDatas(datas);
		graph.addItem(item);

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(new java.awt.Color(255, 255, 255));
		try {
			GraphUtil.getInstance().setAddJScrollPane(false);
			contentPanel.add(GraphUtil.getInstance().createGraph(graph));
		} catch (JDependException e) {
			e.printStackTrace();
		}

		return contentPanel;
	}

	private StringBuilder getText(JDependUnit unit) {
		StringBuilder instabilityText = new StringBuilder();

		instabilityText.append("Ce:");
		instabilityText.append(unit.getEfferentCoupling());
		instabilityText.append("\n");

		instabilityText.append("Ca:");
		instabilityText.append(unit.getAfferentCoupling());
		instabilityText.append("\n");

		instabilityText.append("Ce/(Ca+Ce):");
		instabilityText.append(MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithCount(unit)));
		instabilityText.append("\n");
		instabilityText.append("\n");

		instabilityText.append("CeCoupling:");
		instabilityText.append(MetricsFormat.toFormattedMetrics(unit.ceCoupling()));
		instabilityText.append("\n");

		instabilityText.append("CaCoupling:");
		instabilityText.append(MetricsFormat.toFormattedMetrics(unit.caCoupling()));
		instabilityText.append("\n");

		instabilityText.append("CeCoupling/(CaCoupling+CeCoupling):");
		instabilityText.append(MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithIntensity(unit)));
		instabilityText.append("\n");
		instabilityText.append("\n");

		instabilityText.append("instabilityWithCountScale:");
		instabilityText.append(MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithCountScale()));
		instabilityText.append("\n");

		instabilityText.append("instabilityWithCount:");
		instabilityText.append(MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithCount(unit)
				* MetricsTool.instabilityWithCountScale()));
		instabilityText.append("\n");

		instabilityText.append("instabilityWithIntensity:");
		instabilityText.append(MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithIntensity(unit)
				* (1 - MetricsTool.instabilityWithCountScale())));
		instabilityText.append("\n");
		instabilityText.append("\n");

		instabilityText.append("instabilityWithIntensity:");
		instabilityText.append(MetricsFormat.toFormattedMetrics(unit.getStability()));
		instabilityText.append("\n");

		return instabilityText;

	}
}
