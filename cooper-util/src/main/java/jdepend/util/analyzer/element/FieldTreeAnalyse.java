package jdepend.util.analyzer.element;

import java.util.Collections;
import java.util.List;

import jdepend.metadata.tree.JavaClassFieldTreesCreator;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public class FieldTreeAnalyse extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2825790477419097031L;

	private int node;// 包含树节点数量

	private int width;// 包含树宽度

	private int deep;// 包含树深度

	public FieldTreeAnalyse() {
		super("包含结构分析", Analyzer.Attention, "包含结构分析");
	}

	protected void doSearch(AnalysisResult result) throws AnalyzerException {

		List<JavaClassTree> trees = (new JavaClassFieldTreesCreator()).create(JavaClassUnitUtil.getJavaClasses(result
				.getClasses()));
		// 打印包含树
		this.isPrintTab(false);
		Collections.sort(trees);
		for (JavaClassTree tree : trees) {
			if (this.isPrintTree(tree)) {
				this.print(tree.toString());
				this.print("\n\n");
			}
		}
	}

	private boolean isPrintTree(JavaClassTree tree) {

		boolean isNode = true;
		boolean isWidth = true;
		boolean isDeep = true;

		if (this.node != 0) {
			if (tree.getNodeNumber() < this.node) {
				isNode = false;
			}
		}
		if (this.width != 0) {
			if (tree.getWidth() < this.width) {
				isWidth = false;
			}
		}
		if (this.deep != 0) {
			if (tree.getDeep() < this.deep) {
				isDeep = false;
			}
		}
		return isNode && isWidth && isDeep;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("<strong>包含关系</strong>是类两种关系（have a和is a）之一，并存在多级包含（a包含b，b包含c）的情况。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>包含关系分析</strong>是识别在当前分析结果中规模较大的包含关系体。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;包含关系有两种形成的原因，一个是领域中固有的关系，在采用软件模拟这种关系时自然的延续下来。另一个是为了实现一些技术上的需要而被程序员“设计”出的包含关系。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;包含者和被包含者的生命周期的异同是包含关系很重要的一个属性。对于领域形成关系，其生命周期往往取决于对领域的认识 ，而由于技术形成的关系，其被包含的对象的创建时机会设计取决于使用方便性、性能等因素的考虑。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;对象是否有状态与该对象包含的对象是否存在状态有关，也就是包含了有状态对象的对象也有状态。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;对于采用序列化技术持久化或进行网络传输的对象，其状态是否也序列化是一个话题。一般上对于可以通过其他状态计算得到的状态可以不序列化以减轻对网络传输带宽的压力，但会在反序列化时增加对CPU的负担。<br>");
		return explain.toString();
	}

	public int getNode() {
		return node;
	}

	public void setNode(int node) {
		this.node = node;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getDeep() {
		return deep;
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}
}
