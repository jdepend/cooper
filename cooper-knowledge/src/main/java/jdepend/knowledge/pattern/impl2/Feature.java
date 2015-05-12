package jdepend.knowledge.pattern.impl2;

import jdepend.model.JavaClassUnit;

/**
 * 特征识别器
 * 
 * @author user
 *
 */
public interface Feature {

	public void check(FeatureCheckContext context);

	public JavaClassUnit getCurrent();

	public String getName();

	public String getPatternInfo();

	public void setPatternInfo(String patternInfo);

	public void addIdentifyer(Identifyer identifyer);
}
