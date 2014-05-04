package jdepend.model;

/**
 * 可以被指标评价的
 * 
 * @author user
 * 
 */
public interface Measurable extends Named {

	/**
	 * 标题
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * 代码行数
	 * 
	 * @return
	 */
	public int getLineCount();

	/**
	 * 类数量
	 * 
	 * @return
	 */
	public int getClassCount();

	/**
	 * 具有抽象类计数资格的类个数
	 * 
	 * @return
	 */
	public int getAbstractClassCount();

	/**
	 * 具体类个数
	 * 
	 * @return
	 */
	public int getConcreteClassCount();

	/**
	 * 抽象性（0~1）
	 * 
	 * @return
	 */
	public float abstractness();

	/**
	 * 易变性(0~1)
	 * 
	 * @return
	 */
	public float volatility();

	/**
	 * 传入数量
	 * 
	 * @return
	 */
	public int afferentCoupling();

	/**
	 * 传出数量
	 * 
	 * @return
	 */
	public int efferentCoupling();

	/**
	 * 耦合值
	 * 
	 * @return
	 */
	public float coupling();

	/**
	 * 内聚值
	 * 
	 * @return
	 */
	public float cohesion();

	/**
	 * 稳定性（0~1）
	 * 
	 * @return
	 */
	public float stability();

	/**
	 * 抽象程度合理性（0~1）
	 * 
	 * @return
	 */
	public float distance();

	/**
	 * 内聚性指数（0~1）
	 * 
	 * @return
	 */
	public float balance();

	/**
	 * 封装性（0~1）
	 * 
	 * @return
	 */
	public float encapsulation();

	/**
	 * 面向对象指数（0~）
	 * 
	 * @return
	 */
	public float objectOriented();

	/**
	 * 是否存在循环依赖
	 * 
	 * @return
	 */
	public boolean containsCycle();

	/**
	 * 扩展指标
	 * 
	 * @param metrics
	 * @return
	 */
	public MetricsInfo extendMetrics(String metrics);
	
	/**
	 * 得到指定指标的值
	 * 
	 * @param metrics
	 * @return
	 */
	public Object getValue(String metrics);

}
