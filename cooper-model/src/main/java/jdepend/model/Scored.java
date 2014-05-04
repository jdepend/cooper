package jdepend.model;

/**
 * 可以被分数描述的
 * 
 * @author user
 *
 */
public interface Scored {
	
	/**
	 * 总分
	 * 
	 * @return
	 */
	public float calScore();
	
	/**
	 * 抽象程度合理性得分
	 * 
	 * @return
	 */
	public float calD();
	
	/**
	 * 内聚性得分
	 * 
	 * @return
	 */
	public float calBalance();
	
	/**
	 * 封装性得分
	 * 
	 * @return
	 */
	public float calEncapsulation();
	
	/**
	 * 关系合理性得分
	 * 
	 * @return
	 */
	public float calRelationRationality();

}
