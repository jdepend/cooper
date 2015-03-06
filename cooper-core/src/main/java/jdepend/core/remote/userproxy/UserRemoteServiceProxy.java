package jdepend.core.remote.userproxy;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import jdepend.core.framework.serverconf.ServerConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.service.remote.user.UserRemoteService;

public class UserRemoteServiceProxy {

	private UserRemoteService userRemoteService;

	public static String DEFAULT_USER_SERVICE = "rmi://localhost/UserRemoteService";

	private static UserRemoteServiceProxy proxy = new UserRemoteServiceProxy();

	private UserRemoteServiceProxy() {

	}

	public static UserRemoteServiceProxy getInstance() {
		return proxy;
	}

	public UserRemoteService getUserRemoteService() throws JDependException {
		if (this.userRemoteService == null) {
			try {
				userRemoteService = (UserRemoteService) Naming.lookup(getUserRemoteServiceURL());
			} catch (ConnectException e) {
				e.printStackTrace();
				throw new JDependException("连接服务器失败！", e);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new JDependException("URL地址错误！", e);
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new JDependException("远程服务失败！", e);
			} catch (NotBoundException e) {
				e.printStackTrace();
				throw new JDependException("没有查询到绑定服务！", e);
			}
		}
		return this.userRemoteService;
	}

	private String getUserRemoteServiceURL() {
		String userRemoteServiceURL = (new ServerConfigurator()).getUserRemoteServiceURL();
		if (userRemoteServiceURL == null) {
			userRemoteServiceURL = DEFAULT_USER_SERVICE;
		}
		return userRemoteServiceURL;
	}
}
