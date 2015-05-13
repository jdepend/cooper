package jdepend.knowledge.pattern.impl2;

import java.util.Map;

import jdepend.metadata.JavaClass;

/**
 * 设计模式识别器
 * 
 * @author user
 *
 */
public interface Identifyer {

	public String getName();
	
	/**
	 * 累加特征
	 * 
	 * @param feature
	 */
	public void accumulate(Feature feature);
	
	/**
	 * 注册一个该模式需要的特征
	 * 
	 * @param featureName
	 */
	public void registFeature(String featureName);

	public String getExplain();

	public Map<JavaClass, String> getResult();
	
	public void clear();

}
