package jdepend.ui.motive;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jdepend.knowledge.motive.Motive;
import jdepend.knowledge.motive.MotiveOperation;
import jdepend.ui.JDependCooper;

public class MotiveHistoryPanel extends MotivePanel {

	private JTextArea operationHistory;

	private JTextArea motive;

	public MotiveHistoryPanel(JDependCooper frame, MotiveDialog motiveDialog) {
		super(frame, motiveDialog);

		this.refresh();
	}

	@Override
	protected JPanel createButtonBar() {
		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createCloseButton());

		return buttonBar;
	}

	@Override
	protected JPanel createWorkspacePanel() {
		JPanel workspacePanel = new JPanel(new GridLayout(2, 1));

		JPanel operationHistoryPanel = new JPanel(new BorderLayout());
		operationHistoryPanel.add(BorderLayout.NORTH, new JLabel("执行历史："));
		this.operationHistory = new JTextArea();
		operationHistoryPanel.add(BorderLayout.CENTER, new JScrollPane(this.operationHistory));

		workspacePanel.add(operationHistoryPanel);

		JPanel motivePanel = new JPanel(new BorderLayout());
		motivePanel.add(BorderLayout.NORTH, new JLabel("设计动机："));
		this.motive = new JTextArea();
		motivePanel.add(BorderLayout.CENTER, new JScrollPane(this.motive));

		workspacePanel.add(motivePanel);

		return workspacePanel;
	}

	public void refresh() {
		this.refreshOperationHistory();
		this.refreshMotive();
	}

	private void refreshOperationHistory() {
		StringBuilder operationInfo = new StringBuilder();
		for (MotiveOperation operation : this.motiveContainer.getOperations()) {
			operationInfo.append(operation.getDate());
			operationInfo.append("	");
			operationInfo.append(operation.getDesc());
			operationInfo.append("\n");
		}
		this.operationHistory.setText(operationInfo.toString());
		this.operationHistory.setCaretPosition(0);

	}

	private void refreshMotive() {
		StringBuilder motiveInfo = new StringBuilder();
		for (Motive motive : this.motiveContainer.getMotives()) {
			motiveInfo.append(motive.getDesc());
			motiveInfo.append("\n");
		}
		this.motive.setText(motiveInfo.toString());
		this.motive.setCaretPosition(0);
	}
}
