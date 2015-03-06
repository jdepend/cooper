package jdepend.core.remote.userproxy;

import java.rmi.RemoteException;

import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogListener;
import jdepend.framework.log.Operation;

/**
 * 客户端用户积分代理
 * 
 * @author wangdg
 * 
 */
public final class UserCredits implements BusiLogListener {

	@Override
	public void onBusiLog(String id, String userName, Operation operation) {
		try {
			RemoteSessionProxy proxy = RemoteSessionProxy.getInstance();
			if (proxy.isValid() && proxy.isNormalUser()) {
				UserRemoteServiceProxy.getInstance().getUserRemoteService().processCredits(userName, operation);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (JDependException e) {
			e.printStackTrace();
		}
	}
}
