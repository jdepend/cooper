package jdepend.service.avertcheat.abstractClassQualificationConfirmer;

import java.util.Collection;
import java.util.List;

import jdepend.metadata.JavaClass;
import jdepend.model.JavaClassUnit;
import jdepend.model.profile.model.JavaClassUnitProfile;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.service.avertcheat.framework.JavaClassAvertCheat;

public final class AbstractClassQualificationConfirmer extends JavaClassAvertCheat {

	private static int ChildJavaClassCount = 1;

	@Override
	protected void handle(JavaClassUnit javaClass) {
		if (this.confirmAbstractQualification(javaClass)) {
			javaClass.setAbstractClassQualification(true);
		} else {
			javaClass.setAbstractClassQualification(false);
		}
	}

	private boolean confirmAbstractQualification(JavaClassUnit javaClassUnit) {

		AnalysisResult result = javaClassUnit.getResult();
		List<String> abstractClassRules = result.getRunningContext().getProfileFacade().getJavaClassUnitProfile()
				.getAbstractClassRules();

		Collection<JavaClass> subClasses = javaClassUnit.getJavaClass().getSubClasses();
		// 子类数量大于指定数量
		if (subClasses.size() > ChildJavaClassCount
				&& abstractClassRules.contains(JavaClassUnitProfile.AbstractClassRule_ChildCount)) {
			return true;
		} else {
			// 存在一个子类，又存在父类也具备抽象类计数资格
			if (subClasses.size() > 0 && javaClassUnit.getJavaClass().getSupers().size() > 0
					&& abstractClassRules.contains(JavaClassUnitProfile.AbstractClassRule_SuperAndChild)) {
				return true;
			}
			// 子类不在一个组件中也具备抽象类计数资格
			if (abstractClassRules.contains(JavaClassUnitProfile.AbstractClassRule_ChildAtOtherComponent)) {
				for (JavaClass subClass : subClasses) {
					JavaClassUnit subClassUnit = result.getTheClass(subClass.getId());
					if (subClassUnit != null
							&& (!subClassUnit.containedComponent() || !subClassUnit.getComponent().equals(
									javaClassUnit.getComponent()))) {
						return true;
					}
				}
			}

			// 子类除实现该接口（或继承了该抽象类）外还实现或继承了其他类
			if (abstractClassRules.contains(JavaClassUnitProfile.AbstractClassRule_CooperationUse)) {
				for (JavaClass subClass : subClasses) {
					if (subClass.getSelfSupers().size() > 1) {
						return true;
					}
				}

			}
			return false;
		}
	}

	@Override
	public boolean enable(AnalysisRunningContext context) {
		return context.getProfileFacade().getJavaClassUnitProfile().isAbstractClassRule();
	}

	@Override
	public String getName() {
		return "抽象类计数资格评判器";
	}

	@Override
	public String getTip() {
		return "只有子类个数大于一定数值时，本类才具有抽象类计数资格，该判断将对组件的抽象化程度有影响";
	}

	@Override
	public Integer order() {
		return 2;
	}
}
