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
package org.wilmascope.dotlayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.wilmascope.control.GraphControl;

/**
 * @author dwyer
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ControlPanel extends JPanel {
	public ControlPanel(final GraphControl.Cluster c) {
		final DotLayout dl = (DotLayout) c.getLayoutEngine();
		final JTextField xScaleField = new JTextField("" + dl.getXScale());
		final JTextField yScaleField = new JTextField("" + dl.getYScale());
		final JTextField strataSepField = new JTextField("" + dl.getStrataSeparation());
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dl.setXScale(Float.parseFloat(xScaleField.getText()));
				dl.setYScale(Float.parseFloat(yScaleField.getText()));
				dl.setStrataSeparation(Float.parseFloat(strataSepField.getText()));
				c.unfreeze();
			}
		});
		xScaleField.setPreferredSize(new Dimension(100, 20));
		yScaleField.setPreferredSize(new Dimension(100, 20));
		strataSepField.setPreferredSize(new Dimension(100, 20));
		Box hBox = Box.createHorizontalBox();
		Box vBox = Box.createVerticalBox();
		hBox.add(new JLabel("X Scale"));
		hBox.add(xScaleField);
		vBox.add(hBox);
		hBox = Box.createHorizontalBox();
		hBox.add(new JLabel("Y Scale"));
		hBox.add(yScaleField);
		vBox.add(hBox);
		hBox = Box.createHorizontalBox();
		hBox.add(new JLabel("Strata Separation"));
		hBox.add(strataSepField);
		vBox.add(hBox);
		vBox.add(okButton);
		add(vBox);
	}
}
