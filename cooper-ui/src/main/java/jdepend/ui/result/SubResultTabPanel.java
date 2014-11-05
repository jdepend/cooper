package jdepend.ui.result;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;

public abstract class SubResultTabPanel extends JPanel {

	private boolean inited = false;

	public SubResultTabPanel() {
		this.setLayout(new BorderLayout());
	}

	@Override
	public void setVisible(boolean aFlag) {
		if (aFlag && !this.inited) {
			this.init(JDependUnitMgr.getInstance().getResult());
			this.inited = true;
		}
		super.setVisible(aFlag);
	}

	protected abstract void init(AnalysisResult result);
}
