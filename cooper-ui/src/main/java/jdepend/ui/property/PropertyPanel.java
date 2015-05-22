package jdepend.ui.property;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.knowledge.domainanalysis.ui.KnowledgePanel;
import jdepend.model.JDependUnitMgr;
import jdepend.ui.CooperTabbedPane;
import jdepend.ui.JDependCooper;

public class PropertyPanel extends JPanel {

	private JTabbedPane tabPane;

	// ReportHistroy
	private ReportHistoryPanel reportHistroyPanel;

	// ExecuteHistroy
	private ExecuteHistoryPanel executeHistroyPanel;

	// TODOLIST
	private TODOListPanel toDoListPanel;

	// ClassList
	private ClassListPanelWrapper classPanel;

	// MementoPanel
	private MementoPanel mementoPanel;

	// 智慧专家与知识库
	private KnowledgePanel knowledgePanel;

	// Log
	private LogPanel logPanel;

	private JDependCooper frame;

	public static final String ClassListPanel = "ClassListPanelWrapper";
	public static final String MementoPanel = "MementoPanel";
	public static final String ReportHistoryPanel = "ReportHistoryPanel";
	public static final String ExecuteHistoryPanel = "ExecuteHistoryPanel";
	public static final String KnowledgePanel = "KnowledgePanel";
	public static final String LogPanel = "LogPanel";
	public static final String TODOListPanel = "TODOListPanel";

	private static Map<String, Integer> indexs = new HashMap<String, Integer>();

	static {
		indexs.put(TODOListPanel, 0);
		indexs.put(ClassListPanel, 1);
		indexs.put(MementoPanel, 2);
		indexs.put(ReportHistoryPanel, 3);
		indexs.put(ExecuteHistoryPanel, 4);
		indexs.put(KnowledgePanel, 5);
		indexs.put(LogPanel, 6);
	}

	public PropertyPanel(JDependCooper frame) {
		super();

		this.frame = frame;

		setLayout(new BorderLayout());

		tabPane = new CooperTabbedPane(frame, false, true, CooperTabbedPane.Property);

		toDoListPanel = new TODOListPanel(frame);

		this.addResult(BundleUtil.getString(BundleUtil.ClientWin_Property_TODOList), toDoListPanel);
		classPanel = new ClassListPanelWrapper(frame);

		this.addResult(BundleUtil.getString(BundleUtil.ClientWin_Property_ClassList), classPanel);

		mementoPanel = new MementoPanel(frame);

		this.addResult(BundleUtil.getString(BundleUtil.ClientWin_Property_Memento), mementoPanel);

		reportHistroyPanel = new ReportHistoryPanel(frame);

		this.addResult(BundleUtil.getString(BundleUtil.ClientWin_Property_ReportHistroy), reportHistroyPanel);

		executeHistroyPanel = new ExecuteHistoryPanel(frame);

		this.addResult(BundleUtil.getString(BundleUtil.ClientWin_Property_ExecuteHistroy), executeHistroyPanel);

		knowledgePanel = new KnowledgePanel(frame);

		this.addResult(BundleUtil.getString(BundleUtil.ClientWin_Property_Knowledge), knowledgePanel);

		logPanel = new LogPanel(frame);

		this.addResult(BundleUtil.getString(BundleUtil.ClientWin_Property_Log), logPanel);

		this.add(tabPane);
	}

	public void addResult(String label, JComponent component) {
		this.tabPane.addTab(label, component);
	}

	public void showTODOList() {
		try {
			this.toDoListPanel.refresh();
			this.setTab(TODOListPanel);
		} catch (JDependException e) {
			e.printStackTrace();
			frame.getResultPanel().showError(e);
		}
	}

	public void showClassProperty(String unitID) {
		jdepend.model.Component component = JDependUnitMgr.getInstance().getResult().getTheComponent(unitID);
		if (component != null) {
			classPanel.showClassList(component);
			this.setTab(ClassListPanel);
		}
	}

	public void showMementoList() {
		mementoPanel.refresh();
		this.setTab(MementoPanel);
	}

	public void showReportHistory(String group, String commandName) {
		reportHistroyPanel.showHistory(group, commandName);
		this.setTab(ReportHistoryPanel);
	}

	public void showExecuteHistory(String group, String commandName) {
		try {
			executeHistroyPanel.showHistory(group, commandName);
			this.setTab(ExecuteHistoryPanel);
		} catch (JDependException e) {
			e.printStackTrace();
			frame.getResultPanel().showError(e);
		}

	}

	public TODOListPanel getToDoListPanel() {
		return toDoListPanel;
	}

	public ReportHistoryPanel getReportHistroyPanel() {
		return reportHistroyPanel;
	}

	public ExecuteHistoryPanel getExecuteHistroyPanel() {
		return executeHistroyPanel;
	}

	public ClassListPanelWrapper getClassPanel() {
		return classPanel;
	}

	public MementoPanel getMementoPanel() {
		return mementoPanel;
	}

	public LogPanel getLogPanel() {
		return logPanel;
	}

	public void setTab(String name) {
		this.tabPane.setSelectedIndex(indexs.get(name));
	}

}
