package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.StringUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.MetricsMgr;
import jdepend.model.component.JavaClassComponent;
import jdepend.model.component.modelconf.CandidateUtil;
import jdepend.model.util.JavaClassUtil;
import jdepend.report.util.ReportConstant;

public class ClassListPanel extends JPanel {

	private DefaultTableModel classListModel;

	protected JTable classListTable;

	protected String current;

	protected List<String> selectedJavaClass;

	// 外部JavaClass名称
	protected List<String> extendUnits = new ArrayList<String>();

	private List<jdepend.model.Component> components;

	protected JDependFrame frame;

	public ClassListPanel(JDependFrame frame) {
		super();

		this.frame = frame;

		setLayout(new BorderLayout());

		this.initClassList();

		JScrollPane pane = new JScrollPane(classListTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);
	}

	public int showClassList(jdepend.model.Component component) {
		clearClassList();

		components = new ArrayList<jdepend.model.Component>();
		components.add(component);
		this.loadClassList();

		this.fitCol();

		return classListModel.getRowCount();
	}

	public int showAllClassList() {
		clearClassList();
		components = JDependUnitMgr.getInstance().getComponents();
		this.loadClassList();

		this.fitCol();

		return classListModel.getRowCount();
	}

	public int filterClassList(String nameFilter, String callerFilter, String calleeFilter) {
		clearClassList();
		this.inFilterClassList(nameFilter, callerFilter, calleeFilter);

		this.fitCol();

		return classListModel.getRowCount();
	}

	public void clearClassList() {
		classListModel.setRowCount(0);
		this.extendUnits = new ArrayList<String>();
	}

