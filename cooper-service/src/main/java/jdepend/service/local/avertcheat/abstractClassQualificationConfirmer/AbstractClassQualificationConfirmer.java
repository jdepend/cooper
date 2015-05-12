package jdepend.service.local.avertcheat.abstractClassQualificationConfirmer;

import java.util.Collection;

import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.service.local.avertcheat.framework.JavaClassAvertCheat;

public final class AbstractClassQualificationConfirmer extends JavaClassAvertCheat {

	private static int ChildJavaClassCount = 2;

	@Override
	protected void handle(JavaClassUnit javaClass) {
		if (this.confirmAbstractQualification(javaClass)) {
			javaClass.setAbstractClassQualification(true);
		} else {
			javaClass.setAbstractClassQualification(false);
		}
	}

	private boolean confirmAbstractQualification(JavaClassUnit javaClass) {
		// if (!javaClass.isAbstract())
		// return false;

		Collection<JavaClassUnit> subClasses = javaClass.getSubClasses();
		if (subClasses.size() >= ChildJavaClassCount) {
			return true;
		} else {
			// 存在一个子类，又存在父类也具备抽象类计数资格
			if (subClasses.size() >= 1 && javaClass.getSupers().size() > 0) {
				return true;
			}
			// 子类不在一个组件中也具备抽象类计数资格
			for (JavaClassUnit subClass : subClasses) {
				if (!subClass.containedComponent() || !subClass.getComponent().equals(javaClass.getComponent())) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public boolean enable(AnalysisRunningContext context) {
		return context.isEnableAbstractClassCountQualificationConfirmer();
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
