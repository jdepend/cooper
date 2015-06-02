package jdepend.util.analyzer.framework;

import jdepend.framework.exception.JDependException;

public class AnalyzerException extends JDependException {

	public AnalyzerException() {
		super();
	}

	public AnalyzerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public AnalyzerException(String arg0) {
		super(arg0);
	}

	public AnalyzerException(Throwable arg0) {
		super(arg0);
	}

}
