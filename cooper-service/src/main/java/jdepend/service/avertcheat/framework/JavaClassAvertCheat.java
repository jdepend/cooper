package jdepend.service.avertcheat.framework;

import java.util.concurrent.ExecutorService;

import jdepend.framework.util.ThreadPool;
import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisResult;
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

		for (final JavaClass javaClass : result.getClasses()) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					handle(javaClass);
				}
			});
		}

		ThreadPool.awaitTermination(pool);
	}

	protected abstract void handle(JavaClass javaClass);

}
