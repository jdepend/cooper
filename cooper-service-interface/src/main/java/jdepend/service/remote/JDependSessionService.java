package jdepend.service.remote;

import java.rmi.Remote;

/**
 * Session服务
 * 
 * @author wangdg
 * 
 */
public interface JDependSessionService extends Remote {

	public long createSession(String name, String password) throws java.rmi.RemoteException;

	public void removeSession(Long sessionId) throws java.rmi.RemoteException;

	public boolean isValid(Long sessionId) throws java.rmi.RemoteException;
}
