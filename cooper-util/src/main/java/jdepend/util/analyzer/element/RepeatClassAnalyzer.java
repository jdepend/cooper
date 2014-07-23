package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;

public final class RepeatClassAnalyzer extends AbstractAnalyzer {

	private static final long serialVersionUID = 5472258042892488645L;

	public RepeatClassAnalyzer() {
		super("重复类识别", Attention, "识别不同jar包中是否存在同包名同类名的类（jar包冲突）");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		List<JavaClass> repeatClasses = new ArrayList<JavaClass>();

		boolean firstRecord;
		for (JavaClass javaClass : result.getClasses()) {
			firstRecord = true;
			for (JavaClass javaClass1 : result.getClasses()) {
				if (javaClass.getName().equals(javaClass1.getName())
						&& !javaClass.getPlace().equals(javaClass1.getPlace())) {
					if (firstRecord) {
						repeatClasses.add(javaClass);
						firstRecord = false;
					}
					repeatClasses.add(javaClass1);
				}
			}
		}

		for (JavaClass javaClass : repeatClasses) {
			this.printTable("类名", javaClass.getName());
			this.printTable("位置", javaClass.getPlace().getName());
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("识别不同jar包中是否存在同包名同类名的类（jar包冲突）。<br>");
		return explain.toString();
	}
}
