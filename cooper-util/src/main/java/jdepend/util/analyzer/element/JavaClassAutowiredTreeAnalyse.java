package jdepend.util.analyzer.element;

import jdepend.framework.domain.notPersistent;
import jdepend.metadata.JavaClass;
import jdepend.metadata.tree.JavaClassAutowiredTreeCreator;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;
import jdepend.util.analyzer.framework.ClassAttribute;

public class JavaClassAutowiredTreeAnalyse extends AbstractAnalyzer {

	private static final long serialVersionUID = -4243756500532110103L;

	private transient JavaClass javaClass;

	public JavaClassAutowiredTreeAnalyse() {
		super("Autowired结构分析", Analyzer.Attention, "Autowired结构分析");
	}

	@Override
	protected void doExecute(AnalysisResult result) throws AnalyzerException {

		if (this.javaClass == null) {
			throw new AnalyzerException("请指定javaClass");
		}

		JavaClassAutowiredTreeCreator creator = new JavaClassAutowiredTreeCreator();
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

	public JavaClass getJavaClass() {
		return javaClass;
	}

	@notPersistent
	@ClassAttribute
	public void setJavaClass(JavaClass javaClass) {
		this.javaClass = javaClass;
	}


}
