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
package org.wilmascope.graph;

/**
 * Title: WilmaToo Description: Sequel to the ever popular WilmaScope software
 * Copyright: Copyright (c) 2001 Company: WilmaScope.org
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class ClusterList extends List<Cluster> {
	public ClusterList(ClusterList l) {
		addAll(l);
	}

	public ClusterList() {
	};

	public void add(Cluster c) {
		elements.add(c);
	}

	public void remove(Cluster c) {
		elements.remove(c);
	}

	public void calculateLayout() {
		for (Cluster c : elements) {
			c.calculateLayout();
		}
	}

	/**
	 * applies the layout changes for all clusters that were calculated by a
	 * previous call to {@link org.wilmascope.graph.ClusterList#calculateLayout}
	 * 
	 * @return true if all clusters are balanced
	 */
	public boolean applyLayout() {
		boolean balanced = true;
		for (Cluster c : elements) {
			balanced = c.applyLayout() && balanced;
		}
		return balanced;
	}

	public boolean isAncestor(Cluster c) {
		for (Cluster d : elements) {
			if (d.isAncestor(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * removes the graph element from all clusters in the list
	 * 
	 * @param e
	 *            the GraphElement to remove
	 */
	public void removeFromAll(GraphElement e) {
		for (Cluster c : elements) {
			c.remove(e);
		}
	}
}
