package jdepend.framework.ui.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.component.MultiLineTableCellRender;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.graph.model.TableData;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;

public class CooperTable extends JTable {

	private JPopupMenu popupMenu = new JPopupMenu();

	private List<Object> currentes;

	private DefaultTableModel model;

	public CooperTable(TableData data) {
		this(data, false);
	}

	public CooperTable(TableData data, final boolean editable) {

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return editable;
			}
		};

		TableSorter sorter = new TableSorter(model);

		this.setModel(sorter);
		sorter.setTableHeader(this.getTableHeader());

		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(CooperTable.this);
			}
		});
		popupMenu.add(saveAsItem);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				final JTable table = (JTable) e.getSource();
				currentes = new ArrayList<Object>();
				for (int rowNumber : table.getSelectedRows()) {
					currentes.add(table.getValueAt(rowNumber, 0));
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			 if (e.getButton() == 3) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		int columnIndex = 0;
		int sortColumnIndex = -1;
		for (String columnName : data.getColumnNames()) {
			model.addColumn(columnName);
			if (data.getSortColName() != null && data.getSortColName().equals(columnName)) {
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
					itemData = MetricsFormat.toFormattedMetrics((Float) itemData);
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
			this.getColumn(multiLineColName).setCellRenderer(new MultiLineTableCellRender());
		}
	}

	public JPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public List<Object> getCurrentes() {
		return currentes;
	}
}
