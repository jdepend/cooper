package jdepend.model.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.model.JavaClassUnit;

public class JavaClassTree implements Comparable<JavaClassTree>, Serializable {

	private List<JavaClassNode> nodes = new ArrayList<JavaClassNode>();

	private transient List<JavaClassUnit> javaClasses;

	private boolean close;

	private JavaClassNode currentNode;

	public JavaClassTree(JavaClassUnit javaClass) {
		JavaClassNode node = new JavaClassNode(javaClass, 1);
		this.nodes.add(node);
		this.currentNode = node;
		this.close = false;
	}

	public String getName() {
		for (Node node : this.getRoots()) {
			return node.getName();
		}
		return null;
	}

	/**
	 * 替换root
	 * 
	 * @param javaClass
	 */
	public void setRoot(JavaClassUnit javaClass, JavaClassUnit originalRootClass) {

		this.addLayer(1);

		JavaClassNode root = new JavaClassNode(javaClass, 1);
		this.nodes.add(0, root);

		Node originalRoot = this.getTheNode(originalRootClass);
		originalRoot.setParent(root);

	}

	private void addLayer(int layer) {
		for (Node node : this.nodes) {
			node.addLayer(layer);
		}
	}

	/**
	 * 向树插入中间节点
	 * 
	 * @param javaClass
	 *            插入的节点
	 * @param downClass
	 */
	public void insertNode(JavaClassUnit javaClass, JavaClassUnit downClass) {

		if (this.contains(javaClass)) {
			return;
		}

		Node downNode = this.getTheNode(downClass);
		JavaClassNode insertNode = new JavaClassNode(javaClass, downNode.getLayer() - 1);

		this.nodes.add(insertNode);
	}

	/**
	 * 向树追加节点
	 * 
	 * @param upClass
	 * @param javaClass
	 *            插入的节点
	 */
	public void addNode(JavaClassUnit upClass, JavaClassUnit javaClass) {

		if (this.contains(javaClass)) {
			return;
		}

		Node upNode = this.getTheNode(upClass);
		JavaClassNode addNode = new JavaClassNode(javaClass, upNode.getLayer() + 1);
		addNode.setParent(upNode);

		this.nodes.add(addNode);

		this.currentNode = addNode;
	}

	public List<JavaClassUnit> getJavaClassRoots() {
		List<JavaClassUnit> javaClasses = new ArrayList<JavaClassUnit>();
		for (JavaClassNode node : this.nodes) {
			if (node.getLayer() == 1) {
				javaClasses.add(node.getJavaClass());
			}
		}
		return javaClasses;
	}

	public List<JavaClassNode> getRoots() {
		List<JavaClassNode> nodes = new ArrayList<JavaClassNode>();
		for (JavaClassNode node : this.nodes) {
			if (node.getLayer() == 1) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public Node getTheNode(JavaClassUnit javaClass) {
		for (JavaClassNode node : this.nodes) {
			if (node.getJavaClass().equals(javaClass)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * 当前分析单元
	 * 
	 * @return
	 */
	public JavaClassUnit getCurrent() {
		if (this.currentNode == null) {
			return null;
		} else {
			return this.currentNode.getJavaClass();
		}
	}

	public JavaClassNode getCurrentNode() {
		return this.currentNode;
	}

	public int getNodeNumber() {
		return this.nodes.size();
	}

	public List<JavaClassNode> getNodes() {
		return this.nodes;
	}

	public List<JavaClassUnit> getClasses() {
		if (this.javaClasses == null) {
			this.javaClasses = new ArrayList<JavaClassUnit>();
			for (JavaClassNode node : this.getNodes()) {
				this.javaClasses.add(node.getJavaClass());
			}
		}
		return javaClasses;
	}

	public boolean contains(JavaClassUnit javaClass) {
		if (this.close) {
			return false;
		}
		for (JavaClassNode node : this.nodes) {
			if (node.getJavaClass().equals(javaClass)) {
				return true;
			}
		}
		return false;
	}

	public int getWidth() {

		Map<Integer, Integer> widths = new HashMap<Integer, Integer>();
		for (int layer = 1; layer <= this.getDeep(); layer++) {
			widths.put(layer, 0);
		}
		for (Node node : this.nodes) {
			try {
				widths.put(node.getLayer(), widths.get(node.getLayer()) + 1);
			} catch (Exception e) {
				System.out.println(node);
			}
		}

		int width = 0;
		for (int i = 1; i <= this.getDeep(); i++) {
			if (widths.get(i) > width) {
				width = widths.get(i);
			}
		}

		return width;
	}

	public int getDeep() {
		int layer = 0;
		for (Node node : this.nodes) {
			if (node.getLayer() > layer) {
				layer = node.getLayer();
			}
		}
		return layer;
	}

	public void appendTree(JavaClassUnit currentClass, JavaClassTree tree) {
		JavaClassNode currentNode = (JavaClassNode) this.getTheNode(currentClass);
		tree.addLayer(currentNode.getLayer());
		for (JavaClassNode root : tree.getRoots()) {
			root.setParent(currentNode);
		}
		for (JavaClassNode node : tree.nodes) {
			this.nodes.add(node);
		}
		tree.close();
	}

	public void mergeTree(JavaClassTree tree, JavaClassUnit current, JavaClassUnit depend) {
		JavaClassNode currentNode = (JavaClassNode) this.getTheNode(current);
		JavaClassNode theNode = (JavaClassNode) tree.getTheNode(depend);
		if (currentNode.getLayer() < theNode.getLayer() - 1) {
			int diffLayer = theNode.getLayer() - 1 - currentNode.getLayer();
			this.addLayer(diffLayer);
		} else {
			int diffLayer = currentNode.getLayer() - (theNode.getLayer() - 1);
			tree.addLayer(diffLayer);
		}
		for (JavaClassNode node : tree.nodes) {
			this.nodes.add(node);
		}
		tree.close();

	}

	private void close() {
		this.close = true;
	}

	public boolean isClose() {
		return close;
	}

	@Override
	public String toString() {

		if (this.close) {
			return "该树已经关闭";
		}

		Collections.sort(this.nodes);

		StringBuilder content = new StringBuilder(100);
		for (JavaClassNode node : this.nodes) {
			content.append("Layer[");
			content.append(node.getLayer());
			content.append("] Name [");
			content.append(node.getJavaClass().getName());
			content.append("]\n");
		}

		content.append("\n");
		content.append("sum : ");
		content.append(" nodes : ");
		content.append(this.getNodeNumber());
		content.append(" width : ");
		content.append(this.getWidth());
		content.append(" deep : ");
		content.append(this.getDeep());
		content.append("\n");

		return content.toString();
	}

	public int compareTo(JavaClassTree obj) {
		return (new Integer(obj.getNodeNumber())).compareTo(this.getNodeNumber());
	}
}
