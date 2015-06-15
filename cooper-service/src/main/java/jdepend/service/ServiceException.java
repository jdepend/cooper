package jdepend.service;

import jdepend.framework.exception.JDependException;

public class ServiceException extends JDependException {

	public ServiceException() {
		super();
	}

	public ServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServiceException(String arg0) {
		super(arg0);
	}

	public ServiceException(Throwable arg0) {
		super(arg0);
	}

}
