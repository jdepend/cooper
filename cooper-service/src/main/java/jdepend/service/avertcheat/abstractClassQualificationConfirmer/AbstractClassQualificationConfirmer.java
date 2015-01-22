package jdepend.service.avertcheat.abstractClassQualificationConfirmer;

import java.util.Collection;

import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.service.avertcheat.framework.JavaClassAvertCheat;

public final class AbstractClassQualificationConfirmer extends JavaClassAvertCheat {

	private static int ChildJavaClassCount = 2;

	@Override
	protected void handle(JavaClass javaClass) {
		if (this.confirmAbstractQualification(javaClass)) {
			javaClass.setAbstractClassQualification(true);
		} else {
			javaClass.setAbstractClassQualification(false);
		}
	}

	private boolean confirmAbstractQualification(JavaClass javaClass) {
		if (!javaClass.isAbstract())
			return false;

		Collection<JavaClass> subClasses = javaClass.getSubClasses();
		if (subClasses.size() >= ChildJavaClassCount) {
			return true;
		} else {
			// 子类不在一个组件中也具备抽象类计数资格
			for (JavaClass subClass : subClasses) {
				if (subClass.getComponent() != null && javaClass.getComponent() != null
						&& !subClass.getComponent().equals(javaClass.getComponent())) {
					return true;
				}
			}
			// 存在一个子类，又存在父类也具备抽象类计数资格
			if (subClasses.size() >= 1 && javaClass.getSupers().size() > 0) {
				return true;
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
