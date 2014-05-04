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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class HelpFrame extends JFrame {
	JPanel controlsPanel;
	JButton closeButton;
	HtmlPane html;

	public HelpFrame(String helpFileName) {
		super("Wilma Help");
		html = new HtmlPane(helpFileName);
		this.setSize(400, 500);
		// setBounds( 200, 25, 400, 400);
		getContentPane().setLayout(new BorderLayout());
		controlsPanel = new JPanel();
		getContentPane().add(BorderLayout.CENTER, html);
		getContentPane().add(BorderLayout.NORTH, controlsPanel);
		controlsPanel.add(closeButton = new JButton("Close"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeButtonActionPerformed();
			}
		});
	}

	public void closeButtonActionPerformed() {
		this.dispose();
	}
}
