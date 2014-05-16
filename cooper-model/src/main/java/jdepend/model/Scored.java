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
	public float getScore();
	
	/**
	 * 抽象程度合理性得分
	 * 
	 * @return
	 */
	public float getD();
	
	/**
	 * 内聚性得分
	 * 
	 * @return
	 */
	public float getBalance();
	
	/**
	 * 封装性得分
	 * 
	 * @return
	 */
	public float getEncapsulation();
	
	/**
	 * 关系合理性得分
	 * 
	 * @return
	 */
	public float getRelationRationality();

}
