package org.wilmascope.columnlayout;

import java.util.Properties;
import java.util.Vector;

import javax.swing.JPanel;
import javax.vecmath.Point3f;

import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;

/**
 * Column layout simply places each node in the cluster at the same x,y position
 * as the root cluster but with the z determined by each node's stratum
 */
public class ColumnLayout extends LayoutEngine {
	Vector extraSpacing = new Vector();

	public void setExtraSpacing(Vector extraSpacing) {
		this.extraSpacing = extraSpacing;
	}

	public Vector getExtraSpacing() {
		return extraSpacing;
	}

	public void calculateLayout() {
	}

	public boolean applyLayout() {
		Point3f rootPosition = getRoot().getPosition();
		NodeList nodes = getRoot().getNodes();
		getRoot().setMass(1f);
		for (Node node : nodes) {
			Point3f position = node.getPosition();
			NodeColumnLayout nodeLayout = (NodeColumnLayout) node.getLayout();
			position.x = rootPosition.x;
			position.y = rootPosition.y;
			int s = nodeLayout.getStratum();
			float extraSpace = 0;
			for (int i = 0; i < s && i < extraSpacing.size(); i++) {
				extraSpace += ((Float) extraSpacing.get(i)).floatValue();
			}
			position.z = rootPosition.z + strataSeparation * s + extraSpace;
		}
		return true;
	}

	public void setBalanced(boolean balanced) {
		/** @todo: Implement this org.wilmascope.graph.LayoutEngine method */
		throw new java.lang.UnsupportedOperationException("Method setBalanced() not yet implemented.");
	}

	public org.wilmascope.graph.NodeLayout createNodeLayout(Node n) {
		return new NodeColumnLayout(strataCount++);
	}

	public org.wilmascope.graph.EdgeLayout createEdgeLayout(org.wilmascope.graph.Edge e) {
		return new EdgeColumnLayout();
	}

	public void setBaseStratum(int stratum) {
		baseStratum = strataCount = stratum;
	}

	public int getBaseStratum() {
		return baseStratum;
	}

	public int getStrataCount() {
		return strataCount;
	}

	public void skipStratum() {
		strataCount++;
	}

	public void reset() {
	}

	public void setStrataSeparation(float strataSeparation) {
		this.strataSeparation = strataSeparation;
	}

	public float getStrataSeparation() {
		return strataSeparation;
	}

	public float getHeight() {
		NodeList nodes = getRoot().getAllNodes();
		float zMax = Float.MIN_VALUE, zMin = Float.MAX_VALUE;
		for (org.wilmascope.graph.Node n : nodes) {
			Point3f t = n.getPosition();
			if (t.z > zMax) {
				zMax = t.z;
			}
			if (t.z < zMin) {
				zMin = t.z;
			}
		}
		zMax += ((NodeColumnLayout) nodes.get(nodes.size() - 1).getLayout()).getHeight() / 2;
		zMin -= ((NodeColumnLayout) nodes.get(0).getLayout()).getHeight() / 2;
		return zMax - zMin;
	}

	private float strataSeparation;
	int strataCount = 0;
	int baseStratum = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getControls()
	 */
	public JPanel getControls() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getProperties()
	 */
	public Properties getProperties() {
		super.getProperties().setProperty("BaseLevel", "" + getBaseStratum());
		super.getProperties().setProperty("LevelSeparation", "" + getStrataSeparation());
		return super.getProperties();
	}

	public void resetProperties() {
		setBaseStratum(Integer.parseInt(super.getProperties().getProperty("BaseLevel", "0")));
		setStrataSeparation(Float.parseFloat(super.getProperties().getProperty("LevelSeparation", "1.0")));
	}

	public String getName() {
		return "ColumnLayout";
	}

}
