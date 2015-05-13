package jdepend.metadata.tree;

import java.util.ArrayList;
import java.util.List;

public class JavaPackageNode implements Node {

	private String name;

	private String path;

	private Integer layer;

	private Node parent;

	private List<JavaPackageNode> children = new ArrayList<JavaPackageNode>();

	private boolean existJavaClass = false;

	private int classCount;

	private transient Integer size;

	private static int totalClassCount;

	public JavaPackageNode(String name) {
		super();
		this.name = name;
	}

	public void addChild(Node child) {
		if (child instanceof JavaPackageNode) {
			this.children.add((JavaPackageNode) child);
		}
	}

	public void addLayer(Integer layer) {
		this.layer += layer;

	}

	public int compareTo(Node n) {
		return this.name.compareTo(n.getName());
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<JavaPackageNode> getChildren() {
		return this.children;
	}

	public Integer getLayer() {
		return this.layer;
	}

	public String getName() {
		return this.name;
	}

	public Node getParent() {
		return this.parent;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean isExistSelfJavaClass() {
		return existJavaClass;
	}

	public void setExistSelfJavaClass(boolean existJavaClass) {
		this.existJavaClass = existJavaClass;
	}

	public int getClassCount() {
		return classCount;
	}

	public void addClassCount(int count) {
		this.classCount += count;
	}

	@Override
	public String toString() {
		return this.getPath() + " ClassCount: " + this.classCount + " isExistJavaClass: " + this.existJavaClass;
	}

	@Override
	public Integer getSize() {
		if (this.size == null) {
			this.size = absoluteSize();
		}
		return this.size;
	}

	private int absoluteSize() {
		if (this.parent == null) {
			totalClassCount = this.classCount;
		}
		// 全局三段分
		// 三段分
		Float segment = totalClassCount * 1F / 3;
		// 判断段数并设置size
		if (this.classCount < segment) {
			return 8;
		} else if (this.classCount < segment * 2) {
			return 12;
		} else {
			return 16;
		}
	}

	private int relativelySize() {
		if (this.parent != null) {
			int maxSize = 0;
			int currentSize;
			// 计算兄弟节点中Class数量最大的值
			for (Node node : this.parent.getChildren()) {
				currentSize = ((JavaPackageNode) node).getClassCount();
				if (currentSize > maxSize) {
					maxSize = currentSize;
				}
			}
			// 三段分
			Float segment = maxSize * 1F / 3;
			// 判断段数并设置size
			if (this.classCount < segment) {
				return 8;
			} else if (this.classCount < segment * 2) {
				return 12;
			} else {
				return 16;
			}
		} else {
			return 16;
		}
	}
}
