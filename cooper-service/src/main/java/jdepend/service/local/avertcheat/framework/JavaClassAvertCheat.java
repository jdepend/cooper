package jdepend.service.local.avertcheat.framework;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import jdepend.framework.util.ThreadPool;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassCollection;
import jdepend.service.local.AbstractAnalyseListener;

/**
 * 基于类的防作弊器基类
 * 
 * @author wangdg
 * 
 */
public abstract class JavaClassAvertCheat extends AbstractAnalyseListener implements AvertCheat {

	@Override
	public void onAnalyse(AnalysisResult result) {

		ExecutorService pool = ThreadPool.getPool();

		final Map<String, Collection<JavaClassUnit>> unitJavaClasses = JavaClassCollection.unitTheadClassCollection(result
				.getClasses());

		for (final String unit : unitJavaClasses.keySet()) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					for (JavaClassUnit javaClass : unitJavaClasses.get(unit)) {
						handle(javaClass);
					}
				}
			});
		}

		ThreadPool.awaitTermination(pool);
	}

	protected abstract void handle(JavaClassUnit javaClass);

}
