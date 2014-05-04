package jdepend.framework.context;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jdepend.framework.context.Scope.SCOPE;


/**
 * 客户端上下文
 * 
 * @author wangdg
 * 
 */
public final class ClientContext {

	private final static String CurrentUser = "CurrentUser";

	public static String getUser() {
		String user = (String) JDependContext.getInfo(SCOPE.APP_SCOPSE, CurrentUser);
		if (user == null) {
			try {
				user = InetAddress.getLocalHost().getHostName();
				JDependContext.setInfo(SCOPE.APP_SCOPSE, CurrentUser, user);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	public static void setUser(String user) {
		JDependContext.setInfo(SCOPE.APP_SCOPSE, CurrentUser, user);
	}

}
