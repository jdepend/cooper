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
import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author dwyer
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HueSettingsPanel extends JPanel {
	Box hueSettingsBox = Box.createVerticalBox();
	private ColourPanel hueSliderPanel;
	private JLabel maxLabel;
	private JSlider minHueSlider;
	private JPanel minLabelPanel;
	private JPanel maxLabelPanel;
	private JLabel minLabel;
	private JSlider maxHueSlider;

	private Color getColourFromSlider(JSlider s) {
		return Color.getHSBColor(getHueFromSlider(s), 1f, 1f);
	}

	private float getHueFromSlider(JSlider s) {
		return (float) (100 - s.getValue()) / 100f;
	}

	private void maxHueSliderStateChanged(ChangeEvent evt) {
		maxLabelPanel.setBackground(getColourFromSlider(maxHueSlider));
	}

	private void minHueSliderStateChanged(ChangeEvent evt) {
		minLabelPanel.setBackground(getColourFromSlider(minHueSlider));
	}

	public HueSettingsPanel() {
		hueSliderPanel = new ColourPanel(7, 7);
		minLabel = new JLabel();
		minHueSlider = new JSlider();
		maxLabel = new JLabel();
		maxHueSlider = new JSlider();
		minLabelPanel = new JPanel();
		maxLabelPanel = new JPanel();
		hueSliderPanel.setLayout(new BoxLayout(hueSliderPanel, BoxLayout.Y_AXIS));

		minLabel.setText("Min");
		minLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		minLabelPanel.setLayout(new BorderLayout());
		minLabelPanel.add(minLabel, BorderLayout.WEST);
		hueSettingsBox.add(minLabelPanel);

		minHueSlider.setValue(30);
		minHueSlider.setPaintTrack(false);
		minHueSlider.setOpaque(false);
		minHueSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				minHueSliderStateChanged(evt);
			}
		});

		hueSliderPanel.add(minHueSlider);

		maxLabel.setText("Max");
		maxLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		maxLabelPanel.setLayout(new BorderLayout());
		maxLabelPanel.add(maxLabel, BorderLayout.EAST);

		maxHueSlider.setValue(100);
		maxHueSlider.setOpaque(false);
		maxHueSlider.setPaintTrack(false);
		maxHueSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				maxHueSliderStateChanged(evt);
			}
		});

		hueSliderPanel.add(maxHueSlider);

		hueSettingsBox.add(hueSliderPanel);

		hueSettingsBox.add(maxLabelPanel);

		minLabelPanel.setBackground(getMinColour());
		maxLabelPanel.setBackground(getMaxColour());
		this.add(hueSettingsBox);
	}

	public float getMinHue() {
		return getHueFromSlider(minHueSlider);

	}

	public float getMaxHue() {
		return getHueFromSlider(maxHueSlider);

	}

	public Color getMinColour() {
		return getColourFromSlider(minHueSlider);

	}

	public Color getMaxColour() {
		return getColourFromSlider(maxHueSlider);

	}

}
