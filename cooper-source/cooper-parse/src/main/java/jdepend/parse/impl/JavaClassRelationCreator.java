package jdepend.parse.impl;

import java.util.concurrent.ExecutorService;

import jdepend.framework.log.LogUtil;
import jdepend.framework.util.ThreadPool;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationType;
import jdepend.metadata.relationtype.JavaClassRelationTypes;
import jdepend.metadata.util.JavaClassCollection;
import jdepend.parse.ParseConfigurator;

public class JavaClassRelationCreator {

	private JavaClassRelationTypes javaClassRelationTypes;

	public JavaClassRelationCreator(ParseConfigurator conf) {
		super();
		javaClassRelationTypes = conf.getJavaClassRelationTypes();
	}

	private void init(JavaClassCollection javaClasses) {

		for (JavaClassRelationType javaClassRelationType : javaClassRelationTypes.getTypes().values()) {
			javaClassRelationType.init(javaClasses);
		}

	}

	public void create(final JavaClassCollection javaClasses) {

		this.init(javaClasses);

		ExecutorService pool = ThreadPool.getPool();

		for (final String unit : javaClasses.getUnitJavaClasses().keySet()) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					for (JavaClass javaClass : javaClasses.getUnitJavaClasses().get(unit)) {

						if (javaClass.isInner()) {
							LogUtil.getInstance(JavaClassRelationCreator.class).systemLog(
									"开始建立Class的关系:" + javaClass.getName());

							for (JavaClassRelationType javaClassRelationType : javaClassRelationTypes.getTypes()
									.values()) {
								javaClassRelationType.create(javaClass, javaClasses);
							}
						}
					}
				}
			});
		}

		ThreadPool.awaitTermination(pool);
	}
}
