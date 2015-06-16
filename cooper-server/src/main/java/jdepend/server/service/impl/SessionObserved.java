package jdepend.server.service.impl;

/**
 * Session接口
 * 
 * @author <b>Abner</b>
 * 
 */
public interface SessionObserved {

	public void addListener(SessionListener listener);

	public void removeListener(SessionListener listener);

}
