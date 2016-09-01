package jdepend.client.ui.circle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.circle.util.GuiUtil;

public class SendMessageDialog extends JDialog {

	private JDependCooper frame;

	private JTextArea sendMessage;

	private String ip;

	public SendMessageDialog(JDependCooper frame, String ip, String content) {
		super();
		this.frame = frame;

		this.ip = ip;

		setSize(450, 350);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		this.setLayout(new BorderLayout());

		sendMessage = new JTextArea();
		if (content != null && content.length() > 0) {
			sendMessage.setText(content);
		}

		this.add(BorderLayout.CENTER, new JScrollPane(sendMessage));

		JPanel buttonBar = new JPanel();
		buttonBar.add(createSendMessage());

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private Component createSendMessage() {
		JButton sendButton = new JButton("发送");
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (sendMessage.getText() == null || sendMessage.getText().length() == 0) {
					GuiUtil.showNotice(SendMessageDialog.this, "没有发送信息");
					return;
				}
				IpMsgService.sendMessage(sendMessage.getText(), new String[] { ip });
				SendMessageDialog.this.dispose();
			}
		});
		return sendButton;
	}

}
