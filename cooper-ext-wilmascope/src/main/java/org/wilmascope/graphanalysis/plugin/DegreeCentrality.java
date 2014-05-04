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
package org.wilmascope.graphanalysis.plugin;

import org.wilmascope.control.GraphControl.Node;
import org.wilmascope.graphanalysis.GraphAnalysis;

/**
 * Calculate degree centrality for each node, ie for node v in V:
 * degreecentrality(v) = degree(v)/max(degree(w)|w in V)
 * 
 * @author dwyer
 */
public class DegreeCentrality extends GraphAnalysis {

	/*
	 * @see
	 * org.wilmascope.graphanalysis.GraphAnalysis#analyse(org.wilmascope.control
	 * .GraphControl.Cluster)
	 */
	public void analyse() {
		int maxDegree = 0;
		for (Node n : getCluster().getNodes()) {
			int degree = n.getDegree();
			if (degree > maxDegree) {
				maxDegree = degree;
			}
		}
		if (maxDegree > 0) {
			for (Node n : getCluster().getNodes()) {
				float degreeCentrality = (float) n.getDegree() / (float) maxDegree;
				n.getProperties().setProperty(getName(), "" + degreeCentrality);
			}
		}
	}

	public String getName() {
		return "Degree Centrality";
	}
}
