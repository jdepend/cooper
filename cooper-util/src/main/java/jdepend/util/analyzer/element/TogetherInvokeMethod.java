package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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

		Map<Method, CollectionMethod> invokeMethods = new HashMap<Method, CollectionMethod>();
		Map<JavaClass, Collection<Method>> invokedMethods;

		CollectionMethod cm;
		Map<CollectionMethod, Method> cmm = new HashMap<CollectionMethod, Method>();

		for (Method method : result.getMethods()) {
			// 对方法中调用的方法按着类进行分组
			invokedMethods = new HashMap<JavaClass, Collection<Method>>();
			for (Method invokeMethod1 : method.getInvokeMethods()) {
				if (!invokeMethod1.getJavaClass().equals(method.getJavaClass())) {
					if (!invokedMethods.containsKey(invokeMethod1.getJavaClass())) {
						invokedMethods.put(invokeMethod1.getJavaClass(), new ArrayList<Method>());
					}
					invokedMethods.get(invokeMethod1.getJavaClass()).add(invokeMethod1);
				}
			}
			// 收集分组后大于一个方法的方法集合
			for (JavaClass javaClass : invokedMethods.keySet()) {
				if (invokedMethods.get(javaClass).size() > 1) {
					cm = new CollectionMethod(invokedMethods.get(javaClass));
					// 收集方法集合大于1处调用的情况
					if (cmm.containsKey(cm)) {
						if (!invokeMethods.containsKey(cmm.get(cm))) {
							invokeMethods.put(cmm.get(cm), cm);
							this.printTable("类名", cm.getJavaClass().getName());
							this.printTable("方法集合名", cm.toString());
							this.printTable("调用方法名", cmm.get(cm).getInfo());
							this.printTable("调用类名", cmm.get(cm).getJavaClass().getName());
						}
						invokeMethods.put(method, cm);
						this.printTable("类名", cm.getJavaClass().getName());
						this.printTable("方法集合名", cm.toString());
						this.printTable("调用方法名", method.getInfo());
						this.printTable("调用类名", method.getJavaClass().getName());

					}
					cmm.put(cm, method);
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

	class CollectionMethod {

		private Collection<Method> methods;

		public CollectionMethod(Collection<Method> methods) {
			super();
			this.methods = methods;
		}

		public Collection<Method> getMethods() {
			return methods;
		}

		public JavaClass getJavaClass() {
			return methods.iterator().next().getJavaClass();
		}

		@Override
		public String toString() {
			StringBuilder info = new StringBuilder();
			for (Method method : methods) {
				info.append(method.getInfo());
				info.append("\n");
			}
			info.delete(info.length() - 1, info.length());

			return info.toString();
		}

		@Override
		public int hashCode() {
			int code = 0;
			for (Method method : methods) {
				code += method.hashCode();
			}
			return code;
		}

		@Override
		public boolean equals(Object obj) {
			CollectionMethod cm = (CollectionMethod) obj;
			for (Method method : methods) {
				if (!cm.methods.contains(method)) {
					return false;
				}
			}
			for (Method method : cm.methods) {
				if (!methods.contains(method)) {
					return false;
				}
			}
			return true;
		}
	}
}
