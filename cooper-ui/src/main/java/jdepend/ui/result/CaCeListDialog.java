package jdepend.ui.result;

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
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.MetricsMgr;
import jdepend.report.ui.RelationDetailPanel;
import jdepend.report.util.ReportConstant;

public final class CaCeListDialog extends CooperDialog {

	private String unitID;

	private String metrics;

	private JTable listTable;

	private DefaultTableModel listModel;

	public CaCeListDialog(String unitID, String metrics) {

		super(unitID + " " + metrics + " list");

		this.unitID = unitID;
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

		JDependUnit currentUnit = JDependUnitMgr.getInstance().getUnit(this.unitID);

		Collection<? extends JDependUnit> units = new ArrayList<JDependUnit>();

		if (metrics.equals(ReportConstant.Ca)) {
			units = currentUnit.getAfferents();
		} else if (metrics.equals(ReportConstant.Ce)) {
			units = currentUnit.getEfferents();
		}
		for (JDependUnit unit : units) {
			row = new Object[13];
			row[0] = unit.getName();
			row[1] = unit.getLineCount();
			row[2] = unit.getClassCount();
			row[3] = unit.getAbstractClassCount();
			row[4] = unit.afferentCoupling();
			row[5] = unit.efferentCoupling();
			row[6] = MetricsFormat.toFormattedMetrics(unit.abstractness());
			row[7] = MetricsFormat.toFormattedMetrics(unit.stability());
			row[8] = MetricsFormat.toFormattedMetrics(unit.distance());
			row[9] = MetricsFormat.toFormattedMetrics(MetricsFormat.toFormattedMetrics(unit.coupling()));
			row[10] = MetricsFormat.toFormattedMetrics(MetricsFormat.toFormattedMetrics(unit.cohesion()));
			row[11] = MetricsFormat.toFormattedMetrics(MetricsFormat.toFormattedMetrics(unit.objectOriented()));

			if (unit.containsCycle()) {
				row[12] = MetricsMgr.Cyclic;
			} else {
				row[12] = MetricsMgr.NoCyclic;
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
		listModel.addColumn(ReportConstant.OO);
		listModel.addColumn(ReportConstant.Cycle);

		listTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable table = (JTable) e.getSource();
					String current = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
					String currentCol = (String) table.getColumnModel().getColumn(table.columnAtPoint(e.getPoint()))
							.getHeaderValue();
					if (currentCol.equals(ReportConstant.Name)) {
						String left = null;
						String right = null;
						if (metrics.equals(ReportConstant.Ca)) {
							left = current;
							right = unitID;
						} else if (metrics.equals(ReportConstant.Ce)) {
							left = unitID;
							right = current;
						}
						RelationDetailPanel relationDetailPanel = new RelationDetailPanel(left, right);
						CaCeListDialog.this.getContentPane().removeAll();
						CaCeListDialog.this.getContentPane().add(BorderLayout.CENTER, relationDetailPanel);
						FlowLayout buttonFlowLayout = new FlowLayout();
						buttonFlowLayout.setAlignment(FlowLayout.RIGHT);
						JPanel buttonBar = new JPanel(buttonFlowLayout);
						buttonBar.add(createBackButton());
						CaCeListDialog.this.add(BorderLayout.SOUTH, buttonBar);

						CaCeListDialog.this.setVisible(true);
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
				CaCeListDialog.this.getContentPane().removeAll();
				CaCeListDialog.this.getContentPane().add(BorderLayout.CENTER, new JScrollPane(listTable));
				CaCeListDialog.this.setVisible(true);
			}
		});

		return button;
	}
}
