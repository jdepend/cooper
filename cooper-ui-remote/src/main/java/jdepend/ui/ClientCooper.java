package jdepend.ui;

import jdepend.core.framework.serviceproxy.JDependServiceProxyFactoryMgr;
import jdepend.core.local.serviceproxy.JDependServiceLocalProxyFactory;
import jdepend.core.remote.score.ScoreUpload;
import jdepend.core.remote.serviceproxy.JDependServiceRemoteProxyFactory;
import jdepend.core.remote.userproxy.UserActionGather;
import jdepend.core.remote.userproxy.UserCredits;
import jdepend.framework.context.JDependContext;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.StatusField;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.JDependUtil;
import jdepend.ui.action.LoginAction;
import jdepend.ui.action.LogoutAction;
import jdepend.ui.action.ServiceSettingAction;
import jdepend.ui.start.ClientWelcomeDialog;
import jdepend.ui.start.WorkspaceSetting;
import jdepend.ui.start.WorkspaceSettingDialog;

public class ClientCooper extends JDependCooper {

	public ClientCooper(String name) {
		super(name);

		resourceStrings.put(
				"menubar",
				BundleUtil.getString(BundleUtil.ClientWin_Menu_File) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Setting) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Service) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Data) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Help));

		resourceStrings.put(
				BundleUtil.getString(BundleUtil.ClientWin_Menu_Service),
				BundleUtil.getString(BundleUtil.ClientWin_Menu_ServiceParamSetting) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Login) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Logout));

		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Service), "V");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ServiceParamSetting), "R");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Login), "L");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Logout), "O");

		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ServiceParamSetting), new ServiceSettingAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Login), new LoginAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Logout), new LogoutAction(this));
	}

	@Override
	protected void init(String[] args) {
		super.init(args);

		// 设置ServiceProxyFactory
		JDependServiceProxyFactoryMgr.getInstance().setFactory(new JDependServiceRemoteProxyFactory());
		// 向日志组件注册用户积分监听器
		BusiLogUtil.getInstance().addLogListener(new UserCredits());
		// 向日志组件注册用户行为收集监听器
		BusiLogUtil.getInstance().addLogListener(UserActionGather.getInstance());
		// 启动分数收集器
		String startUploadScore = JDependUtil.getArg(args, "-startUploadScore");
		if (Boolean.parseBoolean(startUploadScore)) {
			ScoreUpload.getInstance().start();
		}

		// 设置状态条信息
		this.getStatusField().setText(JDependContext.Remote, StatusField.Center);
		this.getStatusField().setText(LoginDialog.Logout, StatusField.Right);
	}

	/**
	 * 
	 * @param args
	 *            -startUploadScore（true：启动自动上传分数服务；false：不启动）
	 */
	public static void main(String[] args) {

		System.setProperty("sun.zip.encoding", "default");

		ClientWelcomeDialog welcomeDialog = new ClientWelcomeDialog();
		welcomeDialog.setVisible(true);

		WorkspaceSetting setting = new WorkspaceSetting();
		if (!setting.Inited()) {
			WorkspaceSettingDialog d = new WorkspaceSettingDialog(welcomeDialog, setting, args);
			d.setModal(true);
			d.setVisible(true);
		} else {
			initEnv(args, setting);
			start(setting, welcomeDialog, args);
			welcomeDialog.dispose();
		}
	}

	public static void start(WorkspaceSetting setting, ClientWelcomeDialog welcomeDialog, String[] args) {
		// 初始化ClassList
		// initClassList(welcomeDialog);
		// 启动主窗口
		ClientCooper frame = new ClientCooper(BundleUtil.getString(BundleUtil.ClientWin_Title));
		// 显示
		frame.display();
		// 初始化
		frame.init(args);
		// 记录日志
		BusiLogUtil.getInstance().businessLog(Operation.startCooper);
	}

}
