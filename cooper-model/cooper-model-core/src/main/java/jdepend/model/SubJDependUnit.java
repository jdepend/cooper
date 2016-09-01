package jdepend.model;

/**
 * 二级分析单元
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
	
	/**
	 * 获取分组内聚信息
	 * 
	 * @return
	 */
	public GroupCohesionInfo getGroupCohesionInfo();

}
