package jdepend.framework.ui.component;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class MultiLineTableCellRender extends JTextArea implements TableCellRenderer {

	public JTable extTable = null;

	public MultiLineTableCellRender() {
		setLineWrap(true);
		setWrapStyleWord(true);// JTextArea自动换行的功能
		this.setBorder(BorderFactory.createEmptyBorder(1, 1, 0, 0));

		// 去掉jtextarea的默认边框，因为和JTable的表格线有重叠

	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// 计算当下行的最佳高度

		// 计算了该行所有列的内容所对应的高度，挑选最高的那个
		int maxPreferredHeight = 18;
		for (int i = 0; i < table.getColumnCount(); i++) {
			setText("" + table.getValueAt(row, i));
			setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
			maxPreferredHeight = Math.max(maxPreferredHeight, getPreferredSize().height);
		}

		if (table.getRowHeight(row) != maxPreferredHeight)

		{
			table.setRowHeight(row, maxPreferredHeight);
		}

		if (isSelected) {
			this.setBackground(table.getSelectionBackground());
		} else {
			this.setBackground(table.getBackground());
		}

		setText(value == null ? "" : value.toString());
		return this;
	}

}
