package jdepend.ui.result.panel;

import java.util.Collection;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.metadata.Method;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;

public final class BigLineCountMethodDialog extends CooperDialog {

	public BigLineCountMethodDialog(Collection<Method> bigLineCountMethods) {

		AnalysisResult result = JDependUnitMgr.getInstance().getResult();

		TableData tableData = new TableData();

		for (Method bigLineCountMethod : bigLineCountMethods) {
			tableData.setData("所属类", bigLineCountMethod.getJavaClass().getName());
			tableData.setData("方法名", bigLineCountMethod.getInfo());
			tableData.setData("代码行数", bigLineCountMethod.getSelfLineCount());
			tableData.setData("所属组件", result.getTheClass(bigLineCountMethod.getJavaClass().getId()).getComponent()
					.getName());
		}
		tableData.setSortColName("代码行数");

		this.add(new JScrollPane(new CooperTable(tableData)));
	}

}
