package jdepend.parse;

import jdepend.framework.exception.JDependException;

public class ParseException extends JDependException {

	public ParseException() {
		super();
	}

	public ParseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ParseException(String arg0) {
		super(arg0);
	}

	public ParseException(Throwable arg0) {
		super(arg0);
	}

}
