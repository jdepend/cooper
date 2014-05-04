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
package org.wilmascope.control;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.wilmascope.file.FileHandler;
import org.wilmascope.forcelayout.ForceLayout;
import org.wilmascope.gmlparser.ColumnsImporter;
import org.wilmascope.gui.Actions;
import org.wilmascope.gui.ClusterOptionsMenu;
import org.wilmascope.gui.ControlPanel;
import org.wilmascope.gui.EdgeOptionsMenu;
import org.wilmascope.gui.MenuBar;
import org.wilmascope.gui.NodeOptionsMenu;

/**
 * 
 * Title: WilmaScope
 * 
 * Description: Sequel to the ever popular Wilma graph drawing engine
 * 
 * Copyright: Copyright (c) 2001
 * 
 * Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * 
 * @version 1.0
 * 
 */
public class WilmaMain extends JFrame {
	{
		// JPopupMenu won't work over a (heavyweight) Java3D canvas unless
		// we do the following
		// JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	}

	java.awt.Component graphCanvas;

	public void init() {
		try {
			getContentPane().setLayout(new BorderLayout());
			graphControl = new GraphControl(400, 400);
			graphCanvas = graphControl.getGraphCanvas();
			getContentPane().add("Center", graphCanvas);
			ControlPanel controlPanel = new ControlPanel();
			getContentPane().add("South", controlPanel);
			// RootClusterMenu rootMenu = new RootClusterMenu(this, r,
			// controlPanel);
			Actions actions = Actions.getInstance();
			actions.init(this, graphControl, controlPanel);
			getContentPane().add("North", actions.getToolPanel());
			MenuBar menuBar = new MenuBar(this, actions, graphControl, controlPanel);
			setJMenuBar(menuBar);
			// graphControl.setRootPickingClient(rootMenu);
			GraphControl.getPickListener().setNodeOptionsClient(
					new NodeOptionsMenu(graphCanvas, graphControl, controlPanel));
			GraphControl.getPickListener().setClusterOptionsClient(
					new ClusterOptionsMenu(graphCanvas, graphControl, controlPanel));
			GraphControl.getPickListener().setEdgeOptionsClient(new EdgeOptionsMenu(graphCanvas, graphControl));
			GraphControl.Cluster r = graphControl.getRootCluster();
			r.setLayoutEngine(ForceLayout.createDefaultForceLayout(r.getCluster()));
			// add a background texture
			// ((GraphCanvas)graphCanvas).setBackgroundTexture(org.wilmascope.images.Images.class
			// .getResource("sky.jpg"));
			// add some nodes
			// for(int i=0;i<5;i++) {
			// r.addNode();
			// }
			// I don't know why, but without the following, adding an
			// observer causes weirdness
			// r.freeze();
			graphControl.addObserver(new LayoutObserver() {
				public void update(Observable o, Object state) {
					if (state == LayoutObserver.LAYOUT_FINISHED) {
						System.out.println("centering");
						graphControl.centreGraph();
					} else {
						System.out.print(".");
					}
				}
			});
		} catch (Exception e) {
			showDefaultErrorDialog(e);
		}
	}

	public static void main(String argv[]) {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		WilmaMain main = new WilmaMain();
		main.createJMainFrame();
		System.out.println("In WilmaMain.main");
		if (argv.length != 0) {
			File f = new File(argv[0]);
			if (f.isDirectory()) {
				System.out.println("Importing GML files in directory: " + argv[0]);
				ColumnsImporter.load(main.graphControl, f.getAbsolutePath());
			} else {
				System.out.println("Loading " + argv[0]);
				FileHandler fileHandler = new FileHandler(main.graphControl);
				fileHandler.load(argv[0]);
			}
		}
	}

	public void createJMainFrame() {
		// new SplashWindow(
		// org.wilmascope.images.Images.class.getResource("WilmaSplash.png").getPath(),
		//
		// this,
		//
		// 5000);
		init();
		ImageIcon icon = new ImageIcon(org.wilmascope.images.Images.class.getResource("WilmaW24.png"));
		setIconImage(icon.getImage());
		setTitle("Welcome to Wilma!");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		show();
	}

	GraphControl graphControl;

	public GraphControl getGraphControl() {
		return graphControl;
	}

	public void setSize(int width, int height) {
		System.out.println("resizing: width=" + width + ", height=" + height);
		super.setSize(width, height);
		getRootPane().updateUI();
		validate();
	}

	public static void showDefaultErrorDialog(Exception ex) {
		ex.printStackTrace();
		JOptionPane.showMessageDialog(null,
				"See %WILMA_HOME%/bin/error.log or the console... depending on how you ran Wilma",
				"An error occurred!", JOptionPane.ERROR_MESSAGE);
	}

	public static void showErrorDialog(String msg, Exception ex) {
		ex.printStackTrace();
		JOptionPane.showMessageDialog(null, msg + "", "An error occurred!", JOptionPane.ERROR_MESSAGE);
	}
}
