package jdepend.core.remote.serviceproxy;

import jdepend.core.framework.serviceproxy.JDependServiceProxy;
import jdepend.core.framework.serviceproxy.JDependServiceProxyFactory;

/**
 * 后台服务代理工厂
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependServiceRemoteProxyFactory implements JDependServiceProxyFactory {

	public JDependServiceProxy getJDependServiceProxy(String groupName, String commandName) {
		return new JDependServiceRemoteProxy(groupName, commandName);
	}

	public JDependServiceProxy getJDependServiceProxy() {
		return getJDependServiceProxy(null, null);
	}
}
