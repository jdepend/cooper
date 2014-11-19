package jdepend.service.avertcheat.collectcycle;

import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.service.avertcheat.framework.JavaClassAvertCheat;

public final class CollectCycleUtil extends JavaClassAvertCheat {

	@Override
	public String getName() {
		return "收集循环依赖信息";
	}

	@Override
	public String getTip() {
		return "在多线程环境下先期计算循环依赖信息";
	}

	@Override
	public boolean enable(AnalysisRunningContext context) {
		return true;
	}

	@Override
	protected void handle(JavaClass javaClass) {
		javaClass.collectCycle();
	}

	@Override
	public Integer order() {
		return 3;
	}
}
