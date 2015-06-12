package jdepend.client.ui.circle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jdepend.core.local.score.ScoreRepository;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.dialog.ScoreListPanel;

public final class ResultListDialog extends CooperDialog {

	private String ip;

	private ScoreListPanel scoreListPanel;

	public ResultListDialog(final JDependCooper frame, String ip) {

		super("结果列表");

		this.ip = ip;

		this.setLayout(new BorderLayout());

		this.scoreListPanel = new ScoreListPanel(frame);
		this.add(BorderLayout.CENTER, this.scoreListPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.createSendButton());
		buttonPanel.add(this.createCloseButton());
		this.add(BorderLayout.SOUTH, buttonPanel);

	}

	protected Component createSendButton() {
		JButton button = new JButton("发送");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (scoreListPanel.getId() == null) {
					JOptionPane.showConfirmDialog(ResultListDialog.this, "请选择结果", "提示", JOptionPane.CLOSED_OPTION);
					return;
				}
				try {
					byte[] result = ScoreRepository.getTheResult((String) scoreListPanel.getId()).getBytes();
					IpMsgService.sendResult(result, new String[] { ip });
					ResultListDialog.this.dispose();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(ResultListDialog.this, e1.getMessage(), "错误",
							JOptionPane.CLOSED_OPTION);
				}
			}
		});

		return button;
	}
}
