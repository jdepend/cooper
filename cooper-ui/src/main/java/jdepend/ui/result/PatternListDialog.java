package jdepend.ui.result;

import jdepend.framework.ui.CooperDialog;
import jdepend.model.result.AnalysisResult;

public final class PatternListDialog extends CooperDialog {
	
	private DesignPatternPanel designPatternPanel;

	public PatternListDialog(AnalysisResult result) {
		super("Pattern List");
		designPatternPanel = new DesignPatternPanel();
		this.add(designPatternPanel);
	}

	@Override
	public void setVisible(boolean b) {
		designPatternPanel.setVisible(true);
		super.setVisible(b);
	}
}
