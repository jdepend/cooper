package jdepend.core.remote.session;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import jdepend.core.framework.serverconf.ServerConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.service.remote.JDependSessionService;

/**
 * 客户端Session代理
 * 
 * @author wangdg
 * 
 */
public final class RemoteSessionProxy {

	private static RemoteSessionProxy proxy = new RemoteSessionProxy();

	private JDependSessionService sessionService;

	private Long sessionId;

	private String userName = AnonymousUser;

	private static final String AnonymousUser = "AnonymousUser";

	private static final String DEFAULT_SESSION_SERVICE = "rmi://localhost/JDependSessionService";

	private RemoteSessionProxy() {
	}

	public static RemoteSessionProxy getInstance() {
		return proxy;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public Long loginAnonymousUser() throws RemoteException, JDependException {
		if (!this.isValid()) {
			sessionId = this.getJDependSessionService().createSession(AnonymousUser, null);
			LogUtil.getInstance(RemoteSessionProxy.class).systemLog("以匿名用户建立了远程链接，并初始化了本地在服务器端的配置。");
		}
		return sessionId;
	}

	public Long login(String username, String password) throws RemoteException, JDependException {
		sessionId = this.getJDependSessionService().createSession(username, password);
		this.userName = username;
		return sessionId;
	}

	public void logout() throws RemoteException, JDependException {
		this.getJDependSessionService().removeSession(sessionId);
		sessionId = null;
		this.userName = AnonymousUser;
	}

	/**
	 * 判断session是否有效
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws JDependException
	 */
	public boolean isValid() throws RemoteException, JDependException {
		return sessionId != null && this.getJDependSessionService().isValid(sessionId);
	}

	public boolean isNormalUser() {
		return !AnonymousUser.equals(this.userName);
	}

	private JDependSessionService getJDependSessionService() throws JDependException {
		if (this.sessionService == null) {
			try {
				sessionService = (JDependSessionService) Naming.lookup(getSessionServiceURL());
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
		return this.sessionService;
	}

	private static String getSessionServiceURL() {

		String sessionServiceURL = (new ServerConfigurator()).getSessionServiceURL();
		if (sessionServiceURL == null) {
			sessionServiceURL = DEFAULT_SESSION_SERVICE;
		}
		return sessionServiceURL;
	}
}
