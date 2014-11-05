package jdepend.ui.framework;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTable;

import jdepend.model.JavaClass;
import jdepend.model.Measurable;
import jdepend.model.result.AnalysisResult;
import jdepend.util.refactor.CompareObject;

public class JavaClassCompareTableCellRenderer extends CompareTableCellRenderer {

	private List<String> extendUnits;

	public JavaClassCompareTableCellRenderer(List<String> extendUnits) {
		super();

		this.extendUnits = extendUnits;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			final int row, final int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (this.getComponentCount() > 0) {
			Component component = this.getComponent(0);
			if (extendUnits.contains(table.getValueAt(row, 0))) {
				component.setForeground(Color.GRAY);
			}
		}
		return this;
	}

	@Override
	protected CompareObject getCompareObject(Object value, String id, String metrics) {
		return new CompareObject(value, id, metrics) {
			@Override
			public Object getOriginalityValue(AnalysisResult result) {
				Measurable measurable = result.getTheClass(this.getId());
				if (measurable != null) {
					return measurable.getValue(this.getMetrics());
				} else {
					return null;
				}
			}

			@Override
			public Boolean evaluate(int result, String metrics) {
				if (metrics.equals(JavaClass.Stable)) {
					if (result < 0) {
						return true;
					} else {
						return false;
					}
				} else if (metrics.equals(JavaClass.isPrivateElement)) {
					if (result < 0) {
						return false;
					} else {
						return true;
					}
				} else {
					return null;
				}
			}

		};
	}
}