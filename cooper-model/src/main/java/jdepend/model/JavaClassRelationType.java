package jdepend.model;

import java.io.Serializable;

/**
 * 依赖类型
 * 
 * @author <b>Abner</b>
 * 
 */
public interface JavaClassRelationType extends Serializable {

	/**
	 * 得到依赖类型名称
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 得到类型本身的依赖强度
	 * 
	 * @return
	 */
	public float getIntensity();

	/**
	 * 得到在该依赖类型下，依赖的合理性
	 * 
	 * @param depend
	 *            被依赖者
	 * @param current
	 *            依赖者
	 * @param direction
	 *            依赖方向
	 * @return
	 */
	public float getRationality(JavaClass depend, JavaClass current, String direction);

	/**
	 * 是否可以在关系上采用抽象化技术
	 * 
	 * @return
	 */
	public boolean canAbstraction();

}
