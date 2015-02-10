package jdepend.report.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableSorter;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.report.util.ReportConstant;

public class JavaClassCaCeDetailDialog extends CooperDialog {

	private JavaClass javaClass;

	private Collection<JavaClass> javaClasses;

	private String metrics;

	private JTable listTable;

	private DefaultTableModel listModel;

	private boolean includeInner;

	public JavaClassCaCeDetailDialog(JavaClass javaClass, String metrics, boolean includeInner) {

		super(javaClass.getName() + " " + metrics + " list" + (includeInner ? "（全部）" : "（组件外）"));

		this.javaClass = javaClass;
		this.metrics = metrics;
		this.includeInner = includeInner;

		init();

	}

	public JavaClassCaCeDetailDialog(Collection<JavaClass> javaClasses, String metrics) {
		this.javaClasses = javaClasses;
		this.metrics = metrics;

		init();
	}

	private void init() {
		getContentPane().setLayout(new BorderLayout());

		initList();

		showList();

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Name);
		fitColNames.add(ReportConstant.DependType);
		JTableUtil.fitTableColumns(listTable, fitColNames);

		this.add(new JScrollPane(listTable));
	}

	public void showList() {
		listModel.setRowCount(0);
		this.loadList();
	}

	public void loadList() {

		Object[] row;

		Collection<JavaClassRelationItem> items = new ArrayList<JavaClassRelationItem>();

		if (metrics.equals(ReportConstant.Ca)) {
			if (javaClass != null) {
				items = javaClass.getCaItems();
			} else {
				for (JavaClass javaClass : javaClasses) {
					items.addAll(javaClass.getCaItems());
				}
			}
		} else if (metrics.equals(ReportConstant.Ce)) {
			if (javaClass != null) {
				items = javaClass.getCeItems();
			} else {
				for (JavaClass javaClass : javaClasses) {
					items.addAll(javaClass.getCeItems());
				}
			}
		}
		boolean isInner;
		String metrics1 = null;
		for (JavaClassRelationItem item : items) {
			isInner = item.getCurrent().getComponent().containsClass(item.getDepend());
			// 判断是否是环境外的
			if ((javaClass != null && (includeInner || !isInner)) || (javaClasses != null)) {
				row = new Object[listTable.getColumnCount()];
				for (int i = 0; i < listTable.getColumnCount(); i++) {
					if (i == 2) {
						row[2] = item.getType().getName();
					} else if (i == 3) {
						row[3] = isInner ? "否" : "是";
					} else {
						metrics1 = ReportConstant.toMetrics(listTable.getColumnName(i));
						row[i] = item.getDepend().getValue(metrics1);
					}
				}
				listModel.addRow(row);
			}
		}
	}

	private void initList() {

		listModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(listModel);

		listTable = new JTable(sorter);

		sorter.setTableHeader(listTable.getTableHeader());

		listModel.addColumn(ReportConstant.JavaClass_Place);
		listModel.addColumn(ReportConstant.Name);
		listModel.addColumn(ReportConstant.DependType);
		listModel.addColumn(ReportConstant.JavaClass_isExt);
		listModel.addColumn(ReportConstant.LC);
		listModel.addColumn(ReportConstant.AC);
		listModel.addColumn(ReportConstant.Ca);
		listModel.addColumn(ReportConstant.Ce);
		listModel.addColumn(ReportConstant.Coupling);
		listModel.addColumn(ReportConstant.Cohesion);
		listModel.addColumn(ReportConstant.Balance);
		// listModel.addColumn(ReportConstant.OO);
		listModel.addColumn(ReportConstant.Cycle);
		listModel.addColumn(ReportConstant.JavaClass_State);
		listModel.addColumn(ReportConstant.JavaClass_Stable);
		listModel.addColumn(ReportConstant.JavaClass_isPrivateElement);

		listTable.getColumnModel().getColumn(0).setMinWidth(0);
		listTable.getColumnModel().getColumn(0).setMaxWidth(0);
	}
}
