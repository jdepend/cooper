package jdepend.service.avertcheat.framework;

import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisResult;
import jdepend.service.local.AbstractAnalyseListener;
import jdepend.service.local.AnalyseListener;

/**
 * 基于类的防作弊器基类
 * 
 * @author wangdg
 * 
 */
public abstract class JavaClassAvertCheat extends AbstractAnalyseListener
		implements AvertCheat {

	@Override
	public void onAnalyse(AnalysisResult result) {

		for (JavaClass javaClass : result.getClasses()) {
			this.handle(javaClass);
		}
	}

	protected abstract void handle(JavaClass javaClass);

}
