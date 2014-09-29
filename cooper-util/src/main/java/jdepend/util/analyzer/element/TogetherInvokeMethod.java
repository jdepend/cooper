package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.Method;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public final class TogetherInvokeMethod extends AbstractAnalyzer {

	private static final long serialVersionUID = -8417592572714047463L;

	public TogetherInvokeMethod() {
		super("识别共同调用的方法集合", Analyzer.Attention, "识别同一个类的多个方法被共同调用的情况");

	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		Map<JavaClass, Collection<Method>> invokedMethods;
		for (Method method : result.getMethods()) {
			//对方法中调用的方法按着类进行分组
			invokedMethods = new HashMap<JavaClass, Collection<Method>>();
			for (Method invokeMethod1 : method.getInvokeMethods()) {
				if (!invokeMethod1.getJavaClass().equals(method.getJavaClass())) {
					if (!invokedMethods.containsKey(invokeMethod1.getJavaClass())) {
						invokedMethods.put(invokeMethod1.getJavaClass(), new ArrayList<Method>());
					}
					invokedMethods.get(invokeMethod1.getJavaClass()).add(invokeMethod1);
				}
			}
			//收集分组后大于一个方法的方法
			for (JavaClass javaClass : invokedMethods.keySet()) {
				if (invokedMethods.get(javaClass).size() > 1) {
					for (Method invokedMethod : invokedMethods.get(javaClass)) {
						this.printTable("类名", invokedMethod.getJavaClass().getName());
						this.printTable("方法名", invokedMethod.getInfo());
						this.printTable("共同被调用方法数", invokedMethods.get(javaClass).size());
						this.printTable("调用的类名", method.getJavaClass().getName());
						this.printTable("调用的方法名", method.getInfo());
					}
				}
			}
			this.progress();
		}
	}

	@Override
	public int getMaxProgress(AnalysisResult result) {
		return result.getMethods().size();
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("当一个类中的多个方法经常在一起被其他方法调用时，应考虑是否应该进行合并。<br>");
		return explain.toString();
	}
}
