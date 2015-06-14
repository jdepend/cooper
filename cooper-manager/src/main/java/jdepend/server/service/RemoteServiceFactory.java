package jdepend.server.service;

import java.rmi.RemoteException;

import jdepend.server.service.analyzer.AnalyzerServiceImpl;
import jdepend.server.service.impl.JDependRemoteServiceImpl;
import jdepend.server.service.impl.JDependSessionServiceImpl;
import jdepend.server.service.score.ScoreRemoteServiceImpl;
import jdepend.server.service.user.UserRemoteServiceImpl;
import jdepend.service.remote.JDependRemoteService;
import jdepend.service.remote.JDependSessionService;
import jdepend.service.remote.analyzer.AnalyzerService;
import jdepend.service.remote.score.ScoreRemoteService;
import jdepend.service.remote.user.UserRemoteService;

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
