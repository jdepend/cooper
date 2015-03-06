package jdepend.ui.result.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import jdepend.core.config.CommandConfMgr;
import jdepend.core.config.GroupConf;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.model.AreaComponent;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaPackage;
import jdepend.model.Measurable;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultSummary;
import jdepend.report.ui.BalanceComponentDialog;
import jdepend.report.ui.CohesionDialog;
import jdepend.report.ui.CouplingDialog;
import jdepend.report.ui.CycleDialog;
import jdepend.report.util.ReportConstant;
import jdepend.ui.JDependCooper;
import jdepend.ui.command.GroupIngoreListSettingDialog;
import jdepend.ui.componentconf.ComponentConfDialog;
import jdepend.ui.componentconf.CreateComponentConfDialog;
import jdepend.ui.framework.CompareTableCellRenderer;
import jdepend.ui.result.framework.ReportCreator;
import jdepend.ui.result.framework.SubResultTabPanel;
import jdepend.util.refactor.CompareObject;
import jdepend.util.refactor.RefactorToolFactory;

/**
 * 组件列表
 * 
 * @author wangdg
 * 
 */
public final class ComponentListPanel extends SubResultTabPanel {

	private ReportCreator adapter;

	private String[] headers;

	// 外部分析单元行号
	private List<String> extendUnits = new ArrayList<String>();

	protected ArrayList<String> selectedUnits;

	private JDependCooper frame;

	public ComponentListPanel(JDependCooper frame, ReportCreator adapter) {
		this.frame = frame;
		this.adapter = adapter;
	}

