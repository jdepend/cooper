package jdepend.client.ui.property;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.component.MultiLineTableCellRender;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.client.ui.JDependCooper;
import jdepend.util.refactor.AdjustHistory;
import jdepend.util.refactor.Memento;

public class MementoPanel extends JPanel {

	private JDependCooper frame;

	private JTable mementoListTable;

	private DefaultTableModel mementoModel;

	private List<Date> selectedMementos;

	public MementoPanel(JDependCooper frame) {

		this.frame = frame;

		setLayout(new BorderLayout());

		this.add(this.initMementoList());

	}

	public void clear() {
		mementoModel.setRowCount(0);
	}

	public void refresh() {

		mementoModel.setRowCount(0);

		Object[] row;
		for (Memento memento : AdjustHistory.getInstance().getMementos()) {
			row = new Object[7];
			row[0] = memento.getCreateDate();
			row[1] = getAction(memento.getActions());
			row[2] = MetricsFormat.toFormattedMetrics(memento.getResult().getDistance());
			row[3] = MetricsFormat.toFormattedMetrics(memento.getResult().getBalance());
			row[4] = MetricsFormat.toFormattedMetrics(memento.getResult().getEncapsulation());
			row[5] = MetricsFormat.toFormattedMetrics(memento.getResult().getRelationRationality());
			row[6] = MetricsFormat.toFormattedMetrics(memento.getResult().getScore());
			mementoModel.addRow(row);
		}

		if (AdjustHistory.getInstance().getMementos().size() > 0) {
			row = new Object[7];
			row[1] = getAction(AdjustHistory.getInstance().getActions());
			row[2] = MetricsFormat.toFormattedMetrics(AdjustHistory.getInstance().getCurrent().getDistance());
			row[3] = MetricsFormat.toFormattedMetrics(AdjustHistory.getInstance().getCurrent().getBalance());
			row[4] = MetricsFormat.toFormattedMetrics(AdjustHistory.getInstance().getCurrent().getEncapsulation());
			row[5] = MetricsFormat
					.toFormattedMetrics(AdjustHistory.getInstance().getCurrent().getRelationRationality());
			row[6] = MetricsFormat.toFormattedMetrics(AdjustHistory.getInstance().getCurrent().getScore());
			mementoModel.addRow(row);
		}
	}

	private String getAction(List<String> actions) {
		if (actions == null || actions.size() == 0) {
			return null;
		}
		StringBuilder str = new StringBuilder(1000);
		for (String action : actions) {
			str.append(action);
			str.append("\n");
		}

		return str.toString();
	}

	private JComponent initMementoList() {

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem viewMementoItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewResult));
		viewMementoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewMemento();
			}
		});
		popupMenu.add(viewMementoItem);

		JMenuItem compareItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Compare));
		compareItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedMementos.size() == 2) {
					compare(selectedMementos.get(0), selectedMementos.get(1));
				} else {
					JOptionPane.showMessageDialog(frame, "请选择2条需要比较的记录", "alert", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		popupMenu.add(compareItem);

		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(mementoListTable);
			}
		});
		popupMenu.add(saveAsItem);

		mementoModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		mementoModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_CreateTime));
		mementoModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_ActionList));
		mementoModel.addColumn(BundleUtil.getString(BundleUtil.Metrics_D));
		mementoModel.addColumn(BundleUtil.getString(BundleUtil.Metrics_Balance));
		mementoModel.addColumn(BundleUtil.getString(BundleUtil.Metrics_Encapsulation));
		mementoModel.addColumn(BundleUtil.getString(BundleUtil.Metrics_RelationRationality));
		mementoModel.addColumn(BundleUtil.getString(BundleUtil.Metrics_TotalScore));

		this.mementoListTable = new JTable(mementoModel);

		mementoListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				selectedMementos = new ArrayList<Date>();
				for (int row : table.getSelectedRows()) {
					selectedMementos.add((Date) table.getValueAt(row, 0));
				}
				if (e.getClickCount() == 2) {
					viewMemento();
				} else if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

		this.mementoListTable.getColumnModel().getColumn(0).setMaxWidth(130);
		this.mementoListTable.getColumnModel().getColumn(0).setMinWidth(130);

		this.mementoListTable.getColumn(BundleUtil.getString(BundleUtil.TableHead_ActionList)).setCellRenderer(
				new MultiLineTableCellRender());

		refresh();

		JScrollPane pane = new JScrollPane(this.mementoListTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		return pane;

	}

	private void viewMemento() {
		if (this.selectedMementos != null && this.selectedMementos.size() == 1) {
			if (this.selectedMementos.get(0) != null) {
				Memento memento = AdjustHistory.getInstance().getTheMemento(this.selectedMementos.get(0));
				if (memento != null) {
					JDependUnitMgr.getInstance().setResult(memento.getResult());
				}
			} else {
				JDependUnitMgr.getInstance().setResult(AdjustHistory.getInstance().getCurrent());
			}
			AdjustHistory.getInstance().setCompared(null);
			frame.getResultPanelWrapper().refreshResults();
		} else {
			JOptionPane.showMessageDialog(frame, "请选择一条历史进行查看.", "alert", JOptionPane.WARNING_MESSAGE);
			return;
		}

	}

	private void compare(Date id1, Date id2) {

		Memento memento1 = AdjustHistory.getInstance().getTheMemento(id1);
		AdjustHistory.getInstance().setCompared(memento1);

		AnalysisResult result;
		if (id2 != null) {
			result = AdjustHistory.getInstance().getTheMemento(id2).getResult();
		} else {
			result = AdjustHistory.getInstance().getCurrent();
		}
		JDependUnitMgr.getInstance().setResult(result);

		frame.getResultPanelWrapper().showResults(false);
	}
}
