package jdepend.statistics;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.model.result.AnalysisResult;

public class RelationComponentScaleAction extends ScoreListAction {

	public RelationComponentScaleAction(StaticsFrame frame) {
		super(frame, "关系组件个数比例分析");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		AnalysisResult result;
		String group;
		String command;
		TableData tableData = new TableData();
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = scoreCollection.getTheResult(scoreInfo.id);
			if (result.getComponents().size() != 0) {
				group = result.getRunningContext().getGroup();
				command = result.getRunningContext().getCommand();
				tableData.setData("组名", group);
				tableData.setData("命令名", command);
				tableData.setData("组件个数", result.getComponents().size());
				tableData.setData("关系个数", result.getRelations().size());
				tableData.setData("关系组件比例", result.calRelationComponentScale());
				tableData.setData("关系合理性得分", result.calRelationRationality());
				tableData.setData("封装性得分", result.calEncapsulation());
				tableData.setData("总分", result.calScore());
				tableData.setData("关系数量*关系合理性得分", result.getRelations().size() * result.calRelationRationality());
				tableData.setData("关系组件比例*总分", result.calRelationComponentScale() * result.calScore());
				
				this.progress();
				LogUtil.getInstance(RelationComponentScaleAction.class).systemLog(
						"分析了[" + group + "][" + command + "]的ClassLineCount");
			}
		}

		this.addResult("关系组件个数比例表", new JScrollPane(new CooperTable(tableData)));

	}
}
