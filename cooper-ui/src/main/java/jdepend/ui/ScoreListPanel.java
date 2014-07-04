package jdepend.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jdepend.core.config.CommandConfMgr;
import jdepend.core.config.GroupConf;
import jdepend.core.score.ScoreInfo;
import jdepend.core.score.ScoreRepository;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
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
		List<ScoreInfo> scoreList = ScoreRepository.getScoreList();
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
			tableData.setData(
					BundleUtil.getString(BundleUtil.ClientWin_Group_Attribute),
					attribute);
			tableData.setData(
					BundleUtil.getString(BundleUtil.TableHead_GroupName),
					scoreInfo.group);
			tableData.setData(
					BundleUtil.getString(BundleUtil.TableHead_CommandName),
					scoreInfo.command);
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_LC),
					scoreInfo.lc);
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_D),
					MetricsFormat.toFormattedMetrics(scoreInfo.d));
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_Balance),
					MetricsFormat.toFormattedMetrics(scoreInfo.balance));
			tableData.setData(
					BundleUtil.getString(BundleUtil.Metrics_Encapsulation),
					MetricsFormat.toFormattedMetrics(scoreInfo.encapsulation));
			tableData.setData(BundleUtil
					.getString(BundleUtil.Metrics_RelationRationality),
					MetricsFormat.toFormattedMetrics(scoreInfo.relation));
			tableData.setData(
					BundleUtil.getString(BundleUtil.Metrics_TotalScore),
					MetricsFormat.toFormattedMetrics(scoreInfo.score));
//			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_OO),
//					MetricsFormat.toFormattedMetrics(scoreInfo.oo));
			tableData.setData(
					BundleUtil.getString(BundleUtil.TableHead_CreateTime),
					scoreInfo.getCreateDate());
		}
		tableData.setSortColName(BundleUtil
				.getString(BundleUtil.Metrics_TotalScore));
		tableData.setSortOperation(TableData.DESC);

		tableData.setMinColName("ID");

		return tableData;

	}

	public String getId() {
		return scoreListTable.getCurrentes().get(0);
	}

	public List<String> getCurrentes() {
		return scoreListTable.getCurrentes();
	}

}
