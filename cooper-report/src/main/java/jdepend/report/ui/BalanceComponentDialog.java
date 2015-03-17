package jdepend.report.ui;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.model.Component;

public final class BalanceComponentDialog extends CooperDialog {

	public BalanceComponentDialog(JDependFrame frame, Component component) {
		super(component.getName() + "内聚性");
		this.add(new SubJDependUnitListPanel(frame, component));
	}
}
