package jdepend.client.core.serviceproxy;

import jdepend.core.serviceproxy.framework.JDependServiceProxy;
import jdepend.core.serviceproxy.framework.JDependServiceProxyFactory;

/**
 * 后台服务代理工厂
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependServiceLocalProxyFactory implements JDependServiceProxyFactory {

	public JDependServiceProxy createJDependServiceProxy(String groupName, String commandName) {
		return new JDependServiceLocalProxy(groupName, commandName);
	}
}