	public void initPopupMenu(JavaClassMoveToDialogListener listener) {

		final JPopupMenu popupMenu = new JPopupMenu();

		popupMenu.add(this.createMoveToItem(listener));

		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(getClassListTable());
			}
		});
		popupMenu.add(saveAsItem);

		this.getClassListTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getButton() == 3) {
					popupMenu.show(getClassListTable(), e.getX(), e.getY());
				}
			}
		});
	}

	private void fitCol() {
		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Name);
		JTableUtil.fitTableColumns(classListTable, fitColNames);
	}

	private void loadClassList() {

		Object[] row;
		String metrics = null;
		for (jdepend.model.Component component : components) {
			for (JavaClass javaClass : component.getClasses()) {
				row = new Object[classListTable.getColumnCount()];
				for (int i = 0; i < classListTable.getColumnCount(); i++) {
					metrics = ReportConstant.toMetrics(classListTable.getColumnName(i));
					row[i] = javaClass.getValue(metrics);
				}
				classListModel.addRow(row);
				if (!javaClass.isInner()) {
					this.extendUnits.add(javaClass.getId());
				}
			}
		}
	}

	private void inFilterClassList(String nameFilter, String callerFilter, String calleeFilter) {

		Object[] row;

		String metrics = null;
		for (jdepend.model.Component component : components) {
			for (JavaClass javaClass : component.getClasses()) {
				if ((nameFilter == null || nameFilter.length() == 0 || JavaClassUtil.match(nameFilter, javaClass))
						&& (callerFilter == null || callerFilter.length() == 0 || this.matchCallerFilter(callerFilter,
								javaClass))
						&& (calleeFilter == null || calleeFilter.length() == 0 || this.matchCalleeFilter(calleeFilter,
								javaClass))) {
					row = new Object[classListTable.getColumnCount()];
					for (int i = 0; i < classListTable.getColumnCount(); i++) {
						metrics = ReportConstant.toMetrics(classListTable.getColumnName(i));
						row[i] = javaClass.getValue(metrics);
					}
					classListModel.addRow(row);
					if (!javaClass.isInner()) {
						this.extendUnits.add(javaClass.getId());
					}
				}
			}
		}
	}

	private boolean matchCallerFilter(String callerFilter, JavaClass javaClass) {
		for (JavaClass caClass : javaClass.getCaList()) {
			if (JavaClassUtil.match(callerFilter, caClass)) {
				return true;
			}
		}
		return false;
	}

	private boolean matchCalleeFilter(String calleeFilter, JavaClass javaClass) {
		for (JavaClass ceClass : javaClass.getCeList()) {
			if (JavaClassUtil.match(calleeFilter, ceClass)) {
				return true;
			}
		}
		return false;
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
				if (column == 5 || column == 6) {
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
				current = CandidateUtil.getId((String) table.getValueAt(row, 0), (String) table.getValueAt(row, 1));
				String currentCol = (String) table.getColumnModel().getColumn(col).getHeaderValue();

				selectedJavaClass = new ArrayList<String>();
				for (int rowNumber : table.getSelectedRows()) {
					selectedJavaClass.add(CandidateUtil.getId((String) table.getValueAt(rowNumber, 0),
							(String) table.getValueAt(rowNumber, 1)));
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
						JavaClassCaCeDetailDialog d;
						JavaClass currentClass = JDependUnitMgr.getInstance().getResult().getTheClass(current);
						if (p.x > x && p.x < width) {
							d = new JavaClassCaCeDetailDialog(currentClass, currentCol, false);
						} else {
							d = new JavaClassCaCeDetailDialog(currentClass, currentCol, true);
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

		classListModel.addColumn(ReportConstant.JavaClass_Place);
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
		// classListModel.addColumn(ReportConstant.OO);
		classListModel.addColumn(ReportConstant.Cycle);
		classListModel.addColumn(ReportConstant.JavaClass_State);
		classListModel.addColumn(ReportConstant.JavaClass_Stable);
		classListModel.addColumn(ReportConstant.JavaClass_isPrivateElement);

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

		sorter.setSortingStatus(1, TableSorter.ASCENDING);

		classListTable.getColumnModel().getColumn(0).setMinWidth(0);
		classListTable.getColumnModel().getColumn(0).setMaxWidth(0);

		JavaClassTableRenderer renderer = new JavaClassTableRenderer();
		for (int i = 0; i < classListTable.getColumnCount(); i++) {
			classListTable.getColumn(classListTable.getColumnName(i)).setCellRenderer(renderer);
		}
	}

	protected JTable getClassListTable() {
		return classListTable;
	}

	protected JMenuItem createMoveToItem(final JavaClassMoveToDialogListener listener) {
		JMenuItem moveToItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Move));
		moveToItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedJavaClass.size() > 0) {
					if (JDependUnitMgr.getInstance().getComponents().iterator().next() instanceof JavaClassComponent) {
						JOptionPane.showMessageDialog(frame, "当前的分析单元不能进行移动操作.", "alert", JOptionPane.WARNING_MESSAGE);
					} else {
						moveTo(listener);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "请选择至少一个JavaClass.", "alert", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		return moveToItem;
	}

	private void moveTo(JavaClassMoveToDialogListener listener) {
		JavaClassMoveToDialog d = new JavaClassMoveToDialog(frame, selectedJavaClass);
		if (listener != null) {
			d.setListener(listener);
		}
		d.setModal(true);
		d.setVisible(true);
	}

	public class JavaClassCaCeDetailDialog extends CooperDialog {

		private JavaClass javaClass;

		private String metrics;

		private JTable listTable;

		private DefaultTableModel listModel;

		private boolean includeInner;

		public JavaClassCaCeDetailDialog(JavaClass javaClass, String metrics, boolean includeInner) {

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
			String metrics1 = null;
			for (JavaClassRelationItem item : items) {
				isInner = javaClass.getComponent().containsClass(item.getDepend());
				// 判断是否是环境外的
				if (includeInner || !isInner) {
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
			// listModel.addColumn(ReportConstant.OO);
			listModel.addColumn(ReportConstant.Cycle);
			listModel.addColumn(ReportConstant.JavaClass_State);
			listModel.addColumn(ReportConstant.JavaClass_Stable);
			listModel.addColumn(ReportConstant.JavaClass_isPrivateElement);

			listTable.getColumnModel().getColumn(0).setMinWidth(0);
			listTable.getColumnModel().getColumn(0).setMaxWidth(0);
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
				if (extendUnits.contains(CandidateUtil.getId((String) table.getValueAt(row, 0),
						(String) table.getValueAt(row, 1)))) {
					labelValue.setForeground(Color.GRAY);
				}

				String strValue = String.valueOf(value);
				labelValue.setText(strValue);

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
