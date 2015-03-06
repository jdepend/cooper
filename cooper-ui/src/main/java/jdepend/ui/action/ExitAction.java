package jdepend.ui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.SocketException;
import java.rmi.RemoteException;

import javax.swing.AbstractAction;

import jdepend.core.remote.analyzer.AnalyzerMgr;
import jdepend.core.remote.score.ScoreUpload;
import jdepend.core.remote.session.RemoteSessionProxy;
import jdepend.core.remote.userproxy.UserActionGather;
import jdepend.framework.exception.JDependException;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.UIPropertyConfigurator;

public class ExitAction extends AbstractAction {

	private JDependCooper frame;

	public ExitAction(JDependCooper frame) {
		super("Exit");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
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
		// 关闭CircleService
		try {
			frame.getCirclePanel().close();
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
		// 保存分析器配置
		AnalyzerMgr.getInstance().save();
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
		// 保存UI设置
		try {
			UIPropertyConfigurator.getInstance().save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		frame.dispose();
		System.exit(0);
	}
}
