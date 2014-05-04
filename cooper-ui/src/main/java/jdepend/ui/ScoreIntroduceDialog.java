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
 * The <code>ScoreDialog</code> displays the about information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class ScoreIntroduceDialog extends JDialog {

	/**
	 * Constructs an <code>ScoreDialog</code> with the specified parent frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 */
	public ScoreIntroduceDialog(JFrame parent) {
		super(parent);

		setTitle("Score介绍");

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
		introduce.setFont(new Font("DialogInput", Font.PLAIN, 16));

		introduce.setText(this.getIntroduce());
		introduce.setCaretPosition(0);

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

		content.append("目前总分（满分100）由四部分构成：抽象程度合理性（20%）、内聚性指数（20%）、封装性（20%）和关系合理性（40%）。\n\n");

		content.append("抽象程度合理性的含义是：\n");

		content.append("一个软件系统被划分成多个块儿后，由于之间的配合必定出现一个块儿处于被别人调用或调用别人的区域内，对于处在被别人调用区域内的块儿，一般上都要应对比较多的且有差异的调用者的请求，为此多设计些接口是好手段。而处于调用者区域内的块儿就没有必要设计过多的接口了。\n\n");

		content.append("抽象程度合理性的公式是（1-D）*20。\n\n");

		content.append("内聚性指数的含义是：\n");

		content.append("组件内的JavaClass与组件内的其他JavaClass的关系应该比与其他组件中的javaClass关系紧密（除了复用的考虑外），在复用目的的考虑下，JavaClass与其他组件中的JavaClass的关系应能做到“相互抵消”。\n\n");

		content.append("内聚性指数的公式是 Balance*20。\n\n");
		
		content.append("封装性的含义是：\n");

		content.append("组件对外提供服务（或被其他组件使用）的JavaClass的比例应该尽量小，以利于该组件的内部调整。\n\n");

		content.append("内聚性指数的公式是 Encapsulation*20。\n\n");

		content.append("关系合理性的含义是：\n");

		content.append("组件间的关系单向、合理，不存在彼此、循环、下层组件调用上层组件、稳定性强的组件依赖稳定性弱的组件的情况。\n\n");

		content.append("关系合理性的公式是（1 - 存在问题的关系 * 问题权值/总关系）*40。\n");

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
