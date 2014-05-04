package jdepend.framework.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.JTable;

public class TableMouseMotionAdapter extends MouseMotionAdapter {

	private JTable table;

	private List<String> colNames;

	public TableMouseMotionAdapter(JTable table, List<String> colNames) {
		super();
		this.table = table;
		this.colNames = colNames;
	}

	public void mouseMoved(MouseEvent e) {

		Point p = new Point(e.getX(), e.getY());
		int col = table.columnAtPoint(p);
		int row = table.rowAtPoint(p);
		String colName = table.getColumnName(col);

		if (colNames.contains(colName)) {
			Object obj = table.getValueAt(row, col);
			if (obj == null) {
				table.setCursor(Cursor.getDefaultCursor());
				return;
			} else {
				String value = null;
				if (obj instanceof String) {
					value = (String) obj;
				} else if (obj instanceof Integer) {
					value = Integer.toString((Integer) obj);
				} else if (obj instanceof Float) {
					value = Float.toString((Float) obj);
				} else {
					table.setCursor(Cursor.getDefaultCursor());
					return;
				}
				Rectangle rec = table.getCellRect(row, col, false);
				Component comp = table.getComponentAt(p);
				int x = rec.x;
				int width = x + comp.getFontMetrics(comp.getFont()).stringWidth(value);
				if (p.x > x && p.x < width) {
					table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					table.setCursor(Cursor.getDefaultCursor());
				}
			}
		} else {
			table.setCursor(Cursor.getDefaultCursor());
		}

	}

}
