package jdepend.util.analyzer.element;

import java.util.Collection;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Method;
import jdepend.model.relationtype.ParamRelation;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.element.helper.ServiceOrVO;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

import static jdepend.util.analyzer.element.helper.ServiceOrVO.*;

public class IdentifyJavaClassType extends AbstractAnalyzer {

	private static final long serialVersionUID = 4752453696439145223L;

	public IdentifyJavaClassType() {
		super("识别JavaClass是Service还是VO", Analyzer.Attention, "识别JavaClass是Service还是VO");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {
		ServiceOrVO sov;
		for (JavaClass javaClass : result.getClasses()) {
			if (javaClass.isInner()) {
				continue;
			}
			sov = INIT;
			if (!javaClass.isState()) {
				if (javaClass.getMethods().size() == 1 && javaClass.getMethods().iterator().next().isConstruction()) {
					sov = ONLY_CONSTRUCTION;
				} else {
					if(isState(javaClass.getSubClasses())) {
						sov = doMoreAnalysis(javaClass, SUB_NO_BIZ_METHOD, SUB_STATE_SUPER_STATE, SUB_STATE_NOT_PR, SUB_STATE_IS_PR);
					} else if(isState(javaClass.getSupers())) {
						sov = doMoreAnalysis(javaClass, SUPER_NO_BIZ_METHOD, SUPER_STATE_TWICE, SUPER_STATE_NOT_PR, SUPER_STATE_IS_PR);
					} else {
						sov = ServiceOrVO.UNSURE_SERVICE;
					}
				}
			} else {
				sov = doMoreAnalysis(javaClass, UNSURE_NO_BIZ_METHOD, UNSURE_SUPER_STATE, UNSURE_NOT_PR, UNSURE_IS_PR);
			}

			this.printTable("类名", javaClass.getName());
			this.printTable("类型", sov.getType() + sov.getIndex());
		}
	}
	
	private boolean isState(Collection<JavaClass> collection) {
		for (JavaClass javaClass : collection) {
			if (javaClass.isState()) {
				return true;
			}
		}
		return false;
	}
	
	private ServiceOrVO doMoreAnalysis(JavaClass javaClass, ServiceOrVO s1, ServiceOrVO s2, ServiceOrVO s3, ServiceOrVO s4) {
		if (!hasBizMethod(javaClass.getMethods())) {
			return s1;
		} else {
			if(isState(javaClass.getSupers())) {
				return s2;
			} else {
				if(isParamRelation(javaClass.getCaItems())) {
					return s3;
				} else {
					return s4;
				}
			}
		}
	}
	
	private boolean hasBizMethod(Collection<Method> methods) {
		for (Method method : methods) {
			if (!method.isConstruction() && !method.getName().startsWith("get")
					&& !method.getName().startsWith("set")) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isParamRelation(Collection<JavaClassRelationItem> items) {
		for (JavaClassRelationItem item : items) {
			if (item.getType() instanceof ParamRelation) {
				return true;
			}
		}
		return false;
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
