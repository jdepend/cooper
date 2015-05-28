package jdepend.ui.result.panel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.util.ExceptionPrinter;
import jdepend.knowledge.architectpattern.ArchitectPatternMgr;
import jdepend.knowledge.architectpattern.ArchitectPatternResult;
import jdepend.model.result.AnalysisResult;
import jdepend.ui.result.framework.ResultPanel;
import jdepend.ui.result.framework.SubResultTabPanel;

public final class ArchitectPatternPanel extends SubResultTabPanel {

	@Override
	protected void init(AnalysisResult result) {

		ArchitectPatternResult apResult = null;
		try {
			apResult = ArchitectPatternMgr.getInstance().identify(result);
		} catch (JDependException e) {
			e.printStackTrace();
			this.add(ExceptionPrinter.createComponent(e));
		}
		if (apResult != null) {
			this.add(ResultPanel.createTextViewer(new StringBuilder(apResult.getResult())));
		}
	}

}
