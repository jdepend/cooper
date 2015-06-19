package jdepend.util.analyzer.element;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.JavaClass;
import jdepend.metadata.Method;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public class IdentifyCallback extends AbstractAnalyzer {

	private static final long serialVersionUID = -4566409232231165490L;

	public IdentifyCallback() {
		super("Callback识别", Analyzer.Attention, "识别在组件中设置Callback的情况");
	}

	@Override
	protected void doExecute(AnalysisResult result) throws AnalyzerException {

		boolean isCallback1;// 该类为接口，并且其方法有返回值，该方法被本组件调用
		boolean isCallback2;// 该类的实现类不与该类处于一个组件内
		boolean isCallback3;// 实现类的构造过程不在该组件内

		Method callbackMethod = null;
		Method method1 = null;
		JavaClass subClass2 = null;
		Collection<Method> method3s = null;

		for (JavaClassUnit javaClass : result.getClasses()) {
			isCallback1 = false;
			if (javaClass.getJavaClass().isInterface()) {
				L: for (Method method : javaClass.getJavaClass().getSelfMethods()) {
					if (method.existReturn()) {
						for (Method invokedMethod : method.getInvokedMethods()) {
							if (result.getTheClass(invokedMethod.getJavaClass().getId()).getComponent()
									.equals(javaClass.getComponent())) {
								callbackMethod = method;
								method1 = invokedMethod;
								isCallback1 = true;
								break L;
							}
						}
					}
				}
			}
			isCallback2 = false;
			isCallback3 = false;
			if (isCallback1) {
				M: for (JavaClass subClass : javaClass.getJavaClass().getSubClasses()) {
					if (!result.getTheClass(subClass.getId()).getComponent().equals(javaClass.getComponent())) {
						isCallback2 = true;
						subClass2 = subClass;

						isCallback3 = true;
						method3s = new HashSet<Method>();
						for (Method method : subClass.getConstructorMethods()) {
							for (Method invokedMethod : method.getInvokedMethods()) {
								if (result.getTheClass(invokedMethod.getJavaClass().getId()).getComponent()
										.equals(javaClass.getComponent())) {
									isCallback3 = false;
									break M;
								}
								method3s.add(invokedMethod);
							}
						}
					}
				}
			}
			if (isCallback2 && isCallback3) {
				this.print("\nComponent:" + javaClass.getComponent().getName() + "\n");
				this.print("Callback : " + javaClass.getName() + "." + callbackMethod.getName() + "\n");
				this.printTab();
				this.print("is Invoked Method : " + method1.getJavaClass().getName() + "." + method1.getName() + "\n");
				this.printTab();
				this.print("have subClass : " + subClass2.getName() + " Constructors InvokedMethods:\n");
				for (Method method : method3s) {
					this.printTab();
					this.printTab();
					this.print("Invoked Method : " + method.getJavaClass().getName() + "." + method.getName() + "\n");
				}

			}
		}

	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("当一个组件中内置了Callback后，其测试结果依赖于Callback接口的实例化类，这会增加测试的难度和组件运行时的不确定性。<br>");
		explain.append("识别Callback的规则是：<br>");
		explain.append("1、该类为接口，并且其方法有返回值，该方法被本组件调用<br>");
		explain.append("2、该类的实现类不与该类处于一个组件内<br>");
		explain.append("3、实现类的构造过程不在该组件内<br>");
		return explain.toString();
	}

}
