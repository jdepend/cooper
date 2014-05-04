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

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A combination spinner/slider with labels etc
 * 
 * @author dwyer
 */
public class SpinnerSlider extends JPanel {
	JSlider slider;
	JSpinner spinner;
	int value;

	/**
	 * Creates a labelled slider suitable for adding to the control panel of
	 * graphGenerators
	 * 
	 * @param label
	 *            displayed in border round slider
	 * @param min
	 *            minimum value of slider
	 * @param max
	 *            maximim value of slider
	 * @param initValue
	 *            default
	 */
	public SpinnerSlider(String label, int min, int max, int initValue) {
		value = initValue;
		slider = new JSlider(min, max, initValue);
		SpinnerNumberModel sm = new SpinnerNumberModel(initValue, min, max, 1);
		spinner = new JSpinner(sm);
		int range = max - min;
		setBorder(new javax.swing.border.TitledBorder(label));
		slider.setMinorTickSpacing(range / 10);
		slider.setMajorTickSpacing(slider.getMinorTickSpacing() * 2);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setValue(initValue);
		slider.setLabelTable(slider.createStandardLabels(range / 5));
		add(slider);
		add(spinner);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				value = slider.getValue();
				spinner.setValue(new Integer(value));
			}
		});
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				value = ((Integer) spinner.getValue()).intValue();
				slider.setValue(value);
			}
		});
	}

	public void addChangeListener(ChangeListener l) {
		slider.addChangeListener(l);
		spinner.addChangeListener(l);
	}

	public int getValue() {
		return value;
	}

	public void setEnabled(boolean enabled) {
		slider.setEnabled(enabled);
		spinner.setEnabled(enabled);
	}

	public boolean isEnabled() {
		return spinner.isEnabled();
	}
}
