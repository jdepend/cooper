package jdepend.ui.property;

import javax.swing.JComponent;

import jdepend.ui.result.ResultPanel;

public class TXTTODOItemRender implements TODOItemRender {

	@Override
	public JComponent render(Object info) {
		return ResultPanel.createTextViewer((StringBuilder) info);
	}

}
