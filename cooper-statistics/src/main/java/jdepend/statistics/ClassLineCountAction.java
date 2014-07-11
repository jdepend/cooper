package jdepend.statistics;

import java.awt.event.ActionEvent;

import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.element.ClassLineCountAnalyzer;

public class ClassLineCountAction extends ScoreListAction {

	public ClassLineCountAction(StaticsFrame frame) {
		super(frame, "类规模比例分析");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		GraphData graph = new GraphData();
		GraphDataItem item;
		String title;

		ClassLineCountAnalyzer analyzer = new ClassLineCountAnalyzer();
		AnalysisResult result;
		String group;
		String command;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = scoreCollection.getTheResult(scoreInfo);
			item = new GraphDataItem();
			group = result.getRunningContext().getGroup();
			command = result.getRunningContext().getCommand();
			title = group + " " + command + " 类规模比例";
			item.setTitle(title);
			item.setType(GraphDataItem.PIE);

			analyzer.search(result);
			item.setDatas(analyzer.getData());
			graph.addItem(item);

			this.progress();
			LogUtil.getInstance(ClassLineCountAction.class).systemLog(
					"分析了[" + group + "][" + command + "]的ClassLineCount");
		}

		this.addResult("类规模比例饼图", GraphUtil.getInstance().createGraph(graph));

	}
}
