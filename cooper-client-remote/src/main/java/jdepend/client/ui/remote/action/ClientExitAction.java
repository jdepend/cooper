package jdepend.client.ui.remote.action;

import java.io.IOException;
import java.rmi.RemoteException;

import jdepend.client.core.remote.score.ScoreUpload;
import jdepend.client.core.remote.session.RemoteSessionProxy;
import jdepend.client.core.remote.userproxy.UserActionGather;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.action.ExitAction;
import jdepend.framework.exception.JDependException;

public class ClientExitAction extends ExitAction {

	public ClientExitAction(JDependCooper frame) {
		super(frame);
	}

	@Override
	protected void exit() {
		super.exit();
		// 保存用户行为收集器
		try {
			UserActionGather.getInstance().save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 保存分数收集器
		try {
			ScoreUpload.getInstance().save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// 注销（从服务端登出）
		try {
			if (RemoteSessionProxy.getInstance().isValid()) {
				RemoteSessionProxy.getInstance().logout();
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (JDependException e1) {
			e1.printStackTrace();
		}

	}

}
