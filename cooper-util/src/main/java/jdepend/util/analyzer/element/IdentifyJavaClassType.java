package jdepend.util.analyzer.element;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class IdentifyJavaClassType extends AbstractAnalyzer {

	private static final long serialVersionUID = 4752453696439145223L;

	private final static String Service_TYPE = "Service";
	private final static String VO_TYPE = "VO";
	private final static String Unensure_TYPE = "不确定";

	public IdentifyJavaClassType() {
		super("识别JavaClass是Service还是VO", Analyzer.Attention, "识别JavaClass是Service还是VO");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		for (JavaClass javaClass : result.getClasses()) {
			if (!javaClass.isState()) {
				this.printTable("类名", javaClass.getName());
				this.printTable("类型", Service_TYPE);
			} else {
				this.printTable("类名", javaClass.getName());
				this.printTable("类型", Unensure_TYPE);
			}
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("识别JavaClass是Service还是VO，规则：<br>");
		explain.append("1、该类直接或间接实现了Serializable接口为VO。<br>");
		explain.append("2、该类没有作为返回值类型或其子类型的为Service，否则为VO。<br>");
		explain.append("3、该类只有get和set方法的为VO。<br>");
		explain.append("4、该类无状态的为Seivice。<br>");
		return explain.toString();
	}
}
