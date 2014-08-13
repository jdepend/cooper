package jdepend.parse.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jdepend.framework.log.LogUtil;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassDetail;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.JavaClassRelationType;
import jdepend.model.Method;
import jdepend.model.TableInfo;
import jdepend.model.relationtype.JavaClassRelationTypeMgr;
import jdepend.parse.ParseConfigurator;

public class JavaClassRelationCreator {

	private ParseConfigurator conf;

	private Map<String, JavaClass> parsedClasses;

	private Collection<JavaClass> javaClasses;

	public JavaClassRelationCreator(ParseConfigurator conf) {
		super();
		this.conf = conf;
	}

	private void init(Map<String, JavaClass> javaClassesForName) {

		this.parsedClasses = javaClassesForName;
		this.javaClasses = javaClassesForName.values();

		Map<String, String> entryMapTableName = new HashMap<String, String>();
		String littleClassName;

		for (JavaClass javaClass : javaClasses) {
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
			for (JavaClass javaClass : javaClasses) {
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

	public void create(Map<String, JavaClass> javaClassesForName) {

		this.init(javaClassesForName);

		final JavaClassRelationTypeMgr mgr = JavaClassRelationTypeMgr.getInstance();
		final Collection<String> createRelationTypes = this.conf.getCreateRelationTypes();

		ExecutorService pool = Executors.newFixedThreadPool(4);

		for (final JavaClass javaClass : javaClasses) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					JavaClassDetail info = null;
					JavaClass dependJavaClass = null;
					Collection<String> returnTypes;

					if (javaClass.isInner()) {
						LogUtil.getInstance(JavaClassRelationCreator.class).systemLog(
								"开始建立Class的关系:" + javaClass.getName());
						info = javaClass.getDetail();
						// 处理父类
						if (createRelationTypes.contains(JavaClassRelationTypeMgr.Inherit)) {
							if (info.getSuperClassName() != null) {
								dependJavaClass = getJavaClass(info.getSuperClassName());
								setDependInfo(javaClass, dependJavaClass, mgr.getInheritRelation());
							}
						}

						// 处理接口
						if (createRelationTypes.contains(JavaClassRelationTypeMgr.Inherit)
								&& info.getInterfaceNames().size() != 0) {
							for (String interfaceName : info.getInterfaceNames()) {
								dependJavaClass = getJavaClass(interfaceName);
								setDependInfo(javaClass, dependJavaClass, mgr.getInheritRelation());
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
						if (createRelationTypes.contains(JavaClassRelationTypeMgr.Field)
								&& info.getAttributeTypes().size() != 0) {
							for (String attributeType : info.getAttributeTypes()) {
								dependJavaClass = getJavaClass(attributeType);
								// 分析该属性是包含关系还是调用关系
								if (returnTypes.contains(attributeType)) {
									setDependInfo(javaClass, dependJavaClass, mgr.getFieldRelation());
								} else {
									setDependInfo(javaClass, dependJavaClass, mgr.getVariableRelation());
								}
							}
						}

						// 处理参数
						if (createRelationTypes.contains(JavaClassRelationTypeMgr.Param)
								&& info.getParamTypes().size() != 0) {
							for (String paramType : info.getParamTypes()) {
								dependJavaClass = getJavaClass(paramType);
								setDependInfo(javaClass, dependJavaClass, mgr.getParamRelation());
							}
						}

						// 处理变量
						if (createRelationTypes.contains(JavaClassRelationTypeMgr.Variable)
								&& info.getVariableTypes().size() != 0) {
							for (String variableType : info.getVariableTypes()) {
								dependJavaClass = getJavaClass(variableType);
								setDependInfo(javaClass, dependJavaClass, mgr.getVariableRelation());
							}
						}
						// 处理Table关系
						if (createRelationTypes.contains(JavaClassRelationTypeMgr.Table)
								&& info.getTables().size() != 0) {
							for (TableInfo tableInfo : info.getTables()) {
								if (!tableInfo.isDefine()) {
									// 判断是否忽略指定表的关系建立
									if (!JavaClassRelationTypeMgr.getInstance().isIgnoreTableInfo(tableInfo)) {
										List<JavaClass> dependJavaClasses = getWriteAndDefineToTableClasses(tableInfo);
										for (JavaClass dependJavaClass1 : dependJavaClasses) {
											setDependInfo(javaClass, dependJavaClass1,
													mgr.getTableRelation().clone(tableInfo.getTableName()));
										}
									}
								}
							}
						}
					}
				}
			});
		}

		pool.shutdown();

		boolean loop = true;
		do { // 等待所有任务完成
			loop = !pool.isTerminated();
		} while (loop);

	}

	private void setDependInfo(JavaClass current, JavaClass depend, JavaClassRelationType type) {

		if (depend == null) {
			return;
		}

		if (current.equals(depend)) {
			return;
		}

		JavaClassRelationItem item = new JavaClassRelationItem();
		item.setType(type);
		item.setDirection(JavaClassRelationItem.CE_DIRECTION);
		item.setDepend(depend);
		item.setCurrent(current);
		current.addCeItems(item);

		item = new JavaClassRelationItem();
		item.setType(type);
		item.setDirection(JavaClassRelationItem.CA_DIRECTION);
		item.setDepend(current);
		item.setCurrent(depend);
		depend.addCaItems(item);
	}

	private JavaClass getJavaClass(String javaClassName) {
		return this.parsedClasses.get(javaClassName);
	}

	private List<JavaClass> getWriteAndDefineToTableClasses(TableInfo tableInfo) {

		List<JavaClass> rtn = new ArrayList<JavaClass>();
		for (JavaClass javaClass : this.javaClasses) {
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
