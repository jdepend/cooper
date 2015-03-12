package jdepend.report.ui;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.model.Component;
import jdepend.report.way.mapui.GraphPanel;

public final class BalanceComponentDialog extends CooperDialog {

	public BalanceComponentDialog(JDependFrame frame, Component component) {
		super(component.getName() + "内聚性");

		if (component.getJavaPackages().size() == 0 || component.getJavaPackages().size() == 1) {
			this.add(new GraphPanel(frame, this, component.open()));
		} else {
			this.add(new JavaPackageListPanel(frame, component));
		}
	}
}
