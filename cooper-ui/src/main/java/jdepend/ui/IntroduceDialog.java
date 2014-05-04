package jdepend.ui;

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
		content.append("Cooper可以对您的代码（class文件夹或jar文件）按着您定义的逻辑单元进行依赖关系的分析，并可以由您定义依赖关系的类型、强度。一些显而易见的功能，如：\n");

		content.append("它可以分析出您的代码中Action直接调用Dao的地方；\n");

		content.append("它可以分析出您的代码中Dao反向调用Service的地方；\n");

		content.append("它可以分析出您的代码中Util类（全部是static方法）作为类属性使用的反常地方；\n");

		content.append("它可以分析出您的代码中ActionForm出现在了Dao中；\n");

		content.append("等等。\n");

		content.append("它还可以分析出：\n");

		content.append("逻辑单元的是否稳定、是否内聚、是否与其他逻辑单元有较强的耦合关系；\n");

		content.append("它可以给出逻辑单元的合并和拆分建议。\n");

		content.append("它受一些设计原则的影响，这里它实现了DIP、ADP、SDP、SAP等原则，并可以根据这些原则检查您的代码。\n");

		content.append("当然，它是一个开放的工具，您可以根据自己对设计原则的理解编写分析器对自己的代码进行检查。\n");

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
