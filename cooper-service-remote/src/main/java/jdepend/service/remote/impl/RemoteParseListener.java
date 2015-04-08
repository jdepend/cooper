package jdepend.service.remote.impl;

import jdepend.model.JavaClass;
import jdepend.parse.ParseListener;
import jdepend.service.remote.JDependSession;

public class RemoteParseListener implements ParseListener {

	private JDependSession session;

	public RemoteParseListener(JDependSession session) {
		super();
		this.session = session;
		this.session.clearAnalyzeSchedule();
	}

	@Override
	public void onParsedJavaClass(JavaClass parsedClass, int process) {
		this.session.appendAnalyzeSchedule(process);
	}

}
