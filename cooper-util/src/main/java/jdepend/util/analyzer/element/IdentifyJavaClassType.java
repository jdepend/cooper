package jdepend.util.analyzer.element;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Method;
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
		boolean ensure_VO_TYPE;
		String index;

		for (JavaClass javaClass : result.getClasses()) {
			type = Unensure_TYPE;
			ensure_VO_TYPE = false;
			index = "";
			if (javaClass.isInner()) {
				if (!javaClass.isState()) {
					if (javaClass.getMethods().size() == 1 && javaClass.getMethods().iterator().next().isConstruction()) {
						type = VO_TYPE;
						index = "1";
						ensure_VO_TYPE = true;
					}
					if (type.equals(Unensure_TYPE)) {
						L1: for (JavaClass subClass : javaClass.getSubClasses()) {
							if (subClass.isState()) {
								type = VO_TYPE;
								index = "2";
								break L1;
							}
						}
					}
					if (type.equals(Unensure_TYPE)) {
						L2: for (JavaClass superClass : javaClass.getSupers()) {
							if (superClass.isState()) {
								type = VO_TYPE;
								index = "3";
								break L2;
							}
						}
					}
					if (type.equals(Unensure_TYPE)) {
						type = Service_TYPE;
						index = "4";
					}
				} else {
					type = VO_TYPE;
					index = "5";
				}

				if (type.equals(VO_TYPE) && !ensure_VO_TYPE) {

					boolean haveBusinessMethod = false;
					O: for (Method method : javaClass.getMethods()) {
						if (!method.isConstruction() && !method.getName().startsWith("get")
								&& !method.getName().startsWith("set")) {
							haveBusinessMethod = true;
							break O;
						}
					}
					if (!haveBusinessMethod) {
						type = VO_TYPE;
						index += "6";
						ensure_VO_TYPE = true;
					}

					if (!ensure_VO_TYPE) {
						L1: for (JavaClass superClass : javaClass.getSupers()) {
							if (superClass.isState()) {
								type = VO_TYPE;
								index += "7";
								ensure_VO_TYPE = true;
								break L1;
							}
						}
					}

					if (!ensure_VO_TYPE) {
						boolean isParamRelation = false;
						M: for (JavaClassRelationItem item : javaClass.getCaItems()) {
							if (item.getType() instanceof ParamRelation) {
								isParamRelation = true;
								break M;
							}
						}
						if (!isParamRelation) {
							type = Service_TYPE;
							index += "9";
						} else {
							type = VO_TYPE;
							index += "10";
							ensure_VO_TYPE = true;
						}
					}
				}
			}

			this.printTable("类名", javaClass.getName());
			this.printTable("类型", type + index);
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
