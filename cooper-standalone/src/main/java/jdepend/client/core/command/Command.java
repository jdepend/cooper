package jdepend.client.core.command;

import jdepend.framework.exception.JDependException;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.ParseListener;
import jdepend.service.AnalyseListener;

/**
 * 命令抽象接口 该接口主要连接ui和后台运算
 * 
 * @author <b>Abner</b>
 * 
 */
public interface Command {

	/**
	 * 任务执行
	 * 
	 * @return
	 * @throws JDependException
	 */
	public AnalysisResult execute() throws JDependException;

	/**
	 * 得到执行任务的大小
	 * 
	 * @return
	 * @throws JDependException 
	 */
	public int getTaskSize() throws JDependException;

	/**
	 * Registers the specified parser listener.
	 * 
	 * @param listener
	 *            Parser listener.
	 */
	public void addParseListener(ParseListener listener) throws JDependException;

	/**
	 * Registers the specified analysis listener.
	 * 
	 * @param listener
	 *            analysis listener.
	 */
	public void addAnalyseListener(AnalyseListener listener) throws JDependException;

}
