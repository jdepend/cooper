package jdepend.metadata.tree;

import java.util.ArrayList;
import java.util.List;

import jdepend.metadata.JavaClass;

public class JavaClassNode implements Node {

	private JavaClass javaClass;

	private Integer layer;

	private Node parent;

	private List<Node> children = new ArrayList<Node>();

	public JavaClassNode(JavaClass javaClass, int layer) {
		this.javaClass = javaClass;
		this.layer = layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.tree.Node#getName()
	 */
	public String getName() {
		return this.javaClass.getName();
	}

	public String getPath() {
		return this.javaClass.getName();
	}

	public JavaClass getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(JavaClass javaClass) {
		this.javaClass = javaClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.tree.Node#getLayer()
	 */
	public Integer getLayer() {
		return layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.tree.Node#setLayer(int)
	 */
	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.tree.Node#addLayer(int)
	 */
	public void addLayer(Integer layer) {
		this.layer += layer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.tree.Node#compareTo(jdepend.core.tree.JavaClassNode)
	 */
	public int compareTo(Node n) {
		return (new Integer(this.layer)).compareTo(n.getLayer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.tree.Node#getParent()
	 */
	public Node getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.tree.Node#setParent(jdepend.core.tree.Node)
	 */
	public void setParent(Node parent) {
		this.parent = parent;
		this.parent.addChild(this);
	}

	public void addChild(Node child) {
		this.children.add(child);

	}

	public List<Node> getChildren() {
		return this.children;
	}

	@Override
	public String toString() {
		return this.getPath();
	}

	@Override
	public Integer getSize() {
		return 16;
	}
}
