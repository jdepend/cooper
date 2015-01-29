package jdepend.ui.result.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.TextViewer;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.JDependUnit;
import jdepend.model.util.MetricsTool;

public class MMDialog extends CooperDialog {

	public MMDialog(float score) {

		super("你的分数对应的美女是");

//		this.add(pane);
		
		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createCloseButton());
		
		this.add(BorderLayout.SOUTH, buttonBar);

	}

	
}
