package jdepend.statistics.action;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.GraphData;
import jdepend.framework.ui.graph.GraphDataItem;
import jdepend.framework.ui.graph.GraphUtil;
import jdepend.model.result.AnalysisResult;
import jdepend.statistics.StaticsFrame;

public final class TableRelationScaleAction extends ScoreListAction {

	public TableRelationScaleAction(StaticsFrame frame) {
		super(frame, "基于数据库表实现组件间通讯的比例");
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		Map<Object, Object> datas = new LinkedHashMap<Object, Object>();
		AnalysisResult result;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = scoreCollection.getTheResult(scoreInfo);
			datas.put(result.getRunningContext().getGroup() + "|" + result.getRunningContext().getCommand(),
					result.calTableRelationScale());
			this.progress();
		}

		GraphData data = new GraphData();
		data.setColCount(1);

		GraphDataItem item = new GraphDataItem();

		item.setTitle("基于数据库表实现组件间通讯的比例");
		item.setGroup("Graph");
		item.setType(GraphDataItem.BAR);
		item.setLineXName("项目名称");
		item.setLineYName("基于数据库表实现组件间通讯的比例");
		item.setDatas(datas);

		data.addItem(item);

		this.addResult("基于数据库表实现组件间通讯的比例柱状图", GraphUtil.getInstance().createGraph(data));
	}

}
