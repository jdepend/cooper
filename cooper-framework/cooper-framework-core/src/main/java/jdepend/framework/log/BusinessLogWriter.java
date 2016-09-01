package jdepend.framework.log;

import jdepend.framework.exception.JDependException;

public interface BusinessLogWriter {

	static final String BUSINESSLOG = "BUSINESSLOG";

	void businessLog(String userName, Operation operation) throws JDependException;

	void addLogListener(BusiLogListener logListener);

	void removeLogListener(BusiLogListener logListener);

}
