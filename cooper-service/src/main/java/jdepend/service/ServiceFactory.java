package jdepend.service;

import jdepend.service.impl.JDependLocalServiceImpl;

public class ServiceFactory {

	public static JDependLocalService createJDependLocalService() {
		return createJDependLocalService(null, null);
	}

	public static JDependLocalService createJDependLocalService(String groupName, String commandName) {
		return new JDependLocalServiceImpl(groupName, commandName);
	}

}
