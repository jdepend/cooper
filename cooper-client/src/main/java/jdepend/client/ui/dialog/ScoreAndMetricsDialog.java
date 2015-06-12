package jdepend.client.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;

import jdepend.framework.util.BundleUtil;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.util.ClassPathURLStreamHandler;


public final class ScoreAndMetricsDialog extends JDialog {

	public ScoreAndMetricsDialog(JDependCooper frame) {
		super(frame);

		setTitle("分数和指标体系");

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(JDependCooper.IntroducePopDialogWidth, JDependCooper.IntroducePopDialogHeight);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JButton closeButton = createButton(BundleUtil.getString(BundleUtil.Command_Close));

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(closeButton);

		panel.add(BorderLayout.CENTER, this.getIntroduce());

		panel.add(BorderLayout.SOUTH, buttonBar);

		getContentPane().add(BorderLayout.CENTER, panel);
	}

	private JComponent getIntroduce() {
		JEditorPane text = new JEditorPane();
		text.setEditable(false);
		try {
			HTMLEditorKit kit = new HTMLEditorKit();
			text.setEditorKit(kit);
			text.setPage(new URL(null,"classpath:/introduce/Cooper的分数和指标体系.htm", new ClassPathURLStreamHandler()));
			text.setCaretPosition(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JScrollPane(text);
	}

	private JButton createButton(String label) {

		JButton button = new JButton(label);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

}
