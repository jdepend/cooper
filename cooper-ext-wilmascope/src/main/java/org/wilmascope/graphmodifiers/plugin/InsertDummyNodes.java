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
package org.wilmascope.graphmodifiers.plugin;

import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.control.GraphControl.Edge;
import org.wilmascope.control.GraphControl.Node;
import org.wilmascope.forcelayout.EdgeForceLayout;
import org.wilmascope.graphmodifiers.GraphModifier;

/**
 * @author dwyer
 * 
 */
public class InsertDummyNodes extends GraphModifier {
	JPanel controls;

	/**
   *  
   */
	public InsertDummyNodes() {
		controls = new JPanel();
		controls.add(new JLabel("Insert dummy nodes in edges that span levels."));
	}

	public String getName() {
		return "Insert Dummy Nodes";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.wilmascope.graphmodifiers.GraphModifier#modify(org.wilmascope.control
	 * .GraphControl.Cluster)
	 */
	public void modify(Cluster r) {
		Properties prop = r.getLayoutEngine().getProperties();
		String levelsString = prop.getProperty("Levels");
		if (levelsString != null) {
			int numLevels = Integer.parseInt(levelsString);
			if (numLevels > 0) {
				for (Edge e : r.getEdges()) {
					Node start = (Node) e.getEdge().getStart().getUserData("Facade");
					Node end = (Node) e.getEdge().getEnd().getUserData("Facade");
					String startLevelString = start.getProperties().getProperty("LevelConstraint");
					String endLevelString = end.getProperties().getProperty("LevelConstraint");
					if (startLevelString != null && endLevelString != null) {
						int startLevel = Integer.parseInt(startLevelString);
						int endLevel = Integer.parseInt(endLevelString);
						int bottom, top;
						if (startLevel < endLevel) {
							bottom = startLevel;
							top = endLevel;
						} else {
							top = startLevel;
							Node swap = end;
							end = start;
							start = swap;
							bottom = endLevel;
						}
						if (top - bottom > 1) {
							String viewType = e.getViewType();
							e.delete();
							for (int i = bottom + 1; i < top; i++) {
								Node dummy = r.addNode();
								dummy.setProperty("LevelConstraint", "" + i);
								dummy.setRadius(0.02f);
								dummy.setMass(0.5f);
								dummy.setProperty("DummyNode", "True");
								Edge dummyEdge = r.addEdge(start, dummy, viewType);
								((EdgeForceLayout) dummyEdge.getEdge().getLayout()).setRelaxedLength(0.1f);

								start = dummy;
							}
							r.addEdge(start, end, viewType);
						}
					}
				}
				r.unfreeze();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.util.Plugin#getControls()
	 */
	public JPanel getControls() {
		return controls;
	}

}
