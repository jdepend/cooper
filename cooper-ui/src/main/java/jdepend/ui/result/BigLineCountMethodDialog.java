package jdepend.ui.result;

import java.util.Collection;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.model.Method;

public final class BigLineCountMethodDialog extends CooperDialog {

	public BigLineCountMethodDialog(Collection<Method> bigLineCountMethods) {

		TableData tableData = new TableData();

		for (Method bigLineCountMethod : bigLineCountMethods) {
			tableData.setData("所属类", bigLineCountMethod.getJavaClass().getName());
			tableData.setData("方法名", bigLineCountMethod.getInfo());
			tableData.setData("代码行数", bigLineCountMethod.getSelfLineCount());
			tableData.setData("所属组件", bigLineCountMethod.getJavaClass().getComponent().getName());
		}
		tableData.setSortColName("代码行数");

		this.add(new JScrollPane(new CooperTable(tableData)));
	}

}
