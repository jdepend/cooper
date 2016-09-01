package jdepend.client.core.remote.serviceproxy;

import jdepend.core.serviceproxy.framework.JDependServiceProxy;
import jdepend.core.serviceproxy.framework.JDependServiceProxyFactory;

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
