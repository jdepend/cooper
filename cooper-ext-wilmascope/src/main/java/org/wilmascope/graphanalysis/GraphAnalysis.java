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
package org.wilmascope.graphanalysis;

import javax.swing.JPanel;

import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.util.Plugin;

/**
 * A graph analysis plug-in must extend the
 * org.wilmascope.graphanalysis.GraphAnalysis class, implementing the getName
 * and analyse methods. As with GraphModifier, getName simply returns a
 * descriptive string. The analyse method takes no argument. Rather, you must
 * use getCluster() to operate on the cluster in the analyse method.
 * 
 * There is no need for a getControls method in GraphAnalysis plug-ins. By
 * default an instance of AnalysisPanel is created by this super class.
 * AnalysisPanel allows a user to choose visual mappings for the results of the
 * analysis, and appears in the Graph Analysis window.
 * 
 * @author dwyer
 */

public abstract class GraphAnalysis implements Plugin {
	public abstract void analyse();

	/*
	 * @see org.wilmascope.util.Plugin#getControls()
	 */
	public JPanel getControls() {
		return new AnalysisPanel(this);
	}

	public Cluster getCluster() {
		if (cluster == null) {
			throw new Error("Must call setCluster(c) for GraphAnalysis classes before calling analyse()");
		}
		return cluster;
	}

	public void setCluster(Cluster c) {
		cluster = c;
	}

	Cluster cluster;
}
