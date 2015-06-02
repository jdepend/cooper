package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdepend.metadata.Method;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public class LSPPrinciple extends AbstractAnalyzer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6067470388079134087L;

	public LSPPrinciple() {
		super("LSP检查", Analyzer.Attention, "识别违反李氏替换原则的地方");
	}

	protected void doSearch(AnalysisResult result) throws AnalyzerException {
		List<JavaClassUnit> members = new ArrayList<JavaClassUnit>(result.getClasses());

		Collections.sort(members);
		Iterator<JavaClassUnit> memberIter = members.iterator();
		Method overridedMethod;
		while (memberIter.hasNext()) {
			JavaClassUnit current = memberIter.next();
			if (!current.getJavaClass().isAbstract()) {
				for (Method method : current.getJavaClass().getOverrideMethods()) {
					if (method.getSelfLineCount() == 0) {
						this.printTable("method", method.getName());
						this.printTable("Class", method.getJavaClass().getName());
						overridedMethod = current.getJavaClass().getOverridedMethods(method).iterator().next();
						this.printTable("OverridedMethod", overridedMethod.getName());
						this.printTable("SuperClass", overridedMethod.getJavaClass().getName());
					}
				}
			}
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("子类（自己非抽象类）覆盖父类的方法没有实现内容，或者仅仅抛出异常，有可能违反李氏替换原则。<br>");
		return explain.toString();
	}
}
