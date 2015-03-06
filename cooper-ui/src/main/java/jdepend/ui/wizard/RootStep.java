package jdepend.ui.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JPanel;

import jdepend.core.local.config.CommandConf;
import jdepend.core.local.config.CommandConfMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.file.TargetFileManager;
import jdepend.framework.util.FileUtil;
import jdepend.ui.componentconf.ComponentModelPanel;

public class RootStep extends Step {

	private GroupSettingPanel groupPanel;

	private static int blankPanelHeight = 0;

	public RootStep(NewGroupWizard wizard) {
		super("组信息", wizard);

		this.setLayout(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());

		groupPanel = new GroupSettingPanel(wizard.getFrame());

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createNextButton());
		buttonBar.add(createCancelButton());

		JPanel blankPanel = new JPanel();
		blankPanel.setPreferredSize(new Dimension(ComponentModelPanel.Width,
				blankPanelHeight));

		content.add(BorderLayout.CENTER, groupPanel);

		content.add(BorderLayout.SOUTH, blankPanel);

		this.add(BorderLayout.CENTER, content);

		this.add(BorderLayout.SOUTH, buttonBar);

	}

	@Override
	public Step createNextStep() {
		return new ComponentStep(this.getWizard());
	}

	@Override
	public int doWork() throws JDependException {
		getWorker().setGroupName(groupPanel.getGroupname().getText());
		getWorker().setPath(
				groupPanel.getPathname().getText().replaceAll("\n", ""));
		getWorker().setSrcPath(
				groupPanel.getSrcPathName().getText().replaceAll("\n", ""));
		getWorker().setFilteredPackages(groupPanel.getFilteredPackages());
		getWorker().setAttribute(groupPanel.getAttribute().getText());

		return DO_NEXT_STEP;
	}

	@Override
	protected void validateData() throws JDependException {

		if (groupPanel.getGroupname().getText() == null
				|| groupPanel.getGroupname().getText().length() == 0)
			throw new JDependException("组名不能为空！");

		if (groupPanel.getPathname().getText() == null
				|| groupPanel.getPathname().getText().length() == 0)
			throw new JDependException("路径不能为空！");

		String[] paths = groupPanel.getPathname().getText()
				.replaceAll("\n", "").split(TargetFileManager.FilePathSplit);
		for (String path : paths) {
			if (!CommandConf.DEFAULT_CLASSES.equals(path)) {
				File directory = new File(path);
				if (!directory.isDirectory()
						&& !FileUtil.acceptCompressFile(directory)) {
					throw new IllegalArgumentException("分析的classes路径不合格。");
				}
			}
		}

		if (CommandConfMgr.getInstance().getGroupNames()
				.contains(groupPanel.getGroupname().getText())) {
			throw new JDependException("组名已经存在！");
		}

	}
}
