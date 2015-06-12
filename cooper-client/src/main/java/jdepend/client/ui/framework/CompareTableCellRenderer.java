package jdepend.client.ui.framework;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;

import jdepend.framework.exception.JDependException;
import jdepend.client.report.util.ReportConstant;
import jdepend.util.refactor.AdjustHistory;
import jdepend.util.refactor.CompareInfo;
import jdepend.util.refactor.CompareObject;

public abstract class CompareTableCellRenderer extends JPanel implements TableCellRenderer {

	private Object originality;

	public CompareTableCellRenderer() {
		super();

		this.setLayout(new GridLayout());

		ToolTipManager.sharedInstance().registerComponent(this);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			final int row, final int column) {

		this.removeAll();

		if (value != null) {

			JLabel labelValue = new JLabel();
			labelValue.setFont(table.getFont());
			labelValue.setText(this.getValue(value, row, column));

			this.add(labelValue);

			String metrics = ReportConstant.toMetrics(table.getColumnName(column));
			String id = this.getRowObjectId(table, row);
			try {
				CompareInfo info = AdjustHistory.getInstance().compare(this.getCompareObject(value, id, metrics));
				if (info != null && info.isDiff()) {
					CompareInfoWebWarpper warpper = new CompareInfoWebWarpper(info);
					// 暂存原始数据
					originality = this.getOriginality(info.getOriginality(), row, column);
					JLabel labelDirection = new JLabel();
					labelDirection.setFont(table.getFont());
					labelDirection.setText(warpper.getCompare());
					labelDirection.setForeground(warpper.getDirectionColor());
					this.add(labelDirection);
				}
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}

		if (isSelected) {
			this.setBackground(table.getSelectionBackground());
		} else {
			this.setBackground(table.getBackground());
		}

		return this;
	}

	protected abstract CompareObject getCompareObject(Object value, String id, String metrics);

	protected String getRowObjectId(JTable table, int row) {
		return (String) table.getValueAt(row, 0);
	}

	protected String getValue(Object value, int row, int column) {
		return String.valueOf(value);
	}

	protected Object getOriginality(Object originality, int row, int column) {
		return originality;
	}

	@Override
	public String getToolTipText(MouseEvent e) {
		if (originality != null) {
			return "Originality:" + originality;
		} else {
			return null;
		}
	}
}
