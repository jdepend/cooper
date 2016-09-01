package jdepend.client.ui.dialog;

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
import jdepend.client.ui.JDependCooper;

/**
 * The <code>AboutDialog</code> displays the about information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class MetricsDialog extends JDialog {

	/**
	 * Constructs an <code>AboutDialog</code> with the specified parent frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 */
	public MetricsDialog(JFrame parent) {
		super(parent);

		setTitle("Metrics");

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
		
		content.append("Cooper作为软件结构检查工具，主要手段的是考查系统在不同划分方式下的结构质量。一种划分方式由多个分析单元构成，Cooper通过检查分析单元上的给定指标来最终评价系统的结构质量。\n\n");

		content.append("分析单元上的指标是用于从一个独立的角度评价该分析单元，给出优劣，并可以和其他分析单元对比。\n\n");
		
		content.append("指标分成基本指标和计算指标。\n\n");

		content.append("基本指标比较简单：\n\n");
		
		content.append("	CN 是分析单元含有的JavaClass的总数；CC是具体类个数；AC是抽象类和接口的个数；PC私有类数量；SC 稳定类数量；Ca是有多少个别的单元依赖我；Ce是我依赖了多少个别的单元。\n\n");

		content.append("计算指标包括：\n\n");

		content.append("	A=AC/CN,是抽象类和接口的比例，表达了这个分析单元的抽象程度。越大越抽象。\n");
		
		content.append("	V=SC/CN，是稳定类的比例，表达了这个分析单元中具有稳定业务逻辑的程度。值越大越稳定。\n");

		content.append("	I=传出耦合值/(传出耦合值 + 传入耦合值),是依赖别人占整个关系的比例，表达了这个分析单元的稳定度。越大越不稳定。\n");

		content.append("	 D=A+V+I-1，是评估这个分析单元的合理性。这个指标越接近零越理想。基本公理是：处于软件核心的内容应该提高抽象程度，并处于稳定程度高的状态；而处于软件边缘的内容就没有必要追求高的抽象度和稳定性。因为抽象度和稳定性的追求是需要成本的。这个指标可以使您把自己有限的力量用在最关键的地方，使其费效比最好。\n");

		content.append("	Coupling= sum(依赖的JavaClass * Relation强度)（Relation强度 = RelationType * current依赖合理性 * depend依赖合理性），Coupling体现了分析单元与环境的耦合程度。在满足功能的情况下，越低越好。\n");

		content.append("	Cohesion = sum(内部的JavaClass * Relation强度)（Relation强度 = RelationType * current依赖合理性 * depend依赖合理性），Cohesion体现了分析单元内部元素之间的内聚程度。在满足功能的情况下，越大越好。\n");

		content.append("	Balance = Cohesion/(Cohesion + 分组耦合最大顺序差值），内聚性指数。值越大越好。\n");
		
		content.append("	Encapsulation = PC/CN ，私有类比例，值越大越好。当该分析单元没有其他分析单元依赖的时候，该指标无意义。\n");

		content.append("	ObjectOriented = attributeCount / methodCount，面向对象强调信息和操作的“合作”来完成赋予对象的职责。对象的属性一般认为成对象拥有的信息，方法认为成对象提供的服务。该指标推荐写更多的拥有信息的对象，并且有限程度的提供服务（也就是封装）。\n\n");

		content.append("	DependencyCycles:是否存在循环依赖。\n\n");

		content.append("分析单元的确定：\n\n");

		content.append("	在应用该工具的时候，确定分析单元是一件最具实践借鉴意义的事情。分析单元与设计和开发时的组件相当，当分析的目标比较大时，一个分析单元也可以对应一个子系统或系统。当以maven来管理工程代码的时候，分析单元也可以对应一个maven Module。\n");

		content.append("	在进行深度分析时，可以将特定的类集合作为分析单元，分析它与系统其他部分的关系。这个类集合的选取可以是同一类型的类（如Action、Service、DAO），也可以是打算要复用的类。\n");
		
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
