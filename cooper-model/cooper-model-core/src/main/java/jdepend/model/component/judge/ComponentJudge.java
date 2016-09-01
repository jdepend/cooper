package jdepend.model.component.judge;

import java.io.Serializable;

import jdepend.metadata.tree.JavaPackageNode;

/**
 * 组件判定器
 * 
 * @author ibmuser
 * 
 */
public interface ComponentJudge extends Serializable {
	/**
	 * 判断指定包节点是否可以作为组件
	 * 
	 * @param node
	 * @return
	 */
	public boolean isComponent(JavaPackageNode node);
}
