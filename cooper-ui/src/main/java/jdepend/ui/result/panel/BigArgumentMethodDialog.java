package jdepend.ui.result.panel;

import java.util.Collection;

import javax.swing.JScrollPane;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.metadata.Method;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;

public final class BigArgumentMethodDialog extends CooperDialog {

	public BigArgumentMethodDialog(Collection<Method> bigArgumentMethods) {

		AnalysisResult result = JDependUnitMgr.getInstance().getResult();

		TableData tableData = new TableData();

		for (Method bigArgumentMethod : bigArgumentMethods) {
			tableData.setData("所属类", bigArgumentMethod.getJavaClass().getName());
			tableData.setData("方法名", bigArgumentMethod.getInfo());
			tableData.setData("参数个数", bigArgumentMethod.getArgumentCount());
			tableData.setData("所属组件", result.getTheClass(bigArgumentMethod.getJavaClass().getId()).getComponent()
					.getName());
		}
		tableData.setSortColName("参数个数");

		this.add(new JScrollPane(new CooperTable(tableData)));
	}

}
