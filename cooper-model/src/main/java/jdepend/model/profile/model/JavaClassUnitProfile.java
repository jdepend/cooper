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
	
	public String getExplain() {
		StringBuilder info = new StringBuilder();

		info.append("类的抽象性是提供系统可扩展性的手段之一，并作为计算所属组件的抽象性依据。\n\n");
		info.append("判断类的抽象性有两种方式：一种是只看形式，只要该类为接口或抽象类，该类即具有抽象性；另一种是通过该类与其他类之间的关系来判断该类的抽象性。系统提供了多种规则来判断该类的抽象性，当不选择任何规则时，就以形式化方式来判断该类的抽象性。\n\n");
		

		return info.toString();
	}
}
