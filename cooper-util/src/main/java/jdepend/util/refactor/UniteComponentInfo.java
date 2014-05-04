package jdepend.util.refactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;

final class UniteComponentInfo {

	private List<UniteComponentConf> uniteComponentConfs = new ArrayList<UniteComponentConf>();

	public UniteComponentConf add(String name, int layer, Collection<String> components) throws JDependException {

		UniteComponentConf uniteComponentConf = new UniteComponentConf(name, layer, components);
		if (!this.uniteComponentConfs.contains(uniteComponentConf)) {
			for (UniteComponentConf obj : uniteComponentConfs) {
				for (String component : components) {
					if (obj.getComponents().contains(component)) {
						throw new JDependException("增加组合组件[" + name + "]出错[组件重复错误]，组合组件[" + obj.getName() + "]已包含组件["
								+ component + "]");
					}
				}
			}
			this.uniteComponentConfs.add(uniteComponentConf);
			return uniteComponentConf;
		} else {
			throw new JDependException("增加组合组件[" + name + "]出错[组合组件名重复错误]");
		}
	}

	public void clear() {
		this.uniteComponentConfs = new ArrayList<UniteComponentConf>();
	}

	public List<UniteComponentConf> getUniteComponentConfs() {
		return uniteComponentConfs;
	}
}
