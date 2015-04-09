package jdepend.core.framework.serviceproxy;

/**
 * 后台服务代理工厂管理器
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependServiceProxyFactoryMgr {

	private static JDependServiceProxyFactoryMgr mgr = new JDependServiceProxyFactoryMgr();

	private JDependServiceProxyFactory factory;

	private JDependServiceProxyFactoryMgr() {

	}

	public static JDependServiceProxyFactoryMgr getInstance() {
		return mgr;
	}

	public JDependServiceProxyFactory getFactory() {
		return factory;
	}

	public void setFactory(JDependServiceProxyFactory factory) {
		this.factory = factory;
	}

}
