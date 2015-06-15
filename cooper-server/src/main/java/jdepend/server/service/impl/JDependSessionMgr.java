package jdepend.server.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.server.service.JDependRequest;
import jdepend.server.service.JDependSession;
import jdepend.server.service.SessionListener;
import jdepend.server.service.SessionObserved;

/**
 * 服务端Session管理器
 * 
 * @author wangdg
 * 
 */
public final class JDependSessionMgr implements SessionObserved {

	private static JDependSessionMgr mgr;

	private Map<Long, JDependSession> sessions = new HashMap<Long, JDependSession>();

	private List<SessionListener> listeners = new ArrayList<SessionListener>();

	private int invalidTime = 1800000;// Session失效时间30分钟

	private long WATCH_TIME = 10000;// 监控间隔时间

	private boolean runningDomeanThread = true;

	private JDependSessionMgr() {
		this.startDomeanThread();
	}

	public static JDependSessionMgr getInstance() {
		if (mgr == null) {
			mgr = new JDependSessionMgr();
		}
		return mgr;
	}

	public synchronized void putSession(JDependSession session) {
		if (!this.sessions.containsValue(session)) {
			this.sessions.put(session.getId(), session);
			this.onCreateSession(session);
		}
	}

	public synchronized void removeSession(Long sessionId) {
		if (this.sessions.containsKey(sessionId)) {
			JDependSession session = this.sessions.get(sessionId);
			this.sessions.remove(sessionId);
			this.onRemoveSession(session);
		}
	}

	public JDependSession getSession(JDependRequest request) throws JDependException {
		return this.getSession(request.getSessionId());
	}

	public synchronized JDependSession getSession(Long sessionId) throws JDependException {
		if (!sessions.containsKey(sessionId)) {
			throw new JDependException("Session[" + sessionId + "]失效。");
		}
		return sessions.get(sessionId);
	}

	public synchronized boolean isValid(Long sessionId) {
		for (JDependSession session : this.sessions.values()) {
			if (session.getId() == sessionId) {
				return true;
			}
		}
		return false;
	}

	public synchronized Collection<JDependSession> getSessions() {
		return this.sessions.values();
	}

	protected void onCreateSession(JDependSession session) {
		for (SessionListener listenter : listeners) {
			listenter.onCreateSession(session);
		}
	}

	protected void onRemoveSession(JDependSession session) {
		for (SessionListener listenter : listeners) {
			listenter.onRemoveSession(session);
		}
	}

	protected void onChangeSession(JDependSession session) {
		for (SessionListener listenter : listeners) {
			listenter.onChangeState(session);
		}
	}

	@Override
	public synchronized void addListener(SessionListener listener) {
		if (!this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	@Override
	public synchronized void removeListener(SessionListener listener) {
		if (this.listeners.contains(listener)) {
			this.listeners.remove(listener);
		}

	}

	private void startDomeanThread() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (runningDomeanThread) {

					Iterator<JDependSession> it = sessions.values().iterator();
					while (it.hasNext()) {
						JDependSession session = it.next();
						if (System.currentTimeMillis() - session.getCreateTime().getTime() >= invalidTime) {
							it.remove();
							onRemoveSession(session);
						}
					}
					try {
						Thread.sleep(WATCH_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}).start();

	}

	public void stopDomeanThread() {
		this.runningDomeanThread = false;
	}
}
