package jdepend.client.ui.result.panel;

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
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.ui.graph.model.GraphData;
import jdepend.framework.ui.graph.model.GraphDataItem;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.JDependUnit;
import jdepend.model.profile.model.ComponentProfile;
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
		graph.setAddJScrollPane(false);

		ComponentProfile componentProfile = unit.getResult().getRunningContext().getProfileFacade()
				.getComponentProfile();
		float stabilityWithCountScale = componentProfile.getStabilityWithCountScale();

		Float instabilityWithCount = MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithCount(unit));
		Float instabilityWithIntensity = MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithIntensity(unit));

		GraphDataItem item = null;
		Map<Object, Object> datas = null;

		if (stabilityWithCountScale == 0F || stabilityWithCountScale == 0.5F) {
			item = new GraphDataItem();
			item.setTitle("数量稳定性");
			item.setType(GraphDataItem.PIE);
			datas = new HashMap<Object, Object>();
			datas.put("传出", instabilityWithCount);
			datas.put("传入", 1 - instabilityWithCount);
			item.setDatas(datas);
			graph.addItem(item);
		}

		if (stabilityWithCountScale == 1F || stabilityWithCountScale == 0.5F) {
			item = new GraphDataItem();
			item.setTitle("强度稳定性");
			item.setType(GraphDataItem.PIE);
			datas = new HashMap<Object, Object>();
			datas.put("传出", instabilityWithIntensity);
			datas.put("传入", 1 - instabilityWithIntensity);
			item.setDatas(datas);
			graph.addItem(item);
		}

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(new java.awt.Color(255, 255, 255));
		try {
			contentPanel.add(GraphUtil.createGraph(graph));
		} catch (JDependException e) {
			e.printStackTrace();
		}

		return contentPanel;
	}

	private StringBuilder getText(JDependUnit unit) {
		StringBuilder instabilityText = new StringBuilder();

		ComponentProfile componentProfile = unit.getResult().getRunningContext().getProfileFacade()
				.getComponentProfile();
		float stabilityWithCountScale = componentProfile.getStabilityWithCountScale();

		if (stabilityWithCountScale == 0F || stabilityWithCountScale == 0.5F) {
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

			if (stabilityWithCountScale == 0.5F) {
				instabilityText.append("instabilityWithCount:");
				instabilityText.append(MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithCount(unit)
						* stabilityWithCountScale));
				instabilityText.append("\n");
				instabilityText.append("\n");
			}
		}

		if (stabilityWithCountScale == 1F || stabilityWithCountScale == 0.5F) {
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

			if (stabilityWithCountScale == 0.5F) {
				instabilityText.append("instabilityWithIntensity:");
				instabilityText.append(MetricsFormat.toFormattedMetrics(MetricsTool.instabilityWithIntensity(unit)
						* (1 - stabilityWithCountScale)));
				instabilityText.append("\n");
				instabilityText.append("\n");
			}
		}

		instabilityText.append("instabilityWithCountScale:");
		instabilityText.append(MetricsFormat.toFormattedMetrics(stabilityWithCountScale));
		instabilityText.append("\n");

		instabilityText.append("instabilityWithIntensity:");
		instabilityText.append(MetricsFormat.toFormattedMetrics(unit.getStability()));
		instabilityText.append("\n");

		return instabilityText;

	}
}
