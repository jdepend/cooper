package jdepend.ui.result.panel;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ui.MethodListOperationPanel;
import jdepend.report.ui.MethodListPanel;
import jdepend.ui.result.framework.SubResultTabPanel;

public class MethodListSubTabPanel extends SubResultTabPanel {

	private JDependFrame frame;

	public MethodListSubTabPanel(JDependFrame frame) {
		this.frame = frame;
	}

	@Override
	protected void init(AnalysisResult result) {

		MethodListPanel methodListPanel = new MethodListPanel(result.getMethods());

		MethodListOperationPanel methodListOperationPanel = new MethodListOperationPanel(methodListPanel);

		this.add(methodListOperationPanel);
	}

}
