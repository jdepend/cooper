package jdepend.client.ui;

import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import jdepend.client.core.command.CommandAdapterMgr;
import jdepend.client.core.config.CommandConfMgr;
import jdepend.client.core.persistent.ClientConnectionProvider;
import jdepend.client.ui.action.AddGroupWizardAction;
import jdepend.client.ui.action.ExitAction;
import jdepend.client.ui.action.ImportResultAction;
import jdepend.client.ui.action.IntroduceAction;
import jdepend.client.ui.action.MetricsAction;
import jdepend.client.ui.action.ScoreAction;
import jdepend.client.ui.action.ScoreAndMetricsAction;
import jdepend.client.ui.action.ScoreIntroduceAction;
import jdepend.client.ui.action.SettingAction;
import jdepend.client.ui.action.SettingClassRelationMgrAction;
import jdepend.client.ui.action.SettingWorkspaceAction;
import jdepend.client.ui.action.WorkspaceProfileSettingAction;
import jdepend.client.ui.analyzer.AnalyzerPanel;
import jdepend.client.ui.circle.CirclePanel;
import jdepend.client.ui.command.GroupIngoreListSettingDialog;
import jdepend.client.ui.command.GroupPanel;
import jdepend.client.ui.culture.CulturePanel;
import jdepend.client.ui.framework.PanelMgr;
import jdepend.client.ui.framework.UIPropertyConfigurator;
import jdepend.client.ui.property.PropertyPanel;
import jdepend.client.ui.result.framework.ReportListener;
import jdepend.client.ui.result.framework.ResultPanel;
import jdepend.client.ui.result.panel.ResultPanelWrapper;
import jdepend.client.ui.start.WorkspaceSetting;
import jdepend.client.ui.start.WorkspaceSettingDialog;
import jdepend.core.serviceproxy.JDependServiceLocalProxyFactory;
import jdepend.core.serviceproxy.framework.JDependServiceProxyFactoryMgr;
import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.DBBusinessLogWriter;
import jdepend.framework.log.LogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.framework.ui.action.AboutAction;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.config.UIProperty;
import jdepend.framework.ui.dialog.WelcomeDialog;
import jdepend.framework.ui.panel.StatusField;
import jdepend.framework.ui.panel.StatusPanel;
import jdepend.framework.util.BundleUtil;
import jdepend.metadata.JavaClass;
import jdepend.parse.ParseListener;
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
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_SetClassRelationMgr) + "/"
						+ BundleUtil.getString(BundleUtil.ClientWin_Menu_ProfileSetting));

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
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Help), "H");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_AddGroup), "G");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_Exit), "E");
		accelerators.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ParamSetting), "P");
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
				new SettingClassRelationMgrAction(this));
		actions.put(BundleUtil.getString(BundleUtil.ClientWin_Menu_ProfileSetting), new WorkspaceProfileSettingAction(
				this));
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
		return new ClientStatusField(this);
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

	public static void main(String[] args) {

		System.setProperty("sun.zip.encoding", "default");

		JDependCooper frame = new JDependCooper(BundleUtil.getString(BundleUtil.ClientWin_Title));

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

	public void start(String[] args, WorkspaceSetting setting) {
		// 初始化环境信息
		this.initEnv(args, setting);
		// 初始化ClassList
		// initClassList(welcomeDialog);
		// 初始化
		this.init(args);
		// 显示
		this.display();
		// 记录日志
		BusiLogUtil.getInstance().businessLog(Operation.startCooper);
	}

	private void initEnv(String[] args, WorkspaceSetting setting) {
		// 设置workspacePath
		JDependContext.setWorkspacePath(setting.getWorkspacePath());
		// 设置运行路径（Web or 非Web）
		JDependContext.setRunningPath(System.getProperty("user.dir"));
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

	protected void init(String[] args) {
		// 设置业务日志Writer
		BusiLogUtil.getInstance().setBusiWriter(new DBBusinessLogWriter());
		// 设置ServiceProxyFactory
		JDependServiceProxyFactoryMgr.getInstance().setFactory(new JDependServiceLocalProxyFactory());
		// 向命令组配置组件增加监听器
		try {
			CommandConfMgr.getInstance().addGroupListener(CommandAdapterMgr.getInstance());
		} catch (JDependException e) {
			e.printStackTrace();
			this.showStatusError(e.getMessage());
		}
		// 设置AnalyzerPanel
		PanelMgr.getInstance().setAnalyzerPanel(new AnalyzerPanel(this));

	}

	public synchronized void onParsedJavaClass(JavaClass jClass, int process) {
		this.progress(process);
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
	public void onExecute(Analyzer analyzer) {
		this.progress();
	}

	@Override
	public void refresh() throws JDependException {
		// 刷新Commands
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
}
