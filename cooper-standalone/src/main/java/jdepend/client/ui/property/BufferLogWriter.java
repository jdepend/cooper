package jdepend.client.ui.property;

import jdepend.framework.log.LogUtil;
import jdepend.framework.log.SystemLogWriter;

public class BufferLogWriter implements SystemLogWriter {

	private StringBuffer buffer = new StringBuffer();

	private int count = 0;

	public void systemError(String error) {
		buffer.append(SYSTEMERROR + "-" + LogUtil.getPrinter().getName() + "-"
				+ error + "\n");
		count++;
	}

	public void systemLog(String log) {
		buffer.append(SYSTEMLOG + "-" + LogUtil.getPrinter().getName() + "-"
				+ Runtime.getRuntime().totalMemory() / 1048576 + "M-" + log
				+ "\n");
		count++;
	}

	@Override
	public void systemWarning(String warning) {
		buffer.append(SYSTEMWARNING + "-" + LogUtil.getPrinter().getName()
				+ "-" + warning + "\n");
		count++;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void clear() {
		buffer = new StringBuffer();
		count = 0;
	}

	public int getCount() {
		return count;
	}
}
