package jdepend.statistics.action;

import java.awt.event.ActionEvent;

import jdepend.core.local.score.ScoreFacade;
import jdepend.core.local.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.model.result.AnalysisResult;
import jdepend.statistics.StaticsFrame;

public class ScaleRelationAction extends ScoreListAction {

	public ScaleRelationAction(StaticsFrame frame) {
		super(frame, "规模关系合理性分析");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {

		ScoreFacade.sort(scoreCollection.getScoreInfos(), AnalysisResult.Metrics_LC);

		GraphData graph = new GraphData();
		GraphDataItem item = new GraphDataItem();
		item.setTitle("规模关系合理性折线图");
		item.setLineName("关系合理性折线");
		item.setLineXName("代码行数");
		item.setLineYName("关系合理性");
		item.setType(GraphDataItem.SPLINE);
		String tip;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			item.addData(scoreInfo.lc, scoreInfo.relation);
			tip = scoreInfo.group + " " + scoreInfo.command;
			item.addTip(scoreInfo.lc, tip);
			this.progress();
		}
		graph.addItem(item);

		this.addResult("规模关系合理性折线图", GraphUtil.getInstance().createGraph(graph));
	}

}
