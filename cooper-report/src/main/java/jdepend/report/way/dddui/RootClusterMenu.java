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

package jdepend.report.way.dddui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.forcelayout.ForceLayout;
import org.wilmascope.layoutregistry.LayoutManager;
import org.wilmascope.layoutregistry.LayoutManager.UnknownLayoutTypeException;
import org.wilmascope.view.GraphCanvas;
import org.wilmascope.view.PickingClient;

/**
 * 
 * @author wangdg
 * 
 */
public class RootClusterMenu extends JPopupMenu implements PickingClient {

	private JMenuItem forceDirectedItem = new JMenuItem("Force Directed");
	private JMenuItem multiscaleItem = new JMenuItem("Multiscale");
	private JMenuItem simulatedAnnealingItem = new JMenuItem("Simulated Annealing");
	private JMenuItem highDimensionalEmbeddingItem = new JMenuItem("High-Dimensional Embedding");
	private JMenuItem dotStratifiedItem = new JMenuItem("Dot Stratified");
	private JMenuItem columnLayoutItem = new JMenuItem("ColumnLayout");
	private JMenuItem randomItem = new JMenuItem("Random");

	private JMenuItem toggleRotatorItem = new JMenuItem("toggleRotator");

	private GraphCanvas graphCanvas;

	private Cluster cluster;

	public RootClusterMenu(GraphControl.Cluster cluster, GraphCanvas graphCanvas) {

		this.cluster = cluster;
		this.graphCanvas = graphCanvas;

		forceDirectedItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout("Force Directed");
			}
		});
		this.add(forceDirectedItem);
		multiscaleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout("Multiscale");
			}
		});
		this.add(multiscaleItem);
		simulatedAnnealingItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout("Simulated Annealing");
			}
		});
		this.add(simulatedAnnealingItem);
		highDimensionalEmbeddingItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout("High-Dimensional Embedding");
			}
		});
		this.add(highDimensionalEmbeddingItem);
		/**
		 * dotStratifiedItem.addActionListener(new ActionListener() { public
		 * void actionPerformed(ActionEvent e) { changeLayout("Dot Stratified");
		 * } }); this.add(dotStratifiedItem);
		 */
		columnLayoutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout("ColumnLayout");
			}
		});
		this.add(columnLayoutItem);
		randomItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout("Random");
			}
		});
		this.add(randomItem);

		this.addSeparator();

		toggleRotatorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RootClusterMenu.this.graphCanvas.toggleRotator();
			}
		});
		this.add(toggleRotatorItem);

	}

	private void changeLayout(String name) {
		try {
			if (name.equals("Force Directed")) {
				this.cluster.setLayoutEngine(ForceLayout.createDefaultForceLayout(cluster.getCluster()));
			} else {
				this.cluster.setLayoutEngine(LayoutManager.getInstance().createLayout(name));
			}
			this.cluster.unfreeze();
		} catch (UnknownLayoutTypeException e) {
			e.printStackTrace();
		}
	}

	public void callback(java.awt.event.MouseEvent e) {
		if (e.getButton() == 3 && GraphControl.getPickListener().isOptionPickingEnabled()) {
			show(graphCanvas, e.getX(), e.getY());
			updateUI();
		}
	}
}
