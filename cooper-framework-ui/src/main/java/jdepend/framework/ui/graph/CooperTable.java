package jdepend.framework.ui.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.component.MultiLineTableCellRender;
import jdepend.framework.ui.component.TableMouseMotionAdapter;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;

public class CooperTable extends JTable {

	private JPopupMenu popupMenu = new JPopupMenu();

	private List<String> currentes;

	private DefaultTableModel model;

	public CooperTable(TableData data) {

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		TableSorter sorter = new TableSorter(model);

		this.setModel(sorter);
		sorter.setTableHeader(this.getTableHeader());

		JMenuItem saveAsItem = new JMenuItem(
				BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(CooperTable.this);
			}
		});
		popupMenu.add(saveAsItem);

		final List<TableCallBack> callBacks = data.getCallBacks();

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				final JTable table = (JTable) e.getSource();
				currentes = new ArrayList<String>();
				for (int rowNumber : table.getSelectedRows()) {
					currentes.add((String) table.getValueAt(rowNumber, 0));
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				final JTable table = (JTable) e.getSource();
				String currentCol = (String) table.getColumnModel()
						.getColumn(table.columnAtPoint(e.getPoint()))
						.getHeaderValue();
				if (e.getClickCount() == 2) {
					if (callBacks.size() > 0 && currentes.size() == 1) {
						for (TableCallBack callBack : callBacks) {
							if (currentCol.equals(callBack.colName)) {
								try {
									Constructor c = this
											.getClass()
											.getClassLoader()
											.loadClass(callBack.DialogName)
											.getConstructor(
													new Class[] { String.class });
									JDialog d = (JDialog) c
											.newInstance(new Object[] { currentes
													.get(0) });
									d.setModal(true);
									d.setVisible(true);
								} catch (Exception e1) {
									e1.printStackTrace();
								}

							}
						}
					}
				} else if (e.getButton() == 3) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		if (callBacks.size() > 0) {
			List<String> detailColumnNames = new ArrayList<String>();
			for (TableCallBack callBack : callBacks) {
				detailColumnNames.add(callBack.colName);
			}
			this.addMouseMotionListener(new TableMouseMotionAdapter(this,
					detailColumnNames));
		}

		int columnIndex = 0;
		int sortColumnIndex = -1;
		for (String columnName : data.getColumnNames()) {
			model.addColumn(columnName);
			if (data.getSortColName() != null
					&& data.getSortColName().equals(columnName)) {
				sortColumnIndex = columnIndex;
			}
			columnIndex++;
		}

		if (data.getMinColName() != null) {
			this.getColumn(data.getMinColName()).setMaxWidth(0);
			this.getColumn(data.getMinColName()).setMinWidth(0);
		}

		this.refresh(data);

		if (sortColumnIndex != -1) {
			sorter.setSortingStatus(sortColumnIndex, data.getSortOperation());
		}
	}

	public void refresh(TableData data) {

		model.setRowCount(0);

		Object[] rowData;
		Object itemData;
		int col;
		List<String> multiLineColNames = new ArrayList<String>();
		for (int i = 0; i < data.getRows(); i++) {
			col = 0;
			rowData = new Object[data.getColumnNames().size()];
			for (String columnName : data.getColumnNames()) {
				itemData = data.getColumnValue(columnName, i);
				if (itemData instanceof Float) {
					itemData = MetricsFormat
							.toFormattedMetrics((Float) itemData);
				} else if (itemData instanceof String) {
					if (((String) itemData).indexOf("\n") != -1) {
						multiLineColNames.add(columnName);
					}
				}
				rowData[col++] = itemData;
			}
			model.addRow(rowData);
		}

		for (String multiLineColName : multiLineColNames) {
			this.getColumn(multiLineColName).setCellRenderer(
					new MultiLineTableCellRender());
		}
	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public List<String> getCurrentes() {
		return currentes;
	}
}
