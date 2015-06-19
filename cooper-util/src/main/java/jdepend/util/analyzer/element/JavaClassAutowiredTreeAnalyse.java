package jdepend.util.analyzer.element;

import jdepend.metadata.JavaClass;
import jdepend.metadata.tree.JavaClassAutowiredTreeCreator;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public class JavaClassAutowiredTreeAnalyse extends AbstractAnalyzer {

	private static final long serialVersionUID = -4243756500532110103L;

	private String className;

	public JavaClassAutowiredTreeAnalyse() {
		super("Autowired结构分析", Analyzer.Attention, "Autowired结构分析");
	}

	@Override
	protected void doExecute(AnalysisResult result) throws AnalyzerException {

		if (this.className == null) {
			throw new AnalyzerException("请指定ClassName");
		}

		JavaClassAutowiredTreeCreator creator = new JavaClassAutowiredTreeCreator();

		JavaClass javaClass = null;
		for (JavaClassUnit javaClassUnit : result.getClasses()) {
			if (javaClassUnit.getName().equals(className)) {
				javaClass = javaClassUnit.getJavaClass();
				break;
			}
		}

		if (javaClass == null) {
			throw new AnalyzerException("没有找到名字为[" + className + "]的类");
		}

		JavaClassTree tree = creator.create(javaClass);

		if (tree != null) {
			this.printTree(tree.getRoots().get(0));
		}

	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("<strong>Autowired结构分析</strong>查看Spring的Autowired树结构。<br>");
		return explain.toString();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
