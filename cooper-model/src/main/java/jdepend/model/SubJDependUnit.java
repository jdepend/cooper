package jdepend.model;

/**
 * 用于计算JDependUnit.Balance 的子元素
 * 
 * @author user
 * 
 */
public interface SubJDependUnit extends JDependUnit {
	/**
	 * 获取分组耦合信息
	 * 
	 * @return
	 */
	public GroupCouplingInfo getGroupCouplingInfo();

}
