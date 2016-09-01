package jdepend.framework.log;

import jdepend.framework.config.PropertyConfigurator;

public class LogUtil {

	private static LogUtil inst = new LogUtil();

	public static boolean SYSTEMLOG = true;

	public static boolean SYSTEMWARNING = true;

	public static boolean SYSTEMERROR = true;

	private SystemLogWriter writer;

	private static ThreadLocal<Class> printerSelf = new ThreadLocal<Class>();

	private LogUtil() {
		String logWriterClassName = (new PropertyConfigurator()).getLogWriterClassName();
		if (logWriterClassName == null) {
			writer = new ConsoleLogWriter();
		} else {
			try {
				writer = (SystemLogWriter) this.getClass().getClassLoader().loadClass(logWriterClassName).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				writer = new ConsoleLogWriter();
			}
		}
	}

	public static LogUtil getInstance(Class printer) {
		if (printerSelf.get() == null || !printerSelf.get().getName().equals(printer.getName())) {
			printerSelf.set(printer);
		}
		return inst;
	}

	public static LogUtil getInstance() {
		return inst;
	}

	public static Class getPrinter() {
		return printerSelf.get();
	}
	
	public void setSystemLogWriter(SystemLogWriter writer){
		this.writer = writer;
	}

	public void systemLog(String log) {
		if (SYSTEMLOG)
			writer.systemLog(log);
	}

	public void systemWarning(String log) {
		if (SYSTEMWARNING)
			writer.systemWarning(log);
	}

	public void systemError(String error) {
		if (SYSTEMERROR)
			writer.systemError(error);
	}

	public void addLogListener(LogListener logListener) {
		if (writer instanceof FileLogWriter) {
			((FileLogWriter) writer).addLogListener(logListener);
		}
	}

}
