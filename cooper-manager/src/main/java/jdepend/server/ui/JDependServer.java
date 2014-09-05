package jdepend.server.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.StatusPanel;
import jdepend.framework.util.JDependUtil;
import jdepend.knowledge.ui.KnowledgePanel;
import jdepend.service.ServiceFactory;
import jdepend.service.persistent.ServerConnectionProvider;

/**
 * 服务器端主窗口
 * 
 * @author wangdg
 * 
 */
public class JDependServer extends JDependFrame {

	private Registry registry;

	private SessionMgrPanel sessionMgr;

	private ServiceMonitorPanel serviceMoitor;

	private AnalyzerMgrPanel analyzerMgrPanel;

	private UserMgrPanel userMgrPanel;

	private ScoreMgrPanel scoreMgrPanel;

	public JDependServer(String name) {
		super(name);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				try {
					serviceMoitor.stop();
					sessionMgr.unbindService();
					userMgrPanel.unbindService();
					unbindScoreRemoteService();
					stopServer();
				} catch (JDependException e1) {
					e1.printStackTrace();
				} finally {
					dispose();
					System.exit(0);
				}
			}
		});
	}

	protected void doDisplay() {

		this.getContentPane().setLayout(new BorderLayout());

		JTabbedPane tab = new JTabbedPane();

		sessionMgr = new SessionMgrPanel(this);

		tab.add("会话管理", sessionMgr);

		serviceMoitor = new ServiceMonitorPanel(this);

		tab.add("解析服务", serviceMoitor);

		analyzerMgrPanel = new AnalyzerMgrPanel(this);

		tab.add("分析器", analyzerMgrPanel);

		tab.add("智慧专家", new KnowledgePanel(this));

		userMgrPanel = new UserMgrPanel(this);

		tab.add("用户管理", userMgrPanel);

		scoreMgrPanel = new ScoreMgrPanel(this);

		tab.add("分数管理", scoreMgrPanel);

		this.getContentPane().add(BorderLayout.CENTER, tab);

		StatusPanel statusPanel = getStatusPanel();
		this.getContentPane().add(BorderLayout.SOUTH, statusPanel);
	}

	private void startSessionService() throws JDependException {
		this.sessionMgr.bindService();
	}

	private void startRemoteService() throws JDependException {
		this.serviceMoitor.start();
	}

	private void startAnalyzerService() throws JDependException {
		this.analyzerMgrPanel.bindService();
	}

	private void startUserRemoteService() throws JDependException {
		this.userMgrPanel.bindService();
	}

	private void startScoreRemoteService() throws JDependException {
		scoreMgrPanel.bindService();
	}

	private void startServer() throws JDependException {
		try {
			registry = LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			throw new JDependException("服务已经启动，请终止上次服务，再启动本次服务！", e);
		}
	}

	private void unbindScoreRemoteService() throws JDependException {
		scoreMgrPanel.unbindService();
	}

	private void stopServer() throws JDependException {
		try {
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (NoSuchObjectException e) {
			throw new JDependException("停止服务错误！", e);
		}

	}

	public static void main(String args[]) throws JDependException {

		ServerWelcomeDialog welcomeDialog = new ServerWelcomeDialog();
		welcomeDialog.setVisible(true);

		initEnv(args);

		welcomeDialog.dispose();

		JDependServer server = new JDependServer("Server");
		server.display();
		try {
			server.startServer();
			server.startSessionService();
			server.startAnalyzerService();
			server.startUserRemoteService();
			server.startScoreRemoteService();
			// 启动解析服务
			String startService = JDependUtil.getArg(args, "-startService");
			if (Boolean.parseBoolean(startService)) {
				server.startRemoteService();
			}
		} catch (JDependException e) {
			e.printStackTrace();
			server.showStatusError(e.getMessage());
		}
	}

	public static void initEnv(String[] args) {
		// 设置运行环境
		JDependContext.setRunEnv(JDependContext.Server);
		// 设置workspacePath
		String workspacePath = JDependUtil.getArg(args, "-workspacePath");
		if (workspacePath != null) {
			JDependContext.setWorkspacePath(args[2]);
		} else {
			JDependContext.setWorkspacePath(System.getProperty("user.dir"));
		}
		// 设置RunningPath
		JDependContext.setRunningPath(System.getProperty("user.dir"));
		// 设置ConnectionProvider
		ConnectionFactory.setProvider(new ServerConnectionProvider());
		// 设置日志是否打印
		PropertyConfigurator conf = new PropertyConfigurator();
		BusiLogUtil.BUSINESSLOG = conf.isPrintBusiLog();
		LogUtil.SYSTEMLOG = conf.isPrintSystemLog();
		LogUtil.SYSTEMWARNING = conf.isPrintSystemWarning();

		ServiceFactory.initClassList();
	}

	@Override
	public void refresh() throws JDependException {
	}

	@Override
	public void show(Map<String, JComponent> result) {
		// TODO Auto-generated method stub

	}
}
