package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;

public class JavaClassRelationUtil {

	private List<JavaClassRelationInfo> relationInfos;

	private Map<Object, Object> types;

	private Map<Object, Object> inners;

	private Map<Object, Object> ext_types;

	private Map<Object, Object> in_types;

	public JavaClassRelationUtil(AnalysisResult result) {

		this.relationInfos = new ArrayList<JavaClassRelationInfo>();
		JavaClassRelationInfo tableInfo;
		// 收集TableRelationInfo
		for (JavaClassUnit javaClass : result.getClasses()) {
			for (JavaClassRelationItem item : javaClass.getJavaClass().getCeItems()) {
				tableInfo = new JavaClassRelationInfo(item.getSource().getName(), item.getTarget().getName(), item
						.getType().getName(), JavaClassUnitUtil.isInner(item, result), item.getRelationIntensity());
				relationInfos.add(tableInfo);
			}
		}
		Collections.sort(relationInfos);

		this.calSummary();
	}

	private void calSummary() {

		Map<String, Integer> type1s = new HashMap<String, Integer>();
		Map<Boolean, Integer> inner1s = new HashMap<Boolean, Integer>();

		Map<String, Integer> in_type1s = new HashMap<String, Integer>();
		Map<String, Integer> ext_type1s = new HashMap<String, Integer>();

		for (JavaClassRelationInfo info : relationInfos) {
			// 计算按类型数量
			if (type1s.get(info.type) == null) {
				type1s.put(info.type, 1);
			} else {
				type1s.put(info.type, type1s.get(info.type) + 1);
			}

			// 计算内外数量
			if (inner1s.get(info.isInner) == null) {
				inner1s.put(info.isInner, 1);
			} else {
				inner1s.put(info.isInner, inner1s.get(info.isInner) + 1);
			}
			// 计算内外类型数量
			if (info.isInner) {
				if (in_type1s.get(info.type) == null) {
					in_type1s.put(info.type, 1);
				} else {
					in_type1s.put(info.type, in_type1s.get(info.type) + 1);
				}
			} else {
				if (ext_type1s.get(info.type) == null) {
					ext_type1s.put(info.type, 1);
				} else {
					ext_type1s.put(info.type, ext_type1s.get(info.type) + 1);
				}
			}
		}
		// 计算比例
		types = new HashMap<Object, Object>();
		in_types = new HashMap<Object, Object>();
		ext_types = new HashMap<Object, Object>();
		inners = new HashMap<Object, Object>();
		if (relationInfos.size() > 0) {
			for (String type : type1s.keySet()) {
				types.put(type, type1s.get(type) * 1F / relationInfos.size());
			}
			for (String type : in_type1s.keySet()) {
				in_types.put(type, in_type1s.get(type) * 1F / inner1s.get(true));
			}
			for (String type : ext_type1s.keySet()) {
				ext_types.put(type, ext_type1s.get(type) * 1F / inner1s.get(false));
			}

			inners.put("组件内", inner1s.get(true) == null ? 0 : inner1s.get(true) * 1F / relationInfos.size());
			inners.put("组件间", inner1s.get(false) == null ? 0 : inner1s.get(false) * 1F / relationInfos.size());
		}
	}

	public List<JavaClassRelationInfo> getRelationInfos() {
		return relationInfos;
	}

	public Map<Object, Object> getTypes() {
		return types;
	}

	public Map<Object, Object> getInners() {
		return inners;
	}

	public Map<Object, Object> getExt_types() {
		return ext_types;
	}

	public Map<Object, Object> getIn_types() {
		return in_types;
	}

	public String getSummaryInfo() {

		StringBuilder summary = new StringBuilder();

		boolean blank = false;
		for (Object type : types.keySet()) {
			if (!blank) {
				blank = true;
			} else {
				summary.append("	");
			}
			summary.append("类型为[");
			summary.append(type);
			summary.append("]的关系占[");
			summary.append(MetricsFormat.toFormattedPercent((Float) types.get(type)));
			summary.append("]\n");
		}
		summary.append("\n组件内关系占[");
		summary.append(MetricsFormat.toFormattedPercent((Float) inners.get("组件内")));
		summary.append("]\n");
		for (Object type : in_types.keySet()) {
			summary.append("	");
			summary.append("类型为[");
			summary.append(type);
			summary.append("]的关系占[");
			summary.append(MetricsFormat.toFormattedPercent((Float) in_types.get(type)));
			summary.append("]\n");
		}
		summary.append("组件间关系占[");
		summary.append(MetricsFormat.toFormattedPercent((Float) inners.get("组件间")));
		summary.append("]\n");
		for (Object type : ext_types.keySet()) {
			summary.append("	");
			summary.append("类型为[");
			summary.append(type);
			summary.append("]的关系占[");
			summary.append(MetricsFormat.toFormattedPercent((Float) ext_types.get(type)));
			summary.append("]\n");
		}

		return summary.toString();

	}
}
