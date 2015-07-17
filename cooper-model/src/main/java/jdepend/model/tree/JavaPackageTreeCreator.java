package jdepend.model.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.metadata.JavaPackage;

public class JavaPackageTreeCreator {

	private transient Map<String, JavaPackageNode> javaPackageNodes;

	public JavaPackageNode createTree(Collection<JavaPackage> javaPackages) {
		int layer = 0;// 用于记录层数
		String segment;
		String path;
		boolean found = true;
		JavaPackageNode node;
		List<JavaPackageNode> roots = new ArrayList<JavaPackageNode>();

		javaPackageNodes = new HashMap<String, JavaPackageNode>();
		// 排序
		List<JavaPackage> listJavaPackages = new ArrayList<JavaPackage>(javaPackages);
		Collections.sort(listJavaPackages);
		// 分段搜索javaPackage，建立javaPackageTree
		while (found) {
			found = false;
			// 搜索layer指定的段
			for (JavaPackage javaPackage : listJavaPackages) {
				// 得到路徑名称
				path = this.getPath(javaPackage.getName(), layer);
				if (path != null) {
					node = this.javaPackageNodes.get(path);
					if (node == null) {
						// 得到段名称
						segment = this.getSegment(javaPackage.getName(), layer);
						// 建立JavaPackageNode
						node = new JavaPackageNode(segment);
						node.setPath(path);
						node.setLayer(layer);
						// 增加到缓存中
						this.javaPackageNodes.put(path, node);
						if (layer == 0) {
							roots.add(node);
						} else {
							JavaPackageNode parent = this.getParent(javaPackage.getName(), layer);
							node.setParent(parent);
							parent.addChild(node);
						}
						if (javaPackage.getName().endsWith(segment)) {
							node.setExistSelfJavaClass(true);
							node.addClassCount(javaPackage.getClassCount());
							// 向父亲追加本次新增的类数量
							this.appendClassCount(node, javaPackage.getClassCount());
						}
						found = true;
					}
				}
			}
			layer++;
		}
		return createRoot(roots);
	}

	private static JavaPackageNode createRoot(List<JavaPackageNode> javaPackageTree) {
		if (javaPackageTree.size() == 1) {
			return javaPackageTree.get(0);
		} else {
			JavaPackageNode root = new JavaPackageNode("root");
			for (JavaPackageNode node : javaPackageTree) {
				root.addChild(node);
				// 设置类数量
				root.addClassCount(node.getClassCount());
			}
			root.setLayer(-1);

			return root;
		}
	}

	private String getSegment(String javaPackageName, int layer) {
		String[] segments = javaPackageName.split("\\.");
		if (segments.length <= layer) {
			return null;
		} else {
			return segments[layer];
		}
	}

	private String getPath(String javaPackageName, int layer) {

		StringBuilder path = new StringBuilder(15);
		int index = 0;
		String[] segments = javaPackageName.split("\\.");
		if (segments.length == layer + 1) {
			return javaPackageName;
		} else {
			for (String segment : segments) {
				if (index > layer) {
					return path.toString();
				} else {
					if (index != 0) {
						path.append(".");
					}
					path.append(segment);
					index++;
				}
			}
			return null;
		}

	}

	private JavaPackageNode getParent(String javaPackageName, int layer) {
		if (layer == 0)
			return null;
		String parentPath = this.getPath(javaPackageName, layer - 1);
		if (parentPath != null) {
			return this.javaPackageNodes.get(parentPath);
		}
		return null;
	}

	private void appendClassCount(JavaPackageNode node, int appendCount) {
		JavaPackageNode parent = (JavaPackageNode) node.getParent();
		if (parent != null) {
			parent.addClassCount(appendCount);
			this.appendClassCount(parent, appendCount);
		}
	}
}
