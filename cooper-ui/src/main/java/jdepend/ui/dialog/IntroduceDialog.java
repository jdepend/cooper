package jdepend.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;

/**
 * The <code>IntroduceDialog</code> displays the about information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class IntroduceDialog extends JDialog {

	/**
	 * Constructs an <code>IntroduceDialog</code> with the specified parent
	 * frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 */
	public IntroduceDialog(JFrame parent) {
		super(parent);

		setTitle("介绍");

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

		JEditorPane introduce = new JEditorPane();
		introduce.setEditable(false);

		introduce.setText(this.getIntroduce());
		introduce.setCaretPosition(0);
		introduce.setFont(new Font("DialogInput", Font.PLAIN, 16));

		JScrollPane pane = new JScrollPane(introduce);

		JButton closeButton = createButton(BundleUtil.getString(BundleUtil.Command_Close));

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(closeButton);

		panel.add(BorderLayout.CENTER, pane);

		panel.add(BorderLayout.SOUTH, buttonBar);

		getContentPane().add(BorderLayout.CENTER, panel);

	}

	private String getIntroduce() {
		StringBuilder content = new StringBuilder();
		content.append("Cooper是软件架构师用于查看、评价和改进软件程序结构的工具。\n");

		content.append("产品特性：\n");

		content.append("采用一套遵从业界认可的设计原则而形成的模型来度量程序结构的质量。\n");

		content.append("以图形化的方式直观地展示程序结构中的问题。\n");

		content.append("对发现的结构问题能够给出调整建议。\n");

		content.append("模拟执行架构师对程序结构进行的调整，并给出数据对比。\n");

		content.append("提供多种（当前25种）分析程序细节信息的分析器。\n");

		return content.toString();
	}

	/**
	 * Creates and returns a button with the specified label.
	 * 
	 * @param label
	 *            Button label.
	 * @return Button.
	 */
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
