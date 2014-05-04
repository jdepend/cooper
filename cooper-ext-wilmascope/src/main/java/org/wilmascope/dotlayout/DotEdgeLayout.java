package org.wilmascope.dotlayout;

import org.wilmascope.graph.EdgeLayout;

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

public class DotEdgeLayout extends EdgeLayout {
	public DotEdgeLayout() {
	}

	public void setZLevel(int z) {
		this.z = z;
	}

	public int getZLevel() {
		return z;
	}

	int z = 0;
}
