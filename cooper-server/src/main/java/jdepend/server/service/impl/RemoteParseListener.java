package jdepend.server.service.impl;

import jdepend.metadata.JavaClass;
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
