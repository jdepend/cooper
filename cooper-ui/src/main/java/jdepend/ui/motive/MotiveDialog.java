package jdepend.ui.motive;

import javax.swing.JTabbedPane;

import jdepend.core.command.CommandAdapterMgr;
import jdepend.core.config.CommandConfMgr;
import jdepend.framework.ui.CooperDialog;
import jdepend.knowledge.motive.MotiveContainer;
import jdepend.knowledge.motive.MotiveContainerMgr;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.ui.JDependCooper;

public class MotiveDialog extends CooperDialog {

	private JDependCooper frame;

	private MotiveContainer motiveContainer;

	private JTabbedPane tabPane;

	private MotiveOperationPanel motiveOperationPanel;

	private MotiveHistoryPanel motiveHistoryPanel;

	public MotiveDialog(JDependCooper frame) {
		super("设计动机");

		this.frame = frame;

		this.initMotiveContainer();

		tabPane = new JTabbedPane();
		motiveOperationPanel = new MotiveOperationPanel(frame, this);
		motiveHistoryPanel = new MotiveHistoryPanel(frame, this);
		tabPane.add("操作面板", motiveOperationPanel);
		tabPane.add("操作历史", motiveHistoryPanel);

		this.add(tabPane);

	}

	private void initMotiveContainer() {
		String group = CommandAdapterMgr.getCurrentGroup();
		String command = CommandAdapterMgr.getCurrentCommand();
		motiveContainer = MotiveContainerMgr.getInstance().getMotiveContainer(group, command);
		AnalysisResult result = JDependUnitMgr.getInstance().getResult();
		if (!motiveContainer.isExist()) {
			motiveContainer.createInfo(result);
		} else {
			motiveContainer.reCreateInfo(result);
		}
	}

	public MotiveContainer getMotiveContainer() {
		return motiveContainer;
	}

	public MotiveOperationPanel getMotiveOperationPanel() {
		return motiveOperationPanel;
	}

	public MotiveHistoryPanel getMotiveHistoryPanel() {
		return motiveHistoryPanel;
	}
}
