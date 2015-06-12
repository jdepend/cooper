package jdepend.client.ui.wizard;

import java.util.List;

import jdepend.client.core.local.config.CommandConf;
import jdepend.framework.exception.JDependException;
import jdepend.model.component.modelconf.ComponentModelConf;

public interface NewGroupWorker {

	public void create() throws JDependException;

	public Step getNextStep(Step current);

	public Step getPriStep(Step current);

	public String getGroupName();

	public void setGroupName(String groupName);

	public String getPath();

	public void setPath(String path);

	public String getSrcPath();

	public void setSrcPath(String srcPath);

	public ComponentModelConf getComponentModel(String componentGroup);

	public void addComponentModel(ComponentModelConf components);

	public List<String> getComponentModels();

	public List<CommandConf> getCommandInfos();

	public void setCommandInfos(List<CommandConf> commandInfos);

	public void setFilteredPackages(List<String> filteredPackages);

	public String getAttribute();

	public void setAttribute(String attribute);
}
