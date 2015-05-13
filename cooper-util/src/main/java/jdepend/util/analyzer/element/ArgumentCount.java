package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClassUnit;
import jdepend.model.Method;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public final class ArgumentCount extends AbstractAnalyzer {

	private static final long serialVersionUID = 5047118339494071605L;

	private Integer count;

	public ArgumentCount() {
		super("方法参数个数大于指定数目", Analyzer.AntiPattern, "方法参数个数大于指定数目");
		if (count == null || count == 0) {
			count = 6;
		}
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		List<ArgumentInfo> argumentInfos = new ArrayList<ArgumentInfo>();
		ArgumentInfo argumentInfo;
		int arguments;

		for (JavaClassUnit javaClass : result.getClasses()) {
			for (Method method : javaClass.getJavaClass().getSelfMethods()) {
				arguments = method.getArgumentCount();
				if (arguments >= count) {
					argumentInfo = new ArgumentInfo(method.getInfo(), javaClass.getName(), arguments);
					argumentInfos.add(argumentInfo);
				}
			}
		}

		Collections.sort(argumentInfos);
		for (ArgumentInfo argInfo : argumentInfos) {
			this.printTable("方法签名", argInfo.methodName);
			this.printTable("类名", argInfo.javaClassName);
			this.printTable("参数个数", argInfo.argumentCount);
		}
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	class ArgumentInfo implements Comparable<ArgumentInfo> {

		public String methodName;
		public String javaClassName;
		public int argumentCount;

		public ArgumentInfo(String methodName, String javaClassName, int argumentCount) {
			super();
			this.methodName = methodName;
			this.javaClassName = javaClassName;
			this.argumentCount = argumentCount;
		}

		@Override
		public int compareTo(ArgumentInfo o) {
			return new Integer(o.argumentCount).compareTo(argumentCount);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((javaClassName == null) ? 0 : javaClassName.hashCode());
			result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ArgumentInfo other = (ArgumentInfo) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (javaClassName == null) {
				if (other.javaClassName != null)
					return false;
			} else if (!javaClassName.equals(other.javaClassName))
				return false;
			if (methodName == null) {
				if (other.methodName != null)
					return false;
			} else if (!methodName.equals(other.methodName))
				return false;
			return true;
		}

		private ArgumentCount getOuterType() {
			return ArgumentCount.this;
		}
	}
}
