package jdepend.service.local;

import jdepend.service.local.JDependLocalService;
import jdepend.service.local.impl.JDependLocalServiceImpl;

public class ServiceFactory {

	public static JDependLocalService createJDependLocalService() {
		return createJDependLocalService(null, null);
	}

	public static JDependLocalService createJDependLocalService(String groupName, String commandName) {
		return new JDependLocalServiceImpl(groupName, commandName);
	}

}
