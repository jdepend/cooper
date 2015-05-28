package jdepend.ui.result.panel;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.model.result.AnalysisResult;
import jdepend.ui.result.framework.SubResultTabPanel;

public final class RelationPanel extends SubResultTabPanel {

	private JDependFrame frame;

	public RelationPanel(JDependFrame frame) {
		this.frame = frame;
	}

	@Override
	protected void init(final AnalysisResult result) {
		this.add(new RelationListPanel(frame, result.getRelations()));
	}

}
