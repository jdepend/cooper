package org.wilmascope.columnlayout;

/**
 * This class is a bit of a hack for creating and adding column clusters.
 * Probably not necessary and should be replaced eventually.
 */
import org.wilmascope.control.GraphControl;
import org.wilmascope.forcelayout.ForceLayout;
import org.wilmascope.forcelayout.Origin;
import org.wilmascope.forcelayout.Spring;
import org.wilmascope.view.GraphElementView;
import org.wilmascope.view.SizeAdjustableNodeView;

public class ColumnCluster {
	public ColumnCluster(GraphControl.Cluster root, float initValue, float bottomRadius, int initLevel,
			String clusterViewType, String nodeViewType) {
		this.root = root;
		lastValue = this.initValue = initValue;
		column = root.addCluster(clusterViewType);
		setLevelConstraint(column, 0);
		this.level = initLevel;
		// layout = new ColLayout(level);
		if (columnStyle == FORCECOLUMNS || columnStyle == DOTCOLUMNS) {
			layout = new ColTubeLayout(level, nodeViewType);
		} else if (columnStyle == WORMS) {
			layout = new FrcLayout();
		}
		initTopRadius = lastTopRadius = bottomRadius;
	}

	public ColumnCluster(String label, GraphControl.Cluster root, float initValue, float bottomRadius, int initLevel,
			String nodeViewType) {
		this.root = root;
		this.label = label;
		lastValue = this.initValue = initValue;
		if (columnStyle == WORMS) {
			column = root.addCluster();
			column.hide();
		} else {
			column = root.addCluster("Column Cluster");
		}
		setLevelConstraint(column, 0);
		column.setLabel(label);
		this.level = initLevel;
		// layout = new ColLayout(level);
		if (columnStyle == FORCECOLUMNS || columnStyle == DOTCOLUMNS) {
			layout = new ColTubeLayout(level, nodeViewType);
		} else if (columnStyle == WORMS) {
			layout = new FrcLayout();
		}
		initTopRadius = lastTopRadius = bottomRadius;
	}

	public void setLabel(String[] labels) {
		column.setLabel(labels);
	}

	interface Layout {
		public GraphControl.Node addNode(float radius);

		int getNextLevel();
	}

	class ColTubeLayout implements Layout {
		String viewType;

		ColTubeLayout(int initLevel, String nodeViewType) {
			this.viewType = nodeViewType;
			ColumnLayout l = new ColumnLayout();
			column.setLayoutEngine(l);
			l.setBaseStratum(initLevel);
		}

		public GraphControl.Node addNode(float radius) {
			GraphControl.Node n = column.addNode(viewType);
			GraphElementView v = n.getView();
			if (v instanceof SizeAdjustableNodeView) {
				((SizeAdjustableNodeView) n.getView()).setEndRadii(lastTopRadius, radius);
			} else {
				n.setRadius(radius);
			}
			return n;
		}

		public int getNextLevel() {
			return ((org.wilmascope.columnlayout.ColumnLayout) column.getCluster().getLayoutEngine()).getStrataCount();
		}
	}

	class ColLayout implements Layout {
		ColLayout(int initLevel) {
			ColumnLayout l = new ColumnLayout();
			column.setLayoutEngine(l);
			l.setBaseStratum(initLevel);
			lastTopNode = topNode = addNode(initTopRadius);
		}

		public GraphControl.Node addNode(float radius) {
			GraphControl.Node n = column.addNode();
			n.hide();
			n.setRadius(n.getRadius() * radius);
			n.setColour(211f / 255f, 199f / 255f, 182f / 255f);
			if (lastTopNode != null) {
				GraphControl.Edge e = column.addEdge(lastTopNode, n, "Tapered Edge");
				e.setColour(211f / 255f, 199f / 255f, 182f / 255f);
			}
			return n;
		}

		public int getNextLevel() {
			return ((org.wilmascope.columnlayout.ColumnLayout) column.getCluster().getLayoutEngine()).getStrataCount() - 1;
		}
	}

	class FrcLayout implements Layout {
		FrcLayout() {
			((ForceLayout) column.getLayoutEngine()).addForce(new Origin(15f));
			((ForceLayout) column.getLayoutEngine()).addForce(new Spring(15f));
			// lastTopNode = topNode = addNode(initTopRadius);
		}

		public GraphControl.Node addNode(float radius) {
			GraphControl.Node n = column.addNode();
			setLevelConstraint(n, level++);
			n.setRadius(n.getRadius() * radius);
			// n.setColour(211f/255f, 199f/255f, 182f/255f);
			n.setColour(102f / 255f, 255f / 255f, 51f / 255f);
			if (lastTopNode != null) {
				GraphControl.Edge e = column.addEdge(lastTopNode, n, "Tapered Edge");
				e.setRelaxedLength(0.001f);
				// e.setColour(211f/255f, 199f/255f, 182f/255f);
				e.setColour(102f / 255f, 255f / 255f, 51f / 255f);
			}
			return n;
		}

		public int getNextLevel() {
			return level - 1;
		}
	}

	public int getNextLevel() {
		return layout.getNextLevel();
	}

	public GraphControl.Node addNode(float value) {
		float topRadius = initTopRadius * value / initValue;
		topNode = layout.addNode(topRadius);
		lastTopRadius = topRadius;
		lastTopNode = topNode;
		lastValue = value;
		return topNode;
	}

	public GraphControl.Node addStraightNode(float value) {
		float topRadius = value;
		lastTopRadius = topRadius;
		topNode = layout.addNode(topRadius);
		lastTopNode = topNode;
		lastValue = value;
		return topNode;
	}

	public GraphControl.Node addVariableNode(float value) {
		float topRadius = value;
		topNode = layout.addNode(topRadius);
		lastTopRadius = topRadius;
		lastTopNode = topNode;
		lastValue = value;
		return topNode;
	}

	public GraphControl.Node addInvisibleNode() {
		return topNode;
	}

	public float getTopRadius() {
		return lastTopRadius;
	}

	public GraphControl.Node addNode() {
		return addNode(lastValue);
	}

	public GraphControl.Node getTopNode() {
		return topNode;
	}

	public static void setColumnStyle(int style) {
		columnStyle = style;
	}

	public static int getColumnStyle() {
		return columnStyle;
	}

	public void skipLevel() {
		level++;
		((ColumnLayout) column.getLayoutEngine()).skipStratum();
	}

	public GraphControl.Cluster getClusterFacade() {
		return column;
	}

	protected static void setLevelConstraint(GraphControl.Node n, int level) {
		n.setProperty("LevelConstraint", "" + level);
	}

	public static final int DOTCOLUMNS = 1;
	public static final int FORCECOLUMNS = 2;
	public static final int WORMS = 3;
	static int columnStyle = DOTCOLUMNS;
	GraphControl.Cluster root;
	GraphControl.Cluster column;
	float lastTopRadius, initTopRadius;
	GraphControl.Node topNode, lastTopNode;
	float initValue, lastValue;
	String label;
	int level = 0;
	Layout layout;
}
