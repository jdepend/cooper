package jdepend.model.component.judge;

import jdepend.model.tree.JavaPackageNode;

/**
 * 根据Layer判定是否为组件
 * 
 * @author ibmuser
 * 
 */
public final class LayerComponentJudge implements ComponentJudge {

	private Integer layer;

	public LayerComponentJudge(Integer layer) {
		super();
		this.layer = layer;
	}

	public boolean isComponent(JavaPackageNode node) {
		if (node.getLayer() == layer) {
			return true;
		}
		if (node.getLayer() < layer && node.getChildren().size() == 0 && node.isExistSelfJavaClass()) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + layer;
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
		LayerComponentJudge other = (LayerComponentJudge) obj;
		if (layer != other.layer)
			return false;
		return true;
	}

}
