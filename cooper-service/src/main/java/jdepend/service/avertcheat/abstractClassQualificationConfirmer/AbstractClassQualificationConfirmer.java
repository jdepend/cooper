package jdepend.service.avertcheat.abstractClassQualificationConfirmer;

import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.service.avertcheat.framework.JavaClassAvertCheat;

public final class AbstractClassQualificationConfirmer extends JavaClassAvertCheat {
	@Override
	protected void handle(JavaClass javaClass) {
		if (QualificationConfirmerMgr.getIntance().getConfirmer().confirmAbstractClassCount(javaClass)) {
			javaClass.setAbstractClassQualification(true);
		} else {
			javaClass.setAbstractClassQualification(false);
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
