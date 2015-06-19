package jdepend.server.service.user;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.Operation;

public final class UserRemoteServiceImpl extends UnicastRemoteObject implements UserRemoteService {

	private UserDomainService domainService = new UserDomainService();

	public UserRemoteServiceImpl() throws RemoteException {
		super();
	}

	public void setUserStateChangeListener(UserStateChangeListener listener){
		this.domainService.setListener(listener);
	}

	@Override
	public void processCredits(String userName, Operation operation) throws RemoteException {
		try {
			domainService.processCredits(UserRepository.findTheUser(userName), operation);
		} catch (JDependException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public void uploadUserAction(List<UserActionItem> items) throws RemoteException {
		try {
			domainService.saveUserAction(items);
		} catch (JDependException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}

	}
}
