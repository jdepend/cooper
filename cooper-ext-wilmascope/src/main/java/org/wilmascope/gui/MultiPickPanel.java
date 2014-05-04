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
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.PickListener;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public abstract class MultiPickPanel extends JPanel {

	BorderLayout borderLayout1 = new BorderLayout();
	Box box;
	JButton cancelButton = new JButton();
	JButton okButton = new JButton();
	Component boxGlue;
	GraphControl.Cluster cluster;
	ControlPanel controlPanel;
	Actions actions;

	public MultiPickPanel(ControlPanel controlPanel, GraphControl.Cluster cluster) {
		this.cluster = cluster;
		this.controlPanel = controlPanel;
		this.actions = Actions.getInstance();
		actions.setEnabled(false);
		box = Box.createHorizontalBox();
		boxGlue = Box.createGlue();
		this.setLayout(borderLayout1);
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});
		this.add(box, BorderLayout.NORTH);
		box.add(boxGlue, null);
		box.add(okButton, null);
		box.add(cancelButton, null);
		controlPanel.setMessage(getLabel());
	}

	abstract void okButton_actionPerformed(ActionEvent e);

	abstract String getLabel();

	void cancelButton_actionPerformed(ActionEvent e) {
		PickListener pl = GraphControl.getPickListener();
		while (pl.getPickedListSize() > 0) {
			GraphControl.GraphElementFacade element = pl.pop();
		}
		cleanup();
	}

	void cleanup() {
		GraphControl.getPickListener().disableMultiPicking();
		cluster.setAllPickable(true);
		controlPanel.remove(this);
		actions.setEnabled(true);
	}
}
