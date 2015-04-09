package jdepend.ui.culture;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jdepend.core.local.analyzer.AnalyzerMgr;
import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;
import jdepend.ui.analyzer.AnalyzerPanel;

public class CulturePanel extends JPanel {

	private JDependCooper frame;

	private AnalyzerPanel analyzerPanel;

	private DesignPrinciplePanel designPrinciplePanel;

	private JTabbedPane tabPane = new JTabbedPane();

	public CulturePanel(JDependCooper frame) {
		this.frame = frame;

		this.setLayout(new BorderLayout());

		tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		tabPane.addTab("MINICooper", new JImagePane(frame));

		designPrinciplePanel = new DesignPrinciplePanel(frame);
		tabPane.addTab(BundleUtil.getString(BundleUtil.ClientWin_Culture_DesignPrinciple), designPrinciplePanel);

		analyzerPanel = new AnalyzerPanel(frame);
		tabPane.addTab(BundleUtil.getString(BundleUtil.ClientWin_Culture_Analyzer), analyzerPanel);

		this.add(tabPane);
	}

	public void refreshAnalyzer() {
		AnalyzerMgr.getInstance().refresh();
		analyzerPanel.refresh();
	}

}
