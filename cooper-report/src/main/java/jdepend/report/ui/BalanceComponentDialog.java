package jdepend.report.ui;

import java.awt.BorderLayout;

import jdepend.framework.ui.CooperDialog;
import jdepend.model.Component;
import jdepend.model.JDependUnitMgr;
import jdepend.report.way.mapui.GraphJDepend;

public final class BalanceComponentDialog extends CooperDialog {

	private String componentName;

	public BalanceComponentDialog(String componentName) {
		super(componentName + "内聚性");
		this.componentName = componentName;

		getContentPane().setLayout(new BorderLayout());

		Component component = JDependUnitMgr.getInstance().getTheComponent(this.componentName);

		this.add(GraphJDepend.printGraph(component.open()));

	}

}
