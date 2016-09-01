package jdepend.client.ui.result.framework;

import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import jdepend.client.report.util.ReportConstant;


public class JDependTable extends JTable {

	public JDependTable(TableModel arg0) {
		super(arg0);
	}

	@Override
	protected JTableHeader createDefaultTableHeader() {
		return new JTableHeader(columnModel) {
			@Override
			public String getToolTipText(MouseEvent e) {
				String colName = (String) columnModel.getColumn(table.columnAtPoint(e.getPoint())).getHeaderValue();
				return ReportConstant.Tips.get(colName);
			}
		};
	}

}
