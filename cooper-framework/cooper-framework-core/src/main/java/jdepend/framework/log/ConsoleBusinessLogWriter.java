package jdepend.framework.log;

import java.util.UUID;

import jdepend.framework.exception.JDependException;

public class ConsoleBusinessLogWriter extends AbstractBusinessLogWriter {

	@Override
	protected String doBusinessLog(String userName, Operation operation) throws JDependException {
		System.out.println("userName :" + userName + " Operation :" + operation);
		return UUID.randomUUID().toString();
	}

}
