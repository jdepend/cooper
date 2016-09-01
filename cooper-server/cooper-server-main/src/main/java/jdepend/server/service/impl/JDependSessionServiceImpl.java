package jdepend.server.service.impl;

import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

import jdepend.framework.exception.JDependException;
import jdepend.server.service.session.JDependSession;
import jdepend.server.service.session.JDependSessionService;
import jdepend.server.service.user.UserRepository;
import jdepend.server.service.user.User;

/**
 * Session服务
 * 
 * @author wangdg
 * 
 */
public final class JDependSessionServiceImpl extends UnicastRemoteObject implements JDependSessionService {

	public JDependSessionServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public long createSession(String name, String password) throws java.rmi.RemoteException {

		try {
			validate(name, password);
		} catch (JDependException e1) {
			e1.printStackTrace();
			throw new RemoteException(e1.getMessage(), e1);
		}

		JDependSession session = new JDependSession();
		session.setId(UUID.randomUUID().getLeastSignificantBits());
		session.setUserName(name);

		try {
			session.setClient(getClientHost());
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		JDependSessionMgr.getInstance().putSession(session);

		return session.getId();
	}

	@Override
	public void removeSession(Long sessionId) throws RemoteException {
		JDependSessionMgr.getInstance().removeSession(sessionId);
	}

	private User validate(String name, String password) throws JDependException {
		if (name == null || name.length() == 0) {
			throw new JDependException("用户名为空");
		}
		return UserRepository.findTheUser(name, password);
	}

	@Override
	public boolean isValid(Long sessionId) throws RemoteException {
		return JDependSessionMgr.getInstance().isValid(sessionId);
	}
}
