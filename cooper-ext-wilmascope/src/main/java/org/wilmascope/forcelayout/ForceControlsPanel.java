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

package org.wilmascope.forcelayout;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

import org.wilmascope.control.GraphControl;

/**
 * Title: WilmaToo Description: Sequel to the ever popular Wilma graph drawing
 * engine Copyright: Copyright (c) 2001 Company: WilmaOrg
 * 
 * @author Tim Dwyer
 * @version 1.0
 */

public class ForceControlsPanel extends JPanel {

	public ForceControlsPanel() {
	}

	ForceLayout forceLayout;

	GraphControl.Cluster cluster;

	// Variables declaration
	JSlider velocityAttenuationSlider = new JSlider();

	JSlider frictionSlider = new JSlider();

	JSlider iterationsPerFrameSlider = new JSlider();

	JScrollPane jScrollPane1 = new JScrollPane();

	JScrollPane jScrollPane2 = new JScrollPane();

	Box boxLayout;

	Box forceLayoutControlsBox;

	Box forceControlsBox;

	JPanel jPanel1 = new JPanel();

	JSlider balancedThresholdSlider = new JSlider();

	public ForceControlsPanel(GraphControl.Cluster cluster) {
		this.cluster = cluster;
		this.forceLayout = (ForceLayout) cluster.getLayoutEngine();
		boxLayout = Box.createVerticalBox();
		forceControlsBox = Box.createVerticalBox();
		forceLayoutControlsBox = Box.createVerticalBox();
		this.add(boxLayout, BorderLayout.CENTER);
		boxLayout.add(jScrollPane1, null);
		jScrollPane1.getViewport().add(forceControlsBox, null);
		forceControlsBox.add(jPanel1, null);
		boxLayout.add(jScrollPane2, null);
		jScrollPane2.getViewport().add(forceLayoutControlsBox, null);
		velocityAttenuationSlider.setBorder(new javax.swing.border.TitledBorder("Velocity Scale"));
		velocityAttenuationSlider.setMinorTickSpacing(10);
		velocityAttenuationSlider.setPaintLabels(true);
		velocityAttenuationSlider.setPaintTicks(true);
		velocityAttenuationSlider.setMajorTickSpacing(50);
		velocityAttenuationSlider.setValue((int) ((float) forceLayout.getVelocityAttenuation() * 1000f));
		velocityAttenuationSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				forceLayout.setVelocityAttenuation((float) velocityAttenuationSlider.getValue() / 1000f);
			}
		});
		forceLayoutControlsBox.add(velocityAttenuationSlider);
		frictionSlider.setBorder(new javax.swing.border.TitledBorder("Friction Coefficient"));
		frictionSlider.setMinorTickSpacing(10);
		frictionSlider.setPaintLabels(true);
		frictionSlider.setPaintTicks(true);
		frictionSlider.setMajorTickSpacing(50);
		frictionSlider.setValue((int) ((float) Constants.frictionCoefficient));
		frictionSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				Constants.frictionCoefficient = (float) frictionSlider.getValue();
			}
		});
		forceLayoutControlsBox.add(frictionSlider);
		balancedThresholdSlider.setBorder(new javax.swing.border.TitledBorder("Balanced Threshold"));
		balancedThresholdSlider.setMinorTickSpacing(10);
		balancedThresholdSlider.setPaintLabels(true);
		balancedThresholdSlider.setPaintTicks(true);
		balancedThresholdSlider.setMajorTickSpacing(50);
		balancedThresholdSlider.setValue((int) ((float) forceLayout.getBalancedThreshold() * 1000f));
		balancedThresholdSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				forceLayout.setBalancedThreshold((float) balancedThresholdSlider.getValue() / 1000f);
			}
		});
		forceLayoutControlsBox.add(balancedThresholdSlider);

		for (Force f : ForceManager.getInstance().getAvailableForces()) {
			forceControlsBox.add(new ForceControlPanel(cluster, f));
		}
	}
}
