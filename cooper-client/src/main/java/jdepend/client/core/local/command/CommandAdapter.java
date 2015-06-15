package jdepend.client.core.local.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jdepend.client.core.local.config.CommandConf;
import jdepend.client.core.local.config.CommandConfException;
import jdepend.client.core.local.config.CommandConfMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.log.Operation;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.ParseListener;
import jdepend.service.AnalyseListener;

/**
 * 客户端<Code>Command</Code>适配器
 * 
 * 用于调用<Code>Command</Code>
 * 
 * @author <b>Abner</b>
 * 
 */
public class CommandAdapter {

	private CommandConf info;
	private DefaultCommand command;
	private StringBuilder log;

	/**
	 * 根据CommandInfo创建CommandAdapter
	 * 
	 * @param info
	 */
	public CommandAdapter(CommandConf info) {
		this.info = info;
	}

	private DefaultCommand createCommand() throws JDependException {
		DefaultCommand command = new DefaultCommand(info.group, info.label);
		// 处理路径
		command.addDirectory(info.getPath());
		// 初始化命令参数
		try {
			command.initArgs(info.args);
		} catch (JDependException e) {
			throw new CommandConfException(info.group, info.label, "命令["
					+ command.getClass().getName() + "]初始化参数失败！", e);
		}
		// 设置filteredPackages
		command.addFilteredPackages(CommandConfMgr.getInstance()
				.getTheGroup(info.group).getFilteredPackages());

		return command;

	}

	public void clear() {
		this.command = null;
		this.log = null;
	}

	private DefaultCommand getCommand() throws JDependException {
		if (command == null) {
			command = createCommand();
		}
		return command;
	}

	public String getLabel() {
		return info.label;
	}

	/**
	 * 命令执行方法
	 * 
	 * 在第一次执行Command时构建Command对象，参见方法getCommand
	 * 
	 * @return
	 * @throws JDependException
	 */
	public AnalysisResult execute() throws JDependException {

		ByteArrayOutputStream logStream = new ByteArrayOutputStream();

		getCommand().setLogStream(logStream);

		LogUtil.getInstance(CommandAdapter.class).systemLog("开始执行分析");
		long start = System.currentTimeMillis();
		AnalysisResult result = getCommand().execute();
		LogUtil.getInstance(CommandAdapter.class).systemLog(
				"Command " + info.label + " execute : "
						+ (System.currentTimeMillis() - start));
		// 记录日志
		BusiLogUtil.getInstance().businessLog(Operation.executeCommand);

		try {
			logStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		log = new StringBuilder(logStream.toString());
		try {
			logStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public int getTaskSize() throws JDependException {
		long start = System.currentTimeMillis();
		int size = getCommand().getTaskSize();
		LogUtil.getInstance(CommandAdapter.class).systemLog(
				"Command " + info.label + " getTaskSize : "
						+ (System.currentTimeMillis() - start));

		return size;
	}

	/**
	 * Registers the specified parser listener.
	 * 
	 * @param listener
	 *            Parser listener.
	 * @throws JDependException
	 */
	public void addParseListener(ParseListener listener)
			throws JDependException {
		getCommand().addParseListener(listener);
	}

	/**
	 * Registers the specified analyse listener.
	 * 
	 * @param listener
	 *            analysis listener.
	 * @throws JDependException
	 */
	public void addAnalyseListener(AnalyseListener listener)
			throws JDependException {
		getCommand().addAnalyseListener(listener);
	}

	public StringBuilder getLog() {
		return log;
	}

	public String getTip() {
		return info.tip;
	}

}
