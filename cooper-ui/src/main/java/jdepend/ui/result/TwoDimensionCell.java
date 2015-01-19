package jdepend.ui.result;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import jdepend.framework.ui.JDependFrame;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;
import jdepend.report.ui.RelationDetailDialog;

/**
 * 二维格
 * 
 * @author wangdg
 * 
 */
public final class TwoDimensionCell extends SubResultTabPanel {

	private DefaultTableModel model;

	private JTable table;

	private transient List<JDependUnit> units;

	private JDependFrame frame;

	public TwoDimensionCell(JDependFrame frame) {
		this.frame = frame;
	}

	@Override
	protected void init(final AnalysisResult result) {

		units = new ArrayList<JDependUnit>(result.getComponents());
		Collections.sort(units, new JDependUnitByMetricsComparator(MetricsMgr.Ca));

		this.setLayout(new BorderLayout());

		this.add(initTable());

		this.showTable();

	}

	private JComponent initTable() {

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		table = new JTable(model) {
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				return new TDCRenderer();
			}
		};

		table.setUI(new DragDropRowTableUI());

		table.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = new Point(e.getX(), e.getY());
				int col = table.columnAtPoint(p);
				int row = table.rowAtPoint(p);
				String left = (String) table.getValueAt(row, 0);
				String right = (String) table.getColumnModel().getColumn(col).getHeaderValue();

				jdepend.model.Component leftComponent = JDependUnitMgr.getInstance().getResult().getTheComponent(left);
				jdepend.model.Component rightComponent = JDependUnitMgr.getInstance().getResult()
						.getTheComponent(right);

				if (left != null && left.length() != 0 && right != null && right.length() != 0
						&& isRelation(leftComponent, rightComponent)) {
					table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else if (left != null && left.length() != 0 && right != null && right.length() != 0
						&& isRelation(rightComponent, leftComponent)) {
					table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					table.setCursor(Cursor.getDefaultCursor());
				}
			}
		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();

				if (e.getClickCount() == 2) {
					String left = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
					String right = (String) table.getColumnModel().getColumn(table.columnAtPoint(e.getPoint()))
							.getHeaderValue();
					jdepend.model.Component leftComponent = JDependUnitMgr.getInstance().getResult()
							.getTheComponent(left);
					jdepend.model.Component rightComponent = JDependUnitMgr.getInstance().getResult()
							.getTheComponent(right);

					if (isRelation(leftComponent, rightComponent)) {
						RelationDetailDialog d = new RelationDetailDialog(frame, leftComponent, rightComponent);
						d.setModal(true);
						d.setVisible(true);
					} else if (isRelation(rightComponent, leftComponent)) {
						RelationDetailDialog d = new RelationDetailDialog(frame, rightComponent, leftComponent);
						d.setModal(true);
						d.setVisible(true);
					}
				}
			}
		});

		model.addColumn("");

		for (JDependUnit unit : units) {
			model.addColumn(unit.getName());
		}

		return new JScrollPane(table);
	}

	private void showTable() {
		Object[] row;
		for (JDependUnit unit : units) {
			row = new Object[units.size() + 1];
			row[0] = unit.getName();
			model.addRow(row);
		}
	}

	private boolean isRelation(jdepend.model.Component left, jdepend.model.Component right) {
		return left.getEfferents().contains(right);
	}

	class TDCRenderer extends DefaultTableCellRenderer {

		public TDCRenderer() {
			super();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, final int row, final int column) {
			String left = (String) table.getValueAt(row, 0);
			String right = (String) table.getColumnModel().getColumn(column).getHeaderValue();

			jdepend.model.Component leftComponent = JDependUnitMgr.getInstance().getResult().getTheComponent(left);
			jdepend.model.Component rightComponent = JDependUnitMgr.getInstance().getResult().getTheComponent(right);

			if (left != null && left.length() != 0 && right != null && right.length() != 0
					&& isRelation(leftComponent, rightComponent)) {
				this.setBackground(Color.ORANGE);
			} else if (left != null && left.length() != 0 && right != null && right.length() != 0
					&& TwoDimensionCell.this.isRelation(rightComponent, leftComponent)) {
				this.setBackground(Color.CYAN);
			} else if (column == 0) {
				JTableHeader header = table.getTableHeader();
				setOpaque(true);
				setBorder(BorderFactory.createEtchedBorder());
				setHorizontalAlignment(CENTER);
				setForeground(header.getForeground());
				setBackground(header.getBackground());
				setFont(header.getFont());
				setText(value == null ? "" : value.toString());
			} else {
				return super.getTableCellRendererComponent(table, value, hasFocus, hasFocus, column, column);
			}
			return this;
		}
	}

	class DragDropRowTableUI extends BasicTableUI {

		private boolean draggingRow = false;
		private int startDragPoint;
		private int dyOffset;

		protected MouseInputListener createMouseInputListener() {
			return new DragDropRowMouseInputHandler();
		}

		public void paint(Graphics g, JComponent c) {
			super.paint(g, c);

			if (draggingRow) {
				g.setColor(table.getParent().getBackground());
				Rectangle cellRect = table.getCellRect(table.getSelectedRow(), 0, false);
				g.copyArea(cellRect.x, cellRect.y, table.getWidth(), table.getRowHeight(), cellRect.x, dyOffset);

				if (dyOffset < 0) {
					g.fillRect(cellRect.x, cellRect.y + (table.getRowHeight() + dyOffset), table.getWidth(),
							(dyOffset * -1));
				} else {
					g.fillRect(cellRect.x, cellRect.y, table.getWidth(), dyOffset);
				}
			}
		}

		class DragDropRowMouseInputHandler extends MouseInputHandler {

			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				Point p = new Point(e.getX(), e.getY());
				int col = table.columnAtPoint(p);
				if (col == 0) {
					startDragPoint = (int) e.getPoint().getY();
				}
			}

			public void mouseDragged(MouseEvent e) {
				int fromRow = table.getSelectedRow();
				Point p = new Point(e.getX(), e.getY());
				int col = table.columnAtPoint(p);

				if (fromRow >= 0 && col == 0) {
					draggingRow = true;
					int rowHeight = table.getRowHeight();
					int middleOfSelectedRow = (rowHeight * fromRow) + (rowHeight / 2);

					int toRow = -1;
					int yMousePoint = (int) e.getPoint().getY();

					if (yMousePoint < (middleOfSelectedRow - rowHeight)) {
						// Move row up
						toRow = fromRow - 1;
					} else if (yMousePoint > (middleOfSelectedRow + rowHeight)) {
						// Move row down
						toRow = fromRow + 1;
					}

					if (toRow >= 0 && toRow < table.getRowCount()) {
						TableModel model = table.getModel();

						for (int i = 0; i < model.getColumnCount(); i++) {
							Object fromValue = model.getValueAt(fromRow, i);
							Object toValue = model.getValueAt(toRow, i);

							model.setValueAt(toValue, fromRow, i);
							model.setValueAt(fromValue, toRow, i);
						}
						table.setRowSelectionInterval(toRow, toRow);
						startDragPoint = yMousePoint;
					}

					dyOffset = (startDragPoint - yMousePoint) * -1;
					table.repaint();
				}
			}

			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				draggingRow = false;
				table.repaint();
			}
		}
	}
}
