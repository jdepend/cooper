package jdepend.model.component.judge;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.tree.JavaPackageNode;

/**
 * 根据子包名判定父包是否为组件
 * 
 * @author ibmuser
 * 
 */
public final class ChildrenComponentJudge implements ComponentJudge {

	private List<String> keys = new ArrayList<String>();

	public boolean isComponent(JavaPackageNode node) {
		for (JavaPackageNode child : node.getChildren()) {
			if (keys.contains(child.getName())) {
				return true;
			}
		}
		return false;
	}

	public void addChildrenKey(String key) {
		this.keys.add(key);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keys == null) ? 0 : keys.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChildrenComponentJudge other = (ChildrenComponentJudge) obj;
		if (keys == null) {
			if (other.keys != null)
				return false;
		} else if (!keys.equals(other.keys))
			return false;
		return true;
	}
}
