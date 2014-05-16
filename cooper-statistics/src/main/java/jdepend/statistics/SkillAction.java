package jdepend.statistics;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.knowledge.capacity.Capacity;
import jdepend.knowledge.capacity.CapacityMgr;
import jdepend.model.result.AnalysisResult;

public class SkillAction extends ScoreListAction {

	public SkillAction(StaticsFrame frame) {
		super(frame, "设计技巧分析");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		AnalysisResult result;
		TableData tableData = new TableData();
		Capacity capacity;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = scoreCollection.getTheResult(scoreInfo.id);
			if (result.getComponents().size() != 0) {
				capacity = CapacityMgr.getInstance().getCapacity(result);
				tableData.setData("组名", capacity.getGroup());
				tableData.setData("命令名", capacity.getCommand());
				tableData.setData("总分", capacity.getScore().getScore());
				tableData.setData("能力", capacity.getLevel());
				tableData.setData("复杂度", capacity.getComplexity().getValue());
				tableData.setData("设计模式使用", capacity.getSkill().getPatterns());
				tableData.setData("平均类大小", capacity.getSkill().getClassSize());
				tableData.setData("500以上类比例", capacity.getSkill().getBigClassScale());
				tableData.setData("长参数方法比例", capacity.getSkill().getBigArgumentMethodScale());
				tableData.setData("长方法比例", capacity.getSkill().getBigLineCountMethodScale());
				tableData.setData("继承关系比例", capacity.getSkill().getInheritRelationScale());
				tableData.setData("包含关系比例", capacity.getSkill().getFieldRelationScale());
				tableData.setData("参数关系比例", capacity.getSkill().getParamRelationScale());
				tableData.setData("调用关系比例", capacity.getSkill().getVariableRelationScale());
				tableData.setData("表关系比例", capacity.getSkill().getTableRelationScale());
				tableData.setData("设计技巧", capacity.getSkill().getLevel());
				
				this.progress();
				LogUtil.getInstance(SkillAction.class).systemLog(
						"分析了[" + capacity.getGroup() + "][" + capacity.getCommand() + "]的Capacity");
			}
		}

		this.addResult("关系组件个数比例表", new JScrollPane(new CooperTable(tableData)));

	}
}
