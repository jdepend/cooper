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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.wilmascope.control.GraphControl;
import org.wilmascope.view.PickingClient;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */
public class RootClusterMenu extends JPopupMenu implements PickingClient {
	public RootClusterMenu(java.awt.Component parent, GraphControl.Cluster rootCluster, ControlPanel controlPanel) {
		this();
		this.rootCluster = rootCluster;
		this.controlPanel = controlPanel;
		this.parent = parent;
	}

	public void callback(java.awt.event.MouseEvent e) {
		if (e.isMetaDown() && GraphControl.getPickListener().isOptionPickingEnabled()) {
			show(parent, e.getX(), e.getY());
		}
	}

	GraphControl.Cluster rootCluster;
	JMenuItem addNodeMenuItem = new JMenuItem();
	JMenuItem addEdgeMenuItem = new JMenuItem();
	JMenuItem addClusterMenuItem = new JMenuItem();
	JMenuItem adjustForcesMenuItem = new JMenuItem();

	public RootClusterMenu() {
		addNodeMenuItem.setText("Add Node");
		addNodeMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNodeMenuItem_actionPerformed(e);
			}
		});
		addEdgeMenuItem.setText("Add Edge...");
		addEdgeMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addEdgeMenuItem_actionPerformed(e);
			}
		});
		addClusterMenuItem.setText("Add Cluster...");
		addClusterMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addClusterMenuItem_actionPerformed(e);
			}
		});
		adjustForcesMenuItem.setText("Adjust Forces...");
		adjustForcesMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adjustForcesMenuItem_actionPerformed(e);
			}
		});
		showHiddenMenuItem.setText("Show Hidden");
		showHiddenMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHiddenMenuItem_actionPerformed(e);
			}
		});
		pickableClustersMenuItem.setText("Make all clusters Pickable");
		pickableClustersMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pickableClustersMenuItem_actionPerformed(e);
			}
		});
		this.add(addNodeMenuItem);
		this.add(addEdgeMenuItem);
		this.add(addClusterMenuItem);
		this.add(pickableClustersMenuItem);
		this.add(showHiddenMenuItem);
		this.addSeparator();
		this.add(adjustForcesMenuItem);
	}

	void addNodeMenuItem_actionPerformed(ActionEvent e) {
		GraphControl.Node n = rootCluster.addNode();
		rootCluster.unfreeze();
	}

	void addEdgeMenuItem_actionPerformed(ActionEvent e) {
		GraphControl.getPickListener().enableMultiPicking(2,
				new Class[] { GraphControl.nodeClass, GraphControl.clusterClass });
		controlPanel.add(new EdgePanel(controlPanel, rootCluster));
		controlPanel.updateUI();
	}

	void adjustForcesMenuItem_actionPerformed(ActionEvent e) {
		LayoutEngineFrame forceControls = new LayoutEngineFrame(rootCluster, "Force Controls");
		forceControls.show();
	}

	ControlPanel controlPanel;
	private java.awt.Component parent;
	JMenuItem showHiddenMenuItem = new JMenuItem();
	JMenuItem pickableClustersMenuItem = new JMenuItem();

	void addClusterMenuItem_actionPerformed(ActionEvent e) {
		controlPanel.add(new ClusterPanel(controlPanel, rootCluster));
		controlPanel.updateUI();
		GraphControl.getPickListener().enableMultiPicking(100,
				new Class[] { GraphControl.nodeClass, GraphControl.clusterClass });
	}

	void showHiddenMenuItem_actionPerformed(ActionEvent e) {
		rootCluster.showHiddenChildren();
	}

	void pickableClustersMenuItem_actionPerformed(ActionEvent e) {
		rootCluster.childrenPickable();
	}
}
