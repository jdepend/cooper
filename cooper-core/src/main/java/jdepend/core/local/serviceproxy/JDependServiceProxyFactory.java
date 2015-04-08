package jdepend.core.local.serviceproxy;

import jdepend.core.framework.serverconf.ServerConfigurator;
//import jdepend.core.remote.serviceproxy.JDependServiceRemoteProxy;
import jdepend.framework.context.JDependContext;

/**
 * 后台服务代理工厂
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependServiceProxyFactory {

	/**
	 * 得到后台服务
	 * 
	 * 一个<Code>Command</Code>一个<Code>JDependServiceProxy</Code>实例
	 * 
	 * @param commandName
	 * @return
	 */
	public JDependServiceProxy getJDependServiceProxy(String groupName, String commandName) {
//		if (isLocalService() || groupName == null || commandName == null)
			return new JDependServiceLocalProxy(groupName, commandName);
//		else {
//			return new JDependServiceRemoteProxy(groupName, commandName);
//		}
	}

	public JDependServiceProxy getJDependServiceProxy() {
		return getJDependServiceProxy(null, null);
	}

//	private boolean isLocalService() {
//		if (JDependContext.isLocalService() != null) {
//			return JDependContext.isLocalService();
//		} else {
//			return (new ServerConfigurator()).isLocalService();
//		}
//	}
}
