package jdepend.server.service.impl;

import jdepend.server.service.session.JDependSession;

/**
 * session监听者接口
 * 
 * @author wangdg
 * 
 */
public interface SessionListener {

	public void onCreateSession(JDependSession session);

	public void onRemoveSession(JDependSession session);

	public void onChangeState(JDependSession session);

}
