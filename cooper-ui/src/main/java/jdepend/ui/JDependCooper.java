package jdepend.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import jdepend.core.command.CommandAdapterMgr;
import jdepend.core.config.CommandConfMgr;
import jdepend.core.persistent.ClientConnectionProvider;
import jdepend.core.score.ScoreUpload;
import jdepend.core.serverconf.ServerConfigurator;
import jdepend.core.userproxy.UserActionGather;
import jdepend.core.userproxy.UserCredits;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.framework.ui.AboutAction;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.StatusField;
import jdepend.framework.ui.StatusPanel;
import jdepend.framework.ui.UIProperty;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.FileUtil;
import jdepend.framework.util.JDependUtil;
import jdepend.model.JavaClass;
import jdepend.model.util.ClassSearchUtil;
import jdepend.parse.ParseListener;
import jdepend.parse.util.SearchUtil;
import jdepend.ui.action.AddGroupWizardAction;
import jdepend.ui.action.ExitAction;
import jdepend.ui.action.ImportResultAction;
import jdepend.ui.action.IntroduceAction;
import jdepend.ui.action.LoginAction;
import jdepend.ui.action.LogoutAction;
import jdepend.ui.action.MetricsAction;
import jdepend.ui.action.ScoreAction;
import jdepend.ui.action.ScoreAndMetricsAction;
import jdepend.ui.action.ScoreIntroduceAction;
import jdepend.ui.action.ServiceSettingAction;
import jdepend.ui.action.SettingAction;
import jdepend.ui.action.SettingClassRelationMgAction;
import jdepend.ui.action.SettingWorkspaceAction;
import jdepend.ui.circle.CirclePanel;
import jdepend.ui.command.GroupIngoreListSettingDialog;
import jdepend.ui.command.GroupPanel;
import jdepend.ui.culture.CulturePanel;
import jdepend.ui.framework.UIPropertyConfigurator;
import jdepend.ui.property.PropertyPanel;
import jdepend.ui.result.framework.ReportListener;
import jdepend.ui.result.framework.ResultPanel;
import jdepend.ui.result.panel.ResultPanelWrapper;
import jdepend.ui.start.ClientWelcomeDialog;
import jdepend.ui.start.WorkspaceSetting;
import jdepend.ui.start.WorkspaceSettingDialog;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerExecutorListener;
import jdepend.util.refactor.AdjustHistory;

/**
 * 客户端主窗口
 * 
 * @author wangdg
 * 
 */
public class JDependCooper extends JDependFrame implements ParseListener, ReportListener, AnalyzerExecutorListener {

	private LeftPanel leftPanel;

	private ResultPanel resultPanel;

	private CirclePanel circlePanel;

	private PropertyPanel propertyPanel;

	private JSplitPane verticalSplitPane;

	private JSplitPane horizontalSplitPane;

	private JSplitPane topHorizontalSplitPane;

	public static final int LeftWidth = 256;

	public static final int TopHeight = 467;

	public static final int TopLeftWidth = 820;

	public static final int IntroducePopDialogWidth = 600;
	public static final int IntroducePopDialogHeight = 450;

	public static final int SplitPaneWidth = 2;

	/**
	 * Constructs a <code>JDependCooper</code> instance.
	 */
	public JDependCooper(String name) {
		super(name);

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			javax.swing.SwingUtilities.updateComponentTreeUI(this); // 更新界面
		} catch (Exception e) {
			e.printStackTrace();
		}

		ToolTipManager.sharedInstance().setDismissDelay(100000);

