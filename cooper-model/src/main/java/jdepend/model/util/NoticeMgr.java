package jdepend.model.util;

import java.util.ArrayList;
import java.util.List;

public final class NoticeMgr {

	private static NoticeMgr mgr = new NoticeMgr();

	protected List<String> notices = new ArrayList<String>();

	public static NoticeMgr getInstance() {
		return mgr;
	}

	public static void setInstance(NoticeMgr extend) {
		mgr = extend;
	}

	public void record(String notice) {
		this.notices.add(notice);
	}

	public List<String> getNotices() {
		return notices;
	}
}
