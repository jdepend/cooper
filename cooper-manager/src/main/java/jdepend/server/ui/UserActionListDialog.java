package jdepend.server.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.util.BundleUtil;
import jdepend.server.service.user.UserDomainService;
import jdepend.service.remote.user.UserActionItem;

public class UserActionListDialog extends CooperDialog {

	public UserActionListDialog(String username) {
		super();
		this.setLayout(new BorderLayout());

		getContentPane().setLayout(new BorderLayout());

		DefaultTableModel actionListModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(actionListModel);

		JTable actionListTable = new JTable(sorter);

		sorter.setTableHeader(actionListTable.getTableHeader());

		actionListModel.addColumn("操作");
		actionListModel.addColumn("IP");
		actionListModel.addColumn("创建时间");
		actionListModel.addColumn("收集时间");

		try {
			Object[] row;
			for (UserActionItem item : new UserDomainService().getTheUserActions(username)) {
				row = new Object[4];
				row[0] = item.operation;
				row[1] = item.ip;
				row[2] = item.getCreatedate();
				row[3] = item.getGartherdate();

				actionListModel.addRow(row);
			}
		} catch (JDependException e) {
			e.printStackTrace();
		}

		this.add(BorderLayout.CENTER, new JScrollPane(actionListTable));

		JPanel buttonBar = new JPanel();
		buttonBar.add(createButton(BundleUtil.getString(BundleUtil.Command_Close)));

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		return button;
	}

}
