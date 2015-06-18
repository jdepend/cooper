package jdepend.statistics.action;

import java.awt.event.ActionEvent;

import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.framework.ui.graph.model.GraphData;
import jdepend.framework.ui.graph.model.GraphDataItem;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassRelationUtil;
import jdepend.statistics.StaticsFrame;

public class ClassRelationTypeAction extends ScoreListAction {

	public ClassRelationTypeAction(StaticsFrame frame) {
		super(frame, "类关系类型比例分析");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		GraphData graph = new GraphData();
		GraphDataItem item;
		String title;

		JavaClassRelationUtil javaClassRelationUtil = null;
		AnalysisResult result;
		String group;
		String command;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = scoreCollection.getTheResult(scoreInfo);
			javaClassRelationUtil = new JavaClassRelationUtil(result);
			item = new GraphDataItem();
			group = result.getRunningContext().getGroup();
			command = result.getRunningContext().getCommand();
			title = group + " " + command + " 类关系类型比例";
			item.setTitle(title);
			item.setType(GraphDataItem.PIE);

			item.setDatas(javaClassRelationUtil.getTypes());
			graph.addItem(item);

			this.progress();
			LogUtil.getInstance(ClassRelationTypeAction.class).systemLog(
					"分析了[" + group + "][" + command + "]的ClassRelationType");
		}

		this.addResult("类关系类型比例饼图", GraphUtil.getInstance().createGraph(graph));

	}
}
