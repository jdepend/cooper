package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.bcel.classfile.Utility;

import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.StringUtil;
import jdepend.model.InvokeItem;
import jdepend.model.Method;
import jdepend.model.util.JavaClassUtil;

public class MethodListPanel extends JPanel {

	private DefaultTableModel methodListModel;

	private JTable methodListTable;

	private String current;

	private Collection<Method> methods;

	public MethodListPanel(Collection<Method> methods) {
		super();
		this.setLayout(new BorderLayout());

		this.methods = methods;

		this.initMethodList();

		JScrollPane pane = new JScrollPane(methodListTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);
	}

	public int loadMethodList() {

		methodListModel.setRowCount(0);

		Object[] row;

		for (Method method : this.methods) {
			row = new Object[10];

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

			methodListModel.addRow(row);

		}

		return methodListModel.getRowCount();

	}

	protected void initMethodList() {

		methodListModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(methodListModel);

		methodListTable = new JTable(sorter);
		methodListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point p = new Point(e.getX(), e.getY());
				int col = table.columnAtPoint(p);
				int row = table.rowAtPoint(p);
				current = (String) table.getValueAt(row, 0) + table.getValueAt(row, 2) + table.getValueAt(row, 3);
				String currentCol = (String) table.getColumnModel().getColumn(col).getHeaderValue();

				if (e.getClickCount() == 2) {
					Method currentMethod = getCurrentMethod();
					if (currentMethod != null) {
						if (currentCol.equals("传入")) {
							InvokeItemListDialog d = new InvokeItemListDialog(currentMethod.getInvokedItems(),
									InvokeItem.Ca);
							d.setModal(true);
							d.setVisible(true);
						} else if (currentCol.equals("级联传入")) {
							InvokeItemListDialog d = new InvokeItemListDialog(currentMethod.getCascadeInvokedItems(),
									InvokeItem.Ca);
							d.setModal(true);
							d.setVisible(true);
						} else if (currentCol.equals("传出")) {
							InvokeItemListDialog d = new InvokeItemListDialog(currentMethod.getInvokeItems(),
									InvokeItem.Ce);
							d.setModal(true);
							d.setVisible(true);
						}
					}
				}
			}
		});

		sorter.setTableHeader(methodListTable.getTableHeader());

		methodListModel.addColumn("类名");
		methodListModel.addColumn("访问修饰符");
		methodListModel.addColumn("名称");
		methodListModel.addColumn("参数");
		methodListModel.addColumn("返回值");
		methodListModel.addColumn("行数");
		methodListModel.addColumn("传入");
		methodListModel.addColumn("级联传入");
		methodListModel.addColumn("传出");
		methodListModel.addColumn("是否包含进程间调用");

		// 增加点击图标
		List<String> colNames = new ArrayList<String>();
		colNames.add("传入");
		colNames.add("级联传入");
		colNames.add("传出");

		methodListTable.addMouseMotionListener(new TableMouseMotionAdapter(methodListTable, colNames));

	}

	private Method getCurrentMethod() {
		for (Method method : this.methods) {
			if ((method.getJavaClass().getName() + method.getName() + method.getArgumentInfo()).equals(current)) {
				return method;
			}
		}
		return null;
	}

	public int filterMehtodList(String className, String name) {
		methodListModel.setRowCount(0);

		this.inFilterMethodList(className, name);

		return methodListModel.getRowCount();
	}

	private void inFilterMethodList(String className, String name) {
		Object[] row;

		for (Method method : this.methods) {
			row = new Object[10];

			if ((className == null || className.length() == 0 || JavaClassUtil.match(className, method.getJavaClass()))
					&& (name == null || name.length() == 0 || StringUtil.match(name.toUpperCase(), method.getName()
							.toUpperCase()))) {
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

				methodListModel.addRow(row);
			}
		}
	}
}
