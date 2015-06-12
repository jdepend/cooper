package jdepend.client.core.remote.serviceproxy;

import jdepend.core.framework.serviceproxy.JDependServiceProxy;
import jdepend.core.framework.serviceproxy.JDependServiceProxyFactory;

/**
 * 后台服务代理工厂
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependServiceRemoteProxyFactory implements JDependServiceProxyFactory {

	public JDependServiceProxy createJDependServiceProxy(String groupName, String commandName) {
		return new JDependServiceRemoteProxy(groupName, commandName);
	}
}
