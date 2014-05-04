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

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.MenuEvent;
import javax.vecmath.Vector3f;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.OptionsClient;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;
import org.wilmascope.view.NodeView;
import org.wilmascope.view.ViewManager;

/**
 * A popup menu for clusters
 * 
 * @author Tim Dwyer
 */

public class ClusterOptionsMenu extends JPopupMenu implements OptionsClient {
	JMenuItem deleteMenuItem = new JMenuItem();

	JMenuItem centreMenuItem = new JMenuItem();

	JMenuItem analyseMenuItem = new JMenuItem();
	JMenuItem modifyMenuItem = new JMenuItem();

	GraphControl graphControl;

	public ClusterOptionsMenu(Component parent, GraphControl gc, ControlPanel controlPanel) {
		this();
		this.graphControl = gc;
		this.parent = parent;
		this.controlPanel = controlPanel;
	}

	public void callback(java.awt.event.MouseEvent e, GraphControl.GraphElementFacade cluster) {
		this.cluster = (GraphControl.Cluster) cluster;
		if (this.cluster.isExpanded()) {
			remove(expandMenuItem);
			add(collapseMenuItem);
		} else {
			remove(collapseMenuItem);
			add(expandMenuItem);
		}
		pack();
		show(parent, e.getX(), e.getY());
		updateUI();
	}

