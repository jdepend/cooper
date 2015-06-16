package jdepend.client.ui.remote;

import jdepend.client.core.command.CommandAdapterMgr;
import jdepend.client.core.config.CommandConfMgr;
import jdepend.client.core.remote.score.ScoreUpload;
import jdepend.client.core.remote.serviceproxy.JDependServiceRemoteProxyFactory;
import jdepend.client.core.remote.userproxy.UserActionGather;
import jdepend.client.core.remote.userproxy.UserCredits;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.action.ServiceSettingAction;
import jdepend.client.ui.framework.PanelMgr;
import jdepend.client.ui.remote.action.ClientExitAction;
import jdepend.client.ui.remote.action.LoginAction;
import jdepend.client.ui.remote.action.LogoutAction;
import jdepend.client.ui.remote.analyzer.ClientAnalyzerPanel;
import jdepend.client.ui.start.WorkspaceSetting;
import jdepend.client.ui.start.WorkspaceSettingDialog;
import jdepend.core.serviceproxy.framework.JDependServiceProxyFactoryMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.DBBusinessLogWriter;
import jdepend.framework.ui.dialog.WelcomeDialog;
import jdepend.framework.ui.panel.StatusField;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.JDependUtil;

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

		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Exit), new ClientExitAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ServiceParamSetting), new ServiceSettingAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Login), new LoginAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Logout), new LogoutAction(this));
	}

	@Override
	protected void init(String[] args) {
		// 设置业务日志Writer
		BusiLogUtil.getInstance().setBusiWriter(new DBBusinessLogWriter());
		// 设置ServiceProxyFactory
		JDependServiceProxyFactoryMgr.getInstance().setFactory(new JDependServiceRemoteProxyFactory());
		// 向命令组配置组件增加监听器
		try {
			CommandConfMgr.getInstance().addGroupListener(CommandAdapterMgr.getInstance());
		} catch (JDependException e) {
			e.printStackTrace();
			this.showStatusError(e.getMessage());
		}
		// 设置AnalyzerPanel
		PanelMgr.getInstance().setAnalyzerPanel(new ClientAnalyzerPanel(this));
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
		this.getStatusField().setText(LoginDialog.Logout, StatusField.Right);
	}

	/**
	 * 
	 * @param args
	 *            -startUploadScore（true：启动自动上传分数服务；false：不启动）
	 */
	public static void main(String[] args) {

		System.setProperty("sun.zip.encoding", "default");

		ClientCooper frame = new ClientCooper(BundleUtil.getString(BundleUtil.ClientWin_Title));

		WelcomeDialog welcomeDialog = new WelcomeDialog();
		welcomeDialog.setVisible(true);

		WorkspaceSetting setting = new WorkspaceSetting();
		if (!setting.Inited()) {
			WorkspaceSettingDialog d = new WorkspaceSettingDialog(frame, welcomeDialog, setting, args);
			d.setModal(true);
			d.setVisible(true);
		} else {
			frame.start(args, setting);
			welcomeDialog.dispose();
		}
	}
}
