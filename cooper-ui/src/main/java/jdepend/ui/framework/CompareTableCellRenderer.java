package jdepend.ui.framework;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;

import jdepend.framework.exception.JDependException;
import jdepend.report.util.ReportConstant;
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
			labelValue.setText(String.valueOf(value));

			this.add(labelValue);

			String metrics = ReportConstant.toMetrics(table.getColumnName(column));
			String objectMeasuredName = (String) table.getValueAt(row, 0);
			try {
				CompareInfo info = AdjustHistory.getInstance().compare(
						this.getCompareObject(value, objectMeasuredName, metrics));
				if (info != null && info.isDiff()) {
					// 暂存原始数据
					originality = info.getOriginality();
					JLabel labelDirection = new JLabel();
					labelDirection.setFont(table.getFont());
					labelDirection.setText(getCompare(info.getResult()));
					labelDirection.setForeground(calDirectionColor(info.getEvaluate()));
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

	@Override
	public String getToolTipText(MouseEvent e) {
		if (originality != null) {
			return "Originality:" + originality;
		} else {
			return null;
		}
	}

	private Color calDirectionColor(Boolean evaluate) {
		if (evaluate == null) {
			return Color.blue;
		} else if (evaluate) {
			return Color.green;
		} else {
			return Color.red;
		}
	}

	private String getCompare(int compare) {
		if (compare < 0) {
			return "↓";
		} else if (compare > 0) {
			return "↑";
		} else {
			return "";
		}
	}
}
