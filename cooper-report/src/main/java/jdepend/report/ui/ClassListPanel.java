package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.MetricsMgr;
import jdepend.report.util.ReportConstant;

public class ClassListPanel extends JPanel {

	private DefaultTableModel classListModel;

	private jdepend.model.Component component;

	protected JTable classListTable;

	protected String current;

	protected List<String> selectedJavaClass;

	// 外部JavaClass名称
	private List<String> extendUnits = new ArrayList<String>();

	public ClassListPanel() {
		super();

		setLayout(new BorderLayout());

		this.initClassList();

		JScrollPane pane = new JScrollPane(classListTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);
	}

	public void showClassList(jdepend.model.Component component) {
		clearClassList();
		this.loadClassList(component);

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Name);
		JTableUtil.fitTableColumns(classListTable, fitColNames);
	}

	public void clearClassList() {
		classListModel.setRowCount(0);
		this.extendUnits = new ArrayList<String>();
	}

	private void loadClassList(jdepend.model.Component component) {

		Object[] row;

		this.component = component;

		for (JavaClass javaClass : this.component.getClasses()) {
			row = new Object[17];
			row[0] = javaClass.getName();
			row[1] = javaClass.getLineCount();
			row[2] = javaClass.getConcreteClassCount();
			row[3] = javaClass.getAbstractClassCount();
			row[4] = javaClass.afferentCoupling() + "|" + javaClass.getCaList().size();
			row[5] = javaClass.efferentCoupling() + "|" + javaClass.getCeList().size();
			row[6] = MetricsFormat.toFormattedMetrics(javaClass.abstractness());
			row[7] = MetricsFormat.toFormattedMetrics(javaClass.stability());
			row[8] = MetricsFormat.toFormattedMetrics(javaClass.distance());
			row[9] = MetricsFormat.toFormattedMetrics(javaClass.coupling());
			row[10] = MetricsFormat.toFormattedMetrics(javaClass.cohesion());
			row[11] = MetricsFormat.toFormattedMetrics(javaClass.balance());
			row[12] = MetricsFormat.toFormattedMetrics(javaClass.objectOriented());
			if (javaClass.containsCycle()) {
				row[13] = MetricsMgr.Cyclic;
			}
			if (javaClass.haveState()) {
				row[14] = MetricsMgr.HaveState;
			}
			if (javaClass.isStable()) {
				row[15] = MetricsMgr.Stability;
			}
			if (!javaClass.isUsedByExternal()) {
				row[16] = MetricsMgr.Private;
			}
			classListModel.addRow(row);
			if (!javaClass.isInner()) {
				this.extendUnits.add(javaClass.getName());
			}
		}

	}

	protected void initClassList() {

		classListModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(classListModel) {
			@Override
			protected Comparator getComparator(int column) {
				if (column == 4 || column == 5) {
					return new CaCeComparator();
				} else {
					return super.getComparator(column);
				}
			}
		};

		classListTable = new JTable(sorter);
		classListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point p = new Point(e.getX(), e.getY());
				int col = table.columnAtPoint(p);
				int row = table.rowAtPoint(p);
				current = (String) table.getValueAt(row, 0);
				String currentCol = (String) table.getColumnModel().getColumn(col).getHeaderValue();

				selectedJavaClass = new ArrayList<String>();
				for (int rowNumber : table.getSelectedRows()) {
					selectedJavaClass.add((String) table.getValueAt(rowNumber, 0));
				}
				if (e.getClickCount() == 2) {
					if (currentCol.equals(ReportConstant.Ca) || currentCol.equals(ReportConstant.Ce)) {
						Rectangle rec = table.getCellRect(row, col, false);
						Component comp = table.getComponentAt(p);
						int x = rec.x;
						String value = (String) table.getValueAt(row, col);
						int pos = value.indexOf('|');
						String preValue = value.substring(0, pos);
						int width = x + comp.getFontMetrics(comp.getFont()).stringWidth(preValue);
						DetailDialog d;
						if (p.x > x && p.x < width) {
							d = new DetailDialog(component.getTheClass(current), currentCol, false);
						} else {
							d = new DetailDialog(component.getTheClass(current), currentCol, true);
						}
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Name)) {
						JavaClassDetailDialog d = new JavaClassDetailDialog(current);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Cycle)) {
						if (MetricsMgr.Cyclic.equals(table.getModel().getValueAt(table.rowAtPoint(e.getPoint()),
								table.columnAtPoint(e.getPoint())))) {
							CycleDialog d = new CycleDialog(current);
							d.setModal(true);
							d.setVisible(true);
						}
					} else if (currentCol.equals(ReportConstant.Cohesion)) {
						CohesionDialog d = new CohesionDialog(current);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Coupling)) {
						CouplingDialog d = new CouplingDialog(current);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Balance)) {
						BalanceJavaClassDialog d = new BalanceJavaClassDialog(current);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.I)) {
						DistanceDialog d = new DistanceDialog(current);
						d.setModal(true);
						d.setVisible(true);
					}
				}
			}
		});

		sorter.setTableHeader(classListTable.getTableHeader());

		classListModel.addColumn(ReportConstant.Name);
		classListModel.addColumn(ReportConstant.LC);
		classListModel.addColumn(ReportConstant.CC);
		classListModel.addColumn(ReportConstant.AC);
		classListModel.addColumn(ReportConstant.Ca);
		classListModel.addColumn(ReportConstant.Ce);
		classListModel.addColumn(ReportConstant.A);
		classListModel.addColumn(ReportConstant.I);
		classListModel.addColumn(ReportConstant.D);
		classListModel.addColumn(ReportConstant.Coupling);
		classListModel.addColumn(ReportConstant.Cohesion);
		classListModel.addColumn(ReportConstant.Balance);
		classListModel.addColumn(ReportConstant.OO);
		classListModel.addColumn(ReportConstant.Cycle);
		classListModel.addColumn(ReportConstant.State);
		classListModel.addColumn(ReportConstant.Stable);
		classListModel.addColumn(ReportConstant.isPrivateElement);

		// 增加点击图标
		List<String> colNames = new ArrayList<String>();
		colNames.add(ReportConstant.Name);
		colNames.add(ReportConstant.Ca);
		colNames.add(ReportConstant.Ce);
		colNames.add(ReportConstant.Coupling);
		colNames.add(ReportConstant.Cohesion);
		colNames.add(ReportConstant.Balance);
		colNames.add(ReportConstant.Cycle);
		colNames.add(ReportConstant.I);

		classListTable.addMouseMotionListener(new TableMouseMotionAdapter(classListTable, colNames));

		sorter.setSortingStatus(0, TableSorter.ASCENDING);

		for (int i = 0; i < classListTable.getColumnCount(); i++) {
			classListTable.getColumn(classListTable.getColumnName(i)).setCellRenderer(new JavaClassTableRenderer());
		}
	}

	protected JTable getClassListTable() {
		return classListTable;
	}

	public class DetailDialog extends CooperDialog {

		private JavaClass javaClass;

		private String metrics;

		private JTable listTable;

		private DefaultTableModel listModel;

		private boolean includeInner;

		public DetailDialog(JavaClass javaClass, String metrics, boolean includeInner) {

			super(javaClass.getName() + " " + metrics + " list" + (includeInner ? "（全部）" : "（组件外）"));

			this.javaClass = javaClass;
			this.metrics = metrics;
			this.includeInner = includeInner;

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
				items = javaClass.getCaItems();
			} else if (metrics.equals(ReportConstant.Ce)) {
				items = javaClass.getCeItems();
			}
			boolean isInner;
			for (JavaClassRelationItem item : items) {
				isInner = ClassListPanel.this.component.containsClass(item.getDepend());
				// 判断是否是环境外的
				if (includeInner || !isInner) {
					row = new Object[19];
					row[0] = item.getDepend().getName();
					row[1] = item.getType().getName();
					row[2] = isInner ? "否" : "是";
					row[3] = item.getDepend().getLineCount();
					row[4] = item.getDepend().getConcreteClassCount();
					row[5] = item.getDepend().getAbstractClassCount();
					row[6] = item.getDepend().afferentCoupling() + "|" + item.getDepend().getCaList().size();
					row[7] = item.getDepend().efferentCoupling() + "|" + item.getDepend().getCeList().size();
					row[8] = MetricsFormat.toFormattedMetrics(item.getDepend().abstractness());
					row[9] = MetricsFormat.toFormattedMetrics(item.getDepend().stability());
					row[10] = MetricsFormat.toFormattedMetrics(item.getDepend().distance());
					row[11] = MetricsFormat.toFormattedMetrics(item.getDepend().coupling());
					row[12] = MetricsFormat.toFormattedMetrics(item.getDepend().cohesion());
					row[13] = MetricsFormat.toFormattedMetrics(item.getDepend().balance());
					row[14] = MetricsFormat.toFormattedMetrics(item.getDepend().objectOriented());
					if (item.getDepend().containsCycle()) {
						row[15] = MetricsMgr.Cyclic;
					}
					if (item.getDepend().haveState()) {
						row[16] = MetricsMgr.HaveState;
					}
					if (javaClass.isStable()) {
						row[17] = MetricsMgr.Stability;
					}
					if (!javaClass.isUsedByExternal()) {
						row[18] = MetricsMgr.Private;
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

			listModel.addColumn(ReportConstant.Name);
			listModel.addColumn(ReportConstant.DependType);
			listModel.addColumn(ReportConstant.isExt);
			listModel.addColumn(ReportConstant.LC);
			listModel.addColumn(ReportConstant.CC);
			listModel.addColumn(ReportConstant.AC);
			listModel.addColumn(ReportConstant.Ca);
			listModel.addColumn(ReportConstant.Ce);
			listModel.addColumn(ReportConstant.A);
			listModel.addColumn(ReportConstant.I);
			listModel.addColumn(ReportConstant.D);
			listModel.addColumn(ReportConstant.Coupling);
			listModel.addColumn(ReportConstant.Cohesion);
			listModel.addColumn(ReportConstant.Balance);
			listModel.addColumn(ReportConstant.OO);
			listModel.addColumn(ReportConstant.Cycle);
			listModel.addColumn(ReportConstant.State);
			listModel.addColumn(ReportConstant.Stable);
			listModel.addColumn(ReportConstant.isPrivateElement);
		}
	}

	class JavaClassTableRenderer extends JPanel implements TableCellRenderer {

		public JavaClassTableRenderer() {
			this.setLayout(new BorderLayout());
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {

			this.removeAll();

			if (value != null) {
				JLabel labelValue = new JLabel();

				labelValue.setFont(table.getFont());
				if (extendUnits.contains(table.getValueAt(row, 0))) {
					labelValue.setForeground(Color.GRAY);
				}
				labelValue.setText(String.valueOf(value));

				this.add(labelValue);
			}

			if (isSelected) {
				this.setBackground(table.getSelectionBackground());
			} else {
				this.setBackground(table.getBackground());
			}
			return this;
		}
	}

	class CaCeComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof String && o2 instanceof String) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				if (s1.indexOf('|') != -1 && s2.indexOf('|') != -1) {
					return getValue(s1).compareTo(getValue(s2));
				} else if (o1 instanceof Integer && o2 instanceof Integer) {
					return ((Integer) o1).compareTo((Integer) o2);
				} else if (o1 instanceof Float && o2 instanceof Float) {
					return ((Float) o1).compareTo((Float) o2);
				} else {
					return 0;
				}
			} else if (o1 instanceof Integer && o2 instanceof Integer) {
				return ((Integer) o1).compareTo((Integer) o2);
			} else if (o1 instanceof Float && o2 instanceof Float) {
				return ((Float) o1).compareTo((Float) o2);
			} else {
				return 0;
			}
		}

		private Integer getValue(String data) {
			return Integer.valueOf(data.substring(0, data.indexOf('|')));
		}

	}
}
