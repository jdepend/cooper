package jdepend.model;

/**
 * 用于计算JDependUnit.Balance 的子元素
 * 
 * @author user
 * 
 */
public interface SubJDependUnit extends Named {

	/**
	 * 内聚值
	 * 
	 * @return
	 */
	public float getCohesion();

	/**
	 * 内聚性（0~1）
	 * 
	 * @return
	 */
	public float getBalance();

	/**
	 * 获取分组耦合信息
	 * 
	 * @return
	 */
	public GroupCouplingInfo getGroupCouplingInfo();

}
