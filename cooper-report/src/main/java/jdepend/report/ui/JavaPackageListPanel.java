package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.TableSorter;
import jdepend.model.MetricsMgr;
import jdepend.model.component.VirtualPackageComponent;
import jdepend.report.util.ReportConstant;

public class JavaPackageListPanel extends JPanel {

	private JDependFrame frame;

	private DefaultTableModel listModel;

	private JTable listTable;

	private jdepend.model.Component component;

	public JavaPackageListPanel(JDependFrame frame, jdepend.model.Component component) {
		super();
		this.setLayout(new BorderLayout());

		this.initList();

		this.frame = frame;
		this.component = component;

		this.loadList(component.getPackageComponents());

		JScrollPane pane = new JScrollPane(listTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(pane);
	}

	public int loadList(Collection<VirtualPackageComponent> packageComponents) {

		listModel.setRowCount(0);

		Object[] row;

		for (VirtualPackageComponent packageComponent : packageComponents) {
			row = new Object[12];

			row[0] = packageComponent.getName();
			row[1] = packageComponent.getLineCount();
			row[2] = packageComponent.getClassCount();
			row[3] = packageComponent.getAbstractClassCount();
			row[4] = packageComponent.getAfferentCoupling();
			row[5] = packageComponent.getEfferentCoupling();
			row[6] = packageComponent.getValue(MetricsMgr.A);
			row[7] = packageComponent.getValue(MetricsMgr.I);
			row[8] = packageComponent.getValue(MetricsMgr.D);
			row[9] = packageComponent.getValue(MetricsMgr.Coupling);
			row[10] = packageComponent.getValue(MetricsMgr.Cohesion);
			row[11] = packageComponent.getValue(MetricsMgr.Balance);

			listModel.addRow(row);
		}

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
					VirtualPackageComponent packageComponent = component.getThePackageComponent(current);
					if (currentCol.equals(ReportConstant.Ca) || currentCol.equals(ReportConstant.Ce)) {
						ComponentCaCeListDialog d = new ComponentCaCeListDialog(frame, packageComponent, currentCol);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Balance)) {
						BalanceSubJDependUnitDialog d = new BalanceSubJDependUnitDialog(packageComponent);
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
	}
}
