package jdepend.statistics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import jdepend.core.persistent.ClientConnectionProvider;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.framework.ui.AboutAction;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.StatusPanel;
import jdepend.framework.ui.TabsPanel;
import jdepend.framework.util.JDependUtil;
import jdepend.model.util.ClassSearchUtil;
import jdepend.parse.util.SearchUtil;

public final class StaticsFrame extends JDependFrame {

	private TabsPanel workspacePanel;

	public StaticsFrame() {
		super("统计分析");

		//
		// Install the resource string table.
		//
		resourceStrings = new HashMap<String, String>();
		resourceStrings.put("menubar", "文件/分析项目/帮助");
		resourceStrings.put("文件", "退出");
		resourceStrings
				.put(
						"分析项目",
						"规模分数分析/分数分项分析/规模抽象程度合理性分析/规模内聚性分析/规模关系合理性分析/关系组件个数比例分析/-/组件封装性/基于数据库表实现组件间通讯的比例/-/类关系类型比例分析/类关系内外比例分析/类规模比例分析/-/浏览包结构/-/设计能力浏览/设计技巧浏览/调用类个数排名/架构模式分析/");
		resourceStrings.put("帮助", "关于");
		accelerators = new HashMap<String, String>();
		accelerators.put("文件", "F");
		accelerators.put("分析项目", "X");
		accelerators.put("规模分数分析", "S");
		accelerators.put("分数分项分析", "M");
		accelerators.put("规模抽象程度合理性分析", "D");
		accelerators.put("规模内聚性分析", "B");
		accelerators.put("规模关系合理性分析", "R");
		accelerators.put("关系组件个数比例分析", "J");
		accelerators.put("组件封装性", "G");
		accelerators.put("基于数据库表实现组件间通讯的比例", "C");
		accelerators.put("类关系类型比例分析", "T");
		accelerators.put("类关系内外比例分析", "I");
		accelerators.put("类规模比例分析", "L");
		accelerators.put("浏览包结构", "P");
		accelerators.put("设计能力浏览", "K");
		accelerators.put("设计技巧浏览", "N");
		accelerators.put("调用类个数排名", "O");
		accelerators.put("架构模式分析", "P");
		accelerators.put("退出", "E");
		accelerators.put("帮助", "H");
		accelerators.put("关于", "A");
		//
		// Install the action table.
		//
		actions = new HashMap<String, AbstractAction>();
		actions.put("退出", new ExitAction(this));
		actions.put("关于", new AboutAction(this));
		actions.put("规模分数分析", new ScaleScoreAction(this));
		actions.put("分数分项分析", new ScaleScoreItemAction(this));
		actions.put("规模抽象程度合理性分析", new ScaleDAction(this));
		actions.put("规模内聚性分析", new ScaleBalanceAction(this));
		actions.put("规模关系合理性分析", new ScaleRelationAction(this));
		actions.put("关系组件个数比例分析", new RelationComponentScaleAction(this));
		actions.put("组件封装性", new EncapsulationAction(this));
		actions.put("基于数据库表实现组件间通讯的比例", new TableRelationScaleAction(this));
		actions.put("类关系类型比例分析", new ClassRelationTypeAction(this));
		actions.put("类关系内外比例分析", new ClassRelationInnerAction(this));
		actions.put("类规模比例分析", new ClassLineCountAction(this));
		actions.put("浏览包结构", new PackagesViewerAction(this));
		actions.put("设计能力浏览", new CapacityAction(this));
		actions.put("设计技巧浏览", new SkillAction(this));
		actions.put("调用类个数排名", new ClassInvokeCountAction(this));
		actions.put("架构模式分析", new ArchitectPatternAction(this));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new ExitAction((StaticsFrame) e.getSource()).actionPerformed(null);
			}
		});
	}

	private void display() {
		this.createUI();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
	}

	private void createUI() {

		JMenuBar menuBar = createMenubar();
		this.setJMenuBar(menuBar);

		workspacePanel = new TabsPanel();
		this.getContentPane().add(BorderLayout.CENTER, workspacePanel);

		StatusPanel statusPanel = getStatusPanel();
		this.getContentPane().add(BorderLayout.SOUTH, statusPanel);

	}

	public void refresh() throws JDependException {
		// TODO Auto-generated method stub

	}

	public void show(String label, JComponent component) {
		this.workspacePanel.show(label, component);
	}

	public void showError(Exception e) {
		this.workspacePanel.showError(e);
	}

	public static void main(String args[]) throws JDependException {
		initEnv(args);
		initClassList();
		StaticsFrame statics = new StaticsFrame();
		statics.display();
	}

	private static void initEnv(String[] args) {
		String workspacePath = JDependUtil.getArg(args, "-WorkspacePath");
		if (workspacePath == null) {
			System.err.println("没有设置WorkspacePath，请以-WorkspacePath 设置WorkspacePath");
			System.exit(ERROR);
		} else {
			JDependContext.setWorkspacePath(workspacePath);
		}
		// 设置ConnectionProvider
		ConnectionFactory.setProvider(new ClientConnectionProvider());
	}

	private static void initClassList() {
		SearchUtil search = new SearchUtil();
		for (String path : ClassSearchUtil.getSelfPath()) {
			search.addPath(path);
		}
		ClassSearchUtil.getInstance().setClassList(search.getClasses());
	}

	private class ExitAction extends AbstractAction {

		private JDependFrame frame;

		/**
		 * Constructs an <code>ExitAction</code> instance.
		 */
		ExitAction(JDependFrame frame) {
			super("Exit");
			this.frame = frame;
		}

		/**
		 * Handles the action.
		 */
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			System.exit(0);
		}
	}

	@Override
	public void show(Map<String, JComponent> result) {
		for (String key : result.keySet()) {
			this.show(key, result.get(key));
		}
	}

}
