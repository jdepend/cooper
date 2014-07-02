package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.model.relationtype.TableRelation;
import jdepend.report.util.ReportConstant;

public final class RelationDetailPanel extends JPanel {

	private JTable listTable;

	private DefaultTableModel listModel;

	private Relation currentRelation;

	private List<String> extendUnits = new ArrayList<String>();

	public RelationDetailPanel(String current, String depend) {
		this.currentRelation = JDependUnitMgr.getInstance().getResult().getTheRelation(current, depend);

		if (this.currentRelation == null) {
			return;
		}
		display();
	}

	public RelationDetailPanel(Relation relation) {
		this.currentRelation = relation;
		display();
	}

	private void display() {

		for (JavaClass unit : JDependUnitMgr.getInstance().getResult().getClasses()) {
			if (!unit.isInner()) {
				extendUnits.add(unit.getName());
			}
		}

		this.setLayout(new BorderLayout());

		initList();

		if (this.currentRelation != null) {
			showList();
		}

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.CurrentJC);
		fitColNames.add(ReportConstant.DependJC);
		JTableUtil.fitTableColumns(listTable, fitColNames);

		this.add(new JScrollPane(listTable));
	}

	public void showList() {
		listModel.setRowCount(0);
		this.loadList();
	}

	public void loadList() {

		Object[] row;

		Collection<JavaClassRelationItem> items = currentRelation.getItems();
		if (items != null && items.size() != 0) {

			for (JavaClassRelationItem item : items) {
				row = new Object[4];

				row[0] = item.getCurrent().getName();
				row[1] = item.getDepend().getName();
				row[2] = item.getType().getName();
				row[3] = item.getRelationIntensity();

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

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(listTable);
			}
		});
		popupMenu.add(saveAsItem);

		listTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getButton() == 3) {
					popupMenu.show(listTable, e.getX(), e.getY());
				}
			}
		});

		// 监听鼠标移动，修改TIP
		listTable.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				Point point = e.getPoint();
				int row = listTable.rowAtPoint(point);
				int column = listTable.columnAtPoint(point);
				if (column == 2 && listTable.getValueAt(row, column) != null
						&& ((String) listTable.getValueAt(row, column)).equals("Table")) {
					String current = (String) listTable.getValueAt(row, 0);
					String depend = (String) listTable.getValueAt(row, 1);
					for (JavaClassRelationItem item : currentRelation.getItems()) {
						if (item.getCurrent().getName().equals(current) && item.getDepend().getName().equals(depend)) {
							if (item.getType() instanceof TableRelation) {
								listTable.setToolTipText(((TableRelation) item.getType()).getTableName());
							}
						}
					}
				} else {
					listTable.setToolTipText(null);
				}
			}
		});
		sorter.setTableHeader(listTable.getTableHeader());

		listModel.addColumn(ReportConstant.CurrentJC);
		listModel.addColumn(ReportConstant.DependJC);
		listModel.addColumn(ReportConstant.DependType);
		listModel.addColumn(ReportConstant.Relation_Intensity);

		for (int i = 0; i < listTable.getColumnCount(); i++) {
			listTable.getColumn(listTable.getColumnName(i)).setCellRenderer(new JavaClassRelationTableRenderer());
		}
	}

	class JavaClassRelationTableRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (extendUnits.contains(value)) {
				component.setForeground(Color.GRAY);
			}
			return component;
		}
	}
}
