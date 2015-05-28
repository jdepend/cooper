package jdepend.statistics;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import jdepend.core.local.score.ScoreInfo;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.metadata.tree.JavaPackageNode;
import jdepend.model.result.AnalysisResult;
import jdepend.report.util.TreeGraphUtil;

public final class PackagesViewerAction extends ScoreListAction {

	public PackagesViewerAction(JDependFrame frame) {
		super(frame, "浏览包结构");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void analyse(ActionEvent e) throws JDependException {
		JavaPackageNode javaPackageRoot;
		JComponent treePanel;
		String title;
		JTabbedPane tabs = new JTabbedPane();
		tabs.setTabPlacement(JTabbedPane.LEFT);
		AnalysisResult result;
		for (ScoreInfo scoreInfo : scoreCollection.getScoreInfos()) {
			result = scoreCollection.getTheResult(scoreInfo);

			javaPackageRoot = result.getJavaPackageTree();

			treePanel = (new TreeGraphUtil()).createTree(javaPackageRoot);
			title = result.getRunningContext().getGroup() + "|" + result.getRunningContext().getCommand();
			tabs.addTab(title, treePanel);

			this.progress();
			LogUtil.getInstance(PackagesViewerAction.class).systemLog("生成了[" + title + "]的包结构");
		}

		this.addResult("浏览包结构", tabs);
	}
}
