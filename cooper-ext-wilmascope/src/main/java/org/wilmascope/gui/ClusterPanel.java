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
package org.wilmascope.gui;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.PickListener;
import org.wilmascope.forcelayout.ForceLayout;

public class ClusterPanel extends MultiPickPanel {

	public ClusterPanel(ControlPanel controlPanel, GraphControl.Cluster cluster) {
		super(controlPanel, cluster);
	}

	void okButton_actionPerformed(ActionEvent e) {
		PickListener pl = GraphControl.getPickListener();
		if (pl.getPickedListSize() == 0) {
			return;
		}
		Vector nodes = new Vector();
		while (pl.getPickedListSize() > 0) {
			GraphControl.Node element = (GraphControl.Node) pl.pop();
			nodes.add(element);
		}
		GraphControl.Cluster newCluster = cluster.addCluster(nodes);
		newCluster.setLayoutEngine(ForceLayout.createDefaultClusterForceLayout(newCluster.getCluster()));
		cluster.unfreeze();
		cleanup();
	}

	String getLabel() {
		return "Select nodes to cluster...";
	}
}
