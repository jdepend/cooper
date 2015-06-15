package jdepend.service.avertcheat.stabilityClassIdentifyer;

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
		return true;
	}

	@Override
	protected void handle(JavaClassUnit javaClass) {
		if (!javaClass.getJavaClass().isInnerClass()) {
			boolean stability = true;
			for (Method method : javaClass.getJavaClass().getSelfMethods()) {
				if (!method.isConstruction() && !method.isStatic()) {
					stability = false;
					break;
				}
			}
			if (stability) {
				javaClass.setStable(true);
				return;
			}
			if (javaClass.getJavaClass().getCeList().size() == 0
					&& javaClass.getJavaClass().getClassType().equals(JavaClass.VO_TYPE)) {
				boolean haveBusinessMethod = false;
				O: for (Method method : javaClass.getJavaClass().getMethods()) {
					if (!method.isConstruction() && !method.getName().startsWith("get")
							&& !method.getName().startsWith("set") && !method.getName().equals("toString")
							&& !method.getName().equals("equals") && !method.getName().equals("hashCode")) {
						haveBusinessMethod = true;
						break O;
					}
				}
				if (!haveBusinessMethod) {
					javaClass.setStable(true);
					return;
				}
			}
		}
	}

	@Override
	public Integer order() {
		return 1;
	}
}
