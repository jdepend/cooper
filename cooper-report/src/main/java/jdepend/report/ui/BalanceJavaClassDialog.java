package jdepend.report.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.TextViewer;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.GroupCouplingItem;
import jdepend.model.JavaClass;

public final class BalanceJavaClassDialog extends CooperDialog {

	public BalanceJavaClassDialog(JavaClass javaClass) {
		super(javaClass.getName() + "内聚性指数明细");

		getContentPane().setLayout(new BorderLayout());
		TextViewer balance = new TextViewer();

		balance.setText(getBalance(javaClass));
		balance.setCaretPosition(0);

		this.add(new JScrollPane(balance));
	}

	private String getBalance(JavaClass unit) {

		StringBuilder info = new StringBuilder();

		info.append("内聚性指数（");
		info.append(MetricsFormat.toFormattedMetrics(unit.getBalance()));
		info.append("）=内聚值（");
		info.append(MetricsFormat.toFormattedMetrics(unit.getCohesion()));
		info.append("）/内聚值（");
		info.append(MetricsFormat.toFormattedMetrics(unit.getCohesion()));
		info.append("）+分组耦合最大顺序差值（");
		info.append(MetricsFormat.toFormattedMetrics(unit.getGroupCouplingInfo().getMaxDifference()));
		info.append("）；\n");

		info.append("分组耦合顺序值为：\n");

		for (GroupCouplingItem groupCouplingInfo : unit.getGroupCouplingInfo().getGroupCouplingItems()) {
			info.append(groupCouplingInfo);
			info.append("\n");
		}

		info.append("分组耦合顺序差值为：\n");
		for (Float difference : unit.getGroupCouplingInfo().getDifferences()) {
			info.append(MetricsFormat.toFormattedMetrics(difference));
			info.append("\n");
		}

		info.append("分组耦合最大顺序差值为：");
		info.append(MetricsFormat.toFormattedMetrics(unit.getGroupCouplingInfo().getMaxDifference()));

		return info.toString();
	}

}
