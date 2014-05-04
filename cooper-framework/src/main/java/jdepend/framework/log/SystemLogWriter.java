package jdepend.framework.log;

public interface SystemLogWriter {

	static final String SYSTEMLOG = "[LOG]";

	static final String SYSTEMWARNING = "[WARNING]";

	static final String SYSTEMERROR = "[ERROR]";

	void systemLog(String log);

	void systemError(String error);

	void systemWarning(String warning);
}
