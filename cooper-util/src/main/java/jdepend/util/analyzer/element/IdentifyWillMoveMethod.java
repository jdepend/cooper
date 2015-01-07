package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.model.InvokeItem;
import jdepend.model.JavaClass;
import jdepend.model.Method;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public final class IdentifyWillMoveMethod extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 981733240201462312L;

	public IdentifyWillMoveMethod() {
		super("MoveMethod分析", Analyzer.Attention, "识别有移动倾向的Method");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		Map<Method, String> moveInfos = new LinkedHashMap<Method, String>();

		String invokeClassName;
		List<String> invokeMethods;
		boolean sameInvokeClass;
		boolean selfInvokeClass;

		for (JavaClass javaClass : result.getClasses()) {
			for (Method method : javaClass.getSelfMethods()) {
				if (method.getSelfLineCount() > 10) {
					if (method.getWriteFields().size() == 0 && method.getReadFields().size() == 0) {
						invokeClassName = null;
						invokeMethods = new ArrayList<String>();
						sameInvokeClass = true;
						selfInvokeClass = false;
						L: for (InvokeItem invokeItem : method.getInvokeItems()) {
							if (!invokeItem.getCallee().getJavaClass().equals(method.getJavaClass())) {
								if (invokeClassName != null
										&& !invokeClassName.equals(invokeItem.getCallee().getJavaClass().getName())) {
									sameInvokeClass = false;
									break L;
								} else if (invokeClassName == null) {
									invokeClassName = invokeItem.getCallee().getJavaClass().getName();
								}
								invokeMethods.add(invokeItem.getCallee().getName());
							} else {
								selfInvokeClass = true;
								break L;
							}

						}
						if (invokeClassName != null && sameInvokeClass && !selfInvokeClass) {
							if (invokeMethods.size() == 1) {
								moveInfos.put(method, invokeClassName + "." + invokeMethods.get(0));
							} else {
								moveInfos.put(method, invokeClassName);
							}
						}
					}
				}
			}
		}

		this.isPrintTab(false);

		this.print("以下列出的方法与源类中的属性和方法没有关系，而与目标类中的方法存在调用关系：\n");
		for (Method method : moveInfos.keySet()) {
			this.print("建议将JavaClass[" + method.getJavaClass().getName() + "." + method.getName() + "]移动到["
					+ moveInfos.get(method) + "]中\n");
		}

	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("识别孤立的方法，并为其找到宿主。<br>");
		return explain.toString();
	}

}
