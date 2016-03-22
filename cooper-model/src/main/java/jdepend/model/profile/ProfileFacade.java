package jdepend.model.profile;

import jdepend.metadata.profile.JavaClassRelationItemProfile;
import jdepend.model.profile.model.AnalysisResultProfile;
import jdepend.model.profile.model.AreaComponentProfile;
import jdepend.model.profile.model.ComponentProfile;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.profile.model.RelationProfile;

/**
 * 获取系统计算分数结果时用到的规则配置信息
 * 
 * @author Administrator
 *
 */
public interface ProfileFacade {

	public AnalysisResultProfile getAnalysisResultProfile();

	public AreaComponentProfile getAreaComponentProfile();

	public ComponentProfile getComponentProfile();

	public RelationProfile getRelationProfile();

	public JavaClassUnitProfile getJavaClassUnitProfile();

	public JavaClassRelationItemProfile getJavaClassRelationItemProfile();
}
