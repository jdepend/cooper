package jdepend.service.avertcheat.stabilityClassIdentifyer;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.JavaClass;
import jdepend.metadata.Method;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.service.avertcheat.framework.JavaClassAvertCheat;

public final class StabilityClassIdentifyer extends JavaClassAvertCheat {

	@Override
	public String getName() {
		return "判断指定的类是否稳定";
	}

	@Override
	public String getTip() {
		return "XXXUtil,XXXException、简单VO等都具有相当的稳定性，对它们的依赖应该不敏感";
	}

	@Override
	public boolean enable(AnalysisRunningContext context) {
		return context.getProfileFacade().getJavaClassUnitProfile().isStableRule();
	}

	@Override
	protected void handle(JavaClassUnit javaClassUnit) {
		boolean stability = true;
		// 类只有static的方法
		for (Method method : javaClassUnit.getJavaClass().getSelfMethods()) {
			if (!method.isConstruction() && !method.isStatic()) {
				stability = false;
				break;
			}
		}
		if (stability) {
			javaClassUnit.setStable(true);
			return;
		}

		// 不依赖其他类的VO
		if (javaClassUnit.getJavaClass().getCeList().size() == 0
				&& javaClassUnit.getJavaClass().getClassType().equals(JavaClass.VO_TYPE)) {
			if (!javaClassUnit.getJavaClass().haveBusinessMethod()) {
				javaClassUnit.setStable(true);
				return;
			}
		}
	}

	@Override
	public Integer order() {
		return 1;
	}
}
