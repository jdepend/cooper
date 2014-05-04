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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class ControlPanel extends JPanel {
	BorderLayout borderLayout1 = new BorderLayout();
	final static String defaultMessage = new String("Right click on graph elements to edit...");
	JLabel zoomLabel = new JLabel();
	JLabel translateLabel = new JLabel();
	JLabel rotateLabel = new JLabel();
	JPanel mouseHelpPanel = new JPanel();
	JPanel dialogPanel = new JPanel();
	JLabel messageLabel = new JLabel();
	BorderLayout borderLayout2 = new BorderLayout();

	public ControlPanel() {
		this.setLayout(borderLayout1);
		setMessage();
		zoomLabel.setText("Zoom");
		zoomLabel.setPreferredSize(new Dimension(120, 60));
		zoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		zoomLabel.setIcon(new ImageIcon(org.wilmascope.images.Images.class.getResource("middleDrag.png")));
		translateLabel.setText("Translate");
		translateLabel.setIcon(new ImageIcon(org.wilmascope.images.Images.class.getResource("rightDrag.png")));
		rotateLabel.setText("Rotate");
		rotateLabel.setIcon(new ImageIcon(org.wilmascope.images.Images.class.getResource("leftDrag.png")));
		mouseHelpPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		mouseHelpPanel.setRequestFocusEnabled(false);
		dialogPanel.setLayout(borderLayout2);
		dialogPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		dialogPanel.setMaximumSize(new Dimension(12345, 40));
		messageLabel.setText(defaultMessage);
		this.add(mouseHelpPanel, BorderLayout.SOUTH);
		mouseHelpPanel.add(rotateLabel, null);
		mouseHelpPanel.add(zoomLabel, null);
		mouseHelpPanel.add(translateLabel, null);
		this.add(dialogPanel, BorderLayout.NORTH);
		dialogPanel.add(messageLabel, BorderLayout.WEST);
	}

	public void hideMouseHelp() {
		mouseHelpPanel.setVisible(false);
	}

	public void showMouseHelp() {
		mouseHelpPanel.setVisible(true);
	}

	public void setMessage() {
		messageLabel.setText(defaultMessage);
	}

	public void setMessage(String msg) {
		messageLabel.setText(msg);
	}

	public void add(JPanel panel) {
		dialogPanel.add(panel, BorderLayout.CENTER);
		doLayout();
	}

	public void add(JButton button) {
		dialogPanel.add(button, BorderLayout.CENTER);
		doLayout();
	}

	public void remove(Component component) {
		dialogPanel.remove(component);
		setMessage();
		updateUI();
	}
}
