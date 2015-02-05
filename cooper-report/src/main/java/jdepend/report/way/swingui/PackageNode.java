package jdepend.report.way.swingui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import jdepend.model.JDependUnit;

/**
 * The <code>PackageNode</code> class defines the default behavior for tree
 * nodes representing Java packages.
 * 
 * @author <b>Abner</b>
 * 
 */

public abstract class PackageNode {

	private PackageNode parent;

	private JDependUnit jPackage;

	private ArrayList children;

	private static NumberFormat formatter;
	static {
		formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
	}

	/**
	 * Constructs a <code>PackageNode</code> with the specified package and its
	 * collection of dependent packages.
	 * 
	 * @param parent
	 *            Parent package node.
	 * @param jPackage
	 *            Java package.
	 */
	public PackageNode(PackageNode parent, JDependUnit jPackage) {
		this.parent = parent;
		this.jPackage = jPackage;
		children = null;
	}

	/**
	 * Returns the Java package represented in this node.
	 * 
	 * @return Java package.
	 */
	public JDependUnit getPackage() {
		return jPackage;
	}

	/**
	 * Returns the parent of this package node.
	 * 
	 * @return Parent package node.
	 */
	public PackageNode getParent() {
		return parent;
	}

	/**
	 * Indicates whether this node is a leaf node.
	 * 
	 * @return <code>true</code> if this node is a leaf; <code>false</code>
	 *         otherwise.
	 */
	public boolean isLeaf() {
		if (getCoupledPackages().size() > 0) {
			return false;
		}

		return true;
	}

	/**
	 * Creates and returns a <code>PackageNode</code> with the specified parent
	 * node and Java package.
	 * 
	 * @param parent
	 *            Parent package node.
	 * @param jPackage
	 *            Java package.
	 * @return A non-null <code>PackageNode</code.
	 */
	protected abstract PackageNode makeNode(PackageNode parent, JDependUnit jPackage);

	/**
	 * Returns the collection of Java packages coupled to the package
	 * represented in this node.
	 * 
	 * @return Collection of coupled packages.
	 */
	protected abstract Collection getCoupledPackages();

	/**
	 * Indicates whether the specified package should be displayed as a child of
	 * this node.
	 * 
	 * @param jPackage
	 *            Package to test.
	 * @return <code>true</code> to display the package; <code>false</code>
	 *         otherwise.
	 */
	public boolean isChild(JDependUnit jPackage) {
		return true;
	}

	/**
	 * Returns the child package nodes of this node.
	 * 
	 * @return Collection of child package nodes.
	 */
	public ArrayList getChildren() {

		if (children == null) {

			children = new ArrayList();
			ArrayList packages = new ArrayList(getCoupledPackages());
			Collections.sort(packages);
			Iterator i = packages.iterator();
			while (i.hasNext()) {
				JDependUnit jPackage = (JDependUnit) i.next();
				if (isChild(jPackage)) {
					PackageNode childNode = makeNode(this, jPackage);
					children.add(childNode);
				}
			}
		}

		return children;
	}

	/**
	 * Returns the string representation of this node's metrics.
	 * 
	 * @return Metrics string.
	 */
	public String toMetricsString() {
		StringBuilder label = new StringBuilder();
		label.append(getPackage().getName());
		label.append("  (");
		label.append("LC: " + getPackage().getLineCount() + "  ");
		label.append("CN: " + getPackage().getClassCount() + "  ");
		label.append("CC: " + getPackage().getConcreteClassCount() + "  ");
		label.append("AC: " + getPackage().getAbstractClassCount() + "  ");
		label.append("Ca: " + getPackage().getAfferentCoupling() + "  ");
		label.append("Ce: " + getPackage().getEfferentCoupling() + "  ");
		label.append("A: " + format(getPackage().getAbstractness()) + "  ");
		label.append("I: " + format(getPackage().getStability()) + "  ");
		label.append("D: " + format(getPackage().getDistance()) + "  ");
		label.append("Coupling: " + format(jPackage.getCoupling()) + "  ");
		label.append("Cohesion: " + format(jPackage.getCohesion()) + "  ");
		if (getPackage().getContainsCycle()) {
			label.append(" Cyclic");
		}

		label.append(")");

		return label.toString();
	}

	/**
	 * Returns the string representation of this node in it's current tree
	 * context.
	 * 
	 * @return Node label.
	 */
	public String toString() {

		if (getParent().getParent() == null) {
			return toMetricsString();
		}

		return getPackage().getName();
	}

	/*
	 * Returns the specified number in a displayable format. @param number
	 * Number to format. @return Formatted number.
	 */
	private static String format(float f) {
		return formatter.format(f);
	}
}