	public ClusterOptionsMenu() {
		centreMenuItem.setText("Centre");
		centreMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centerMenuItem_actionPerformed(e);
			}
		});
		deleteMenuItem.setText("Delete");
		deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteMenuItem_actionPerformed(e);
			}
		});
		hideMenuItem.setText("Hide");
		hideMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideMenuItem_actionPerformed(e);
			}
		});
		clusterTypeMenu.setText("Select Type");
		clusterTypeMenu.addMenuListener(new javax.swing.event.MenuListener() {
			public void menuSelected(MenuEvent e) {
				clusterTypeMenu_menuSelected(e);
			}

			public void menuDeselected(MenuEvent e) {
			}

			public void menuCanceled(MenuEvent e) {
			}
		});
		contentsPickingMenuItem.setText("Pick Contents");
		contentsPickingMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentsPickingMenuItem_actionPerformed(e);
			}
		});
		adjustForcesMenuItem.setText("Adjust Cluster Forces...");
		adjustForcesMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adjustForcesMenuItem_actionPerformed(e);
			}
		});
		analyseMenuItem.setText("Analyse Cluster...");
		analyseMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				analyseMenuItem_actionPerformed(e);
			}
		});
		modifyMenuItem.setText("Modify Cluster...");
		modifyMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifyMenuItem_actionPerformed(e);
			}

		});
		addToMenuItem.setText("Add to Cluster...");
		addToMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addToMenuItem_actionPerformed(e);
			}
		});
		removeFromMenuItem.setText("Remove from Cluster...");
		removeFromMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeFromMenuItem_actionPerformed(e);
			}
		});
		collapseMenuItem.setText("Collapse Cluster");
		collapseMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				collapseMenuItem_actionPerformed(e);
			}
		});
		expandMenuItem.setText("Expand Cluster");
		expandMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expandMenuItem_actionPerformed(e);
			}
		});
		setLabelMenuItem.setText("Set Label...");
		setLabelMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLabelMenuItem_actionPerformed(e);
			}
		});
		hideNonNeighboursMenuItem.setToolTipText("");
		hideNonNeighboursMenuItem.setText("Show Only Neighbours");
		hideNonNeighboursMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideNonNeighboursMenuItem_actionPerformed(e);
			}
		});
		this.add(centreMenuItem);
		this.add(deleteMenuItem);
		this.add(hideMenuItem);
		this.add(contentsPickingMenuItem);
		this.add(addToMenuItem);
		this.add(removeFromMenuItem);
		this.add(hideNonNeighboursMenuItem);
		this.add(setLabelMenuItem);
		this.add(clusterTypeMenu);
		this.addSeparator();
		this.add(adjustForcesMenuItem);
		this.add(analyseMenuItem);
		this.add(modifyMenuItem);
	}

	void deleteMenuItem_actionPerformed(ActionEvent e) {
		cluster.delete();
		cluster.unfreeze();
	}

	private Component parent;

	private GraphControl.Cluster cluster;

	JMenuItem hideMenuItem = new JMenuItem();

	JMenu clusterTypeMenu = new JMenu();

	JMenuItem contentsPickingMenuItem = new JMenuItem();

	JMenuItem adjustForcesMenuItem = new JMenuItem();

	JMenuItem addToMenuItem = new JMenuItem();

	JMenuItem removeFromMenuItem = new JMenuItem();

	void hideMenuItem_actionPerformed(ActionEvent e) {
		cluster.hide();
	}

	void clusterTypeMenu_menuSelected(MenuEvent e) {
		ViewManager.Registry reg = ViewManager.getInstance().getClusterViewRegistry();
		String[] names = reg.getViewNames();
		clusterTypeMenu.removeAll();
		for (int i = 0; i < names.length; i++) {
			final String name = names[i];
			JMenuItem clusterTypeMenuItem = new JMenuItem(name);
			clusterTypeMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println(name);
					try {
						cluster.setView(ViewManager.getInstance().createClusterView(name));
						cluster.draw();
					} catch (ViewManager.UnknownViewTypeException ex) {
						WilmaMain.showErrorDialog("Unknown View Type!", ex);
					}
				}
			});
			clusterTypeMenu.add(clusterTypeMenuItem);
		}
	}

	void centerMenuItem_actionPerformed(ActionEvent e) {
		Vector3f position = new Vector3f(cluster.getPosition());
		javax.media.j3d.Transform3D localToVworld = new javax.media.j3d.Transform3D();
		((NodeView) cluster.getView()).getTransformGroup().getLocalToVworld(localToVworld);
		Vector3f realpos = new Vector3f(position);
		localToVworld.transform(position);
		graphControl.getGraphCanvas().reorient(position, cluster.getRadius() * 2f);
	}

	void contentsPickingMenuItem_actionPerformed(ActionEvent e) {
		cluster.childrenPickable();
	}

	void adjustForcesMenuItem_actionPerformed(ActionEvent e) {
		LayoutEngineFrame forceControls = new LayoutEngineFrame(cluster, "Cluster Layout");
		forceControls.setVisible(true);
	}

	void analyseMenuItem_actionPerformed(ActionEvent e) {
		AnalysisFrame analysisFrame = new AnalysisFrame("Analyse Cluster", cluster);
		analysisFrame.setVisible(true);
	}

	private void modifyMenuItem_actionPerformed(ActionEvent e) {
		ModifyGraphFrame modifyFrame = new ModifyGraphFrame("Modify Cluster", cluster);
		modifyFrame.setVisible(true);
	}

	void addToMenuItem_actionPerformed(ActionEvent e) {
		controlPanel.add(new AddToClusterPanel(controlPanel, cluster));
		controlPanel.updateUI();
		cluster.makeNonChildrenPickable();
		GraphControl.getPickListener().enableMultiPicking(100,
				new Class[] { GraphControl.nodeClass, GraphControl.clusterClass });
	}

	void removeFromMenuItem_actionPerformed(ActionEvent e) {
		controlPanel.add(new RemoveFromClusterPanel(controlPanel, cluster, graphControl.getRootCluster()));
		controlPanel.updateUI();
		cluster.makeJustChildrenPickable();
		GraphControl.getPickListener().enableMultiPicking(100,
				new Class[] { GraphControl.nodeClass, GraphControl.clusterClass });
	}

	ControlPanel controlPanel;

	JMenuItem collapseMenuItem = new JMenuItem();

	void collapseMenuItem_actionPerformed(ActionEvent e) {
		cluster.collapse();
		cluster.unfreeze();
	}

	boolean expanded = true;

	JMenuItem expandMenuItem = new JMenuItem();

	JMenuItem setLabelMenuItem = new JMenuItem();

	void expandMenuItem_actionPerformed(ActionEvent e) {
		cluster.expand();
		cluster.unfreeze();
	}

	void setLabelMenuItem_actionPerformed(ActionEvent e) {
		controlPanel.setMessage("Opening dialog...");
		String label = JOptionPane.showInputDialog(parent, "What would you like to call this node?", "Node Label",
				JOptionPane.QUESTION_MESSAGE);
		controlPanel.setMessage();
		cluster.setLabel(label);
	}

	JMenuItem hideNonNeighboursMenuItem = new JMenuItem();

	private void traceNeighbours(NodeList neighbours, Cluster c, int depth) {
		if (depth == 0)
			return;
		for (Edge e : c.getEdges()) {
			Cluster neighbour = e.getNeighbour(c).getOwner();
			if (!neighbours.contains(neighbour))
				neighbours.add(neighbour);
			traceNeighbours(neighbours, neighbour, depth - 1);
		}
	}

	void hideNonNeighboursMenuItem_actionPerformed(ActionEvent e) {
		int depth = Integer.parseInt(JOptionPane.showInputDialog(parent, "Depth?",
				"Warning: non neighbours will be permanantly deleted!", JOptionPane.QUESTION_MESSAGE));
		Cluster c = cluster.getCluster();
		NodeList neighbours = new NodeList();
		traceNeighbours(neighbours, c, depth);
		NodeList allNodes = new NodeList(graphControl.getRootCluster().getCluster().getNodes());
		for (Node n : allNodes) {
			if (n != c && !neighbours.contains(n)) {
				n.delete();
			}
		}
	}
}
