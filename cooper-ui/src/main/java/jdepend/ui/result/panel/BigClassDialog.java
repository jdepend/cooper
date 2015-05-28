package jdepend.ui.result.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;
import jdepend.report.ui.MethodListDialog;

public final class BigClassDialog extends CooperDialog {

	public BigClassDialog(Collection<JavaClassUnit> bigClasses) {

		TableData tableData = new TableData();

		for (JavaClassUnit bigClass : bigClasses) {
			tableData.setData("类名", bigClass.getName());
			tableData.setData("行数", bigClass.getLineCount());
			tableData.setData("所属组件", bigClass.getComponent().getName());
		}

		final CooperTable table = new CooperTable(tableData);
		JMenuItem viewMethodListItem = new JMenuItem(
				BundleUtil.getString(BundleUtil.Command_ViewMethodList));
		viewMethodListItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getCurrentes().size() == 1) {
					viewMethodList(table.getCurrentes().get(0));
				}
			}
		});
		table.getPopupMenu().add(viewMethodListItem);

		this.add(new JScrollPane(table));
	}

	private void viewMethodList(String current) {
		JavaClassUnit javaClass = JDependUnitMgr.getInstance().getResult().getTheClass(current);
		MethodListDialog d = new MethodListDialog(javaClass);
		d.setModal(true);
		d.setVisible(true);
	}

}
