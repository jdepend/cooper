package jdepend.service;

import java.rmi.RemoteException;
import java.util.Collection;

import jdepend.model.JavaClass;
import jdepend.model.util.ClassSearchUtil;
import jdepend.parse.util.SearchUtil;
import jdepend.service.local.JDependLocalService;
import jdepend.service.local.impl.JDependLocalServiceImpl;
import jdepend.service.remote.JDependRemoteService;
import jdepend.service.remote.JDependSessionService;
import jdepend.service.remote.analyzer.AnalyzerService;
import jdepend.service.remote.analyzer.AnalyzerServiceImpl;
import jdepend.service.remote.impl.JDependRemoteServiceImpl;
import jdepend.service.remote.impl.JDependSessionServiceImpl;
import jdepend.service.remote.score.ScoreRemoteService;
import jdepend.service.remote.score.ScoreRemoteServiceImpl;
import jdepend.service.remote.user.UserRemoteService;
import jdepend.service.remote.user.UserRemoteServiceImpl;

public class ServiceFactory {

	public static void init() {

//		initClassList();
	}

	public static int initClassList() {
		SearchUtil search = new SearchUtil();
		for (String path : ClassSearchUtil.getSelfPath()) {
			search.addPath(path);
		}
		Collection<JavaClass> javaClasses = search.getClasses();
		ClassSearchUtil.getInstance().setClassList(javaClasses);

		return javaClasses.size();
	}

	public static JDependLocalService createJDependLocalService() {
		return createJDependLocalService(null, null);
	}

	public static JDependLocalService createJDependLocalService(String groupName, String commandName) {
		return new JDependLocalServiceImpl(groupName, commandName);
	}

	public static AnalyzerServiceImpl createAnalyzerService() throws RemoteException {
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
