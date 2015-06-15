package jdepend.statistics.action;

import java.awt.Color;
import java.awt.event.ActionEvent;

import jdepend.core.local.score.ScoreFacade;
import jdepend.core.local.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.BgColorData;
import jdepend.framework.ui.graph.FgColorData;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.ui.graph.RegionColor;
import jdepend.model.result.AnalysisResult;
import jdepend.statistics.StaticsFrame;

public class ScaleScoreAction extends ScoreListAction {

	public ScaleScoreAction(StaticsFrame frame) {
		super(frame, "规模分数分析");
	}

	@Override
	public void analyse(ActionEvent e) throws JDependException {
		ScoreFacade.sort(scoreCollection.getScoreInfos(), AnalysisResult.Metrics_LC);

		GraphData graph = new GraphData();
		GraphDataItem item = new GraphDataItem();
		item.setTitle("规模分数折线图");
		item.setLineName("总分折线");
		item.setLineXName("代码行数");
		item.setLineYName("总分");
		item.setType(GraphDataItem.SPLINE);

		BgColorData backgroundData = new BgColorData();
		backgroundData.setType(GraphDataItem.SPLINE);
		RegionColor region;
		region = new RegionColor();
		region.title = "优";
		region.begin = 90F;
		region.end = 100F;
		region.color = new Color(0, 255, 0, 128);
		backgroundData.addData(region);
		region = new RegionColor();
		region.title = "良";
		region.begin = 80F;
		region.end = 90F;
		region.color = new Color(137, 0, 255, 128);
		backgroundData.addData(region);
		region = new RegionColor();
		region.title = "中";
		region.begin = 70F;
		region.end = 80F;
		region.color = new Color(198, 229, 229, 128);
		backgroundData.addData(region);
		region = new RegionColor();
		region.title = "劣";
		region.begin = 0F;
		region.end = 70F;
		region.color = new Color(255, 0, 0, 32);
		backgroundData.addData(region);

		item.setBgColorData(backgroundData);
		item.setFgColorData(new FgColorData(new Color(0, 0, 255, 128)));

		String tip;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			item.addData(scoreInfo.lc, scoreInfo.score);
			tip = scoreInfo.group + " " + scoreInfo.command;
			item.addTip(scoreInfo.lc, tip);

			this.progress();
		}
		graph.addItem(item);

		this.addResult("规模分数折线图", GraphUtil.getInstance().createGraph(graph));

	}
}
