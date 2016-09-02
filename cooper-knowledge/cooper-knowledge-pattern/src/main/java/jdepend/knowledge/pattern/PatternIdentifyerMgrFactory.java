package jdepend.knowledge.pattern;

import jdepend.knowledge.pattern.impl1.PatternIdentifyerMgrImpl;

public class PatternIdentifyerMgrFactory {

	public PatternIdentifyerMgr create() {
//		return new IdentifyerMgr();
		return new PatternIdentifyerMgrImpl();
	}

}
