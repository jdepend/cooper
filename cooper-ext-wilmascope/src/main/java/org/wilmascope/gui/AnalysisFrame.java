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
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import org.wilmascope.control.WilmaMain;
import org.wilmascope.control.GraphControl.Cluster;
import org.wilmascope.control.GraphControl.Node;
import org.wilmascope.graphanalysis.AnalysisManager;
import org.wilmascope.graphanalysis.AnalysisPanel;
import org.wilmascope.graphanalysis.GraphAnalysis;
import org.wilmascope.graphanalysis.VisualMapping;
import org.wilmascope.util.UnknownTypeException;

/**
 * Controls for analysis plugins and determining their mappings to visual
 * elements
 * 
 * @author dwyer
 */

public class AnalysisFrame extends JFrame {
	final Box analysersBox = Box.createVerticalBox();

	Cluster rootCluster;

	public AnalysisFrame(String title, final Cluster rootCluster) {
		this.rootCluster = rootCluster;
		setTitle(title);
		Box topBox = Box.createHorizontalBox();
		AnalysisManager manager = AnalysisManager.getInstance();
		final JComboBox analysisComboBox = new JComboBox(manager.getTypeList());
		analysisComboBox.setSelectedItem(manager.getDefault().getName());
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String s = (String) analysisComboBox.getSelectedItem();
				addPlugin(s, null);
			}

		});
		JButton chartButton = new JButton("Chart");
		chartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Node[] nodes = rootCluster.getNodes();
				List<String> keys = getAnalysisKeys();
				int m = keys.size();
				int n = nodes.length;
				String[] names = new String[m];
				String[] nodeLabels = new String[n];
				float[][] data = new float[n][m];
				for (int i = 0; i < m; i++) {
					names[i] = keys.get(i);
					for (int j = 0; j < n; j++) {
						Node v = nodes[j];
						float cent = Float.parseFloat(v.getProperties().getProperty(names[i]));
						nodeLabels[j] = "" + j;
						data[j][i] = cent;
					}
				}
				// Chart chart = new Chart(nodeLabels, data, names);
				// chart.setSize(640, 400);
				// chart.setVisible(true);
			}
		});
		topBox.add(analysisComboBox);
		topBox.add(addButton);
		topBox.add(chartButton);
		analysersBox.add(topBox);
		add(analysersBox);
		for (String mapping : VisualMapping.getClusterMappings(rootCluster)) {
			String[] m = mapping.split("-");
			addPlugin(m[0], m[1]);
		}
		pack();
	}

	public List<String> getAnalysisKeys() {
		List<String> list = new ArrayList<String>();
		for (String mapping : VisualMapping.getClusterMappings(rootCluster)) {
			String[] m = mapping.split("-");
			list.add(m[0]);
		}
		return list;
	}

	private void addPlugin(final String pluginName, String mappingName) {
		GraphAnalysis plugin;
		try {
			plugin = AnalysisManager.getInstance().getPlugin(pluginName);
			plugin.setCluster(rootCluster);
			final AnalysisPanel ap = (AnalysisPanel) plugin.getControls();
			if (mappingName != null)
				ap.setDefaultMapping(mappingName);
			ap.addRemoveListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					analysersBox.remove(ap);
					VisualMapping.removeStoredMapping(rootCluster, pluginName);
					pack();
				}
			});
			ap.addPackListener(new Observer() {
				public void update(Observable o, Object arg) {
					pack();
				}

			});
			analysersBox.add(ap);
			pack();
		} catch (UnknownTypeException e1) {
			WilmaMain.showErrorDialog(e1.getMessage(), e1);
		}
	}

}
