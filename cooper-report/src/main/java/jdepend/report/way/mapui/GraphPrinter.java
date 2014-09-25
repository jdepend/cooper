package jdepend.report.way.mapui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import jdepend.framework.ui.JDependFrame;
import jdepend.model.result.AnalysisResult;
import jdepend.model.tree.JavaPackageNode;
import jdepend.report.util.TreeGraphUtil;
import jdepend.report.util.TreePanel;

public final class GraphPrinter extends JPanel {

	private JPanel graphPanel;

	private JPanel treePanel;

	private JSplitPane splitPane;

	private TreePanel treeCompoennt;

	private JavaPackageNode javaPackageTree;

	private JDependFrame frame;

	public GraphPrinter(JDependFrame frame, AnalysisResult result) {

		this.setLayout(new BorderLayout());

		this.frame = frame;

		this.javaPackageTree = result.getJavaPackageTree();

		graphPanel = new GraphPanel(this, result.getRelations());

		treePanel = new JPanel();
		treePanel.setLayout(new BorderLayout());

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, graphPanel, treePanel);

		treeCompoennt = null;

		hiddenPackageTree();

		this.add(splitPane);
	}

	void showPackageTree() {
		// 创建packageTree
		if (treeCompoennt == null) {
			treeCompoennt = (new TreeGraphUtil()).createTree(this.javaPackageTree);
			treeCompoennt.setSize(150, 400);
			this.setPackageTree(treeCompoennt);
		}

		splitPane.setDividerLocation(0.6D);
		treePanel.setVisible(true);
	}

	void hiddenPackageTree() {
		splitPane.setDividerLocation(Integer.MAX_VALUE);
		treePanel.setVisible(false);
	}

	private void setPackageTree(JComponent treeCompoennt) {
		treePanel.removeAll();
		treePanel.add(treeCompoennt);
	}

	boolean isPackageTreeVisible() {
		return treePanel.isVisible();
	}

	void setPackageTreeFocus(String focus) {
		treeCompoennt.setFocus(focus);
	}

	JDependFrame getFrame() {
		return frame;
	}

}
