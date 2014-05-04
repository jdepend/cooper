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

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wilmascope.control.GraphControl;
import org.wilmascope.file.FileHandler;
import org.wilmascope.gmlparser.ColumnsImporter;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class MenuBar extends JMenuBar {
	JMenu fileMenu;
	JMenu helpMenu = new JMenu();
	JMenu editMenu;
	JMenu toolsMenu;
	// JMenuItem queryMenuItem = new JMenuItem();
	JMenuItem exitMenuItem = new JMenuItem();
	JMenuItem screenCaptureMenuItem = new JMenuItem();
	JMenuItem animatedCaptureMenuItem = new JMenuItem();
	JMenuItem importMenuItem = new JMenuItem();
	JMenu viewMenu;
	JCheckBoxMenuItem antialiasingCheckBoxMenuItem = new JCheckBoxMenuItem();
	JCheckBoxMenuItem parallelCheckBoxMenuItem = new JCheckBoxMenuItem();
	JMenuItem stereoMenuItem = new JMenuItem();
	JCheckBoxMenuItem showMouseHelpCheckBoxMenuItem = new JCheckBoxMenuItem();
	JCheckBoxMenuItem fullScreenCheckBoxMenuItem = new JCheckBoxMenuItem();
	JMenuItem stretchMenuItem = new JMenuItem();
	JMenuItem backgroundColourMenuItem = new JMenuItem();
	JMenuItem axisPlaneMenuItem = new JMenuItem();
	JMenuItem helpMenuItem = new JMenuItem();
	JMenuItem licenseMenuItem = new JMenuItem();
	JMenuItem aboutMenuItem = new JMenuItem();
	JMenuItem edgeViewMenuItem = new JMenuItem();
	ControlPanel controlPanel;
	JFrame parallelScaleControlFrame = null;
	JFrame eyeSeparationControlFrame = null;
	JFrame stretchControlFrame = null;
	JFrame frame;

	public MenuBar(JFrame frame, Actions actions, GraphControl graphControl, ControlPanel controlPanel) {
		this.frame = frame;
		this.graphControl = graphControl;
		this.controlPanel = controlPanel;
		editMenu = actions.getEditMenu();
		toolsMenu = actions.getToolsMenu();
		fileMenu = actions.getFileMenu();
		viewMenu = actions.getViewMenu();
		helpMenu.setText("Help");
		helpMenu.setMnemonic('H');
		importMenuItem.setText("Import");
		importMenuItem.setMnemonic('I');
		importMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importMenuItem_actionPerformed(e);
			}
		});
		// queryMenuItem.setText("Query");
		// queryMenuItem.setMnemonic('Q');
		// queryMenuItem.addActionListener(new java.awt.event.ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// queryMenuItem_actionPerformed(e);
		// }
		// });
		screenCaptureMenuItem.setText("Screen Capture");
		screenCaptureMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				screenCaptureMenuItem_actionPerformed(e);
			}
		});
		animatedCaptureMenuItem.setText("Screen Capture (animated)");
		animatedCaptureMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animatedCaptureMenuItem_actionPerformed(e);
			}
		});
		exitMenuItem.setText("Exit");
		exitMenuItem.setMnemonic('x');
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitMenuItem_actionPerformed(e);
			}
		});
		fullScreenCheckBoxMenuItem.setText("Full Screen Mode");
		fullScreenCheckBoxMenuItem.setMnemonic('F');
		fullScreenCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fullScreen();
			}
		});
		showMouseHelpCheckBoxMenuItem.setText("Show Mouse Help Panel");
		showMouseHelpCheckBoxMenuItem.setMnemonic('M');
		showMouseHelpCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMouseHelpCheckBoxMenuItem_actionPerformed(e);
			}
		});
		showMouseHelpCheckBoxMenuItem.setState(true);
		antialiasingCheckBoxMenuItem.setText("Antialiasing");
		antialiasingCheckBoxMenuItem.setMnemonic('A');
		antialiasingCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				antialiasingCheckBoxMenuItem_actionPerformed(e);
			}
		});
		parallelCheckBoxMenuItem.setText("Parallel Projection");
		parallelCheckBoxMenuItem.setMnemonic('P');
		parallelCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parallelCheckBoxMenuItem_actionPerformed(e);
			}
		});
		stereoMenuItem.setText("Stereo Separation...");
		stereoMenuItem.setMnemonic('P');
		stereoMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stereoMenuItem_actionPerformed(e);
			}
		});
		stretchMenuItem.setText("Stretch Vertical Axis...");
		stretchMenuItem.setMnemonic('S');
		stretchMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stretchMenuItem_actionPerformed(e);
			}
		});

		axisPlaneMenuItem.setText("Show axis plane...");
		axisPlaneMenuItem.setMnemonic('x');
		axisPlaneMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				axisPlaneMenuItem_actionPerformed(e);
			}
		});
		backgroundColourMenuItem.setText("Set Background Colour...");
		backgroundColourMenuItem.setMnemonic('B');
		backgroundColourMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backgroundColourMenuItem_actionPerformed(e);
			}
		});
		helpMenuItem.setText("Help Contents...");
		helpMenuItem.setMnemonic('H');
		helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpMenuItem_actionPerformed(e);
			}
		});
		licenseMenuItem.setText("License details...");
		licenseMenuItem.setMnemonic('L');
		licenseMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				licenseMenuItem_actionPerformed(e);
			}
		});
		aboutMenuItem.setText("About");
		aboutMenuItem.setMnemonic('A');
		aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutMenuItem_actionPerformed(e);
			}
		});
		edgeViewMenuItem.setText("Edge View Control...");
		edgeViewMenuItem.setMnemonic('E');
		edgeViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edgeViewMenuItem_actionPerformed(e);
			}
		});

		this.add(fileMenu);
		this.add(editMenu);
		this.add(viewMenu);
		this.add(toolsMenu);
		this.add(helpMenu);
		fileMenu.add(importMenuItem);
		// fileMenu.add(queryMenuItem);
		fileMenu.add(screenCaptureMenuItem);
		fileMenu.add(animatedCaptureMenuItem);
		fileMenu.add(exitMenuItem);
		viewMenu.add(fullScreenCheckBoxMenuItem);
		viewMenu.add(showMouseHelpCheckBoxMenuItem);
		viewMenu.add(antialiasingCheckBoxMenuItem);
		viewMenu.add(parallelCheckBoxMenuItem);
		viewMenu.add(stereoMenuItem);
		viewMenu.add(stretchMenuItem);
		viewMenu.add(axisPlaneMenuItem);
		viewMenu.add(backgroundColourMenuItem);
		viewMenu.add(edgeViewMenuItem);
		helpMenu.add(helpMenuItem);
		helpMenu.add(licenseMenuItem);
		helpMenu.add(aboutMenuItem);
	}

	void exitMenuItem_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	void antialiasingCheckBoxMenuItem_actionPerformed(ActionEvent e) {
		if (antialiasingCheckBoxMenuItem.isSelected()) {
			graphControl.getGraphCanvas().setAntialiasingEnabled(true);
		} else {
			graphControl.getGraphCanvas().setAntialiasingEnabled(false);
		}
	}

	void parallelCheckBoxMenuItem_actionPerformed(ActionEvent e) {
		if (parallelCheckBoxMenuItem.isSelected()) {
			graphControl.getGraphCanvas().setParallelProjection(true);
			parallelScaleControlFrame = new JFrame();
			JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);
			scaleSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					graphControl.getGraphCanvas().setScale(0.01 * source.getValue());
				}
			});
			parallelScaleControlFrame.getContentPane().add(scaleSlider);
			parallelScaleControlFrame.setTitle("Adjust Scale...");
			parallelScaleControlFrame.pack();
			parallelScaleControlFrame.show();
		} else {
			graphControl.getGraphCanvas().setParallelProjection(false);
			parallelScaleControlFrame.dispose();
		}
	}

	void stereoMenuItem_actionPerformed(ActionEvent e) {
		graphControl.getGraphCanvas().setStereoEnable(true);
		eyeSeparationControlFrame = new JFrame();
		JSlider eyeSlider = new JSlider(JSlider.HORIZONTAL, 0, 99, 10);
		eyeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				graphControl.getGraphCanvas().setStereoSeparation(0.0006 * source.getValue());
			}
		});
		eyeSeparationControlFrame.getContentPane().add(eyeSlider);
		eyeSeparationControlFrame.setTitle("Adjust Eye Separation...");
		eyeSeparationControlFrame.pack();
		eyeSeparationControlFrame.show();
	}

	void stretchMenuItem_actionPerformed(ActionEvent e) {
		stretchControlFrame = new JFrame();
		JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);
		scaleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				graphControl.getGraphCanvas().setScale(new javax.vecmath.Vector3d(1, 0.1 * source.getValue(), 1));
			}
		});
		stretchControlFrame.getContentPane().add(scaleSlider);
		stretchControlFrame.setTitle("Adjust Vertical Scale...");
		stretchControlFrame.pack();
		stretchControlFrame.show();
	}

	void showMouseHelpCheckBoxMenuItem_actionPerformed(ActionEvent e) {
		if (showMouseHelpCheckBoxMenuItem.isSelected()) {
			controlPanel.showMouseHelp();
		} else {
			controlPanel.hideMouseHelp();
		}
	}

	GraphControl graphControl;

	void importMenuItem_actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(org.wilmascope.global.GlobalConstants.getInstance().getProperty(
				"DefaultDataPath"));
		// chooser.setFileFilter(fileHandler.getFileFilter());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ColumnsImporter.load(graphControl, chooser.getSelectedFile().getAbsolutePath());
		}
	}

	void queryMenuItem_actionPerformed(ActionEvent e) {
		QueryFrame q = new QueryFrame(graphControl);
		q.show();
	}

	void screenCaptureMenuItem_actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(FileHandler.getJPEGFileFilter());
		int r = fc.showSaveDialog(this);
		// should probably allow user to pick the following in a dialog
		float scale = org.wilmascope.global.GlobalConstants.getInstance().getFloatValue("ScreenCaptureScale");
		if (r == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			graphControl.getGraphCanvas().writeJPEG(path, scale);
		}
	}

	void animatedCaptureMenuItem_actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(FileHandler.getJPEGFileFilter());
		int r = fc.showSaveDialog(this);
		// should probably allow user to pick the following in a dialog
		float scale = org.wilmascope.global.GlobalConstants.getInstance().getFloatValue("ScreenCaptureScale");
		if (r == JFileChooser.APPROVE_OPTION) {
			String path = fc.getSelectedFile().getAbsolutePath();
			String str = JOptionPane.showInputDialog(this, "How many frames ?", "FramesCount",
					JOptionPane.QUESTION_MESSAGE);
			int c = Integer.parseInt(str);
			for (int i = 0; i < c; i++) {
				graphControl.getGraphCanvas().writeJPEG(path + "_" + i + ".jpg", scale);
				try {
					Thread.sleep(10);
				} catch (InterruptedException ex) {
				}
			}
		}
	}

	void testMenuItem_actionPerformed(ActionEvent e) {
		GenerateGraphFrame setup = new GenerateGraphFrame("Generate Test Graph", graphControl);
		setup.show();
	}

	void helpMenuItem_actionPerformed(ActionEvent e) {
		HelpFrame h = new HelpFrame("../userdoc/index.html");
		h.show();
	}

	void licenseMenuItem_actionPerformed(ActionEvent e) {
		HelpFrame h = new HelpFrame("../userdoc/license.html");
		h.show();
	}

	void aboutMenuItem_actionPerformed(ActionEvent e) {
		new About(null, "images" + java.io.File.separator + "WilmaSplash.png").show();
	}

	void edgeViewMenuItem_actionPerformed(ActionEvent e) {
		new EdgeViewFrame(graphControl, graphControl.getRootCluster()).show();
	}

	void backgroundColourMenuItem_actionPerformed(ActionEvent e) {
		java.awt.Color colour = JColorChooser.showDialog(this, "Please select nice colours...", graphControl
				.getGraphCanvas().getBackgroundColor());
		if (colour != null) {
			graphControl.getGraphCanvas().setBackgroundColor(colour);
		}
	}

	void axisPlaneMenuItem_actionPerformed(ActionEvent e) {
		if (axisPlaneControlFrame == null) {
			axisPlaneControlFrame = new SliceViewControlFrame(graphControl);
			axisPlaneControlFrame.pack();
		}
		axisPlaneControlFrame.show();
	}

	public void fullScreen() {
		this.setVisible(false);
	}

	SliceViewControlFrame axisPlaneControlFrame;
}
