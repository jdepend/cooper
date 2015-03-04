package jdepend.ui.result.panel;

import java.util.Map;

import javax.swing.JComponent;

import jdepend.framework.exception.JDependException;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.ui.result.framework.ResultPanel;

public class ResultPanelWrapper {

	private ResultPanel resultPanel;

	public ResultPanelWrapper(ResultPanel resultPanel) {
		this.resultPanel = resultPanel;
	}

	/**
	 * 显示内存中JDependUnitMgr中的结果（不弹出分数窗口）
	 */
	public void showResults(boolean isRefreshTodoList) {

		resultPanel.removeAll();

		Map<String, ? extends JComponent> results = null;
		try {
			// 得到内存分析结果
			AnalysisResult result = JDependUnitMgr.getInstance().getResult();
			// 构造报告生成器
			JDependReport jdependReport = new JDependReport(result.getRunningContext().getGroup(), result
					.getRunningContext().getCommand());
			jdependReport.setFrame(resultPanel.getFrame());
			jdependReport.addReportListener(resultPanel.getFrame());
			// 创建图形化结果
			results = jdependReport.createReport(result);
			// 显示结果
			resultPanel.showResults(results, false);
		} catch (Exception ex) {
			ex.printStackTrace();
			resultPanel.showError(ex);
		}
		resultPanel.getFrame().getPropertyPanel().getClassPanel().clearClassList();

		if (isRefreshTodoList) {
			// 刷新TODOList
			new Thread() {
				@Override
				public void run() {
					try {
						resultPanel.getFrame().getPropertyPanel().getToDoListPanel().refresh();
					} catch (JDependException e) {
						e.printStackTrace();
						resultPanel.getFrame().getResultPanel().showError(e);
					}
				}
			}.start();
		}
	}

	/**
	 * 显示内存操作后的结果
	 */
	public void showMemoryResults() {
		int defaultOneIndex = resultPanel.getOneIndex();
		int defaultTwoIndex = resultPanel.getTwoIndex();

		this.showResults(false);

		if (defaultOneIndex != -1 && defaultTwoIndex != -1) {
			resultPanel.setDefaultTab(defaultOneIndex, defaultTwoIndex);
		}
	}

}