	@Override
	protected void init(AnalysisResult result) {

		this.headers = new String[] { ReportConstant.Name, ReportConstant.Component_Area, ReportConstant.LC,
				ReportConstant.CN, ReportConstant.CC, ReportConstant.AC, ReportConstant.Ca, ReportConstant.Ce,
				ReportConstant.A, ReportConstant.V, ReportConstant.I, ReportConstant.D, ReportConstant.Coupling,
				ReportConstant.Cohesion, ReportConstant.Balance, ReportConstant.Encapsulation, ReportConstant.Cycle };

		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		TableSorter sorter = new TableSorter(model);

		final JTable table = new JDependTable(sorter) {
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				return new ComponentTableCellRenderer();
			}
		};

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem addIgnoreItem1 = new JMenuItem(BundleUtil.getString(BundleUtil.Command_AddIgnoreList));
		addIgnoreItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addIgnoreList(false);
				} catch (JDependException ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(addIgnoreItem1);

		JMenuItem addIgnoreItem2 = new JMenuItem(BundleUtil.getString(BundleUtil.Command_AddIgnoreListWithChildren));
		addIgnoreItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					addIgnoreList(true);
				} catch (JDependException ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(addIgnoreItem2);

		JMenuItem viewIgnoreItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewIgnoreList));
		viewIgnoreItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewIgnoreList();
			}
		});
		popupMenu.add(viewIgnoreItem);

		popupMenu.addSeparator();

		JMenuItem uniteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Unite));
		uniteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					uniteComponent();
				} catch (JDependException ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(uniteItem);

		JMenuItem createItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_CreateComponent));
		createItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					createComponent();
				} catch (JDependException ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(createItem);

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_DeleteComponent));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					deleteComponent();
				} catch (JDependException ex) {
					ex.printStackTrace();
					Component source = (Component) e.getSource();
					JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(deleteItem);

		popupMenu.addSeparator();

		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(table);
			}
		});
		popupMenu.add(saveAsItem);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable table1 = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				String current = (String) table1.getValueAt(currentRow, 0);
				adapter.onClickedSummary(current);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectedUnits = new ArrayList<String>();
				for (int row : table.getSelectedRows()) {
					selectedUnits.add((String) table.getValueAt(row, 0));
				}
				String current = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
				String currentCol = (String) table.getColumnModel().getColumn(table.columnAtPoint(e.getPoint()))
						.getHeaderValue();
				if (e.getClickCount() == 2) {
					jdepend.model.Component currentComponent = JDependUnitMgr.getInstance().getResult()
							.getTheComponent(current);
					JTable table = (JTable) e.getSource();
					if (currentCol.equals(ReportConstant.Ca) || currentCol.equals(ReportConstant.Ce)) {
						ComponentCaCeListDialog d = new ComponentCaCeListDialog(frame, currentComponent, currentCol);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Cycle)) {
						if (MetricsMgr.Cyclic.equals(table.getModel().getValueAt(table.rowAtPoint(e.getPoint()),
								table.columnAtPoint(e.getPoint())))) {
							CycleDialog d = new CycleDialog(currentComponent);
							d.setModal(true);
							d.setVisible(true);
						}
					} else if (currentCol.equals(ReportConstant.Name)) {
						ComponentDetailDialog d = new ComponentDetailDialog(currentComponent);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Cohesion)) {
						CohesionDialog d = new CohesionDialog(currentComponent);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Coupling)) {
						CouplingDialog d = new CouplingDialog(currentComponent);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.I)) {
						InstabilityDialog d = new InstabilityDialog(currentComponent);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Balance)) {
						BalanceComponentDialog d = new BalanceComponentDialog(frame, currentComponent);
						d.setModal(true);
						d.setVisible(true);
					}
				} else if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

		sorter.setTableHeader(table.getTableHeader());

		for (int i = 0; i < headers.length; i++) {
			model.addColumn(headers[i]);
		}

		List<String> detailColumnNames = new ArrayList<String>();
		detailColumnNames.add(ReportConstant.Name);
		detailColumnNames.add(ReportConstant.Ca);
		detailColumnNames.add(ReportConstant.Ce);
		detailColumnNames.add(ReportConstant.I);
		detailColumnNames.add(ReportConstant.Coupling);
		detailColumnNames.add(ReportConstant.Cohesion);
		detailColumnNames.add(ReportConstant.Balance);
		detailColumnNames.add(ReportConstant.Cycle);

		table.addMouseMotionListener(new TableMouseMotionAdapter(table, detailColumnNames) {
			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				Point p = new Point(e.getX(), e.getY());
				int col = table.columnAtPoint(p);
				int row = table.rowAtPoint(p);
				if (col == 0 && row > -1) {
					String componentName = (String) table.getValueAt(row, col);
					jdepend.model.Component component = JDependUnitMgr.getInstance().getResult()
							.getTheComponent(componentName);
					if (component != null) {
						table.setToolTipText(component.getLayerDesc());
					}
				} else {
					table.setToolTipText(null);
				}
			}
		});

		sorter.setSortingStatus(0, TableSorter.ASCENDING);

		Object[] row;
		for (jdepend.model.Component unit : result.getComponents()) {
			row = new Object[headers.length];
			for (int i = 0; i < headers.length; i++) {
				row[i] = unit.getValue(ReportConstant.toMetrics(headers[i]));
			}
			if (!unit.isInner()) {
				this.extendUnits.add(unit.getName());
			}
			model.addRow(row);
		}
		model.addRow(this.formatSummary(result.getSummary()));

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Name);
		JTableUtil.fitTableColumns(table, fitColNames);

		JScrollPane rtn = new JScrollPane(table);
		rtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(rtn);

	}

	protected void uniteComponent() throws JDependException {
		if (this.selectedUnits == null || this.selectedUnits.size() < 2) {
			throw new JDependException("请选择至少两个分析单元");
		}
		for (String unit : this.selectedUnits) {
			if (unit.equals(AnalysisResultSummary.Name)) {
				throw new JDependException("不能选择小结进行合并");
			}
		}

		CreateUniteComponentDialog d = new CreateUniteComponentDialog(selectedUnits);
		d.setModal(true);
		d.setVisible(true);

	}

	protected void createComponent() throws JDependException {
		CreateComponentDialog d = new CreateComponentDialog();
		d.setModal(true);
		d.setVisible(true);

	}

	protected void deleteComponent() throws JDependException {
		if (this.selectedUnits == null || this.selectedUnits.size() != 1) {
			throw new JDependException("请选择一个分析单元");
		}
		String componentName = this.selectedUnits.get(0);
		RefactorToolFactory.createTool().deleteComponent(componentName);
		// 刷新
		adapter.onRefactoring();
		// 记录日志
		BusiLogUtil.getInstance().businessLog(Operation.deleteComponent);
	}

	protected void viewIgnoreList() {
		GroupIngoreListSettingDialog d = new GroupIngoreListSettingDialog(frame, adapter.getGroup());
		d.setModal(true);
		d.setVisible(true);
	}

	protected void addIgnoreList(boolean incluedsub) throws JDependException {

		GroupConf group = CommandConfMgr.getInstance().getTheGroup(
				JDependUnitMgr.getInstance().getResult().getRunningContext().getGroup());

		for (String ingorePackage : this.calPackages(incluedsub)) {
			group.addFilteredPackage(ingorePackage);
		}

		CommandConfMgr.getInstance().updateGroups();
		adapter.onAddIgnoreList(this.selectedUnits);
	}

	private List<String> calPackages(boolean incluedsub) throws JDependException {

		if (this.selectedUnits == null || this.selectedUnits.size() == 0) {
			throw new JDependException("请选择分析单元");
		}

		List<String> javaPackages = new ArrayList<String>();
		for (String unitName : this.selectedUnits) {
			jdepend.model.Component component = JDependUnitMgr.getInstance().getResult().getTheComponent(unitName);
			if (component != null) {
				for (JavaPackage javaPackage : component.getJavaPackages()) {
					if (incluedsub) {
						javaPackages.add(javaPackage.getName() + ".*");
					} else {
						javaPackages.add(javaPackage.getName());
					}
				}
			}
		}

		return javaPackages;

	}

	private Object[] formatSummary(AnalysisResultSummary summary) {

		Object[] row = new Object[headers.length];
		for (int i = 0; i < headers.length; i++) {
			row[i] = summary.getValue(ReportConstant.toMetrics(headers[i]));
		}

		return row;
	}

	class CreateUniteComponentDialog extends CreateComponentConfDialog {

		public CreateUniteComponentDialog(List<String> units) {
			super(units);

			StringBuffer name = new StringBuffer();
			for (String unit : units) {
				name.append(unit);
				name.append("|");
			}
			name.delete(name.length() - 1, name.length());

			this.componentname.setText(name.toString());
		}

		@Override
		protected void doService(ActionEvent e) throws JDependException {
			RefactorToolFactory.createTool().uniteComponent(this.componentname.getText(), this.getComponentLayer(),
					units);
			adapter.onRefactoring();
			// 记录日志
			BusiLogUtil.getInstance().businessLog(Operation.uniteComponent);

		}

	}

	class CreateComponentDialog extends ComponentConfDialog {

		@Override
		protected void doService(ActionEvent e) throws JDependException {
			String componentName = this.componentname.getText();
			RefactorToolFactory.createTool().createComponent(componentName, this.getComponentLayer());
			adapter.onRefactoring();
			// 记录日志
			BusiLogUtil.getInstance().businessLog(Operation.createComponent);
		}
	}

	class ComponentTableCellRenderer extends CompareTableCellRenderer {

		public ComponentTableCellRenderer() {
			super();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, final int row, final int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (this.getComponentCount() > 0) {
				Component component = this.getComponent(0);
				if (extendUnits.contains(table.getValueAt(row, 0))) {
					component.setForeground(Color.GRAY);
				}
			}
			return this;
		}

		@Override
		protected String getValue(Object value, int row, int column) {
			if (column != 1) {
				return super.getValue(value, row, column);
			} else {
				if (value != null) {
					return ((AreaComponent) value).getName();
				} else {
					return null;
				}
			}
		}

		@Override
		protected Object getOriginality(Object originality, int row, int column) {
			if (column != 1) {
				return super.getOriginality(originality, row, column);
			} else {
				if (originality != null) {
					return ((AreaComponent) originality).getName();
				} else {
					return null;
				}
			}
		}

		@Override
		protected CompareObject getCompareObject(Object value, String id, String metrics) {
			return new CompareObject(value, id, metrics) {
				@Override
				public Object getOriginalityValue(AnalysisResult result) {
					Measurable measurable;
					if (this.getId().equals(AnalysisResultSummary.Name)) {
						measurable = result.getSummary();
					} else {
						measurable = result.getTheComponent(this.getId());
					}
					if (measurable != null) {
						return measurable.getValue(this.getMetrics());
					} else {
						return null;
					}
				}
			};
		}
	}
}
