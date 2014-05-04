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
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.MenuEvent;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.OptionsClient;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.view.EdgeView;
import org.wilmascope.view.ElementData;
import org.wilmascope.view.ViewManager;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class EdgeOptionsMenu extends JPopupMenu implements OptionsClient {
	public EdgeOptionsMenu(Component parent, GraphControl graphControl) {
		this();
		this.parent = parent;
		this.graphControl = graphControl;
	}

	public void callback(java.awt.event.MouseEvent e, GraphControl.GraphElementFacade edge) {
		this.edge = (GraphControl.Edge) edge;
		Object userData;
		detailsMenuItem.setEnabled(false);
		detailsMenuItem.setText("Show Details...");
		if ((userData = edge.getUserData()) != null && userData instanceof ElementData) {
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
		show(parent, e.getX(), e.getY());
		updateUI();
	}

	public EdgeOptionsMenu() {
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
		setColourMenuItem.setText("Set Colour...");
		setColourMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setColourMenuItem_actionPerformed(e);
			}
		});
		weightMenuItem.setText("Weight...");
		weightMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				weightMenuItem_actionPerformed(e);
			}
		});
		edgeTypeMenu.setText("Select Type");
		edgeTypeMenu.addMenuListener(new javax.swing.event.MenuListener() {
			public void menuSelected(MenuEvent e) {
				edgeTypeMenu_menuSelected(e);
			}

			public void menuDeselected(MenuEvent e) {
			}

			public void menuCanceled(MenuEvent e) {
			}
		});
		reverseMenuItem.setText("Reverse Direction");
		reverseMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reverseMenuItem_actionPerformed(e);
			}
		});
		radiusMenuItem.setText("Radius...");
		radiusMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				radiusMenuItem_actionPerformed(e);
			}
		});
		this.add(edgeTypeMenu);
		this.add(deleteMenuItem);
		this.add(hideMenuItem);
		this.add(radiusMenuItem);
		this.add(weightMenuItem);
		this.add(setColourMenuItem);
		this.add(reverseMenuItem);
		this.add(detailsMenuItem);
	}

	void deleteMenuItem_actionPerformed(ActionEvent e) {
		edge.delete();
		graphControl.getRootCluster().unfreeze();
	}

	void weightMenuItem_actionPerformed(ActionEvent e) {
		String s = (String) JOptionPane.showInputDialog(parent, "Enter weight for this edge...",
				(String) "Edge Weight", JOptionPane.QUESTION_MESSAGE, null, null, "" + edge.getWeight());
		if (s == null) {
			return;
		}
		try {
			float w = Float.parseFloat(s);
			if (w < 0 || w > 1) {
				throw new NumberFormatException();
			}
			edge.setWeight(w);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(parent,
					"Invalid edge weight entered... enter a floating point number between 0 and 1",
					"Invalid edge weight", JOptionPane.ERROR_MESSAGE);
		}
	}

	void hideMenuItem_actionPerformed(ActionEvent e) {
		edge.hide();
	}

	void edgeTypeMenu_menuSelected(MenuEvent e) {
		ViewManager.Registry reg = ViewManager.getInstance().getEdgeViewRegistry();
		String[] names = reg.getViewNames();
		edgeTypeMenu.removeAll();
		for (int i = 0; i < names.length; i++) {
			final String name = names[i];
			JMenuItem edgeTypeMenuItem = new JMenuItem(name);
			try {
				edgeTypeMenuItem.setIcon(reg.getIcon(name));
			} catch (ViewManager.UnknownViewTypeException ex) {
				WilmaMain.showErrorDialog("Unknown View Type", ex);
			}
			edgeTypeMenuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println(name);
					try {
						edge.setView(ViewManager.getInstance().createEdgeView(name));
					} catch (ViewManager.UnknownViewTypeException ex) {
						WilmaMain.showErrorDialog("Unknown View Type", ex);
					}
				}
			});
			edgeTypeMenu.add(edgeTypeMenuItem);
		}
	}

	void reverseMenuItem_actionPerformed(ActionEvent e) {
		edge.reverseDirection();
	}

	void setColourMenuItem_actionPerformed(ActionEvent e) {
		java.awt.Color colour = JColorChooser.showDialog(this, "Please select nice colours...", edge.getColour());
		if (colour != null) {
			edge.setColour(colour);
		}
	}

	public static GraphControl.Edge getSelectedEdge() {
		return edge;
	}

	public void setParent(Component parent) {
		this.parent = parent;
	}

	public void setGraphControl(GraphControl gc) {
		this.graphControl = gc;
	}

	JMenuItem deleteMenuItem = new JMenuItem();
	JMenuItem weightMenuItem = new JMenuItem();
	JMenuItem detailsMenuItem = new JMenuItem();
	ActionListener customListener = null;
	private Component parent;
	static private GraphControl.Edge edge;
	private GraphControl graphControl;
	JMenuItem hideMenuItem = new JMenuItem();
	JMenu edgeTypeMenu = new JMenu();
	JMenuItem reverseMenuItem = new JMenuItem();
	JMenuItem setColourMenuItem = new JMenuItem();
	JMenuItem radiusMenuItem = new JMenuItem();

	void radiusMenuItem_actionPerformed(ActionEvent e) {
		String s = (String) JOptionPane.showInputDialog(parent, "Enter radius for this edge...",
				(String) "Edge Radius", JOptionPane.QUESTION_MESSAGE, null, null, ""
						+ ((EdgeView) edge.getEdge().getView()).getRadius());
		if (s == null) {
			return;
		}
		try {
			float radius = Float.parseFloat(s);
			if (radius < 0 || radius > 5) {
				throw new NumberFormatException();
			}
			((EdgeView) edge.getEdge().getView()).setRadius(radius);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(parent,
					"Invalid edge weight entered... enter a floating point number between 0 and 1",
					"Invalid edge weight", JOptionPane.ERROR_MESSAGE);
		}
		graphControl.getRootCluster().unfreeze();
	}
}
