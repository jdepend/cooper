package jdepend.framework.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.FileUtil;

public class FileLogWriter implements SystemLogWriter {

	private String file;

	private List<LogListener> logListeners = new ArrayList<LogListener>();

	public transient static final String DEFAULT_PROPERTY_FILE = "JDependLog.log";

	public FileLogWriter() {
		file = JDependContext.getWorkspacePath() + "//" + DEFAULT_PROPERTY_FILE;
	}

	public void systemError(String error) {
		this.writer(SYSTEMERROR, error);
	}

	public void systemWarning(String warning) {
		this.writer(SYSTEMWARNING, warning);
	}

	public void systemLog(String log) {
		this.writer(SYSTEMLOG, Runtime.getRuntime().totalMemory() / 1048576 + "M-" + log);
	}

	private void writer(String type, String log) {
		StringBuilder info = new StringBuilder();
		info.append(type);
		info.append("-");
		info.append(LogUtil.getPrinter().getName());
		info.append("-");
		info.append(getCurrentDate());
		info.append("-");
		info.append(log);
		info.append("\r\n");
		try {
			FileUtil.appendFileContent(file, info, "UTF-8");
		} catch (JDependException e) {
			e.printStackTrace();
		}
		onLog();
	}

	private String getCurrentDate() {
		return (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(Calendar.getInstance().getTime());
	}

	private void onLog() {
		for (LogListener logListener : logListeners) {
			if (logListener != null) {
				logListener.onLog();
			}
		}
	}

	public StringBuilder read() throws JDependException {
		try {
			return FileUtil.readFileContent(file, "GBK");
		} catch (JDependException e) {
			e.printStackTrace();
			throw new JDependException("读取日志文件[" + file + "]失败。", e);
		}
	}

	public void addLogListener(LogListener logListener) {
		if (!this.logListeners.contains(logListener)) {
			this.logListeners.add(logListener);
		}
	}

	public void clear() throws JDependException {
		try {
			FileUtil.saveFileContent(file, new StringBuilder(), "GBK");
		} catch (JDependException e) {
			e.printStackTrace();
			throw new JDependException("清除日志文件[" + file + "]失败。", e);
		}

	}
}
