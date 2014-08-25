package jdepend.ui.command;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JComponent;

import jdepend.core.command.CommandAdapter;
import jdepend.core.command.CommandAdapterMgr;
import jdepend.core.score.ScoreUtil;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.AsynAction;
import jdepend.framework.ui.JDependFrame;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ReportCreatorFactory;
import jdepend.ui.JDependCooper;
import jdepend.ui.result.JDependReport;
import jdepend.ui.result.JDependReportAdapterFactory;
import jdepend.ui.result.ResultPanel;

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
		this.addResults(reportRender.createMainReport(result));

	}

	@Override
	protected void showResultLater() {

		// 输出其他报告
		Map<String, JComponent> otherReport = this.reportRender.createOtherReport(result);
		// 输出日志
		if (adapter.getLog() != null && adapter.getLog().length() != 0) {
			otherReport.put("log", ResultPanel.createTextViewer(adapter.getLog()));
		}
		frame.getResultPanel().appendResult(otherReport);

		// 刷新TODOList
		frame.getPropertyPanel().showTODOList();
		// 保存分数
		if ((new PropertyConfigurator()).isAutoSaveScore()) {
			try {
				ScoreUtil.saveScore(result, ScoreUtil.ScoreAndResult);
			} catch (JDependException e1) {
				e1.printStackTrace();
				addResults(createErrorResult(e1));
			}
		}
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

		ReportCreatorFactory reportAdapterFactory = new JDependReportAdapterFactory();
		reportRender = (JDependReport) reportAdapterFactory.create(group, command);
		reportRender.setFrame(frame);
		reportRender.addReportListener(frame);
		// 设置正在运行的group和command名称
		CommandAdapterMgr.setCurrentGroup(group);
		CommandAdapterMgr.setCurrentCommand(command);
	}

	@Override
	protected int getProcess() throws JDependException {
		return adapter.getTaskSize();
	}
}
