package jdepend.model;

/**
 * 可以被分数描述的
 * 
 * @author user
 * 
 */
public interface Scored {

	public static final String NoValue = "--";
	/**
	 * 总分
	 * 
	 * @return
	 */
	public Float getScore();

	/**
	 * 抽象程度合理性得分
	 * 
	 * @return
	 */
	public Float getDistance();

	/**
	 * 内聚性得分
	 * 
	 * @return
	 */
	public Float getBalance();

	/**
	 * 封装性得分
	 * 
	 * @return
	 */
	public Float getEncapsulation();

	/**
	 * 关系合理性得分
	 * 
	 * @return
	 */
	public Float getRelationRationality();

}
