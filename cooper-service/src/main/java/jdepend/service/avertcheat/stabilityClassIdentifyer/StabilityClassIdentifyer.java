package jdepend.service.avertcheat.stabilityClassIdentifyer;

import jdepend.model.JavaClass;
import jdepend.model.Method;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.service.avertcheat.framework.JavaClassAvertCheat;

public final class StabilityClassIdentifyer extends JavaClassAvertCheat {

	@Override
	public String getName() {
		return "判断指定的类是否稳定";
	}

	@Override
	public String getTip() {
		return "XXXUtil,XXXException等都具有相当的稳定性，对它们的依赖应该不敏感";
	}

	@Override
	public boolean enable(AnalysisRunningContext context) {
		return true;
	}

	@Override
	protected void handle(JavaClass javaClass) {
		if (!javaClass.isInnerClass()) {
			boolean stability = true;
			for (Method method : javaClass.getSelfMethods()) {
				if (!method.isConstruction() && !method.isStatic()) {
					stability = false;
					break;
				}
			}
			if (stability) {
				javaClass.setStable(true);
			}
		}
	}

	@Override
	public Integer order() {
		return 1;
	}
}
