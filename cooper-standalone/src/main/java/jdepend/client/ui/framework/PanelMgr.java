package jdepend.client.ui.framework;

import jdepend.client.ui.analyzer.AnalyzerPanel;

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
