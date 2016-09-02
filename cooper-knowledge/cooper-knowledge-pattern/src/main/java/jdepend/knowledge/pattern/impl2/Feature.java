package jdepend.knowledge.pattern.impl2;

import jdepend.metadata.JavaClass;

/**
 * 特征识别器
 * 
 * @author user
 *
 */
public interface Feature {

	public void check(FeatureCheckContext context);

	public JavaClass getCurrent();

	public String getName();

	public String getPatternInfo();

	public void setPatternInfo(String patternInfo);

	public void addIdentifyer(Identifyer identifyer);
}
