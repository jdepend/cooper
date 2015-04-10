package jdepend.core.framework.serviceproxy;

/**
 * 后台服务代理工厂
 * 
 * @author <b>Abner</b>
 * 
 */
public interface JDependServiceProxyFactory {

	/**
	 * 得到后台服务
	 * 
	 * @param groupName
	 * @param commandName
	 * @return
	 */
	public JDependServiceProxy createJDependServiceProxy(String groupName, String commandName);
}
