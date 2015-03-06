package jdepend.ui.wizard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.core.local.config.CommandConf;
import jdepend.core.local.config.CommandConfMgr;
import jdepend.framework.exception.JDependException;
import jdepend.model.component.modelconf.ComponentModelConf;

public final class DefaultNewGroupWorker implements NewGroupWorker {

	private String groupName;

	private String path;

	private String srcPath;

	private List<String> filteredPackages;

	private String attribute;

	private Map<String, ComponentModelConf> componentModels = new LinkedHashMap<String, ComponentModelConf>();

	private List<CommandConf> commandInfos = new ArrayList<CommandConf>();

	public void create() throws JDependException {
		CommandConfMgr.getInstance().createGroup(groupName, path, srcPath, filteredPackages, attribute, commandInfos,
				componentModels);
	}

	public Step getNextStep(Step current) {
		return null;
	}

	public Step getPriStep(Step current) {
		return null;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ComponentModelConf getComponentModel(String componentModelName) {
		return componentModels.get(componentModelName);
	}

	public List<String> getComponentModels() {
		return new ArrayList<String>(componentModels.keySet());
	}

	public void addComponentModel(ComponentModelConf components) {
		this.componentModels.put(components.getName(), components);
	}

	public List<CommandConf> getCommandInfos() {
		return commandInfos;
	}

	public void setCommandInfos(List<CommandConf> commandInfos) {
		this.commandInfos = commandInfos;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder(500);

		content.append("Group Name:");
		content.append(groupName);
		content.append("\n\n");

		content.append("path:");
		content.append(path);
		content.append("\n\n");

		content.append("srcPath:");
		content.append(srcPath);
		content.append("\n\n");

		content.append("filteredPackages:\n");
		for (String filteredPackage : filteredPackages) {
			content.append(tab(1) + filteredPackage);
			content.append("\n");
		}
		content.append("\n\n");

		if (this.attribute != null) {
			content.append("Attribute:");
			content.append(attribute);
			content.append("\n\n");
		}

		content.append("componentModels:\n\n");
		if (componentModels == null || componentModels.size() == 0) {
			content.append(tab(1) + "以包为默认组件。\n");
		} else {
			for (ComponentModelConf componentModelConf : this.componentModels.values()) {
				content.append(componentModelConf);
				content.append("\n");
			}
		}
		content.append("\n");

		content.append("commands:\n\n");
		for (CommandConf commandInfo : commandInfos) {
			content.append(commandInfo);
		}
		content.append("\n");

		return content.toString();
	}

	private String tab(int n) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < n; i++) {
			s.append("    ");
		}

		return s.toString();
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public void setFilteredPackages(List<String> filteredPackages) {
		this.filteredPackages = filteredPackages;

	}

	public List<String> getFilteredPackages() {
		return filteredPackages;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
