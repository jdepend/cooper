package jdepend.server.service;

import java.rmi.RemoteException;

import jdepend.server.service.analyzer.AnalyzerService;
import jdepend.server.service.analyzer.AnalyzerServiceImpl;
import jdepend.server.service.impl.JDependRemoteServiceImpl;
import jdepend.server.service.impl.JDependSessionServiceImpl;
import jdepend.server.service.score.ScoreRemoteService;
import jdepend.server.service.score.ScoreRemoteServiceImpl;
import jdepend.server.service.session.JDependSessionService;
import jdepend.server.service.user.UserAppService;
import jdepend.server.service.user.UserRemoteServiceImpl;

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

	public static UserAppService createUserAppService() throws RemoteException {
		return new UserRemoteServiceImpl();
	}

}
