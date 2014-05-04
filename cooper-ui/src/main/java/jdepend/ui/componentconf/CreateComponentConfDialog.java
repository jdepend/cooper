package jdepend.ui.componentconf;

import java.util.List;

import jdepend.model.Component;

/**
 * 用于创建组件配置的弹出窗口基类
 * 
 * @author wangdg
 * 
 */
public abstract class CreateComponentConfDialog extends ComponentConfDialog {

	protected List<String> units;

	public CreateComponentConfDialog(List<String> units) {
		super();
		this.units = units;
	}

	public CreateComponentConfDialog(List<String> units, boolean isFullComponentName) {
		this(units);

		if (units.size() > 0) {
			componentname.setText(Component.getDefaultComponentName(units, isFullComponentName));
		}
	}
}
