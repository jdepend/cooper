package jdepend.ui.wizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jdepend.framework.exception.JDependException;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.component.modelconf.JavaPackageComponentModelConf;
import jdepend.ui.componentconf.ComponentModelPanel;

public class ComponentStep extends Step {

	private ComponentModelPanel componentPanel;

	private boolean create = true;// 用于区分是否采用上一步回到的该页面

	private int count = 1;

	public ComponentStep(NewGroupWizard wizard, int count) {
		this(wizard, "Describe [" + count + "] ComponentModel");
		this.count = count;
	}

	public ComponentStep(NewGroupWizard wizard) {
		this(wizard, "Describe ComponentModel");
	}

	private ComponentStep(NewGroupWizard wizard, String name) {

		super(name, wizard);

		this.setLayout(new BorderLayout());

		this.componentPanel = new ComponentModelPanel(wizard.getFrame(), wizard.getWorker().getPath(), wizard
				.getWorker().getGroupName());

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createPriorButton());
		buttonBar.add(createNextButton());
		buttonBar.add(createRepeatButton());
		buttonBar.add(createSkipButton());
		buttonBar.add(createCancelButton());

		this.add(BorderLayout.CENTER, this.componentPanel);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	@Override
	public void refresh() {
		this.componentPanel.setPath(this.getWizard().getWorker().getPath());
		this.componentPanel.refresh();
	}

	@Override
	protected Step createNextStep() {
		return new CommandStep(this.getWizard());
	}

	@Override
	protected int doWork() throws JDependException {
		JavaPackageComponentModelConf componentModel = componentPanel.getComponentModelConf();

		if (componentModel.size() == 0) {
			if (JOptionPane.showConfirmDialog(this, "您没有设置组件，之后的分析将以包为单位，你是否确认继续？") != JOptionPane.OK_OPTION) {
				return CANCEL_NEXT_STEP;
			}
		}
		if (componentModel.size() > 0) {
			List<String> ignorePackages = this.componentPanel.calIgnorePackages();
			if (ignorePackages != null && ignorePackages.size() > 0) {
				if (JOptionPane.showConfirmDialog(this, "包[" + ignorePackages.get(0) + "]等" + ignorePackages.size()
						+ "个没有被包含的组件中，你是否确认继续？") != JOptionPane.OK_OPTION) {
					return CANCEL_NEXT_STEP;
				}
			}
			// 设置未包含的packages
			componentModel.setIgnorePackages(ignorePackages);
		}
		if (componentModel.getName() == null || componentModel.getName().length() == 0) {
			if (this.componentPanel.getComponentModelConf().size() != 0) {
				JOptionPane.showMessageDialog(this, "未指定组件组名称！");
				return CANCEL_NEXT_STEP;
			}
		}

		if (this.create) {
			// 重复设计组件模型时，判断名称是否重复
			if (this.getWorker().getComponentModels().contains(componentModel.getName())) {
				JOptionPane.showMessageDialog(this, "组件组名称已经存在！");
				return CANCEL_NEXT_STEP;
			}
			if (componentModel.getName() != null && componentModel.getName().length() != 0) {
				this.getWorker().addComponentModel(componentModel);

			}
			// 设置状态
			this.create = false;
			this.componentPanel.setReadOnlyName();
		} else {
			// 更新组件模型
			this.getWorker().addComponentModel(componentModel);
		}

		return DO_NEXT_STEP;
	}

	@Override
	protected void validateData() throws JDependException {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		int nextCount = count + 1;
		return new ComponentStep(this.getWizard(), nextCount);
	}
}
