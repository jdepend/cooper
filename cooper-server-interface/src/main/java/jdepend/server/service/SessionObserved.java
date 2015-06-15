package jdepend.server.service;

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
