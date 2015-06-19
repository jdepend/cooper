package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdepend.metadata.tree.JavaClassInheritTreesCreator;
import jdepend.metadata.tree.JavaClassTree;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public class InheritTreeAnalyse extends AbstractAnalyzer {

	private int node;// 继承树节点数量

	private int width;// 继承树宽度

	private int deep;// 继承树深度
	/**
	 * 
	 */
	private static final long serialVersionUID = -8957118649761919194L;

	public InheritTreeAnalyse() {
		super("继承结构分析", Analyzer.Attention, "继承结构分析");
	}

	@Override
	protected void doExecute(AnalysisResult result) throws AnalyzerException {

		List<JavaClassTree> trees = (new JavaClassInheritTreesCreator()).create(JavaClassUnitUtil.getJavaClasses(result
				.getClasses()));
		// 打印继承树
		this.isPrintTab(false);
		Collections.sort(trees);
		List<JavaClassTree> printTrees = new ArrayList<JavaClassTree>();
		for (JavaClassTree tree : trees) {
			if (this.isPrintTree(tree)) {
				printTrees.add(tree);

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
		explain.append("<strong>继承</strong>是面向对象语言提供的复用手段之一。一个项目中规模较大的继承树一般都是业务逻辑较集中的地方。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;<strong>继承结构分析</strong>就是识别在当前分析结果中规模较大的继承树。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;继承树有“宽而浅”和“窄而深”两种表现比较极端的形态。“宽而浅”就是继承的深度不大，而在同一继承层的元素较多，“窄而深”恰恰相反。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;“宽而浅”的结构比较简单，多数子类继承的父类往往是技术方面的复用。“窄而深”则是对业务逻辑进行了深入分析，抽取了较为细致的复用单元，但存在过度的可能性以及容易出现面对需求变化修改困难的局面，故“窄而深”的结构需要对业务做较为深入的交流。<br>");
		explain.append("<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;设计继承结构有一些约定：<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、接口上的方法应表现该接口的主要职责（负责实现类状态设置的方法不应该出现在接口签名上）。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、实现多个接口的实现类说明它具有多个职责。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、抽象类都会有抽象方法（子类必须覆盖）。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4、在继承树上一般都会应用模板模式来规范不同逻辑职责单元的调用顺序。并预留出一些增加新逻辑的地点。对于抽象了核心执行顺序的方法一般都通过final关键字来避免子类覆盖。<br>");
		explain.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5、为了在继承树上避免修改父类或子类方法签名而丢失多态效果的情况发生，在JDK1.5以上都会使用Override注解来引起修改者的注意，并在修改这些方法签名时报编译错误。<br>");
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
