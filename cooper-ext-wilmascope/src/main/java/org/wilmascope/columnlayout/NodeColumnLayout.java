package org.wilmascope.columnlayout;

import javax.vecmath.Vector3f;

import org.wilmascope.forcelayout.NodeForceLayout;
import org.wilmascope.global.GlobalConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class NodeColumnLayout extends NodeForceLayout {
	public NodeColumnLayout(int level) {
		setLevel(level);
		setHeight(GlobalConstants.getInstance().getFloatValue("ImportedColumnNodeHeight"));
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStratum() {
		return level;
	}

	public void addForce(Vector3f f) {
		((NodeForceLayout) getNode().getOwner().getLayout()).addForce(f);
	}

	public void subForce(Vector3f f) {
		((NodeForceLayout) getNode().getOwner().getLayout()).subForce(f);
	}

	int level;
	float height;

	/**
	 * @return height of the node, often different from radius for nodes in
	 *         Column Clusters
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param f
	 *            height of the node, often different from radius for nodes in
	 *            Column Clusters
	 */
	public void setHeight(float f) {
		height = f;
	}

}
