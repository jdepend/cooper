package jdepend.report.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import jdepend.framework.ui.component.TextViewer;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.GroupCouplingItem;
import jdepend.model.SubJDependUnit;

public final class BalanceSubJDependUnitDialog extends CooperDialog {

	public BalanceSubJDependUnitDialog(SubJDependUnit subUnit) {
		super(subUnit.getName() + "内聚性指数明细");

		getContentPane().setLayout(new BorderLayout());
		TextViewer balance = new TextViewer();

		balance.setText(getBalance(subUnit));
		balance.setCaretPosition(0);

		this.add(new JScrollPane(balance));
	}

	private String getBalance(SubJDependUnit subUnit) {

		StringBuilder info = new StringBuilder();

		info.append("内聚性指数（");
		info.append(MetricsFormat.toFormattedMetrics(subUnit.getBalance()));
		info.append("）=内聚值（");
		info.append(MetricsFormat.toFormattedMetrics(subUnit.getCohesion()));
		info.append("）/内聚值（");
		info.append(MetricsFormat.toFormattedMetrics(subUnit.getCohesion()));
		info.append("）+分组耦合顺序差值均值（");
		info.append(MetricsFormat.toFormattedMetrics(subUnit.getGroupCouplingInfo().getAverageDifference()));
		info.append("）；\n");

		info.append("分组耦合顺序值为：\n");

		for (GroupCouplingItem groupCouplingInfo : subUnit.getGroupCouplingInfo().getGroupCouplingItems()) {
			info.append(groupCouplingInfo);
			info.append("\n");
		}

		info.append("分组耦合顺序差值为：\n");
		for (Float difference : subUnit.getGroupCouplingInfo().getDifferences()) {
			info.append(MetricsFormat.toFormattedMetrics(difference));
			info.append("\n");
		}

		info.append("分组耦合顺序差值均值为：");
		info.append(MetricsFormat.toFormattedMetrics(subUnit.getGroupCouplingInfo().getAverageDifference()));
	
		return info.toString();
	}

}
