package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.bcel.classfile.Utility;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.model.JDependUnitMgr;
import jdepend.model.Method;

public class MethodListDialog extends CooperDialog {

	private DefaultTableModel methodListModel;

	private JTable methodListTable;

	private String current;

	private Collection<Method> methods;

	private Collection<Method> allMethods;

	public MethodListDialog(Collection<Method> methods) {
		super();
		getContentPane().setLayout(new BorderLayout());

		this.methods = methods;

		allMethods = JDependUnitMgr.getInstance().getResult().getMethods();

		this.initMethodList();

		this.loadClassList(methods);

		JScrollPane pane = new JScrollPane(methodListTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);
	}

	public MethodListDialog(jdepend.model.JavaClass javaClass) {
		this(javaClass.getMethods());

	}

	private void loadClassList(Collection<Method> methods) {

		Object[] row;

		for (Method method : methods) {
			row = new Object[8];

			row[0] = method.getJavaClass().getName();
			row[1] = Utility.accessToString(method.getAccessFlags());
			row[2] = method.getName();
			row[3] = method.getArgumentInfo();
			row[4] = method.getReturnTypes().size() == 0 ? "" : method.getReturnTypes();
			row[5] = method.getSelfLineCount();
			row[6] = method.getInvokedMethods(allMethods).size();
			row[7] = method.getInvokeMethods().size();

			methodListModel.addRow(row);

		}

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
							MethodListDialog d = new MethodListDialog(currentMethod.getInvokedMethods(allMethods));
							d.setModal(true);
							d.setVisible(true);
						} else if (currentCol.equals("传出")) {
							MethodListDialog d = new MethodListDialog(currentMethod.getInvokeMethods());
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
		methodListModel.addColumn("传出");

		// 增加点击图标
		List<String> colNames = new ArrayList<String>();
		colNames.add("传入");
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
}
