package jdepend.client.report.ui;

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

import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.component.TableMouseMotionAdapter;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.metadata.CandidateUtil;
import jdepend.metadata.InvokeItem;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.relationtype.TableRelation;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;
import jdepend.model.RelationDetail;
import jdepend.model.result.AnalysisResult;
import jdepend.client.report.util.ReportConstant;

public final class RelationDetailPanel extends JPanel {

	private JDependFrame frame;

	private JTable listTable;

	private DefaultTableModel listModel;

	private RelationDetail currentRelation;

	private List<String> extendUnits = new ArrayList<String>();

	private AnalysisResult result;

	public RelationDetailPanel(JDependFrame frame, JDependUnit current, JDependUnit depend) {

		this.frame = frame;
		this.result = JDependUnitMgr.getInstance().getResult();

		this.currentRelation = current.ceCouplingDetail(depend);

		if (this.currentRelation == null) {
			return;
		}
		display();
	}

	public RelationDetailPanel(JDependFrame frame, RelationDetail relation) {

		this.frame = frame;
		this.result = JDependUnitMgr.getInstance().getResult();

		this.currentRelation = relation;
		display();
	}

	private void display() {

		for (JavaClassUnit unit : result.getClasses()) {
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
				row = new Object[7];

				row[0] = item.getId();
				row[1] = item.getSource().getPlace();
				row[2] = item.getSource().getName();
				row[3] = item.getTarget().getPlace();
				row[4] = item.getTarget().getName();
				row[5] = item.getType().getName();
				row[6] = item.getRelationIntensity();

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
				JTable table = (JTable) e.getSource();
				Point p = new Point(e.getX(), e.getY());
				int col = table.columnAtPoint(p);
				int row = table.rowAtPoint(p);

				if (e.getClickCount() == 2) {
					String currentCol = (String) table.getColumnModel().getColumn(col).getHeaderValue();
					if (currentCol.equals(ReportConstant.CurrentJC) || currentCol.equals(ReportConstant.DependJC)) {
						String current;
						if (currentCol.equals(ReportConstant.CurrentJC)) {
							current = CandidateUtil.getId((String) table.getValueAt(row, 1),
									(String) table.getValueAt(row, 2));
						} else {
							current = CandidateUtil.getId((String) table.getValueAt(row, 3),
									(String) table.getValueAt(row, 4));
						}
						JavaClassUnit currentClass = result.getTheClass(current);
						JavaClassRelationGraphDialog d = new JavaClassRelationGraphDialog(frame, currentClass);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.DependType)) {
						String id = (String) table.getValueAt(row, 0);
						JavaClassRelationItem item = result.getTheJavaClassRelationItem(id);
						Collection<InvokeItem> invokedItems = item.getInvokeDetail();
						if (invokedItems.size() > 0) {
							InvokeItemListDialog d = new InvokeItemListDialog(invokedItems);
							d.setModal(true);
							d.setVisible(true);
						}
					}
				} else if (e.getButton() == 3) {
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
				String currentCol = (String) listTable.getColumnModel().getColumn(column).getHeaderValue();
				if (currentCol.equals(ReportConstant.DependType) && listTable.getValueAt(row, column) != null
						&& ((String) listTable.getValueAt(row, column)).equals("Table")) {
					String current = (String) listTable.getValueAt(row, 1);
					String depend = (String) listTable.getValueAt(row, 3);
					for (JavaClassRelationItem item : currentRelation.getItems()) {
						if (item.getSource().getName().equals(current) && item.getTarget().getName().equals(depend)) {
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

		listModel.addColumn("id");
		listModel.addColumn(ReportConstant.CurrentJC_Place);
		listModel.addColumn(ReportConstant.CurrentJC);
		listModel.addColumn(ReportConstant.DependJC_Place);
		listModel.addColumn(ReportConstant.DependJC);
		listModel.addColumn(ReportConstant.DependType);
		listModel.addColumn(ReportConstant.Relation_Intensity);

		listTable.getColumnModel().getColumn(0).setMinWidth(0);
		listTable.getColumnModel().getColumn(0).setMaxWidth(0);

		listTable.getColumnModel().getColumn(1).setMinWidth(0);
		listTable.getColumnModel().getColumn(1).setMaxWidth(0);

		listTable.getColumnModel().getColumn(3).setMinWidth(0);
		listTable.getColumnModel().getColumn(3).setMaxWidth(0);

		for (int i = 0; i < listTable.getColumnCount(); i++) {
			listTable.getColumn(listTable.getColumnName(i)).setCellRenderer(new JavaClassRelationTableRenderer());
		}

		// 增加点击图标
		List<String> colNames = new ArrayList<String>();
		colNames.add(ReportConstant.CurrentJC);
		colNames.add(ReportConstant.DependJC);
		colNames.add(ReportConstant.DependType);

		listTable.addMouseMotionListener(new TableMouseMotionAdapter(listTable, colNames));
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
