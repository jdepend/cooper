package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.MetricsMgr;
import jdepend.report.util.ReportConstant;

public final class ComponentCaCeListDialog extends CooperDialog {

	private JDependFrame frame;

	private Component component;

	private String metrics;

	private JTable listTable;

	private DefaultTableModel listModel;

	public ComponentCaCeListDialog(JDependFrame frame, Component component, String metrics) {

		super(component.getName() + " " + metrics + " list");

		this.frame = frame;

		this.component = component;
		this.metrics = metrics;

		initList();

		showList();

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Name);
		JTableUtil.fitTableColumns(listTable, fitColNames);

		this.add(new JScrollPane(listTable));
	}

	public void showList() {
		listModel.setRowCount(0);
		this.loadList();
	}

	public void loadList() {

		Object[] row;

		Collection<? extends JDependUnit> units = new ArrayList<JDependUnit>();

		if (metrics.equals(ReportConstant.Ca)) {
			units = this.component.getAfferents();
		} else if (metrics.equals(ReportConstant.Ce)) {
			units = this.component.getEfferents();
		}
		for (JDependUnit unit : units) {
			row = new Object[13];
			row[0] = unit.getName();
			row[1] = unit.getLineCount();
			row[2] = unit.getClassCount();
			row[3] = unit.getAbstractClassCount();
			row[4] = unit.getAfferentCoupling();
			row[5] = unit.getEfferentCoupling();
			row[6] = unit.getValue(MetricsMgr.A);
			row[7] = unit.getValue(MetricsMgr.I);
			row[8] = unit.getValue(MetricsMgr.D);
			row[9] = unit.getValue(MetricsMgr.Coupling);
			row[10] = unit.getValue(MetricsMgr.Cohesion);
			row[11] = unit.getValue(MetricsMgr.Balance);

			if (unit.getContainsCycle()) {
				row[12] = MetricsMgr.Cyclic;
			} else {
				row[12] = MetricsMgr.NoValue;
			}

			listModel.addRow(row);
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

		listModel.addColumn(ReportConstant.Cycle);

		listTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable table = (JTable) e.getSource();
					String current = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
					String currentCol = (String) table.getColumnModel().getColumn(table.columnAtPoint(e.getPoint()))
							.getHeaderValue();
					Component currentComponent = JDependUnitMgr.getInstance().getResult().getTheComponent(current);
					if (currentCol.equals(ReportConstant.Name)) {
						Component left = null;
						Component right = null;
						if (metrics.equals(ReportConstant.Ca)) {
							left = currentComponent;
							right = component;
						} else if (metrics.equals(ReportConstant.Ce)) {
							left = component;
							right = currentComponent;
						}
						RelationDetailPanel relationDetailPanel = new RelationDetailPanel(frame, left, right);
						ComponentCaCeListDialog.this.getContentPane().removeAll();
						ComponentCaCeListDialog.this.getContentPane().add(BorderLayout.CENTER, relationDetailPanel);
						FlowLayout buttonFlowLayout = new FlowLayout();
						buttonFlowLayout.setAlignment(FlowLayout.RIGHT);
						JPanel buttonBar = new JPanel(buttonFlowLayout);
						buttonBar.add(createBackButton());
						ComponentCaCeListDialog.this.add(BorderLayout.SOUTH, buttonBar);

						ComponentCaCeListDialog.this.setVisible(true);
					}
				}
			}
		});

		List<String> detailColumnNames = new ArrayList<String>();
		detailColumnNames.add(ReportConstant.Name);

		listTable.addMouseMotionListener(new TableMouseMotionAdapter(listTable, detailColumnNames));
	}

	private JLabel createBackButton() {

		JLabel button = new JLabel("<html><a href='#'>返回</a></html>");
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ComponentCaCeListDialog.this.getContentPane().removeAll();
				ComponentCaCeListDialog.this.getContentPane().add(BorderLayout.CENTER, new JScrollPane(listTable));
				ComponentCaCeListDialog.this.setVisible(true);
			}
		});

		return button;
	}
}