		UIManager.put("SplitPane.dividerSize", SplitPaneWidth);
		//
		// Install the resource string table.
		//
		resourceStrings = new HashMap<String, String>();
		resourceStrings.put(
				"menubar",
				BundleUtil.getString(BundleUtil.ClientWin_Menu_File) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Setting) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Service) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Data) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Help));
		resourceStrings.put(
				BundleUtil.getString(BundleUtil.ClientWin_Menu_File),
				BundleUtil.getString(BundleUtil.ClientWin_Menu_AddGroup) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Exit));
		resourceStrings.put(
				BundleUtil.getString(BundleUtil.ClientWin_Menu_Setting),
				BundleUtil.getString(BundleUtil.ClientWin_Menu_ParamSetting) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_ChangeWorkspace) + "/-/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_SetClassRelationMgr));
		resourceStrings.put(
				BundleUtil.getString(BundleUtil.ClientWin_Menu_Service),
				BundleUtil.getString(BundleUtil.ClientWin_Menu_ServiceParamSetting) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Login) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_Logout));
		resourceStrings.put(
				BundleUtil.getString(BundleUtil.ClientWin_Menu_Data),
				BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreList) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_ImportResult));
		resourceStrings.put(
				BundleUtil.getString(BundleUtil.ClientWin_Menu_Help),
				BundleUtil.getString(BundleUtil.ClientWin_Menu_Introduce) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_MetricsExplain) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreExplain) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreAndMetrics) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_About));

		accelerators = new HashMap<String, String>();
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_File), "F");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Setting), "S");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ChangeWorkspace), "K");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Service), "V");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Help), "H");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_AddGroup), "G");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Exit), "E");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ParamSetting), "P");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ServiceParamSetting), "R");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Login), "L");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Logout), "O");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Data), "D");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreList), "U");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ImportResult), "J");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Introduce), "I");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_MetricsExplain), "M");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreExplain), "C");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreAndMetrics), "B");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_About), "A");
		//
		// Install the action table.
		//
		actions = new HashMap<String, AbstractAction>();
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_About), new AboutAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_AddGroup), new AddGroupWizardAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Exit), new ExitAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Introduce), new IntroduceAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_MetricsExplain), new MetricsAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreExplain), new ScoreIntroduceAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreAndMetrics), new ScoreAndMetricsAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ParamSetting), new SettingAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ChangeWorkspace), new SettingWorkspaceAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_SetClassRelationMgr),
				new SettingClassRelationMgAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ServiceParamSetting), new ServiceSettingAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Login), new LoginAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Logout), new LogoutAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ScoreList), new ScoreAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ImportResult), new ImportResultAction(this));

		getContentPane().setLayout(new BorderLayout());
		setBackground(SystemColor.control);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new ExitAction((JDependCooper) e.getSource()).actionPerformed(null);
			}
		});
	}

	protected void doDisplay() {
		createUI();
		this.defaultLayout();
	}

	private void defaultLayout() {
		verticalSplitPane.setDividerLocation(TopHeight);
		horizontalSplitPane.setDividerLocation(LeftWidth);
		this.leftPanel.setDividerLocation(TopHeight);

		this.refreshLayout();
	}

	public void refreshLayout() {
		if (UIPropertyConfigurator.getInstance().isVisibleCircle()) {
			topHorizontalSplitPane.setDividerLocation(TopLeftWidth);
			circlePanel.setVisible(true);
			circlePanel.setHidden(false);
		} else {
			topHorizontalSplitPane.setDividerLocation(Integer.MAX_VALUE);
			circlePanel.setVisible(false);
			circlePanel.setHidden(true);
		}
	}

	public void hiddenCirclePanel() {
		topHorizontalSplitPane.setDividerLocation(Integer.MAX_VALUE);
		circlePanel.setHidden(true);
	}

	private void createUI() {

		JMenuBar menuBar = createMenubar();
		this.setJMenuBar(menuBar);

		StatusPanel statusPanel = getStatusPanel();
		this.getContentPane().add(BorderLayout.SOUTH, statusPanel);

		JPanel workspacePanel = createWorkspacePanel();
		this.getContentPane().add(BorderLayout.CENTER, workspacePanel);
	}

	private JPanel createWorkspacePanel() {

		JPanel panel = new JPanel(new BorderLayout());

		topHorizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, this.getResultPanel(),
				this.getCirclePanel());

		PropertyPanel propertyPanel = this.getPropertyPanel();

		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, topHorizontalSplitPane, propertyPanel);

		JPanel leftPanel = new JPanel();
		try {
			leftPanel = this.createLeftPanel();
		} catch (JDependException e) {
			e.printStackTrace();
			showStatusError(e.getMessage());
		}

		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, leftPanel, verticalSplitPane);

		panel.add(horizontalSplitPane);

		return panel;
	}

	private ResultPanel createResultPanel() {
		return new ResultPanel(this);
	}

	private PropertyPanel createPropertyPanel() {
		return new PropertyPanel(this);
	}

	private LeftPanel createLeftPanel() throws JDependException {
		if (this.leftPanel == null) {
			this.leftPanel = new LeftPanel(this);
		}
		return this.leftPanel;
	}

	public ResultPanel getResultPanel() {
		if (resultPanel == null) {
			resultPanel = createResultPanel();
		}
		return resultPanel;
	}

	public ResultPanelWrapper getResultPanelWrapper() {
		return new ResultPanelWrapper(this.getResultPanel());
	}

	public CirclePanel getCirclePanel() {
		if (circlePanel == null) {
			circlePanel = new CirclePanel(this);
		}
		return circlePanel;
	}

	public PropertyPanel getPropertyPanel() {
		if (propertyPanel == null) {
			propertyPanel = createPropertyPanel();
		}
		return propertyPanel;
	}

	private LeftPanel getLeftPanel() throws JDependException {
		if (leftPanel == null) {
			leftPanel = createLeftPanel();
		}
		return leftPanel;
	}

	public GroupPanel getGroupPanel() throws JDependException {
		return this.getLeftPanel().getGroupPanel();
	}

	public CulturePanel getCulturePanel() throws JDependException {
		return this.getLeftPanel().getCulturePanel();
	}

	@Override
	protected StatusField createStatusField() {

		final StatusField statusField = super.createStatusField();
		// 单机版运行模式可以切换单机或联机运行模式
		if (JDependContext.isStandaloneMode()) {
			final JPopupMenu popupMenu = new JPopupMenu();

			JMenuItem localItem = new JMenuItem(JDependContext.Local);
			localItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDependContext.setIsLocalService(true);
					statusField.getStatusCenter().setText(JDependContext.Local);
					try {
						synServiceConf();
					} catch (JDependException e1) {
						e1.printStackTrace();
					}
				}
			});
			popupMenu.add(localItem);

			JMenuItem remoteItem = new JMenuItem(JDependContext.Remote);
			remoteItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDependContext.setIsLocalService(false);
					statusField.getStatusCenter().setText(JDependContext.Remote);
					try {
						synServiceConf();
					} catch (JDependException e1) {
						e1.printStackTrace();
					}
				}
			});
			popupMenu.add(remoteItem);
			statusField.getStatusCenter().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
					}
				}
			});
		}
		return statusField;
	}

	private void synServiceConf() throws JDependException {
		String filePath = JDependContext.getWorkspacePath() + "/" + PropertyConfigurator.DEFAULT_PROPERTY_DIR + "/"
				+ ServerConfigurator.DEFAULT_PROPERTY_FILE;

		StringBuilder content = FileUtil.readFileContent(filePath, "UTF-8");

		int startPos = content.indexOf("isLocalService=") + 15;
		int endPos = content.indexOf("\n", startPos);
		String value = content.substring(startPos, endPos);
		boolean isNeedSave = false;
		if (JDependContext.isLocalService() && value.equalsIgnoreCase("false")) {
			content.replace(startPos, endPos, "true");
			isNeedSave = true;
		} else if (!JDependContext.isLocalService() && value.equalsIgnoreCase("true")) {
			content.replace(startPos, endPos, "false");
			isNeedSave = true;
		}
		if (isNeedSave) {
			FileUtil.saveFileContent(filePath, content, "UTF-8");
		}
	}

	public void maxWorkspace() {
		verticalSplitPane.setDividerLocation(this.getHeight());
		horizontalSplitPane.setDividerLocation(0);
		topHorizontalSplitPane.setDividerLocation(Integer.MAX_VALUE);
		circlePanel.setVisible(false);
	}

	public void resume() {
		verticalSplitPane.setDividerLocation(TopHeight);
		horizontalSplitPane.setDividerLocation(LeftWidth);
		if (circlePanel.isHidden()) {
			topHorizontalSplitPane.setDividerLocation(Integer.MAX_VALUE);
			circlePanel.setVisible(false);
		} else {
			topHorizontalSplitPane.setDividerLocation(TopLeftWidth);
			circlePanel.setVisible(true);
		}
	}

	public void maxProperty() {
		verticalSplitPane.setDividerLocation(0);
		horizontalSplitPane.setDividerLocation(0);
	}

	/**
	 * 
	 * @param args
	 *            -RunMode（0：单机版运行模式；1：客户端运行模式）
	 *            -isLocalService（true：本地运行模式；false：远程运行模式）
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
		JDependCooper frame = new JDependCooper(BundleUtil.getString(BundleUtil.ClientWin_Title));
		// 显示
		frame.display();
		// 初始化
		frame.init(args);

		// 设置状态条信息
		if (JDependContext.isLocalService()) {
			frame.getStatusField().setText(JDependContext.Local, StatusField.Center);
		} else {
			frame.getStatusField().setText(JDependContext.Remote, StatusField.Center);
		}
		frame.getStatusField().setText(LoginDialog.Logout, StatusField.Right);

		// 记录日志
		BusiLogUtil.getInstance().businessLog(Operation.startCooper);
	}

	public static void initEnv(String[] args, WorkspaceSetting setting) {
		// 设置运行环境
		JDependContext.setRunEnv(JDependContext.Client);
		// 设置workspacePath
		JDependContext.setWorkspacePath(setting.getWorkspacePath());
		// 设置运行路径（Web or 非Web）
		JDependContext.setRunningPath(System.getProperty("user.dir"));
		// 设置运行模式（单机版运行模式or客户端运行模式）
		String runMode = JDependUtil.getArg(args, "-RunMode");
		if (runMode != null) {
			JDependContext.setRunMode(Integer.parseInt(runMode));
		}
		if (JDependContext.isStandaloneMode()) {
			// 设置是否本地模式运行
			String isLocalService = JDependUtil.getArg(args, "-isLocalService");
			if (isLocalService != null) {
				JDependContext.setIsLocalService(Boolean.parseBoolean(isLocalService));
			}

			if (JDependContext.isLocalService() == null) {
				JDependContext.setIsLocalService(((new ServerConfigurator()).isLocalService()));
			}
		} else {
			JDependContext.setIsLocalService(false);
		}
		// 设置ConnectionProvider
		ConnectionFactory.setProvider(new ClientConnectionProvider());
		// 设置日志是否打印
		PropertyConfigurator conf = new PropertyConfigurator();
		BusiLogUtil.BUSINESSLOG = conf.isPrintBusiLog();
		LogUtil.SYSTEMLOG = conf.isPrintSystemLog();
		LogUtil.SYSTEMWARNING = conf.isPrintSystemWarning();

		// 设置字体大小
		UIProperty.setSize(UIPropertyConfigurator.getInstance().getTextFontSize());
	}

	private void init(String[] args) {
		// 向命令组配置组件增加监听器
		try {
			CommandConfMgr.getInstance().addGroupListener(CommandAdapterMgr.getInstance());
		} catch (JDependException e) {
			e.printStackTrace();
			JDependCooper.this.showStatusError(e.getMessage());
		}
		// 向日志组件注册用户积分监听器
		BusiLogUtil.getInstance().addLogListener(new UserCredits());
		// 向日志组件注册用户行为收集监听器
		BusiLogUtil.getInstance().addLogListener(UserActionGather.getInstance());
		// 启动分数收集器
		String startUploadScore = JDependUtil.getArg(args, "-startUploadScore");
		if (Boolean.parseBoolean(startUploadScore)) {
			ScoreUpload.getInstance().start();
		}
	}

	private static void initClassList(ClientWelcomeDialog welcomeDialog) {
		SearchUtil search = new SearchUtil();
		for (String path : ClassSearchUtil.getSelfPath()) {
			search.addPath(path);
		}
		welcomeDialog.startProgressMonitor(search.getClassCount());
		search.addParseListener(welcomeDialog);
		ClassSearchUtil.getInstance().setClassList(search.getClasses());
	}

	public synchronized void onParsedJavaClass(JavaClass jClass, int process) {
		this.progress(process);
	}

	public void onBuildJavaClasses(List<JavaClass> parsedClasses) {
	}

	public void onSaveReport(String group, String command) {
		this.getPropertyPanel().showReportHistory(group, command);
	}

	public void onClickedSummary(String unitID) {
		this.getPropertyPanel().showClassProperty(unitID);
	}

	public void onAddIgnoreList(List<String> ignoreList) {
		try {
			this.getLeftPanel().getGroupPanel().refreshGroup();
		} catch (JDependException e) {
			e.printStackTrace();
		}
	}

	public void onViewIgnoreList(String group) {
		GroupIngoreListSettingDialog d = new GroupIngoreListSettingDialog(this, group);
		d.setModal(true);
		d.setVisible(true);
	}

	@Override
	public void onRefactoring() throws JDependException {
		// 显示重构结果
		this.getResultPanelWrapper().showMemoryResults();
		// 刷新、显示虚拟重构历史
		this.getPropertyPanel().showMementoList();
		// 刷新TODOList
		this.getPropertyPanel().getToDoListPanel().refresh();
	}

	@Override
	public void refresh() throws JDependException {
		// 刷新Commads
		this.getLeftPanel().getGroupPanel().refreshGroup();

	}

	@Override
	public void show(Map<String, JComponent> result) {
		this.getResultPanel().showResults(result);

	}

	public void clearPriorResult() throws JDependException {
		LogUtil.getInstance(JDependCooper.class).systemLog("清空上一次结果");
		// 清空memento
		AdjustHistory.getInstance().clear();
		// 清空之前的结果
		this.getResultPanel().removeAll();
		// 刷新移动历史
		this.getPropertyPanel().getMementoPanel().clear();
		// 清空类列表
		this.getPropertyPanel().getClassPanel().clearClassList();
		// 清空报告历史
		this.getPropertyPanel().getReportHistroyPanel().clearHistory();
		// 清空执行历史
		this.getPropertyPanel().getExecuteHistroyPanel().clearHistory();
		// 清空TODOList
		this.getPropertyPanel().getToDoListPanel().clear();
	}

	@Override
	public void onExecute(Analyzer analyzer) {
		this.progress();
	}
}
