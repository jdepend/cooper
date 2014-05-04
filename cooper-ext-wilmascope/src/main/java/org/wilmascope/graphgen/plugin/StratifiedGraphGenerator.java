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
package org.wilmascope.graphgen.plugin;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.wilmascope.columnlayout.ColumnCluster;
import org.wilmascope.control.GraphControl;
import org.wilmascope.graphgen.GraphGenerator;

/**
 * Creates a small "stratified graph": that is, an evolving graph with each
 * iteration of the graph arranged in a plane, and the set of planes for all
 * time steps stacked into the third dimension.
 * 
 * @author dwyer
 */
public class StratifiedGraphGenerator extends GraphGenerator {

	JPanel controlPanel;

	public StratifiedGraphGenerator() {
		controlPanel = new JPanel();
		controlPanel.add(new JLabel("Generates simple stratified graph... no controls at the moment"));
	}

	public String getName() {
		return "Stratified";
	}

	public void generate(GraphControl gc) {
		GraphControl.Cluster r = gc.getRootCluster();
		// r = r.addCluster();
		/*
		 * ForceLayout f = new ForceLayout(); r.setLayoutEngine(f);
		 * f.setVelocityAttenuation(0.001f); f.setFrictionCoefficient(90f);
		 * f.addForce(new Spring(0.1f)); f.addForce(new Repulsion(5f, 100f));
		 * f.addForce(new Origin(2f));
		 */

		ColumnCluster.setColumnStyle(ColumnCluster.WORMS);
		// String edgeStyle = "SplineTube";
		String nodeStyle = "Tube Node";
		// String nodeStyle = "DefaultNodeView";
		String edgeStyle = "Arrow";
		ColumnCluster ca = new ColumnCluster("ABC", r, 1.0f, 1.0f, 0, nodeStyle);
		ColumnCluster cb = new ColumnCluster("DEF", r, 1.5f, 1.5f, 0, nodeStyle);
		ColumnCluster cc = new ColumnCluster("GHI", r, 1.0f, 1.0f, 0, nodeStyle);
		ColumnCluster cd = new ColumnCluster("JKL", r, 1.0f, 1.0f, 0, nodeStyle);

		GraphControl.Node a1 = ca.addNode(1.5f);
		GraphControl.Node a2 = ca.addNode(1.0f);
		GraphControl.Node a3 = ca.addNode(1.0f);
		GraphControl.Node a4 = ca.addNode(0.8f);
		GraphControl.Node b1 = cb.addNode(1f);
		GraphControl.Node b2 = cb.addNode(1.5f);
		GraphControl.Node b3 = cb.addNode(1.0f);
		GraphControl.Node b4 = cb.addNode(1.2f);
		GraphControl.Node c1 = cc.addNode(1.0f);
		GraphControl.Node c2 = cc.addNode(1.2f);
		GraphControl.Node c3 = cc.addNode(1.4f);
		GraphControl.Node c4 = cc.addNode(1.5f);
		GraphControl.Node d1 = cd.addNode(1.0f);
		GraphControl.Node d2 = cd.addNode(1.2f);
		GraphControl.Node d3 = cd.addNode(1.4f);
		GraphControl.Node d4 = cd.addNode(1.5f);
		r.addEdge(a2, c2, edgeStyle, 0.008f).setColour(2f / 4f, 2f / 4f, 1f);
		r.addEdge(b1, c1, edgeStyle, 0.01f).setColour(3f / 4f, 3f / 4f, 1f);
		r.addEdge(b3, d3, edgeStyle, 0.01f).setColour(3f / 4f, 3f / 4f, 1f);
		r.addEdge(a1, d1, edgeStyle, 0.015f).setColour(1f, 1f, 1f);
		r.addEdge(a4, d4, edgeStyle, 0.015f).setColour(1f, 1f, 1f);

		gc.unfreeze();

	}

	public JPanel getControls() {
		return controlPanel;
	}

}
