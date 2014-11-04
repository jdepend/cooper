package jdepend.ui.result;

import jdepend.framework.ui.JDependFrame;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ui.ClassListOperationPanel;

public class ClassListSubTabPanel extends SubResultTabPanel {

	private JDependFrame frame;

	public ClassListSubTabPanel(JDependFrame frame) {
		this.frame = frame;
	}

	@Override
	protected void init(AnalysisResult result) {
		ClassListOperationPanel classListPanel = new ClassListOperationPanel(frame);

		this.add(classListPanel);
	}

}
