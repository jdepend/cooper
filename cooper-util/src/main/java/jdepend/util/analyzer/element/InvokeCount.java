package jdepend.util.analyzer.element;

import java.util.Collection;

import jdepend.framework.exception.JDependException;
import jdepend.model.Method;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public final class InvokeCount extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1119743992641846769L;

	public InvokeCount() {
		super("统计方法被调用的次数", Analyzer.Attention, "统计方法被调用的次数");

	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		Collection<Method> invokedMethods;
		for (Method method : result.getMethods()) {
			invokedMethods = method.getInvokedMethods(result.getMethods());
			if (invokedMethods.size() > 0) {
				for (Method invokeMethod : invokedMethods) {
					this.printTable("类名", method.getJavaClass().getName());
					this.printTable("方法名", method.getName());
					this.printTable("参数", method.getArgumentInfo());
					this.printTable("调用次数", invokedMethods.size());
					this.printTable("调用者", invokeMethod.getJavaClass().getName() + "." + invokeMethod.getInfo());
					this.printTable("是否组件间", method.getJavaClass().getComponent().equals(invokeMethod.getJavaClass().getComponent()) ? "否" : "是");
				}
			} else {
				this.printTable("类名", method.getJavaClass().getName());
				this.printTable("方法名", method.getName());
				this.printTable("参数", method.getArgumentInfo());
				this.printTable("调用次数", 0);
				this.printTable("调用者", "");
				this.printTable("是否组件间", "");
			}
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("当一个方法只在一处被使用，并且该方法是一个领域层方法，值得分析该方法放在领域层类上是否合适。<br>");
		explain.append("当一个方法在多处被使用，并且该方法是一个应用层方法，值得分析该方法放在应用层类上是否合适。<br>");
		return explain.toString();
	}
}
