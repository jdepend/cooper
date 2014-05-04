package jdepend.report;

import java.util.List;

import jdepend.framework.exception.JDependException;

/**
 * 报告层监听器
 * 
 * 监听输出报告的Listener
 * 
 * @author <b>Abner</b>
 * 
 */
public interface ReportListener {

	/**
	 * 当保存文本报告时触发
	 * 
	 * @param group
	 * @param command
	 */
	public void onSaveReport(String group, String command);

	/**
	 * 当点击Summary行时触发
	 * 
	 * @param unitID
	 */
	public void onClickedSummary(String unitID);

	/**
	 * 当增加组忽略列表时触发
	 * 
	 * @param ignoreList
	 */
	public void onAddIgnoreList(List<String> ignoreList);

	/**
	 * 当点击查看组忽略列表时触发
	 * 
	 * @param group
	 */
	public void onViewIgnoreList(String group);

	/**
	 * 当做内存重构时触发
	 * 
	 * @throws JDependException
	 * 
	 */
	public void onRefactoring() throws JDependException;

}
