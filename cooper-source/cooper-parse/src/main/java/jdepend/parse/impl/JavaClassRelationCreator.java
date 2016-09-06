package jdepend.parse.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import jdepend.framework.log.LogUtil;
import jdepend.framework.util.ThreadPool;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationType;
import jdepend.metadata.TableInfo;
import jdepend.metadata.relationtype.JavaClassRelationTypes;
import jdepend.metadata.util.JavaClassCollection;
import jdepend.parse.ParseConfigurator;

public class JavaClassRelationCreator {

	private ParseConfigurator conf;

	private JavaClassCollection javaClasses;

	public JavaClassRelationCreator(ParseConfigurator conf) {
		super();
		this.conf = conf;
	}

	private void init(JavaClassCollection javaClasses) {

		this.javaClasses = javaClasses;

		Map<String, String> entryMapTableName = new HashMap<String, String>();
		String littleClassName;

		for (JavaClass javaClass : this.javaClasses.getJavaClasses()) {
			// 收集Entry和TableName对应关系信息
			L: for (TableInfo tableInfo : javaClass.getDetail().getTables()) {
				if (tableInfo.isDefine()) {
					littleClassName = javaClass.getName().substring(javaClass.getName().lastIndexOf('.') + 1)
							.toUpperCase();
					entryMapTableName.put(littleClassName, tableInfo.getTableName());

					break L;
				}
			}
		}
		// 更新TableName
		if (entryMapTableName.size() > 0) {
			for (JavaClass javaClass : this.javaClasses.getJavaClasses()) {
				for (TableInfo tableInfo : javaClass.getDetail().getTables()) {
					if (!tableInfo.isDefine()) {
						if (entryMapTableName.containsKey(tableInfo.getTableName())) {
							tableInfo.setTableName(entryMapTableName.get(tableInfo.getTableName()));
						}
					}
				}
			}
		}

	}

	public void create(final JavaClassCollection javaClasses) {

		this.init(javaClasses);

		final JavaClassRelationTypes javaClassRelationTypes = this.conf.getJavaClassRelationTypes();

		ExecutorService pool = ThreadPool.getPool();

		for (final String unit : javaClasses.getUnitJavaClasses().keySet()) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					for (JavaClass javaClass : javaClasses.getUnitJavaClasses().get(unit)) {

						if (javaClass.isInner()) {
							LogUtil.getInstance(JavaClassRelationCreator.class).systemLog(
									"开始建立Class的关系:" + javaClass.getName());
							
							for(JavaClassRelationType javaClassRelationType : javaClassRelationTypes.getTypes().values()){
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
