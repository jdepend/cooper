package jdepend.ui.circle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;
import jdepend.ui.circle.domain.DataPacket;
import jdepend.ui.circle.domain.IpMsgConstant;
import jdepend.ui.circle.domain.SystemVar;
import jdepend.ui.circle.util.NetUtil;

public final class AddIPDialog extends JDialog {

	private JDependCooper frame;

	private JFormattedTextField field;

	public AddIPDialog(JDependCooper frame) throws ParseException {
		super();
		this.frame = frame;

		setSize(250, 100);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		this.setLayout(new BorderLayout());

		MaskFormatter ipmask = new MaskFormatter("###.###.###.###");
		ipmask.setPlaceholderCharacter(' ');
		field = new JFormattedTextField(ipmask);
		field.setFont(new Font("Courier", Font.PLAIN, 18));
		field.setColumns(16);
		this.add(BorderLayout.CENTER, field);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.createOkButton());
		buttonPanel.add(this.createCloseButton());

		this.add(BorderLayout.SOUTH, buttonPanel);

	}

	private Component createOkButton() {
		JButton button = new JButton("增加");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataPacket dp = new DataPacket(IpMsgConstant.IPMSG_BR_ENTRY);
				dp.setAdditional(SystemVar.USER_NAME + '\0' + "");
				dp.setIp(NetUtil.getLocalHostIp());
				NetUtil.sendUdpPacket(dp, field.getText());
				AddIPDialog.this.dispose();
			}
		});

		return button;
	}

	private Component createCloseButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddIPDialog.this.dispose();
			}
		});

		return button;
	}

}
