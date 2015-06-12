package jdepend.client.ui.culture;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import jdepend.framework.util.BundleUtil;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.util.ClassPathURLStreamHandler;

public class DesignPrinciplePanel extends JPanel {

	private JDependCooper parent;

	private Map<String, ArrayList<DesignPrinciple>> datas = new LinkedHashMap<String, ArrayList<DesignPrinciple>>();

	private String currentPrinciple;

	public DesignPrinciplePanel(final JDependCooper parent) {

		this.parent = parent;

		this.setLayout(new BorderLayout());

		JTabbedPane tab = new JTabbedPane(JTabbedPane.BOTTOM);

		tab.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				JTabbedPane obj = (JTabbedPane) e.getSource();
				currentPrinciple = obj.getTitleAt(obj.getSelectedIndex());
			}
		});

		initDesignPrinciple();

		for (final String principle : this.datas.keySet()) {

			DefaultTableModel model = new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

			};

			JTable table = new JTable(model);

			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					JTable table = (JTable) e.getSource();
					int i = table.rowAtPoint(e.getPoint());
					// parent.getResultPanel().removeAll();
					parent.getResultPanel().addResult(datas.get(principle).get(i).name,
							datas.get(principle).get(i).content);
					// parent.getResultPanel().setLastedTab();
				}
			});

			model.addColumn("名称");
			model.addColumn("提示");

			Object[] row;

			for (DesignPrinciple data : datas.get(principle)) {
				row = new Object[2];
				row[0] = data.name;
				row[1] = data.tip;

				model.addRow(row);
			}

			JScrollPane pane = new JScrollPane(table);
			pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

			tab.add(principle, pane);
		}
		this.add(tab);
	}

	private void initDesignPrinciple() {

		ArrayList<DesignPrinciple> principles;

		principles = new ArrayList<DesignPrinciple>();

		principles.add(new DesignPrinciple("REP", "重用发布等价原则", createREP()));
		principles.add(new DesignPrinciple("ADP", "无环依赖原则", createADP()));
		principles.add(new DesignPrinciple("SAP", "稳定抽象等价原则", createSAP()));
		principles.add(new DesignPrinciple("SDP", "稳定依赖原则", createSDP()));
		principles.add(new DesignPrinciple("CCP", "共同封闭原则", createCCP()));
		principles.add(new DesignPrinciple("CRP", "全部重用原则", createCRP()));

		this.datas.put(BundleUtil.getString(BundleUtil.ClientWin_Culture_DesignPrinciple_Package), principles);

		principles = new ArrayList<DesignPrinciple>();

		principles.add(new DesignPrinciple("SRP", "单一职责原则", createSRP()));
		principles.add(new DesignPrinciple("DRY", "避免重复原则", createDRY()));
		principles.add(new DesignPrinciple("OCP", "开闭原则", createOCP()));
		principles.add(new DesignPrinciple("LSP", "李氏替换原则", createLSP()));
		principles.add(new DesignPrinciple("DIP", "依赖倒置原则", createDIP()));
		principles.add(new DesignPrinciple("ISP", "接口隔离原则", createISP()));

		this.datas.put(BundleUtil.getString(BundleUtil.ClientWin_Culture_DesignPrinciple_Class), principles);

		principles = new ArrayList<DesignPrinciple>();

		principles.add(new DesignPrinciple("High Cohesion、Low Coupling", "高内聚、低耦合", createHL()));
		principles.add(new DesignPrinciple("Information Expert", "信息专家", createIE()));
		principles.add(new DesignPrinciple("Creator", "创建者", createCreator()));
		principles.add(new DesignPrinciple("Controller", "控制器", createController()));
		principles.add(new DesignPrinciple("Polymorphism", "多态", createPolymorphism()));
		principles.add(new DesignPrinciple("Pure Fabrication", "纯虚构模式", createPF()));
		principles.add(new DesignPrinciple("Indirection", "间接性", createIndirection()));
		principles.add(new DesignPrinciple("Protected Variation", "防止变异", createPV()));

		this.datas.put(BundleUtil.getString(BundleUtil.ClientWin_Culture_DesignPrinciple_GRASP), principles);

		currentPrinciple = BundleUtil.getString(BundleUtil.ClientWin_Culture_DesignPrinciple_Package);
	}

	private JComponent createHL() {
		return this.createPackageDesignPrinciple("HL.htm");
	}

	private JComponent createIE() {
		return this.createPackageDesignPrinciple("IE.htm");
	}

	private JComponent createCreator() {
		return this.createPackageDesignPrinciple("Creator.htm");
	}

	private JComponent createController() {
		return this.createPackageDesignPrinciple("Controller.htm");
	}

	private JComponent createPolymorphism() {
		return this.createPackageDesignPrinciple("Polymorphism.htm");
	}

	private JComponent createPF() {
		return this.createPackageDesignPrinciple("PF.htm");
	}

	private JComponent createIndirection() {
		return this.createPackageDesignPrinciple("Indirection.htm");
	}

	private JComponent createPV() {
		return this.createPackageDesignPrinciple("PV.htm");
	}

	private JComponent createCRP() {
		return this.createPackageDesignPrinciple("CRP.htm");
	}

	private JComponent createCCP() {
		return this.createPackageDesignPrinciple("CCP.htm");
	}

	private JComponent createSDP() {
		return this.createPackageDesignPrinciple("SDP.htm");
	}

	private JComponent createSAP() {
		return this.createPackageDesignPrinciple("SAP.htm");
	}

	private JComponent createADP() {
		return this.createPackageDesignPrinciple("ADP.htm");
	}

	private JComponent createREP() {
		return this.createPackageDesignPrinciple("REP.htm");
	}

	private JComponent createPackageDesignPrinciple(String path) {
		JEditorPane text = new JEditorPane();
		text.setEditable(false);
		try {

			StyleSheet ss = new StyleSheet();
			StyleSheet s1 = new StyleSheet();
			s1.importStyleSheet(new URL(null, "classpath:/culture/包的设计原则_files/style.css",
					new ClassPathURLStreamHandler()));
			ss.addStyleSheet(s1);

			HTMLEditorKit kit = new HTMLEditorKit();
			ss.addStyleSheet(kit.getStyleSheet());

			kit.setStyleSheet(ss);
			text.setEditorKit(kit);

			text.setPage(new URL(null, "classpath:/culture/" + path, new ClassPathURLStreamHandler()));
			text.setCaretPosition(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JScrollPane(text);
	}

	private JComponent createSRP() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("系统中的每一个对象都应该只有一个单独的职责，而所有对象所关注的就是自身职责的完成。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 1));

		panel.add(new JLabel("\n"), createConstraints(1, 2));

		nameLabel = new JLabel("Every object in your system should have a single responsibility ,and all the objects");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 3));

		nameLabel = new JLabel("services should be focused on carrying out that single responsibility.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 4));

		panel.add(new JLabel("\n"), createConstraints(1, 5));

		nameLabel = new JLabel("    1、每一个职责都是一个设计的变因，需求变化的时候，需求变化反映为类");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 6));
		nameLabel = new JLabel("    职责的变化。当你系统里面的对象都只有一个变化的原因的时候，你就");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 7));
		nameLabel = new JLabel("    已经很好的遵循了SRP原则。 ");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 8));

		panel.add(new JLabel("\n"), createConstraints(1, 9));

		nameLabel = new JLabel("    2、如果一个类承担的职责过多，就等于把这些职责耦合在了一起。一个职责的变");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 10));
		nameLabel = new JLabel("    化就可能削弱或者抑制这个类其它职责的能力。这种设计会导致脆弱的");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 11));
		nameLabel = new JLabel("    设计。当变化发生的时候，设计会遭到意想不到的破坏。 ");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 12));

		panel.add(new JLabel("\n"), createConstraints(1, 13));

		nameLabel = new JLabel("    3、SRP 让这个系统更容易管理维护，因为不是所有的问题都搅在一起。 ");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 14));
		nameLabel = new JLabel("    内聚Cohesion 其实是SRP原则的另外一个名字.你写了高内聚的软件其");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 15));
		nameLabel = new JLabel("    实就是说你很好的应用了SRP原则。 ");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 16));

		panel.add(new JLabel("\n"), createConstraints(1, 17));

		nameLabel = new JLabel("    4、怎么判断一个职责是不是一个对象的呢？你试着让这个对象自己来完成这");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 18));
		nameLabel = new JLabel("    个职责，比如：“书自己阅读内容”，阅读的职责显然不是书自己的。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 19));

		panel.add(new JLabel("\n"), createConstraints(1, 20));

		nameLabel = new JLabel("    5、仅当变化发生时，变化的轴线才具有实际的意义，如果没有征兆，那么应用SRP");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 21));
		nameLabel = new JLabel("    或者任何其它的原则都是不明智的。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 22));

		return new JScrollPane(panel);
	}

	private JComponent createDRY() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("通过抽取公共部分放置在一个地方避免代码重复.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 1));

		panel.add(new JLabel("\n"), createConstraints(1, 2));

		nameLabel = new JLabel("Avoid duplicate code by abstracting out things that are common and");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 3));

		nameLabel = new JLabel(" placing those thing in a single location.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 4));

		panel.add(new JLabel("\n"), createConstraints(1, 5));

		nameLabel = new JLabel("    1、DRY 很简单，但却是确保我们代码容易维护和复用的关键。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 6));

		panel.add(new JLabel("\n"), createConstraints(1, 7));

		nameLabel = new JLabel("    2、你尽力避免重复代码候实际上在做一件什么事情呢？是在确保每一个需");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 8));
		nameLabel = new JLabel("    求和功能在你的系统中只实现一次，否则就存在浪费！系统用例不存在");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 9));
		nameLabel = new JLabel("    交集，所以我们的代码更不应该重复，从这个角度看DRY可就不只是在说代码了。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 10));

		panel.add(new JLabel("\n"), createConstraints(1, 11));

		nameLabel = new JLabel("    3、DRY 关注的是系统内的信息和行为都放在一个单一的，明显的位置。就");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 12));
		nameLabel = new JLabel("    像你可以猜到正则表达式在.net中的位置一样，因为合理所以可以猜到。 ");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 13));

		panel.add(new JLabel("\n"), createConstraints(1, 14));

		nameLabel = new JLabel("    4、DRY 原则：如何对系统职能进行良好的分割！职责清晰的界限一定程度");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 15));
		nameLabel = new JLabel("    上保证了代码的单一性。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 16));

		return new JScrollPane(panel);
	}

	private JComponent createOCP() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("类应该对修改关闭，对扩展打开。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 1));

		panel.add(new JLabel("\n"), createConstraints(1, 2));

		nameLabel = new JLabel("Classes should be open for extension ,and closed  for modification.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 3));

		panel.add(new JLabel("\n"), createConstraints(1, 4));

		nameLabel = new JLabel("    1、OCP 关注的是灵活性，改动是通过增加代码进行的，而不是改动现有的代码.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 5));

		panel.add(new JLabel("\n"), createConstraints(1, 6));

		nameLabel = new JLabel("    2、OCP的应用限定在可能会发生的变化上，通过创建抽象来隔离以后发生");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 7));
		nameLabel = new JLabel("    的同类变化.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 8));

		panel.add(new JLabel("\n"), createConstraints(1, 9));

		nameLabel = new JLabel("    3、OCP原则传递出来这样一个思想：一旦你写出来了可以工作的代码，就");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 14));
		nameLabel = new JLabel("    要努力保证这段代码一直可以工作。这可以说是一个底线。稍微提高一");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 15));
		nameLabel = new JLabel("    点要求,一旦我们的代码质量到了一个水平，我们要尽最大努力保证代码");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 16));
		nameLabel = new JLabel("    质量不回退。这样的要求使我们面对一个问题的时候不会使用凑活的方");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 17));
		nameLabel = new JLabel("    法来解决，或者说是放任自流的方式来解决一个问题；比如代码添加了");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 18));
		nameLabel = new JLabel("    无数对特定数据的处理，特化的代码越来越多，代码意图开始含混不清");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 19));
		nameLabel = new JLabel("    ，开始退化。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 20));

		panel.add(new JLabel("\n"), createConstraints(1, 21));

		nameLabel = new JLabel("    4、OCP 背后的机制：封装和抽象；封装是建立在抽象基础上的，使用抽象");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 22));
		nameLabel = new JLabel("    获得显示的封装；继承是OCP最简单的例子。除了子类化和方法重载我");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 23));
		nameLabel = new JLabel("    们还有一些更优雅的方法来实现比如组合；");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 24));
		nameLabel = new JLabel("    怎样在不改变源代码（关闭修改）的情况下更改它的行为呢？答案就是");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 25));
		nameLabel = new JLabel("    抽象，OCP背后的机制就是抽象和多态.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 26));

		panel.add(new JLabel("\n"), createConstraints(1, 27));

		nameLabel = new JLabel("    5、没有一个可以适应所有情况的贴切的模型！一定会有变化，不可能完全");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 28));
		nameLabel = new JLabel("    封闭.对程序中的每一个部分都肆意的抽象不是一个好主意，正确的做法");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 29));
		nameLabel = new JLabel("    是开发人员仅仅对频繁变化的部分做出抽象。拒绝不成熟的抽象和抽象");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 30));
		nameLabel = new JLabel("    本身一样重要。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 31));

		panel.add(new JLabel("\n"), createConstraints(1, 32));

		nameLabel = new JLabel("    6、OCP是OOD很多说法的核心，如果这个原则有效应用，我们就可以获更");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 33));
		nameLabel = new JLabel("    强的可维护性 可重用 灵活性 健壮性 LSP是OCP成为可能的主要原则之一.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 34));

		return new JScrollPane(panel);
	}

	private JComponent createLSP() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("子类必须能够替换基类。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 1));

		panel.add(new JLabel("\n"), createConstraints(1, 2));

		nameLabel = new JLabel("Subtypes must be substitutable  for their base types.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 3));

		panel.add(new JLabel("\n"), createConstraints(1, 4));

		nameLabel = new JLabel("    1、LSP关注的是怎样良好的使用继承. ");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 5));

		panel.add(new JLabel("\n"), createConstraints(1, 6));

		nameLabel = new JLabel("    2、必须要清楚是使用一个Method还是要扩展它，但是绝对不是改变它。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 7));

		panel.add(new JLabel("\n"), createConstraints(1, 8));

		nameLabel = new JLabel("    3、LSP清晰的指出，OOD的IS-A关系是就行为方式而言，行为方式是可以进");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 9));
		nameLabel = new JLabel("    行合理假设的，是客户程序所依赖的。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 10));

		panel.add(new JLabel("\n"), createConstraints(1, 11));

		nameLabel = new JLabel("    4、LSP让我们得出一个重要的结论：一个模型如果孤立的看，并不具有真正");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 12));
		nameLabel = new JLabel("    意义的有效性。模型的有效性只能通过它的客户程序来表现。必须根据");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 13));
		nameLabel = new JLabel("    设计的使用者做出的合理假设来审视它。而假设是难以预测的，直到设");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 14));
		nameLabel = new JLabel("    计臭味出现的时候才处理它们。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 15));

		panel.add(new JLabel("\n"), createConstraints(1, 16));

		nameLabel = new JLabel("    5、对于LSP的违反也潜在的违反了OCP.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 17));

		return new JScrollPane(panel);
	}

	private JComponent createDIP() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("高层模块不应该依赖于底层模块 二者都应该依赖于抽象.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 1));

		panel.add(new JLabel("\n"), createConstraints(1, 2));

		nameLabel = new JLabel("抽象不应该依赖于细节 细节应该依赖于抽象.");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 3));

		panel.add(new JLabel("\n"), createConstraints(1, 4));

		nameLabel = new JLabel("    1、什么是高层模块？高层模块包含了应用程序中重要的策略选择和业务模");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 5));
		nameLabel = new JLabel("    型。这些高层模块使其所在的应用程序区别于其它。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 6));

		panel.add(new JLabel("\n"), createConstraints(1, 7));

		nameLabel = new JLabel("    2、如果高层模块依赖于底层模块，那么在不同的上下文中重用高层模块就");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 8));
		nameLabel = new JLabel("    会变得十分困难。然而，如果高层模块独立于底层模块，那么高层模块");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 9));
		nameLabel = new JLabel("    就可以非常容易的被重用。该原则就是框架设计的核心原则。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 10));

		panel.add(new JLabel("\n"), createConstraints(1, 11));

		nameLabel = new JLabel("    3、这里的倒置不仅仅是依赖关系的倒置也是接口所有权的倒置。应用了DIP");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 12));
		nameLabel = new JLabel("    我们会发现往往是客户拥有抽象的接口，而服务者从这些抽象接口派生。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 13));

		panel.add(new JLabel("\n"), createConstraints(1, 14));

		nameLabel = new JLabel("    4、这就是著名的Hollywood原则:\"Don't call us we'll call you.\"底层模块实现");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 15));
		nameLabel = new JLabel("    了在高层模块声明并被高层模块调用的接口。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 16));

		panel.add(new JLabel("\n"), createConstraints(1, 17));

		nameLabel = new JLabel("    5、通过倒置我们创建了更灵活 更持久更容易改变的结构 ");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 18));

		panel.add(new JLabel("\n"), createConstraints(1, 19));

		nameLabel = new JLabel("    6、DIP的简单的启发规则：依赖于抽象；这是一个简单的陈述，该规则建议");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 20));
		nameLabel = new JLabel("    不应该依赖于具体的类，也就是说程序汇总所有的依赖都应该种植于抽");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 21));
		nameLabel = new JLabel("    象类或者接口。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 22));

		panel.add(new JLabel("\n"), createConstraints(1, 23));

		nameLabel = new JLabel("    7、如果一个类很稳定，那么依赖于它不会造成伤害。然而我们自己的具体");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 24));
		nameLabel = new JLabel("    类大多是不稳定的，通过把他们隐藏在抽象接口后面可以隔离不稳定性。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 25));

		panel.add(new JLabel("\n"), createConstraints(1, 26));

		nameLabel = new JLabel("    8、依赖倒置可以应用于任何存在一个类向另一个类发送消息的地方");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 27));

		panel.add(new JLabel("\n"), createConstraints(1, 28));

		nameLabel = new JLabel("    9、依赖倒置原则是实现许多面向对象技术多宣称的好处的基本底层机制，");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 29));
		nameLabel = new JLabel("    是面向对象的标志所在。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 30));

		return new JScrollPane(panel);
	}

	private JComponent createISP() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("不应该强迫客户程序依赖它们不需要的使用的方法。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 1));

		panel.add(new JLabel("\n"), createConstraints(1, 2));

		nameLabel = new JLabel("    1、接口不是高内聚的，一个接口可以分成N组方法，那么这个接口就需要");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 3));

		nameLabel = new JLabel("    使用ISP处理一下。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 4));

		panel.add(new JLabel("\n"), createConstraints(1, 5));

		nameLabel = new JLabel("    2、接口的划分是由使用它的客户程序决定的，客户程序是分离的接口也应");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 6));
		nameLabel = new JLabel("    该是分离的。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 7));

		panel.add(new JLabel("\n"), createConstraints(1, 8));

		nameLabel = new JLabel("    3、一个接口中包含太多行为时候，导致它们的客户程序之间产生不正常的");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 9));
		nameLabel = new JLabel("    依赖关系，我们要做的就是分离接口，实现解耦。");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 10));

		panel.add(new JLabel("\n"), createConstraints(1, 11));

		nameLabel = new JLabel("    4、应用了ISP之后，客户程序看到的是多个内聚的接口。 ");
		nameLabel.setFont(new Font("dialog", Font.PLAIN, 14));
		panel.add(nameLabel, createConstraints(1, 12));

		return new JScrollPane(panel);
	}

	private GridBagConstraints createConstraints(int x, int y) {

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;

		return constraints;
	}

	class DesignPrinciple {
		String name;
		String tip;
		JComponent content;

		public DesignPrinciple(String name, String tip, JComponent content) {
			this.name = name;
			this.tip = tip;
			this.content = content;
		}

	}

}
