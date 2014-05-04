package jdepend.model;

import java.io.Serializable;

/**
 * JDependUnit 类型接口
 * 
 * @author <b>Abner</b>
 * 
 */
public interface JDependUnitType extends Serializable {

	/**
	 * 判断unit是否是本类型的
	 * 
	 * @param unit
	 * @return
	 */
	public boolean isMember(JDependUnit unit);

	/**
	 * 返回类型名称
	 * 
	 * @return
	 */
	public String getName();
}
