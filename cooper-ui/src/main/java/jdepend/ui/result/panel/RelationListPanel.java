package jdepend.ui.result.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.component.TableMouseMotionAdapter;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.Element;
import jdepend.model.JDependUnitMgr;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ui.CohesionDialog;
import jdepend.report.ui.RelationDetailDialog;
import jdepend.report.util.ReportConstant;
import jdepend.ui.framework.CompareTableCellRenderer;
import jdepend.ui.result.framework.JDependTable;
import jdepend.util.refactor.CompareObject;

public final class RelationListPanel extends JPanel {

	private Collection<Relation> relations;

	private List<String> extendUnits = new ArrayList<String>();

	private DefaultTableModel model;

	private TableSorter sorter;

	private JTable table;

	private JDependFrame frame;

	public RelationListPanel(JDependFrame frame, Collection<Relation> relations) {

		this.setLayout(new BorderLayout());

		this.frame = frame;
		this.relations = relations;

		for (Element element : Relation.calElements(this.relations)) {
			if (!element.getComponent().isInner()) {
				extendUnits.add(element.getComponent().getName());
			}
		}

		initTable();

		loadTableData();

		this.add(new JScrollPane(table));

	}

	private void initTable() {

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.addColumn(ReportConstant.Relation_CurrentName);
		model.addColumn(ReportConstant.Relation_DependName);
		model.addColumn(ReportConstant.Relation_Intensity);
		model.addColumn(ReportConstant.Relation_CurrentCohesion);
		model.addColumn(ReportConstant.Relation_DependCohesion);
		model.addColumn(ReportConstant.Relation_Balance);
		model.addColumn(ReportConstant.Relation_AttentionType);
		model.addColumn(ReportConstant.Relation_AttentionLevel);
		model.addColumn(ReportConstant.Relation_isProblem);

		sorter = new TableSorter(model);

		table = new JDependTable(sorter);

		sorter.setTableHeader(table.getTableHeader());

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				final JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				if (currentRow >= 0) {
					table.setRowSelectionInterval(currentRow, currentRow);
				}
				if (e.getButton() == 3) {
					final JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
					saveAsItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JTableUtil.exportTableToExcel(table);
						}
					});
					popupMenu.add(saveAsItem);
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable table = (JTable) e.getSource();

					String currentCol = (String) table.getColumnModel().getColumn(table.columnAtPoint(e.getPoint()))
							.getHeaderValue();

					if (currentCol.equals(ReportConstant.Relation_Intensity)) {
						String current = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
						String depend = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 1);
						jdepend.model.Component currentComponent = JDependUnitMgr.getInstance().getResult()
								.getTheComponent(current);
						jdepend.model.Component dependComponent = JDependUnitMgr.getInstance().getResult()
								.getTheComponent(depend);
						RelationDetailDialog d = new RelationDetailDialog(frame, currentComponent, dependComponent);
						d.setModal(true);
						d.setVisible(true);
					} else if (currentCol.equals(ReportConstant.Relation_CurrentName)
							|| currentCol.equals(ReportConstant.Relation_DependName)) {

						String element = (String) table.getValueAt(table.rowAtPoint(e.getPoint()),
								table.columnAtPoint(e.getPoint()));

						jdepend.model.Component component = JDependUnitMgr.getInstance().getResult()
								.getTheComponent(element);
						CohesionDialog d = new CohesionDialog(component);
						d.setModal(true);
						d.setVisible(true);
					}
				}
			}
		});

		List<String> colNames = new ArrayList<String>();
		colNames.add(ReportConstant.Relation_CurrentName);
		colNames.add(ReportConstant.Relation_DependName);
		colNames.add(ReportConstant.Relation_Intensity);

		table.addMouseMotionListener(new TableMouseMotionAdapter(table, colNames));

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(table.getColumnName(i)).setCellRenderer(new RelationTableRenderer());
		}
	}

	private void loadTableData() {
		Object[] arg;

		for (Relation relation : relations) {
			arg = new Object[9];
			arg[0] = relation.getCurrent().getName();
			arg[1] = relation.getDepend().getName();
			arg[2] = MetricsFormat.toFormattedMetrics(relation.getIntensity());
			arg[3] = MetricsFormat.toFormattedMetrics(relation.getCurrent().getIntensity());
			arg[4] = MetricsFormat.toFormattedMetrics(relation.getDepend().getIntensity());
			arg[5] = MetricsFormat.toFormattedMetrics(relation.getBalance());
			arg[6] = relation.getAttentionType();
			arg[7] = MetricsFormat.toFormattedMetrics(relation.getAttentionLevel());
			arg[8] = relation.isNormality() ? "" : "是";
			model.addRow(arg);
		}

		sorter.setSortingStatus(7, TableSorter.DESCENDING);

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Relation_CurrentName);
		fitColNames.add(ReportConstant.Relation_DependName);
		JTableUtil.fitTableColumns(table, fitColNames);
	}

	class RelationTableRenderer extends CompareTableCellRenderer {

		public RelationTableRenderer() {
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {

			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (this.getComponentCount() > 0) {
				Component component = this.getComponent(0);
				if (extendUnits.contains(value)) {
					component.setForeground(Color.GRAY);
				}
			}

			return this;
		}

		@Override
		protected String getValue(Object value, int row, int column) {
			if (column != 6) {
				return super.getValue(value, row, column);
			} else {
				return Relation.AttentionTypeList.get(value);
			}
		}

		@Override
		protected Object getOriginality(Object originality, int row, int column) {
			if (column != 6) {
				return super.getOriginality(originality, row, column);
			} else {
				return Relation.AttentionTypeList.get(originality);
			}
		}

		@Override
		protected CompareObject getCompareObject(Object value, String id, String metrics) {
			return new CompareObject(value, id, metrics) {
				@Override
				public Object getOriginalityValue(AnalysisResult result) {
					String current = this.getId().substring(0, this.getId().indexOf('|'));
					String depend = this.getId().substring(this.getId().indexOf('|') + 1);
					Relation relation = result.getTheRelation(current, depend);
					if (relation != null) {
						return relation.getValue(this.getMetrics());
					} else {
						return null;
					}
				}

				@Override
				public Boolean evaluate(int result, String metrics) {
					if (metrics.equals(Relation.CurrentCohesion)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else if (metrics.equals(Relation.DependCohesion)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else if (metrics.equals(Relation.Intensity)) {
						if (result < 0) {
							return true;
						} else {
							return false;
						}
					} else if (metrics.equals(Relation.Balance)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else if (metrics.equals(Relation.AttentionLevel)) {
						if (result < 0) {
							return true;
						} else {
							return false;
						}
					} else if (metrics.equals(Relation.AttentionType)) {
						if (result < 0) {
							return true;
						} else {
							return false;
						}
					} else if (metrics.equals(Relation.isProblem)) {
						if (result < 0) {
							return true;
						} else {
							return false;
						}
					} else {
						return null;
					}
				}
			};
		}

		@Override
		protected String getRowObjectId(JTable table, int row) {
			return (String) table.getValueAt(row, 0) + "|" + (String) table.getValueAt(row, 1);
		}

	}
}
