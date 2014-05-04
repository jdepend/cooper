package jdepend.knowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdviseInfo implements Serializable {

	private static final long serialVersionUID = 3320461453807909958L;

	private String info;

	private String desc;

	private List<String> componentNames = new ArrayList<String>();

	private transient String componentNameInfo;

	public AdviseInfo() {
		super();
	}

	public AdviseInfo(String info) {
		super();
		this.info = info;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getComponentNameInfo() {
		if (componentNameInfo == null) {
			StringBuilder info = new StringBuilder();
			for (String componentName : componentNames) {
				info.append(componentName);
				info.append(",");
			}
			info.delete(info.length() - 1, info.length());
			componentNameInfo = info.toString();
		}
		return componentNameInfo;
	}

	public List<String> getComponentNames() {
		return componentNames;
	}

	public void addComponentName(String componentName) {
		if (!this.componentNames.contains(componentName)) {
			this.componentNames.add(componentName);
		}
	}

	private String calInfo() {
		StringBuilder info = new StringBuilder();
		info.append(desc);
		info.append(this.getComponentNameInfo());
		return info.toString();
	}

	@Override
	public String toString() {
		if (this.info == null) {
			this.info = this.calInfo();
		}

		return this.info;

	}
}
