package jdepend.metadata;

import java.io.Serializable;

import jdepend.metadata.relationtype.JavaClassRelationTypes;
import jdepend.metadata.util.JavaClassCollection;

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
	 * 创建关系对象
	 * 
	 * @param javaClass
	 * @param javaClasses
	 * @return
	 */
	public boolean create(JavaClass javaClass, JavaClassCollection javaClasses);

	/**
	 * 是否可以在关系上采用抽象化技术
	 * 
	 * @return
	 */
	public boolean canAbstraction();

	/**
	 * 是否与方法调用相关
	 * 
	 * @return
	 */
	public boolean invokeRelated();

	/**
	 * 设置类型集合
	 * 
	 * @param types
	 */
	public void setTypes(JavaClassRelationTypes types);

	/**
	 * 得到类型集合
	 * 
	 * @return
	 */
	public JavaClassRelationTypes getTypes();

}
