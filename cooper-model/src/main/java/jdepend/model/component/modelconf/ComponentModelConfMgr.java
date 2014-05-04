package jdepend.model.component.modelconf;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 组件模型配置信息管理器
 * 
 * @author <b>Abner</b>
 * 
 */
public class ComponentModelConfMgr {

	private static ComponentModelConfMgr mgr = new ComponentModelConfMgr();

	private List<GroupComponentModelConf> groupComponentModelConfs = new ArrayList<GroupComponentModelConf>();

	private ComponentModelConfMgr() {
	}

	public static ComponentModelConfMgr getInstance() {
		return mgr;
	}

	public void init() {
		this.groupComponentModelConfs = new ArrayList<GroupComponentModelConf>();
	}

	public void addComponentModelConf(GroupComponentModelConf groupComponentModelConf) {
		if (!groupComponentModelConfs.contains(groupComponentModelConf)) {
			groupComponentModelConfs.add(groupComponentModelConf);
		}
	}

	/**
	 * 得到指定组下的特定组件模型配置信息
	 * 
	 * @param group
	 * @param name
	 * @return
	 */
	public ComponentModelConf getTheComponentModelConf(String group, String modelName) {
		for (GroupComponentModelConf groupComponent : groupComponentModelConfs) {
			if (groupComponent.getGroup().equals(group)) {
				return groupComponent.getComponentModelConfs().get(modelName);
			}
		}
		return null;
	}

	/**
	 * 得到指定组的组件模型配置信息
	 * 
	 * @param group
	 * @param name
	 * @return
	 */
	public GroupComponentModelConf getTheGroupComponentModelConf(String group) {
		for (GroupComponentModelConf groupComponent : groupComponentModelConfs) {
			if (groupComponent.getGroup().equals(group)) {
				return groupComponent;
			}
		}
		return null;
	}
}
