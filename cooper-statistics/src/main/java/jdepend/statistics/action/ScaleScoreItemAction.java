package jdepend.statistics.action;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.ui.graph.model.GraphData;
import jdepend.framework.ui.graph.model.GraphDataItem;
import jdepend.model.result.AnalysisResult;
import jdepend.statistics.StaticsFrame;

public class ScaleScoreItemAction extends ScoreListAction {

	public ScaleScoreItemAction(StaticsFrame frame) {
		super(frame, "分数分项分析");
	}

	@Override
	public void analyse(ActionEvent e) throws JDependException {
		Map<Object, Object> scoreItems = new LinkedHashMap<Object, Object>();
		scoreItems.put("抽象程度合理性", 0F);
		scoreItems.put("内聚性", 0F);
		scoreItems.put("封装性", 0F);
		scoreItems.put("关系合理性", 0F);
		if (this.scoreCollection.getScoreInfos().size() > 0) {
			for (ScoreInfo info : this.scoreCollection.getScoreInfos()) {
				scoreItems.put("抽象程度合理性", (Float) scoreItems.get("抽象程度合理性") + info.distance);
				scoreItems.put("内聚性", (Float) scoreItems.get("内聚性") + info.balance);
				scoreItems.put("封装性", (Float) scoreItems.get("封装性") + info.encapsulation);
				scoreItems.put("关系合理性", (Float) scoreItems.get("关系合理性") + info.relation);
			}
//			scoreItems.put("抽象程度合理性", (Float) scoreItems.get("抽象程度合理性") / this.scoreCollection.getScoreInfos().size() / AnalysisResult.Distance);
//			scoreItems.put("内聚性", (Float) scoreItems.get("内聚性") / this.scoreCollection.getScoreInfos().size() / AnalysisResult.Balance);
//			scoreItems.put("封装性", (Float) scoreItems.get("封装性") / this.scoreCollection.getScoreInfos().size() / AnalysisResult.Encapsulation);
//			scoreItems.put("关系合理性", (Float) scoreItems.get("关系合理性") / this.scoreCollection.getScoreInfos().size()
//					/ AnalysisResult.RelationRationality);
		}

		GraphData data = new GraphData();
		data.setColCount(1);

		GraphDataItem item = new GraphDataItem();

		item.setTitle("按分数分项比例");
		item.setGroup("Graph");
		item.setType(GraphDataItem.BAR);
		item.setLineXName("分项名称");
		item.setLineYName("得分比例");
		item.setDatas(scoreItems);

		data.addItem(item);

		this.addResult("分数分项比例柱状图", GraphUtil.getInstance().createGraph(data));

	}
}
