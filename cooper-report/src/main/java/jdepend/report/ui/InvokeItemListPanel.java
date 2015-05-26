package jdepend.report.ui;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.TableSorter;
import jdepend.metadata.InvokeItem;
import jdepend.metadata.Method;
import jdepend.metadata.RemoteInvokeItem;

public class InvokeItemListPanel extends JPanel {

	private DefaultTableModel invokeItemListModel;

	private JTable invokedItemListTable;

	private Collection<InvokeItem> invokedItems;

	public InvokeItemListPanel(Collection<InvokeItem> invokedItems) {
		super();
		this.setLayout(new BorderLayout());

		this.invokedItems = invokedItems;

		this.initInvokeItemList();

		JScrollPane pane = new JScrollPane(invokedItemListTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);
	}

	public int loadInvokeItemList() {

		invokeItemListModel.setRowCount(0);

		Object[] row;

		Method caller;
		Method Callee;

		for (InvokeItem invokeItem : this.invokedItems) {
			caller = invokeItem.getCaller();
			Callee = invokeItem.getCallee();

			row = new Object[4];

			row[0] = caller.getMethodInfo();
			row[1] = Callee.getMethodInfo();
			row[2] = invokeItem instanceof RemoteInvokeItem ? "是" : "否";
			row[3] = invokeItem.getName();

			invokeItemListModel.addRow(row);

		}

		return invokeItemListModel.getRowCount();

	}

	protected void initInvokeItemList() {

		invokeItemListModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(invokeItemListModel);

		invokedItemListTable = new JTable(sorter);

		sorter.setTableHeader(invokedItemListTable.getTableHeader());

		invokeItemListModel.addColumn("调用者");
		invokeItemListModel.addColumn("被调用者");
		invokeItemListModel.addColumn("是否包含进程间调用");
		invokeItemListModel.addColumn("调用类型");

	}
}
