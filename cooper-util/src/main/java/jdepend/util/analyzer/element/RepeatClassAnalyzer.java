package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public final class RepeatClassAnalyzer extends AbstractAnalyzer {

	private static final long serialVersionUID = 5472258042892488645L;

	public RepeatClassAnalyzer() {
		super("重复类识别", Attention, "识别不同jar包中是否存在同包名同类名的类（jar包冲突）");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws AnalyzerException {

		List<JavaClassUnit> repeatClasses = new ArrayList<JavaClassUnit>();

		boolean firstRecord;
		boolean needCheck;
		for (JavaClassUnit javaClass : result.getClasses()) {
			// 检查是否已经检测过（被检查时已经收集到repeatClasses中，不必再检查）
			needCheck = true;
			L: for (JavaClassUnit repeatClass : repeatClasses) {
				if (repeatClass.equals(javaClass)) {
					needCheck = false;
					break L;
				}
			}
			if (needCheck) {
				firstRecord = true;
				for (JavaClassUnit javaClass1 : result.getClasses()) {
					if (this.repeat(javaClass, javaClass1)) {
						if (firstRecord) {
							repeatClasses.add(javaClass);
							firstRecord = false;
						}
						repeatClasses.add(javaClass1);
					}
				}
			}
		}

		for (JavaClassUnit javaClass : repeatClasses) {
			this.printTable("类名", javaClass.getName());
			this.printTable("位置", javaClass.getJavaClass().getPlace());
		}
	}

	private boolean repeat(JavaClassUnit javaClass, JavaClassUnit javaClass1) {
		if (javaClass.getName().equals(javaClass1.getName())) {
			if (javaClass.getJavaClass().getPlace() == null && javaClass1.getJavaClass().getPlace() != null) {
				return true;
			} else if (javaClass.getJavaClass().getPlace() != null && javaClass1.getJavaClass().getPlace() == null) {
				return true;
			} else if (javaClass.getJavaClass().getPlace() == null && javaClass1.getJavaClass().getPlace() == null) {
				return false;
			} else if (!javaClass.getJavaClass().getPlace().equals(javaClass1.getJavaClass().getPlace())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("识别不同jar包中是否存在同包名同类名的类（jar包冲突）。<br>");
		return explain.toString();
	}
}
