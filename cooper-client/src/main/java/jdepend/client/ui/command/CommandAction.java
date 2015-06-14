package jdepend.client.ui.command;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import jdepend.client.core.local.command.CommandAdapter;
import jdepend.client.core.local.command.CommandAdapterMgr;
import jdepend.core.local.score.ScoreFacade;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.action.AsynAction;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.result.framework.ResultPanel;
import jdepend.client.ui.result.panel.JDependReport;

public final class CommandAction extends AsynAction {

	private JDependCooper frame;

	private CommandAdapter adapter;

	private JDependReport reportRender;

	private AnalysisResult result;

	private String group;

	private String command;

	public CommandAction(JDependFrame frame, String group, String command) throws JDependException {
		super(frame, command);

		this.frame = (JDependCooper) frame;
		this.group = group;
		this.command = command;
		this.adapter = CommandAdapterMgr.getInstance().getTheCommandAdapter(group, command);
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		result = adapter.execute();
		// 创建UI结果
		this.addResults(reportRender.createReport(result));
	}

	@Override
	protected void showResultLater() {
		// 保存分数
		if ((new PropertyConfigurator()).isAutoSaveScore()) {
			try {
				ScoreFacade.saveScore(result, ScoreFacade.ScoreAndResult);
			} catch (JDependException e1) {
				e1.printStackTrace();
				addResults(createErrorResult(e1));
			}
		}
		// 输出日志
		Map<String, JComponent> logReport = new HashMap<String, JComponent>();
		if (adapter.getLog() != null && adapter.getLog().length() != 0) {
			logReport.put("log", ResultPanel.createTextViewer(adapter.getLog()));
		}
		frame.getResultPanel().appendResult(logReport);
		// 刷新TODOList
		frame.getPropertyPanel().showTODOList();
		// 清空缓存
		adapter.clear();
		reportRender.clear();
	}

	@Override
	protected void stopProgressLater() {
		frame.showStatusMessage("分析了 "
				+ JDependUnitMgr.getInstance().getResult().getRunningContext().getJavaClasses().size() + " classes "
				+ JDependUnitMgr.getInstance().getResult().getRunningContext().getJavaPackages().size() + " packages.");
	}

	@Override
	protected void beforeAnalyse() throws JDependException {
		frame.clearPriorResult();

		LogUtil.getInstance(CommandPanel.class).systemLog("准备运行环境");

		adapter.addParseListener(frame);

		reportRender = new JDependReport(group, command);
		reportRender.setFrame(frame);
		reportRender.addReportListener(frame);
	}

	@Override
	protected int getProcess() throws JDependException {
		return adapter.getTaskSize();
	}
}
