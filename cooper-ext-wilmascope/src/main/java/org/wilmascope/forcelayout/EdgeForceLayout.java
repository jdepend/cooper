/*
 * The following source code is part of the WilmaScope 3D Graph Drawing Engine
 * which is distributed under the terms of the GNU Lesser General Public License
 * (LGPL - http://www.gnu.org/copyleft/lesser.html).
 *
 * As usual we distribute it with no warranties and anything you chose to do
 * with it you do at your own risk.
 *
 * Copyright for this work is retained by Tim Dwyer and the WilmaScope organisation
 * (www.wilmascope.org) however it may be used or modified to work as part of
 * other software subject to the terms of the LGPL.  I only ask that you cite
 * WilmaScope as an influence and inform us (tgdwyer@yahoo.com)
 * if you do anything really cool with it.
 *
 * The WilmaScope software source repository is hosted by Source Forge:
 * www.sourceforge.net/projects/wilma
 *
 * -- Tim Dwyer, 2001
 */
package org.wilmascope.forcelayout;

import java.util.Properties;

import org.wilmascope.graph.EdgeLayout;

/**
 * @author Tim Dwyer
 * @version 1.0
 */

public class EdgeForceLayout extends EdgeLayout {
	public float getRelaxedLength() {
		return relaxedLength;
	}

	public void setRelaxedLength(float relaxedLength) {
		this.relaxedLength = relaxedLength;
	}

	public void setStiffness(float stiffness) {
		this.stiffness = stiffness;
	}

	public float getStiffness() {
		return stiffness;
	}

	private float relaxedLength = Constants.defaultEdgeLength;
	private float stiffness = 1f;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.EdgeLayout#resetProperties()
	 */
	public void resetProperties() {
		Properties p = getEdge().getProperties();
	}
}
