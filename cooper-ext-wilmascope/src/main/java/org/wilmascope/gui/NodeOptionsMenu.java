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
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputAdapter;
import javax.vecmath.Vector3f;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.OptionsClient;
import org.wilmascope.control.PickClient;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.view.ElementData;
import org.wilmascope.view.NodeView;
import org.wilmascope.viewplugin.DefaultNodeData;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class NodeOptionsMenu extends JPopupMenu implements OptionsClient {
	public NodeOptionsMenu(Component parent, GraphControl graphControl, ControlPanel controlPanel) {
		this();
		this.parent = parent;
		this.graphControl = graphControl;
		this.controlPanel = controlPanel;
	}

	ActionListener customListener = null;

	public void callback(java.awt.event.MouseEvent e, GraphControl.GraphElementFacade node) {
		this.node = (GraphControl.Node) node;
		Object userData;
		detailsMenuItem.setEnabled(false);
		detailsMenuItem.setText("Show Details...");
		DefaultNodeData data = new DefaultNodeData();
		String str = ((GraphControl.Node) node).getNode().getProperties().toString();
		data.setData(str);
		node.setUserData(data);
		if ((userData = node.getUserData()) != null && userData instanceof ElementData) {
			if (customListener != null) {
				detailsMenuItem.removeActionListener(customListener);
			}
			customListener = ((ElementData) userData).getActionListener();
			if (customListener != null) {
				detailsMenuItem.addActionListener(customListener);
				detailsMenuItem.setEnabled(true);
				detailsMenuItem.setText(((ElementData) userData).getActionDescription());
			}
		}
		boolean fixed = false;
		String fixedString = this.node.getProperties().getProperty("FixedPosition");
		if (fixedString != null) {
			fixed = Boolean.parseBoolean(fixedString.toLowerCase());
		}
		fixedCheckBoxMenuItem.setSelected(fixed);
		show(parent, e.getX(), e.getY());
		updateUI();
	}

	JMenuItem addEdgeMenuItem = new JMenuItem();
	JMenuItem centerNodeMenuItem = new JMenuItem();
	JMenuItem deleteMenuItem = new JMenuItem();
	JMenuItem detailsMenuItem = new JMenuItem();
	JMenuItem radiusMenuItem = new JMenuItem();

	public NodeOptionsMenu() {
		addEdgeMenuItem.setText("Add Edge...");
		addEdgeMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addEdgeMenuItem_actionPerformed(e);
			}
		});
		centerNodeMenuItem.setText("Center Node");
		centerNodeMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centerNodeMenuItem_actionPerformed(e);
			}
		});
		deleteMenuItem.setText("Delete");
		deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteMenuItem_actionPerformed(e);
			}
		});
		radiusMenuItem.setText("Set radius...");
		radiusMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radiusMenuItem_actionPerformed(e);
			}
		});

		detailsMenuItem.setEnabled(false);
		detailsMenuItem.setText("Show Details...");
		addNodeMenuItem.setText("Add Node...");
		addNodeMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNodeMenuItem_actionPerformed(e);
			}
		});
		setLabelMenuItem.setText("Set Label...");
		setLabelMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLabelMenuItem_actionPerformed(e);
			}
		});
		hideMenuItem.setText("Hide");
		hideMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideMenuItem_actionPerformed(e);
			}
		});
		setColourMenuItem.setText("Set Colour...");
		setColourMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setColourMenuItem_actionPerformed(e);
			}
		});
		fixedCheckBoxMenuItem.setText("Fixed Position");
		fixedCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fixedCheckBoxMenuItem_actionPerformed(e);
			}
		});
		dragMenuItem.setText("Drag");
		dragMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dragMenuItem_actionPerformed(e);
			}
		});
		this.add(addNodeMenuItem);
		this.add(addEdgeMenuItem);
		this.add(centerNodeMenuItem);
		this.add(deleteMenuItem);
		this.add(dragMenuItem);
		this.add(fixedCheckBoxMenuItem);
		this.add(setLabelMenuItem);
		this.add(setColourMenuItem);
		this.add(radiusMenuItem);
		this.add(hideMenuItem);
		this.add(detailsMenuItem);
	}

	void addEdgeMenuItem_actionPerformed(ActionEvent e) {
		node.highlightColour();
		controlPanel.setMessage("Left click on another node to create an edge...");
		GraphControl.getPickListener().setSinglePickClient(new PickClient() {
			public void callback(GraphControl.GraphElementFacade edgeNode) {
				if (edgeNode != node) {
					graphControl.getRootCluster().addEdge(node, (GraphControl.Node) edgeNode);
					graphControl.getRootCluster().unfreeze();
				}
				node.defaultColour();
				controlPanel.setMessage();
			}
		}, GraphControl.nodeClass);
	}

	private Component parent;
	private GraphControl.Node node;
	private ControlPanel controlPanel;
	JMenuItem addNodeMenuItem = new JMenuItem();
	JMenuItem setLabelMenuItem = new JMenuItem();
	JMenuItem hideMenuItem = new JMenuItem();
	JMenuItem setColourMenuItem = new JMenuItem();
	JCheckBoxMenuItem fixedCheckBoxMenuItem = new JCheckBoxMenuItem();

	void addNodeMenuItem_actionPerformed(ActionEvent e) {
		GraphControl.Node newNode = graphControl.getRootCluster().addNode();
		newNode.setPosition(node.getPosition());
		graphControl.getRootCluster().addEdge(node, newNode);
		graphControl.getRootCluster().unfreeze();
	}

	void centerNodeMenuItem_actionPerformed(ActionEvent e) {
		Vector3f position = new Vector3f(node.getPosition());
		javax.media.j3d.Transform3D localToVworld = new javax.media.j3d.Transform3D();
		((NodeView) node.getView()).getTransformGroup().getLocalToVworld(localToVworld);
		Vector3f realpos = new Vector3f(position);
		System.out.println("Node pos: " + position);
		localToVworld.transform(position);
		System.out.println("Real pos: " + position);
		graphControl.getGraphCanvas().reorient(position, node.getRadius() * 2f);
	}

	void deleteMenuItem_actionPerformed(ActionEvent e) {
		node.delete();
		graphControl.getRootCluster().unfreeze();
	}

	void setLabelMenuItem_actionPerformed(ActionEvent e) {
		controlPanel.setMessage("Opening dialog...");
		String label = (String) JOptionPane.showInputDialog(parent, "What would you like to call this node?",
				(String) "Node Label", JOptionPane.QUESTION_MESSAGE, null, null, node.getLabel());
		controlPanel.setMessage();
		if (label != null) {
			node.setLabel(label);
		}
	}

	void expandMenuItem_actionPerformed(ActionEvent e) {
		((GraphControl.Cluster) node).expand();
	}

	void hideMenuItem_actionPerformed(ActionEvent e) {
		node.hide();
	}

	void setColourMenuItem_actionPerformed(ActionEvent e) {
		java.awt.Color colour = JColorChooser.showDialog(this, "Please select nice colours...", node.getColour());
		if (colour != null) {
			node.setColour(colour);
		}
	}

	void radiusMenuItem_actionPerformed(ActionEvent e) {
		controlPanel.setMessage("Opening dialog...");
		String radius = (String) JOptionPane.showInputDialog(parent, "Enter a float radius...", (String) "Node Radius",
				JOptionPane.QUESTION_MESSAGE, null, null, "" + node.getRadius());
		try {
			node.setRadius(Float.parseFloat(radius));
		} catch (NumberFormatException e1) {
			WilmaMain.showErrorDialog("You must enter a float value for radius", e1);
		}
		controlPanel.setMessage();
		node.getView().draw();
	}

	GraphControl graphControl;
	JMenuItem dragMenuItem = new JMenuItem();

	void fixedCheckBoxMenuItem_actionPerformed(ActionEvent e) {
		if (fixedCheckBoxMenuItem.isSelected()) {
			node.setColour(java.awt.Color.cyan);
			node.setProperty("FixedPosition", "true");
		} else {
			node.defaultColour();
			node.removeProperty("FixedPosition");
		}
	}

	void dragMenuItem_actionPerformed(ActionEvent e) {
		node.highlightColour();
		final org.wilmascope.view.GraphCanvas c = graphControl.getGraphCanvas();
		c.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		final JButton doneButton = new JButton("Done");
		final MouseInputAdapter ma = new MouseInputAdapter() {
			public void mouseDragged(MouseEvent mevent) {
				reposition(mevent);
			}

			public void mouseClicked(MouseEvent mevent) {
				reposition(mevent);
			}

			private void reposition(MouseEvent e) {
				if (!e.isAltDown() && e.isMetaDown()) {
					node.moveToCanvasPos(e.getX(), e.getY());
					graphControl.getRootCluster().unfreeze();
				}
			}
		};
		doneButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlPanel.remove(doneButton);
				c.setPickingEnabled(true);
				c.getMouseTranslate().setEnable(true);
				c.removeMouseListener(ma);
				c.removeMouseMotionListener(ma);
				c.setCursor(null);
				node.defaultColour();
			}
		});
		controlPanel.add(doneButton);
		controlPanel.updateUI();
		controlPanel.setMessage("Right click (or drag) above to place node...");
		c.setPickingEnabled(false);
		c.getMouseTranslate().setEnable(false);
		c.addMouseMotionListener(ma);
		c.addMouseListener(ma);
	}
}
