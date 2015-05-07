package jdepend.util.analyzer.element;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.relationtype.ParamRelation;
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

		String type;
		for (JavaClass javaClass : result.getClasses()) {
			type = Unensure_TYPE;
			if (!javaClass.isState()) {
				L1: for (JavaClass subClass : javaClass.getSubClasses()) {
					if (subClass.isState()) {
						type = VO_TYPE;
						break L1;
					}
				}
				if (type.equals(Unensure_TYPE)) {
					L2: for (JavaClass superClass : javaClass.getSupers()) {
						if (superClass.isState()) {
							type = VO_TYPE;
							break L2;
						}
					}
				}
				if (type.equals(Unensure_TYPE)) {
					type = Service_TYPE;
				}
			} else {
				type = VO_TYPE;
			}

			if (type.equals(VO_TYPE)) {
				boolean isParamRelation = false;
				M: for (JavaClassRelationItem item : javaClass.getCaItems()) {
					if (item.getType() instanceof ParamRelation) {
						isParamRelation = true;
						break M;
					}
				}
				if (!isParamRelation) {
					type = Service_TYPE;
				}
			}

			this.printTable("类名", javaClass.getName());
			this.printTable("类型", type);
		}
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("识别JavaClass是Service还是VO，规则：<br>");
		explain.append("1、Service : 本身无状态+子类或父类没也没有状态。<br>");
		explain.append("2、VO：有状态+作为其他方法的参数或返回值。<br>");
		return explain.toString();
	}
}
