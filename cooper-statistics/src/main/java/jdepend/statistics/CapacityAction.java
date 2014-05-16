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

public class CapacityAction extends ScoreListAction {

	public CapacityAction(StaticsFrame frame) {
		super(frame, "设计能力分析");
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
				tableData.setData("组件个数", capacity.getComplexity().getComponents());
				tableData.setData("关系个数", capacity.getComplexity().getRelations());
				tableData.setData("类个数", capacity.getComplexity().getClasses());
				tableData.setData("复杂度", capacity.getComplexity().getValue());
				tableData.setData("设计模式使用", capacity.getSkill().getPatterns());
				tableData.setData("平均类大小", capacity.getSkill().getClassSize());
				tableData.setData("500以上类比例", capacity.getSkill().getBigClassScale());
				tableData.setData("设计技巧", capacity.getSkill().getLevel());
				
				this.progress();
				LogUtil.getInstance(CapacityAction.class).systemLog(
						"分析了[" + capacity.getGroup() + "][" + capacity.getCommand() + "]的Capacity");
			}
		}

		this.addResult("关系组件个数比例表", new JScrollPane(new CooperTable(tableData)));

	}
}
