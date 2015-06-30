package jdepend.parse.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import jdepend.framework.log.LogUtil;
import jdepend.framework.util.ThreadPool;
import jdepend.metadata.HttpInvokeItem;
import jdepend.metadata.InvokeItem;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassDetail;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.JavaClassRelationType;
import jdepend.metadata.Method;
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
		final Collection<String> createRelationTypes = this.conf.getCreateRelationTypes();

		ExecutorService pool = ThreadPool.getPool();

		for (final String unit : javaClasses.getUnitJavaClasses().keySet()) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					JavaClassDetail info = null;
					JavaClass dependJavaClass = null;
					Collection<String> returnTypes;

					for (JavaClass javaClass : javaClasses.getUnitJavaClasses().get(unit)) {

						if (javaClass.isInner()) {
							LogUtil.getInstance(JavaClassRelationCreator.class).systemLog(
									"开始建立Class的关系:" + javaClass.getName());

							info = javaClass.getDetail();
							// 处理父类
							if (createRelationTypes.contains(JavaClassRelationTypes.Inherit)) {
								if (info.getSuperClass() != null) {
									setDependInfo(javaClass, info.getSuperClass(),
											javaClassRelationTypes.getInheritRelation());
								}
							}

							// 处理接口
							if (createRelationTypes.contains(JavaClassRelationTypes.Inherit)
									&& info.getInterfaceNames().size() != 0) {
								for (JavaClass interfaceClass : info.getInterfaces()) {
									setDependInfo(javaClass, interfaceClass,
											javaClassRelationTypes.getInheritRelation());
								}
							}

							// 处理属性
							// 1.收集该类的返回值类型
							returnTypes = new HashSet<String>();
							for (Method method : javaClass.getSelfMethods()) {
								for (String returnType : method.getReturnTypes()) {
									if (!returnTypes.contains(returnType)) {
										returnTypes.add(returnType);
									}
								}
							}
							// 2.建立包含或者调用关系
							if (createRelationTypes.contains(JavaClassRelationTypes.Field)
									&& info.getAttributeClasses().size() != 0) {
								for (JavaClass attributeClass : info.getAttributeClasses()) {
									// 分析该属性是包含关系还是调用关系
									if (returnTypes.contains(attributeClass.getName())) {
										setDependInfo(javaClass, attributeClass,
												javaClassRelationTypes.getFieldRelation());
									} else {
										setDependInfo(javaClass, attributeClass,
												javaClassRelationTypes.getVariableRelation());
									}
								}
							}

							// 处理参数
							if (createRelationTypes.contains(JavaClassRelationTypes.Param)
									&& info.getParamTypes().size() != 0) {
								for (String paramType : info.getParamTypes()) {
									dependJavaClass = javaClasses.getTheClass(javaClass.getPlace(), paramType);
									setDependInfo(javaClass, dependJavaClass, javaClassRelationTypes.getParamRelation());
								}
							}

							// 处理变量
							if (createRelationTypes.contains(JavaClassRelationTypes.Variable)
									&& info.getVariableTypes().size() != 0) {
								for (String variableType : info.getVariableTypes()) {
									dependJavaClass = javaClasses.getTheClass(javaClass.getPlace(), variableType);
									setDependInfo(javaClass, dependJavaClass,
											javaClassRelationTypes.getVariableRelation());
								}
							}
							// 处理Table关系
							if (createRelationTypes.contains(JavaClassRelationTypes.Table)
									&& info.getTables().size() != 0) {
								for (TableInfo tableInfo : info.getTables()) {
									if (!tableInfo.isDefine()) {
										// 判断是否忽略指定表的关系建立
										if (!javaClassRelationTypes.isIgnoreTableInfo(tableInfo)) {
											List<JavaClass> dependJavaClasses = getWriteAndDefineToTableClasses(tableInfo);
											for (JavaClass dependJavaClass1 : dependJavaClasses) {
												setDependInfo(javaClass, dependJavaClass1, javaClassRelationTypes
														.getTableRelation().clone(tableInfo.getTableName()));
											}
										}
									}
								}
							}
							// 处理Http调用
							if (createRelationTypes.contains(JavaClassRelationTypes.Http) && info.isHttpCaller()) {
								// 收集Http调用的类集合
								Collection<JavaClass> dependClasses = new HashSet<JavaClass>();
								for (Method method : javaClass.getSelfMethods()) {
									for (InvokeItem invokeItem : method.getInvokeItems()) {
										if (invokeItem instanceof HttpInvokeItem) {
											if (!dependClasses.contains(invokeItem.getCallee().getJavaClass())) {
												dependClasses.add(invokeItem.getCallee().getJavaClass());
											}
										}
									}
								}
								// 建立Http类关系
								for (JavaClass dependClass : dependClasses) {
									setDependInfo(javaClass, dependClass, javaClassRelationTypes.getHttpRelation());
								}
							}
						}
					}
				}
			});
		}

		ThreadPool.awaitTermination(pool);
	}

	private void setDependInfo(JavaClass source, JavaClass target, JavaClassRelationType type) {

		if (target == null) {
			return;
		}

		if (source.equals(target)) {
			return;
		}

		if (source.containsInnerClass(target)) {
			return;
		}

		if (source.containedInnerClass(target)) {
			return;
		}

		JavaClassRelationItem item = new JavaClassRelationItem();
		item.setType(type);
		item.setTarget(target);
		item.setSource(source);

		source.addCeItems(item);
		target.addCaItems(item);
	}

	private List<JavaClass> getWriteAndDefineToTableClasses(TableInfo tableInfo) {

		List<JavaClass> rtn = new ArrayList<JavaClass>();
		for (JavaClass javaClass : this.javaClasses.getJavaClasses()) {
			for (TableInfo currentTableInfo : javaClass.getDetail().getTables()) {
				if (currentTableInfo.getTableName().equalsIgnoreCase(tableInfo.getTableName())
						&& (currentTableInfo.isWrite()// 目标为写
						|| currentTableInfo.isDefine())// 目标为定义
				) {
					rtn.add(javaClass);
				}
			}
		}
		return rtn;
	}
}
