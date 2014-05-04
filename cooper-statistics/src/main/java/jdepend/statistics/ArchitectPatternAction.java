package jdepend.statistics;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.knowledge.AdviseInfo;
import jdepend.knowledge.ExpertFactory;
import jdepend.knowledge.Structure;
import jdepend.knowledge.StructureCategory;
import jdepend.knowledge.capacity.Capacity;
import jdepend.model.result.AnalysisResult;

public class ArchitectPatternAction extends ScoreListAction {

	public ArchitectPatternAction(StaticsFrame frame) {
		super(frame, "架构模式分析");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		AnalysisResult result;
		TableData tableData = new TableData();
		Capacity capacity;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = scoreCollection.getTheResult(scoreInfo.id);
			if (result.getComponents().size() != 0) {
				tableData.setData("组名", result.getRunningContext().getGroup());
				tableData.setData("命令名", result.getRunningContext().getCommand());
				Structure structure = new Structure();
				structure.setCategory(StructureCategory.ArchitectPatternDomainAnalysis);
				structure.setData(result);
				try {
					AdviseInfo advise = new ExpertFactory().createExpert().advise(structure);
					if (advise != null) {
						tableData.setData("架构模式", advise.toString());
					} else {
						tableData.setData("架构模式", "");
					}
				} catch (JDependException e1) {
					e1.printStackTrace();
					tableData.setData("架构模式", "");
				}

				this.progress();
				LogUtil.getInstance(ArchitectPatternAction.class).systemLog(
						"分析了[" + result.getRunningContext().getGroup() + "][" + result.getRunningContext().getCommand()
								+ "]的ArchitectPattern");
			}
		}

		this.addResult("关系组件个数比例表", new JScrollPane(new CooperTable(tableData)));

	}
}
