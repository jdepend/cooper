package jdepend.statistics;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import jdepend.core.local.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassWrapper;
import jdepend.model.result.AnalysisResult;

public class ClassInvokeCountAction extends ScoreListAction {

	public ClassInvokeCountAction(StaticsFrame frame) {
		super(frame, "调用类个数排名");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		AnalysisResult result;
		TableData tableData;
		String title;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = scoreCollection.getTheResult(scoreInfo);
			tableData = new TableData();
			for (JavaClass javaClass : result.getClasses()) {
				tableData.setData("类名", javaClass.getName());
				tableData.setData("调用类数量", new JavaClassWrapper(javaClass).getInvokeClasses().size());
			}

			this.progress();
			title = result.getRunningContext().getGroup() + result.getRunningContext().getCommand();
			this.addResult(title, new JScrollPane(new CooperTable(tableData)));
			LogUtil.getInstance(ClassInvokeCountAction.class).systemLog("分析了[" + title + "]的调用类数量");
		}

	}
}
