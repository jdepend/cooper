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
package org.wilmascope.graphanalysis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AnalysisPanel extends JPanel {
	/**
	 * Comment for <code>analysis</code>
	 */
	private final GraphAnalysis analysis;

	class ObservableChanged extends Observable {
		void changed() {
			setChanged();
			notifyObservers();
		}
	};

	private ObservableChanged observableChanged = new ObservableChanged();

	JButton removeButton = new JButton("Remove Mapping");
	final JComboBox mappingComboBox;
	JButton applyButton = new JButton("Apply Mapping");
	Hashtable<String, VisualMapping> mappings = new Hashtable<String, VisualMapping>();

	public AnalysisPanel(GraphAnalysis analysis) {
		this(analysis, "No Visual Mapping");
	}

	public AnalysisPanel(final GraphAnalysis analysis, String defaultMapping) {
		this.analysis = analysis;
		final String analysisType = analysis.getName();

		addMapping(new NoVisualMapping());
		addMapping(new NodeSizeMapping());
		addMapping(new NodeMassMapping());
		addMapping(new NodeColourMapping());
		addMapping(new NodeLabelMapping());
		addMapping(new LevelConstraintMapping());
		addMapping(new OrbitalConstraintMapping());
		Box b = Box.createHorizontalBox();
		final JPanel mappingPanel = new JPanel();
		final Box vb = Box.createVerticalBox();
		mappingPanel.add(vb);
		JLabel mapsToLabel = new JLabel("<html> " + analysisType + " <i> maps to... </i></html>");
		mappingComboBox = new JComboBox(new Vector<String>(mappings.keySet()));
		mappingComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualMapping m = mappings.get((String) mappingComboBox.getSelectedItem());
				vb.add(m.getControls());
				observableChanged.changed();
			}
		});
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VisualMapping m = mappings.get((String) mappingComboBox.getSelectedItem());
				analysis.analyse();
				m.apply(analysis.getCluster(), analysisType);
				m.storeMapping(analysis.getCluster(), analysisType);
				observableChanged.changed();
			}
		});
		b.add(mapsToLabel);
		vb.add(mappingComboBox);
		vb.add(applyButton);
		b.add(mappingPanel);
		b.add(removeButton);
		add(b);
		setDefaultMapping(defaultMapping);
	}

	public void setDefaultMapping(String defaultMapping) {
		mappingComboBox.setSelectedItem(defaultMapping);
	}

	private void addMapping(VisualMapping m) {
		mappings.put(m.getAttributeName(), m);
	}

	public void addRemoveListener(ActionListener l) {
		removeButton.addActionListener(l);
	}

	public void addPackListener(Observer l) {
		observableChanged.addObserver(l);
	}
}
