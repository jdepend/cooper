package jdepend.service.remote;

import java.rmi.RemoteException;

import jdepend.service.remote.analyzer.AnalyzerService;
import jdepend.service.remote.analyzer.AnalyzerServiceImpl;
import jdepend.service.remote.impl.JDependRemoteServiceImpl;
import jdepend.service.remote.impl.JDependSessionServiceImpl;
import jdepend.service.remote.score.ScoreRemoteService;
import jdepend.service.remote.score.ScoreRemoteServiceImpl;
import jdepend.service.remote.user.UserRemoteService;
import jdepend.service.remote.user.UserRemoteServiceImpl;

public class RemoteServiceFactory {

	public static AnalyzerService createAnalyzerService() throws RemoteException {
		return new AnalyzerServiceImpl();
	}

	public static JDependRemoteService createJDependRemoteService() throws RemoteException {
		return new JDependRemoteServiceImpl();
	}

	public static JDependSessionService createJDependSessionService() throws RemoteException {
		return new JDependSessionServiceImpl();
	}

	public static ScoreRemoteService createScoreRemoteService() throws RemoteException {
		return new ScoreRemoteServiceImpl();
	}

	public static UserRemoteService createUserRemoteService() throws RemoteException {
		return new UserRemoteServiceImpl();
	}

}
