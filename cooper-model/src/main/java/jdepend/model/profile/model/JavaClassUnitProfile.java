package jdepend.model.profile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JavaClassUnitProfile implements Serializable {

	private static final long serialVersionUID = -4336618115325941714L;

	private List<String> abstractClassRules;

	public final static String AbstractClassRule_ChildCount = "子类数量大于1";
	public final static String AbstractClassRule_SuperAndChild = "存在子类，又存在父类";
	public final static String AbstractClassRule_ChildAtOtherComponent = "子类不在一个组件中";

	private List<String> stableRules;

	public final static String StableRule_HaveOnlyStaticMethod = "类只有static的方法";
	public final static String StableRule_SimpleVO = "不依赖其他类的VO";

	public static List<String> getAllAbstractClassRules() {

		List<String> allAbstractClassRules = new ArrayList<String>();
		allAbstractClassRules.add(AbstractClassRule_ChildCount);
		allAbstractClassRules.add(AbstractClassRule_SuperAndChild);
		allAbstractClassRules.add(AbstractClassRule_ChildAtOtherComponent);

		return allAbstractClassRules;
	}

	public static List<String> getAllStableRules() {

		List<String> allStableRules = new ArrayList<String>();
		allStableRules.add(StableRule_HaveOnlyStaticMethod);
		allStableRules.add(StableRule_SimpleVO);

		return allStableRules;
	}

	public List<String> getAbstractClassRules() {
		return abstractClassRules;
	}

	public void setAbstractClassRules(List<String> abstractClassRules) {
		this.abstractClassRules = abstractClassRules;
	}

	public boolean isAbstractClassRule() {
		return this.abstractClassRules != null && this.abstractClassRules.size() > 0;
	}

	public List<String> getStableRules() {
		return stableRules;
	}

	public void setStableRules(List<String> stableRules) {
		this.stableRules = stableRules;
	}

	public boolean isStableRule() {
		return this.stableRules != null && this.stableRules.size() > 0;
	}
}
