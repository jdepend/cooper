package jdepend.framework.ui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import jdepend.framework.util.StringUtil;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public final class JTableUtil {

	// 自动设置列宽
	public static void fitTableColumns(JTable table, List<String> colNames) {
		JTableHeader header = table.getTableHeader();
		int rowCount = table.getRowCount();
		Enumeration columns = table.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			if (colNames.contains(column.getHeaderValue())) {
				int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
				int width = (int) table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table,
						column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();
				for (int row = 0; row < rowCount; row++) {
					int preferedWidth = (int) table.getCellRenderer(row, col).getTableCellRendererComponent(table,
							table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
					width = Math.max(width, preferedWidth);
				}
				header.setResizingColumn(column); // 此行很重要
				column.setWidth(width + table.getIntercellSpacing().width);
			}
		}
	}

	// 手动设置列宽
	public static void fitTableColumns(JTable table, int[] columnWidths) {
		for (int i = 0; i < columnWidths.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
		}
	}



	public static void exportTableToExcel(JTable table) {
		File selectedFile = JDependUIUtil.getSelectedFile(".xls", table);
		if (selectedFile != null) {
			exportTableToExcel(selectedFile, table);
		}
	}

	public static void exportTableToExcel(File file, JTable table) {
		try {
			WritableWorkbook workbook = null;// 创建工作薄

			if (file.exists()) {// 文件已经存在
				workbook = Workbook.createWorkbook(file, Workbook.getWorkbook(file));
			} else {// 文件还不存在
				workbook = Workbook.createWorkbook(file);
			}

			WritableSheet sheet = workbook.createSheet("Result", workbook.getNumberOfSheets());// 创建工作表

			// 取得TABLE的行数
			int rowNum = table.getRowCount();
			// 取得TABLE的列数
			int columnNum = table.getColumnCount();

			WritableCellFormat format = new WritableCellFormat();
			format.setWrap(true); // 是否换行

			for (int col = 0; col < columnNum; col++) {
				Label labelN = new Label(col, 0, table.getColumnName(col));
				try {
					sheet.addCell(labelN);
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
			Integer[] colWidth = new Integer[columnNum];

			int maxWidth;
			int currentWidth;
			Object value;
			for (int i = 0; i < columnNum; i++) {// 列
				maxWidth = 0;
				for (int j = 1; j <= rowNum; j++) {//
					value = table.getValueAt(j - 1, i);
					if (value != null) {
						String str = value.toString();
						Label labelN = new Label(i, j, str, format);
						try {
							sheet.addCell(labelN);
						} catch (RowsExceededException e) {
							e.printStackTrace();
						} catch (WriteException e) {
							e.printStackTrace();
						}
						for (String segment : str.split("\n")) {
							currentWidth = StringUtil.length(segment);
							if (currentWidth > maxWidth) {
								maxWidth = currentWidth + 1;
							}
						}
					}
				}
				currentWidth = StringUtil.length(table.getColumnName(i));
				if (currentWidth > maxWidth) {
					maxWidth = currentWidth + 1;
				}
				colWidth[i] = maxWidth;
			}
			for (int i = 0; i < columnNum; i++) {
				sheet.setColumnView(i, colWidth[i]); // 设置列的宽度
			}
			// 写入工作表
			workbook.write();
			try {
				workbook.close();
				int dialog = JOptionPane.showConfirmDialog(null, "导出成功！是否打开？", "温馨提示", JOptionPane.YES_NO_OPTION);
				if (dialog == JOptionPane.YES_OPTION) {
					Runtime.getRuntime().exec("cmd /c start \"\" \"" + file + "\"");
				}
			} catch (WriteException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "导入数据前请关闭工作表");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "保存失败");
		}
	}
}
