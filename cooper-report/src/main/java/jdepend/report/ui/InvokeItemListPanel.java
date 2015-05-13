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

import org.apache.bcel.classfile.Utility;

public class InvokeItemListPanel extends JPanel {

	private DefaultTableModel invokeItemListModel;

	private JTable invokedItemListTable;

	private Collection<InvokeItem> invokedItems;

	private String type;

	public InvokeItemListPanel(Collection<InvokeItem> invokedItems, String type) {
		super();
		this.setLayout(new BorderLayout());

		this.invokedItems = invokedItems;

		this.type = type;

		this.initInvokeItemList();

		JScrollPane pane = new JScrollPane(invokedItemListTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);
	}

	public int loadInvokeItemList() {

		invokeItemListModel.setRowCount(0);

		Object[] row;

		Method method;

		for (InvokeItem invokeItem : this.invokedItems) {
			if (type.equals(InvokeItem.Ca)) {
				method = invokeItem.getCaller();
			} else {
				method = invokeItem.getCallee();
			}
			row = new Object[11];

			row[0] = method.getJavaClass().getName();
			row[1] = Utility.accessToString(method.getAccessFlags());
			row[2] = method.getName();
			row[3] = method.getArgumentInfo();
			row[4] = method.getReturnTypes().size() == 0 ? "" : method.getReturnTypes();
			row[5] = method.getSelfLineCount();
			row[6] = method.getInvokedMethods().size();
			row[7] = method.getCascadeInvokedMethods().size();
			row[8] = method.getInvokeMethods().size();
			row[9] = method.containRemoteInvokeItem() ? "是" : "否";
			row[10] = invokeItem.getName();

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

		invokeItemListModel.addColumn("类名");
		invokeItemListModel.addColumn("访问修饰符");
		invokeItemListModel.addColumn("名称");
		invokeItemListModel.addColumn("参数");
		invokeItemListModel.addColumn("返回值");
		invokeItemListModel.addColumn("行数");
		invokeItemListModel.addColumn("传入");
		invokeItemListModel.addColumn("级联传入");
		invokeItemListModel.addColumn("传出");
		invokeItemListModel.addColumn("是否包含进程间调用");
		invokeItemListModel.addColumn("调用类型");

	}
}
