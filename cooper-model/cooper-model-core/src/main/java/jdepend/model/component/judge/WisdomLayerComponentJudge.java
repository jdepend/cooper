package jdepend.model.component.judge;

import jdepend.metadata.tree.JavaPackageNode;

public final class WisdomLayerComponentJudge implements ComponentJudge {

	@Override
	public boolean isComponent(JavaPackageNode node) {
		// 当包没有子包时，并且本身含有Class时，该包是组件
		if (node.getChildren().size() == 0 & node.isExistSelfJavaClass()) {
			return true;
		}
		// 当包本身不含有Class,并且仅仅存在一个子包时，该包不是组件
		if (node.getChildren().size() == 1 && !node.isExistSelfJavaClass()) {
			return false;
		}

		if (node.getLayer() == -1 || node.getLayer() == 0) {
			return false;
		}
		boolean is = true;
		int subComponentCount;
		for (JavaPackageNode child : node.getChildren()) {
			// 计算含有3个或以上子包的包数量
			subComponentCount = 0;
			if (child.getChildren().size() > 2) {
				subComponentCount++;
			}
			// 当规模较大的子包数量超过0，判断结论是本包不是组件
			if (subComponentCount > 0) {
				is = false;
				break;
			}
		}
		return is;
	}

}
