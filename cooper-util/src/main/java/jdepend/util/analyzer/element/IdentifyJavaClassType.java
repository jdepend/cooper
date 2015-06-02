package jdepend.util.analyzer.element;

import jdepend.metadata.util.ClassTypeInfo;
import jdepend.metadata.util.JavaClassUtil;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public class IdentifyJavaClassType extends AbstractAnalyzer {

	private static final long serialVersionUID = 4752453696439145223L;

	public IdentifyJavaClassType() {
		super("识别JavaClass是Service还是VO", Analyzer.Attention, "识别JavaClass是Service还是VO");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws AnalyzerException {

		for (JavaClassUnit javaClass : result.getClasses()) {
			ClassTypeInfo classTypeInfo = JavaClassUtil.getType(javaClass.getJavaClass());

			this.printTable("类名", javaClass.getName());
			this.printTable("类型", classTypeInfo.toString());
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("识别JavaClass是Service还是VO，规则：<br>");
		explain.append("1、Service : 本身无状态+子类或父类没也没有状态，等等。<br>");
		explain.append("2、VO：有状态+作为其他方法的参数或返回值，等等。<br>");
		return explain.toString();
	}
}
