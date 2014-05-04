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

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.wilmascope.control.WilmaMain;
import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.control.GraphControl.Edge;
import org.wilmascope.control.GraphControl.Node;
import org.wilmascope.graphmodifiers.GraphModifier;
import org.wilmascope.view.ViewManager;
import org.wilmascope.view.ViewManager.UnknownViewTypeException;

/**
 * @author dwyer
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChangeViews extends GraphModifier {
	public ChangeViews() {
		Box b = Box.createVerticalBox();
		final JCheckBox nodeCheckBox = new JCheckBox("Node views");
		final JCheckBox edgeCheckBox = new JCheckBox("Edge views");
		nodeCheckBox.setSelected(nodes);
		edgeCheckBox.setSelected(edges);
		nodeCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nodes = nodeCheckBox.isSelected();
			}
		});
		edgeCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edges = edgeCheckBox.isSelected();
			}
		});
		b.add(new JLabel("Change views to defaults from toolbar:"));
		b.add(nodeCheckBox);
		b.add(edgeCheckBox);
		controls.add(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.util.Plugin#getName()
	 */
	public String getName() {
		return "Change Graph Element Views";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.wilmascope.graphmodifiers.GraphModifier#modify(org.wilmascope.control
	 * .GraphControl.Cluster)
	 */
	public void modify(Cluster cluster) {
		try {
			if (nodes) {
				for (Node v : cluster.getNodes()) {
					v.setView(ViewManager.getInstance().createNodeView());
				}
			}
			if (edges) {
				for (Edge e : cluster.getEdges()) {
					e.setView(ViewManager.getInstance().createEdgeView());
				}
			}
		} catch (UnknownViewTypeException e) {
			WilmaMain.showErrorDialog("Unknown view", e);
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

	JPanel controls = new JPanel();

	boolean nodes = true;

	boolean edges = true;
}
