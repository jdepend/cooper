package jdepend.ui.property;

import javax.swing.JComponent;

import jdepend.framework.ui.JDependFrame;
import jdepend.ui.result.ResultPanel;

public class TXTTODOItemRender implements TODOItemRender {

	@Override
	public JComponent render(JDependFrame frame, Object info) {
		return ResultPanel.createTextViewer((StringBuilder) info);
	}

}
