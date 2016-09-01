package jdepend.client.ui.dialog;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jdepend.client.core.config.CommandConfMgr;
import jdepend.client.core.config.GroupConf;
import jdepend.client.report.util.ReportConstant;
import jdepend.client.ui.JDependCooper;
import jdepend.core.score.ScoreFacade;
import jdepend.core.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.model.TableData;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;

public class ScoreListPanel extends JPanel {

	protected JDependCooper frame;

	protected CooperTable scoreListTable;

	protected JScrollPane pane;

	public ScoreListPanel(JDependCooper frame) {
		super();
		this.frame = frame;

		this.setLayout(new BorderLayout());

		try {
			this.add(BorderLayout.CENTER, this.initTable());
		} catch (JDependException e) {
			e.printStackTrace();
			frame.showStatusError(e.getMessage());
		}

	}

	protected JScrollPane initTable() throws JDependException {

		scoreListTable = new CooperTable(this.calTableData());

		pane = new JScrollPane(scoreListTable);

		return pane;
	}

	protected void refresh() throws JDependException {
		scoreListTable.refresh(this.calTableData());
	}

	private TableData calTableData() throws JDependException {

		TableData tableData = new TableData();
		List<ScoreInfo> scoreList = ScoreFacade.getScoreList();
		GroupConf group;
		String attribute;
		for (ScoreInfo scoreInfo : scoreList) {
			tableData.setData("ID", scoreInfo.id);
			group = CommandConfMgr.getInstance().getTheGroup(scoreInfo.group);
			if (group != null) {
				attribute = group.getAttribute();
			} else {
				attribute = null;
			}
			tableData.setData(ReportConstant.Group_Attribute, attribute);
			tableData.setData(ReportConstant.GroupName, scoreInfo.group);
			tableData.setData(ReportConstant.CommandName, scoreInfo.command);
			tableData.setData(ReportConstant.LC, scoreInfo.lc);
			tableData.setData(ReportConstant.Result_ComponentCount, scoreInfo.componentCount);
			tableData.setData(ReportConstant.Result_RelationCount, scoreInfo.relationCount);
			tableData.setData(ReportConstant.Cohesion, scoreInfo.cohesion);
			tableData.setData(ReportConstant.Coupling, scoreInfo.coupling);
			tableData.setData(ReportConstant.D, MetricsFormat.toFormattedMetrics(scoreInfo.distance));
			tableData.setData(ReportConstant.Balance, MetricsFormat.toFormattedMetrics(scoreInfo.balance));
			tableData.setData(ReportConstant.Encapsulation, MetricsFormat.toFormattedMetrics(scoreInfo.encapsulation));
			tableData.setData(ReportConstant.Result_Metrics_RelationRationality,
					MetricsFormat.toFormattedMetrics(scoreInfo.relation));
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_TotalScore),
					MetricsFormat.toFormattedMetrics(scoreInfo.score));
			tableData.setData(BundleUtil.getString(BundleUtil.TableHead_CreateTime), scoreInfo.getCreateDate());
		}
		tableData.setSortColName(BundleUtil.getString(BundleUtil.Metrics_TotalScore));
		tableData.setSortOperation(TableData.DESC);

		if (scoreList.size() > 0) {
			tableData.setMinColName("ID");
		}
		return tableData;

	}

	public Object getId() {
		return scoreListTable.getCurrentes().get(0);
	}

	public List<Object> getCurrentes() {
		return scoreListTable.getCurrentes();
	}

}
