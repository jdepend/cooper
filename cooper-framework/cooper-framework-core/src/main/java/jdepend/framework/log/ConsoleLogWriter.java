package jdepend.framework.log;

public class ConsoleLogWriter implements SystemLogWriter {

	public void systemError(String error) {
		System.out.println(SYSTEMERROR + "-" + LogUtil.getPrinter().getName() + "-" + error);
	}

	public void systemLog(String log) {
		System.out.println(SYSTEMLOG + "-" + LogUtil.getPrinter().getName() + "-" + Runtime.getRuntime().totalMemory()
				/ 1048576 + "M-" + log);

	}

	@Override
	public void systemWarning(String warning) {
		System.out.println(SYSTEMWARNING + "-" + LogUtil.getPrinter().getName() + "-" + warning);

	}
}
