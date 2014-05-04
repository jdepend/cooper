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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.control.GraphControl.Edge;
import org.wilmascope.control.GraphControl.Node;
import org.wilmascope.forcelayout.ForceLayout;
import org.wilmascope.graphmodifiers.GraphModifier;
import org.wilmascope.gui.SpinnerSlider;
import org.wilmascope.view.ViewManager;
import org.wilmascope.view.ViewManager.UnknownViewTypeException;

/**
 * Copy the specified cluster and add the copy/ies to the cluster's owner
 * 
 * @author dwyer
 */
public class CopyCluster extends GraphModifier {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.util.Plugin#getName()
	 */
	int copyCount = 1;

	boolean levels = false;

	boolean linkDuplicates = false;

	public CopyCluster() {
		// User Interface
		final SpinnerSlider numberSlider = new SpinnerSlider("Number of copies", 1, 10, copyCount);
		final JCheckBox levelConstraintsCB = new JCheckBox("Constrain to Levels?");
		final JCheckBox linkDuplicatesCB = new JCheckBox("Link duplicates?");
		levelConstraintsCB.setSelected(levels);
		linkDuplicatesCB.setSelected(linkDuplicates);
		numberSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				copyCount = numberSlider.getValue();
			}
		});
		levelConstraintsCB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				levels = levelConstraintsCB.isSelected();
			}

		});
		linkDuplicatesCB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				linkDuplicates = linkDuplicatesCB.isSelected();
			}

		});
		controlPanel.add(numberSlider);
		controlPanel.add(levelConstraintsCB);
		controlPanel.add(linkDuplicatesCB);
	}

	public String getName() {
		return "Copy Cluster";
	}

	/*
	 * @see
	 * org.wilmascope.graphmodifiers.GraphModifier#modify(org.wilmascope.graph
	 * .Cluster)
	 */
	JPanel controlPanel = new JPanel();

	public void modify(Cluster cluster) {
		Cluster root = null;
		Cluster c1 = null;
		Hashtable<Node, Node> mapping = new Hashtable<Node, Node>();
		if (cluster.getCluster().getOwner() == null) {
			root = cluster;
			Vector<Node> nodes = new Vector<Node>();
			for (Node n : cluster.getNodes()) {
				nodes.add(n);
			}
			c1 = root.addCluster(nodes);
		} else {
			root = (Cluster) cluster.getCluster().getOwner().getUserData("Facade");
			c1 = cluster;
		}
		c1.setLayoutEngine(ForceLayout.createDefaultClusterForceLayout(c1.getCluster()));
		try {
			c1.setView(ViewManager.getInstance().createClusterView("Planar Cluster"));
		} catch (UnknownViewTypeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (levels) {
			root.getLayoutEngine().getProperties().setProperty("Levels", "" + (copyCount + 1));
			root.getLayoutEngine().resetProperties();
			// place contents of original cluster on level 0
			c1.getLayoutEngine().getProperties().setProperty("Levels", "" + (copyCount + 1));
			c1.getLayoutEngine().getProperties().setProperty("LevelSeparation", "2");
			c1.getLayoutEngine().resetProperties();
			c1.setProperty("LevelConstraint", "0");
			for (Node n : c1.getNodes()) {
				n.setProperty("LevelConstraint", "0");
			}
		}
		for (int i = 1; i <= copyCount; i++) {
			Cluster c2 = root.addCluster("Planar Cluster");
			for (Node u : c1.getNodes()) {
				Node v = c2.addNode();
				if (linkDuplicates) {
					Edge uv = root.addEdge(u, v);
					uv.setRelaxedLength(0.2f);
					uv.hide();
				}
				mapping.put(u, v);
			}

			for (Edge e : c1.getEdges()) {
				Node a = (Node) e.getEdge().getStart().getUserData("Facade");
				Node b = (Node) e.getEdge().getEnd().getUserData("Facade");
				c2.addEdge(mapping.get(a), mapping.get(b));
			}

			c2.setLayoutEngine(ForceLayout.createDefaultClusterForceLayout(c2.getCluster()));

			if (levels) {
				c2.getLayoutEngine().getProperties().setProperty("Levels", "" + (copyCount + 1));
				c2.getLayoutEngine().getProperties().setProperty("LevelSeparation", "2");
				c2.getLayoutEngine().resetProperties();
				c2.setProperty("LevelConstraint", "" + i);
				for (Node n : c2.getNodes()) {
					n.setProperty("LevelConstraint", "" + i);
				}
			}
			c1 = c2;
		}
		root.unfreeze();
	}

	/**
	 * @see org.wilmascope.util.Plugin#getControls()
	 */
	public JPanel getControls() {
		return controlPanel;
	}

}
