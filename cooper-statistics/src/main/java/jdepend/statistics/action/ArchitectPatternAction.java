package jdepend.statistics.action;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import jdepend.core.domain.WisdomAnalysisResult;
import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.model.TableData;
import jdepend.knowledge.domainanalysis.AdviseInfo;
import jdepend.knowledge.domainanalysis.StructureCategory;
import jdepend.statistics.StaticsFrame;

public class ArchitectPatternAction extends ScoreListAction {

	public ArchitectPatternAction(StaticsFrame frame) {
		super(frame, "架构模式分析");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		WisdomAnalysisResult result;
		TableData tableData = new TableData();
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = new WisdomAnalysisResult(scoreCollection.getTheResult(scoreInfo));
			if (result.getComponents().size() != 0) {
				tableData.setData("组名", result.getRunningContext().getGroup());
				tableData.setData("命令名", result.getRunningContext().getCommand());
				try {
					AdviseInfo advise = result.getAdvise(StructureCategory.ArchitectPatternDomainAnalysis);
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
