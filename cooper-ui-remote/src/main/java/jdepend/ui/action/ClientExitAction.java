package jdepend.ui.action;

import java.io.IOException;
import java.rmi.RemoteException;

import jdepend.core.remote.score.ScoreUpload;
import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.core.remote.userproxy.UserActionGather;
import jdepend.framework.exception.JDependException;
import jdepend.ui.JDependCooper;

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
