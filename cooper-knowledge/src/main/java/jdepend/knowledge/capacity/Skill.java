package jdepend.knowledge.capacity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.pattern.PatternIdentifyerMgr;
import jdepend.knowledge.pattern.PatternIdentifyerMgrFactory;
import jdepend.knowledge.pattern.PatternInfo;
import jdepend.metadata.Method;
import jdepend.metadata.relationtype.JavaClassRelationTypeMgr;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassRelationUtil;

public class Skill {

	private int patterns;

	private int classSize;

	private Map<Integer, Float> bigClassScales;

	private Collection<JavaClassUnit> bigClasses;

	private Map<Object, Object> classRelations;

	private float bigArgumentMethodScale;

	private Collection<Method> bigArgumentMethods;

	private float bigLineCountMethodScale;

	private Collection<Method> bigLineCountMethods;

	private Map<String, Collection<PatternInfo>> patternList;

	public static final String High = "High";
	public static final String Middle = "Middle";
	public static final String Low = "Low";

	public Skill(AnalysisResult result) {
		// 收集使用设计模式的地方
		PatternIdentifyerMgr mgr = new PatternIdentifyerMgrFactory().create();
		patternList = mgr.identify(result);
		Collection<PatternInfo> items;
		for (String patternName : patternList.keySet()) {
			items = patternList.get(patternName);
			if (items != null && items.size() > 0) {
				patterns += 1;
			}
		}

		this.classSize = result.calClassSize();

		bigClassScales = new HashMap<Integer, Float>();
		bigClassScales.put(500, 0F);
		bigClassScales.put(0, 0F);
		bigClasses = new ArrayList<JavaClassUnit>();
		bigArgumentMethods = new ArrayList<Method>();
		bigLineCountMethods = new ArrayList<Method>();

		int classCount = 0;
		int bigArgumentMethodCount = 0;
		int bigLineCountMethodCount = 0;
		int methodCount = 0;
		for (JavaClassUnit javaClass : result.getClasses()) {
			if (javaClass.getLineCount() != 0) {
				if (javaClass.getLineCount() > 500) {
					bigClassScales.put(500, bigClassScales.get(500) + 1);
					bigClasses.add(javaClass);
				} else {
					bigClassScales.put(0, bigClassScales.get(0) + 1);
				}
				classCount++;
			}

			for (Method method : javaClass.getJavaClass().getSelfMethods()) {
				if (method.getArgumentCount() >= 6) {
					bigArgumentMethodCount++;
					bigArgumentMethods.add(method);
				}
				if (method.getSelfLineCount() > 200) {
					bigLineCountMethodCount++;
					bigLineCountMethods.add(method);
				}
				methodCount++;
			}

		}

		if (classCount != 0) {
			bigClassScales.put(500, bigClassScales.get(500) * 1F / classCount);
			bigClassScales.put(0, bigClassScales.get(0) * 1F / classCount);
		}

		if (methodCount != 0) {
			bigArgumentMethodScale = bigArgumentMethodCount * 1F / methodCount;
			bigLineCountMethodScale = bigLineCountMethodCount * 1F / methodCount;
		}
		// 计算类关系比例
		JavaClassRelationUtil javaClassRelationUtil = new JavaClassRelationUtil(result);
		classRelations = javaClassRelationUtil.getTypes();

	}

	public String getLevel() {
		if (this.patterns > 8 && this.classSize < 200 && getBigClassScale() < 0.05) {
			return High;
		} else if (this.patterns <= 5 && this.classSize >= 300 && getBigClassScale() > 0.1) {
			return Low;
		} else if (this.getTableRelationScale() >= 0.1) {
			return Low;
		} else if (this.getVariableRelationScale() > 0.9) {
			return Low;
		} else {
			return Middle;
		}
	}

	public float getBigClassScale() {
		return this.bigClassScales.get(500);
	}

	public Collection<JavaClassUnit> getBigClasses() {
		return bigClasses;
	}

	public int getPatterns() {
		return patterns;
	}

	public int getClassSize() {
		return classSize;
	}

	public Map<Integer, Float> getBigClassScales() {
		return bigClassScales;
	}

	public float getBigArgumentMethodScale() {
		return bigArgumentMethodScale;
	}

	public Collection<Method> getBigArgumentMethods() {
		return bigArgumentMethods;
	}

	public float getBigLineCountMethodScale() {
		return bigLineCountMethodScale;
	}

	public Collection<Method> getBigLineCountMethods() {
		return bigLineCountMethods;
	}

	public Map<Object, Object> getClassRelations() {
		return classRelations;
	}

	public float getFieldRelationScale() {
		return classRelations.get(JavaClassRelationTypeMgr.Field) == null ? 0F : (Float) classRelations
				.get(JavaClassRelationTypeMgr.Field);
	}

	public float getInheritRelationScale() {
		return classRelations.get(JavaClassRelationTypeMgr.Inherit) == null ? 0F : (Float) classRelations
				.get(JavaClassRelationTypeMgr.Inherit);
	}

	public float getParamRelationScale() {
		return classRelations.get(JavaClassRelationTypeMgr.Param) == null ? 0F : (Float) classRelations
				.get(JavaClassRelationTypeMgr.Param);
	}

	public float getTableRelationScale() {
		return classRelations.get(JavaClassRelationTypeMgr.Table) == null ? 0F : (Float) classRelations
				.get(JavaClassRelationTypeMgr.Table);
	}

	public float getVariableRelationScale() {
		return classRelations.get(JavaClassRelationTypeMgr.Variable) == null ? 0F : (Float) classRelations
				.get(JavaClassRelationTypeMgr.Variable);
	}

	public Map<String, Collection<PatternInfo>> getPatternList() {
		return patternList;
	}

	@Override
	public String toString() {

		StringBuilder info = new StringBuilder();

		info.append("Level:");
		info.append(this.getLevel());
		info.append("\n");

		info.append("Patterns:");
		info.append(this.patterns);
		info.append("\n");

		info.append("平均类尺寸:");
		info.append(this.classSize);
		info.append("\n");

		info.append("超过500行类的比例:");
		info.append(MetricsFormat.toFormattedPercent(this.bigClassScales.get(500)));
		info.append("\n");

		info.append("超过6个参数的方法的比例:");
		info.append(MetricsFormat.toFormattedPercent(this.bigArgumentMethodScale));
		info.append("\n");

		info.append("超过200行的方法的比例:");
		info.append(MetricsFormat.toFormattedPercent(this.bigLineCountMethodScale));
		info.append("\n");

		info.append("包含关系比例:");
		info.append(MetricsFormat.toFormattedPercent(this.getFieldRelationScale()));
		info.append("\n");
		info.append("继承关系比例:");
		info.append(MetricsFormat.toFormattedPercent(this.getInheritRelationScale()));
		info.append("\n");
		info.append("参数关系比例:");
		info.append(MetricsFormat.toFormattedPercent(this.getParamRelationScale()));
		info.append("\n");
		info.append("变量关系比例:");
		info.append(MetricsFormat.toFormattedPercent(this.getVariableRelationScale()));
		info.append("\n");
		info.append("表关系比例:");
		info.append(MetricsFormat.toFormattedPercent(this.getTableRelationScale()));
		info.append("\n");

		return info.toString();
	}
}
