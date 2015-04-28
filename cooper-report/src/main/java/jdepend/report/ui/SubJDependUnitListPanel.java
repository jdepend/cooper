package jdepend.report.ui;

import java.awt.BorderLayout;
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

import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.model.MetricsMgr;
import jdepend.model.SubJDependUnit;
import jdepend.report.util.ReportConstant;

public class SubJDependUnitListPanel extends JPanel {

	private JDependFrame frame;

	private DefaultTableModel listModel;

	private JTable listTable;

	private jdepend.model.Component component;

	public SubJDependUnitListPanel(JDependFrame frame, jdepend.model.Component component) {
		super();
		this.setLayout(new BorderLayout());

		this.initList();

		this.frame = frame;
		this.component = component;

		this.loadList(component.getSubJDependUnits());

		JScrollPane pane = new JScrollPane(listTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);
	}

	public int loadList(Collection<? extends SubJDependUnit> subJDependUnits) {

		listModel.setRowCount(0);

		Object[] row;

		for (SubJDependUnit subJDependUnit : subJDependUnits) {
			row = new Object[12];

			row[0] = subJDependUnit.getName();
			row[1] = subJDependUnit.getLineCount();
			row[2] = subJDependUnit.getClassCount();
			row[3] = subJDependUnit.getAbstractClassCount();
			row[4] = subJDependUnit.getAfferentCoupling();
			row[5] = subJDependUnit.getEfferentCoupling();
			row[6] = subJDependUnit.getValue(MetricsMgr.A);
			row[7] = subJDependUnit.getValue(MetricsMgr.I);
			row[8] = subJDependUnit.getValue(MetricsMgr.D);
			row[9] = subJDependUnit.getValue(MetricsMgr.Coupling);
			row[10] = subJDependUnit.getValue(MetricsMgr.Cohesion);
			row[11] = subJDependUnit.getValue(MetricsMgr.Balance);

			listModel.addRow(row);
		}

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Name);
		JTableUtil.fitTableColumns(listTable, fitColNames);

		return listModel.getRowCount();

	}

	protected void initList() {

		listModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(listModel);

		listTable = new JTable(sorter);
		listTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				String current = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
				String currentCol = (String) table.getColumnModel().getColumn(table.columnAtPoint(e.getPoint()))
						.getHeaderValue();
				if (e.getClickCount() == 2) {
					SubJDependUnit subJDependUnit = component.getTheSubJDependUnit(current);
					if (currentCol.equals(ReportConstant.Name)) {
						ClassListDialog d = new ClassListDialog(frame, subJDependUnit.getClasses());
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Ca) || currentCol.equals(ReportConstant.Ce)) {
						JDependUnitCaCeListDialog d = new JDependUnitCaCeListDialog(frame, subJDependUnit, currentCol);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Cohesion)) {
						CohesionSubJDependUnitDialog d = new CohesionSubJDependUnitDialog(subJDependUnit);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Balance)) {
						BalanceSubJDependUnitDialog d = new BalanceSubJDependUnitDialog(subJDependUnit);
						d.setModal(true);
						d.setVisible(true);
					}
				}
			}
		});

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

		// 增加点击图标
		List<String> colNames = new ArrayList<String>();
		colNames.add(ReportConstant.Name);
		colNames.add(ReportConstant.Ca);
		colNames.add(ReportConstant.Ce);
		colNames.add(ReportConstant.Cohesion);
		colNames.add(ReportConstant.Balance);

		listTable.addMouseMotionListener(new TableMouseMotionAdapter(listTable, colNames));
	}
}
