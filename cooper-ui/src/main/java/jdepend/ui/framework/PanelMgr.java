package jdepend.ui.framework;

import jdepend.ui.analyzer.AnalyzerPanel;

public final class PanelMgr {

	private static PanelMgr mgr = new PanelMgr();

	private AnalyzerPanel analyzerPanel;

	private PanelMgr() {

	}

	public static PanelMgr getInstance() {
		return mgr;
	}

	public AnalyzerPanel getAnalyzerPanel() {
		return analyzerPanel;
	}

	public void setAnalyzerPanel(AnalyzerPanel analyzerPanel) {
		this.analyzerPanel = analyzerPanel;
	}

}
