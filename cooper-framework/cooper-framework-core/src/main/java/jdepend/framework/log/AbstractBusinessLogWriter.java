package jdepend.framework.log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jdepend.framework.exception.JDependException;

public abstract class AbstractBusinessLogWriter implements BusinessLogWriter {

	private List<BusiLogListener> listeners = new ArrayList<BusiLogListener>();

	@Override
	public void addLogListener(BusiLogListener logListener) {
		if (!this.listeners.contains(logListener)) {
			this.listeners.add(logListener);
		}
	}

	@Override
	public void removeLogListener(BusiLogListener logListener) {
		this.listeners.remove(logListener);
	}

	@Override
	public final void businessLog(String userName, Operation operation) throws JDependException {
		String id = this.doBusinessLog(userName, operation);

		this.onWrite(id, userName, operation);
	}

	protected abstract String doBusinessLog(String userName, Operation operation) throws JDependException;

	private void onWrite(String id, String userName, Operation operation) {
		for (BusiLogListener listener : listeners) {
			if (listener != null) {
				listener.onBusiLog(id, userName, operation);
			}
		}
	}

}
