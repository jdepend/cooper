package jdepend.client.ui.property;

import javax.swing.JComponent;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.client.ui.result.framework.ResultPanel;

public class TXTTODOItemRender implements TODOItemRender {

	@Override
	public JComponent render(JDependFrame frame, Object info) {
		return ResultPanel.createTextViewer((StringBuilder) info);
	}

}
