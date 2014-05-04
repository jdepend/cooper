package jdepend.ui.componentconf;

import java.awt.event.ActionEvent;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.ComponentModelConfMgr;

/**
 * 用于修改组件配置的弹出窗口基类
 * 
 * @author wangdg
 * 
 */
public class UpdateComponentConfDialog extends ComponentConfDialog {

	private String group;

	private ComponentConf componentConf;

	public UpdateComponentConfDialog(String group, String componentModelName, ComponentConf componentConf) {
		super();

		this.group = group;
		this.componentConf = componentConf;

		componentname.setText(componentConf.getName());
		this.componentname.setEditable(false);

		if (componentConf.getLayer() == Component.PlatformComponentLevel) {
			this.platformType.setSelected(true);
		} else if (componentConf.getLayer() == Component.DomainComponentLevel) {
			this.domainType.setSelected(true);
		} else if (componentConf.getLayer() == Component.AppComponentLevel) {
			this.appType.setSelected(true);
		} else  if (componentConf.getLayer() == Component.InteractiveComponentLevel) {
			this.interactiveType.setSelected(true);
		}
	}

	protected void doService(ActionEvent e) throws JDependException {

		this.componentConf.setLayer(this.getComponentLayer());
		ComponentModelConfMgr.getInstance().getTheGroupComponentModelConf(group).save();

	}
}
