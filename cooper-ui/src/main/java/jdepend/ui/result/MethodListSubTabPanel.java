package jdepend.ui.result;

import jdepend.framework.ui.JDependFrame;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ui.MethodListPanel;

public class MethodListSubTabPanel extends SubResultTabPanel {

	private JDependFrame frame;

	public MethodListSubTabPanel(JDependFrame frame) {
		this.frame = frame;
	}

	@Override
	protected void init(AnalysisResult result) {

		MethodListPanel methodListPanel = new MethodListPanel(result.getMethods());

		this.add(methodListPanel);
	}

}
