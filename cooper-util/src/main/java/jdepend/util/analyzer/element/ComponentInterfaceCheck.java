package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.graph.TableCallBack;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public class ComponentInterfaceCheck extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8972772836615167269L;

	private String rule;

	private transient String[] keys;

	public ComponentInterfaceCheck() {
		super("组件接口检查", Analyzer.Attention, "检查组件接口是否按着规定统一放在指定的位置上");
		if (this.rule != null) {
			this.keys = this.rule.split(",");
		}
	}

	@Override
	protected void doSearch(AnalysisResult result) throws AnalyzerException {
		if (this.rule == null) {
			throw new AnalyzerException("没有设置规则,rule是所在文件夹的名字，如果有多个文件夹，以逗号分隔，{root}代表组件的根路径");
		}
		if (this.keys == null) {
			this.keys = this.rule.split(",");
		}
		for (JavaClassUnit javaClass : result.getClasses()) {
			for (JavaClassUnit ceClass : javaClass.getEfferents()) {
				if (this.isAttention(ceClass)) {
					this.printTable("类名称", ceClass.getName());
					this.printTable("组件名", ceClass.getComponent().getName());
					this.printTable("调用者", javaClass.getName());
				}
			}
		}
		// 增加回调函数
		this.addTableCallBack(new TableCallBack("类名称", "jdepend.client.report.ui.JavaClassDetailDialog"));
		this.addTableCallBack(new TableCallBack("调用者", "jdepend.client.report.ui.JavaClassDetailDialog"));

		// 收集未使用的在接口位置存放的类
		List<JavaClassUnit> privateElements = new ArrayList<JavaClassUnit>();
		for (JavaClassUnit javaClass : result.getClasses()) {
			for (String key : this.keys) {
				if (key.equals("{root}")) {
					if (javaClass.getJavaClass().getPackageName().equals(javaClass.getComponent().getPath())) {
						if (!javaClass.isUsedByExternal()) {
							privateElements.add(javaClass);
							break;
						}
					}
				}
				if (javaClass.getJavaClass().getPackageName().endsWith(key)) {
					if (!javaClass.isUsedByExternal()) {
						privateElements.add(javaClass);
						break;
					}
				}
			}
		}

		if (privateElements.size() > 0) {
			this.print("不该放入组件接口位置的类列表：\n");
			for (JavaClassUnit javaClass : privateElements) {
				this.printTab();
				this.print(javaClass.getName() + "\n");
			}
		}

	}

	private boolean isAttention(JavaClassUnit javaClass) {
		boolean isAttention = true;
		for (String key : this.keys) {
			if (key.equals("{root}")) {
				if (javaClass.getJavaClass().getPackageName().equals(javaClass.getComponent().getPath())) {
					isAttention = false;
					break;
				}
			}
			if (javaClass.getJavaClass().getPackageName().endsWith(key)) {
				isAttention = false;
				break;
			}
		}
		return isAttention;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("<strong>组件接口检查</strong>主要适用于检查组件接口涉及的类是否按着规定统一放在指定的位置上。<br>");
		explain.append("一般来说，组件的接口涉及的类都会统一放在一个地方，这样便于管理和使用该组件。<br>");
		explain.append("参数中rule是所在文件夹的名字，如果有多个文件夹，以逗号分隔，{root}代表组件的根路径。<br>");
		return explain.toString();
	}

}
