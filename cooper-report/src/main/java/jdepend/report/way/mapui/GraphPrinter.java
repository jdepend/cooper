package jdepend.report.way.mapui;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import jdepend.framework.ui.JDependFrame;
import jdepend.model.Relation;
import jdepend.model.tree.JavaPackageNode;
import jdepend.report.util.TreeGraphUtil;
import jdepend.report.util.TreePanel;

public final class GraphPrinter {

	private JPanel graphPanel;

	private JPanel treePanel;

	private JSplitPane splitPane;

	private TreePanel treeCompoennt;

	private JavaPackageNode javaPackageTree;

	private static GraphPrinter printer = new GraphPrinter();

	private GraphPrinter() {

	}

	public static GraphPrinter getIntance() {
		return printer;
	}

	public JComponent print(JDependFrame frame, Collection<Relation> relations, JavaPackageNode javaPackageTree) {

		this.javaPackageTree = javaPackageTree;

		graphPanel = new GraphPanel(frame, relations);

		treePanel = new JPanel();
		treePanel.setLayout(new BorderLayout());

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, graphPanel, treePanel);

		treeCompoennt = null;

		hiddenPackageTree();

		return splitPane;

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

}
