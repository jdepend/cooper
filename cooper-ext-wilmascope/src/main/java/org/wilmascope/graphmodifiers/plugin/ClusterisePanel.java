package org.wilmascope.graphmodifiers.plugin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.wilmascope.control.GraphControl;
import org.wilmascope.forcelayout.ForceLayout;

/**
 * Controls for clustering.
 * 
 * @author dwyer
 * @version 1.0
 */

public class ClusterisePanel extends JPanel {
	JTabbedPane ClusterChoicePane = new JTabbedPane();
	Box kMeansBox;
	JPanel jPanel1 = new JPanel();
	JLabel jLabel1 = new JLabel();
	JTextField kField = new JTextField();
	JPanel jPanel2 = new JPanel();
	JLabel jLabel2 = new JLabel();
	JTextField keepField = new JTextField();

	public ClusterisePanel() {
		kMeansBox = Box.createVerticalBox();
		jLabel1.setText("Clusters to examine (k)");
		kField.setPreferredSize(new Dimension(50, 27));
		kField.setText("10");
		jLabel2.setText("Clusters to keep (<=k)");
		keepField.setPreferredSize(new Dimension(50, 27));
		keepField.setText("5");
		expandedCheckBox.setText("Show Expanded");
		expandedCheckBox.setSelected(true);
		add(ClusterChoicePane, BorderLayout.CENTER);
		ClusterChoicePane.add(kMeansBox, "k-Means");
		kMeansBox.add(jPanel1, null);
		jPanel1.add(jLabel1, null);
		jPanel1.add(kField, null);
		kMeansBox.add(jPanel2, null);
		jPanel2.add(jLabel2, null);
		jPanel2.add(keepField, null);
		kMeansBox.add(jPanel4, null);
		jPanel4.add(expandedCheckBox, null);
	}

	public void applyClustering(GraphControl.Cluster rootCluster) {
		KMeans transformer = new KMeans();
		int k = Integer.parseInt(kField.getText());
		int n = Integer.parseInt(keepField.getText());
		Vector[] clusters = transformer.kMeansClustering(rootCluster, k, n);
		for (int i = 0; i < clusters.length; i++) {
			GraphControl.Cluster newCluster = rootCluster.addCluster(clusters[i]);
			newCluster.setLayoutEngine(ForceLayout.createDefaultClusterForceLayout(newCluster.getCluster()));
			if (!expandedCheckBox.isSelected()) {
				newCluster.collapse();
			}
		}
	}

	public void setK(int value) {
		kField.setText("" + value);
	}

	public void setN(int value) {
		keepField.setText("" + value);
	}

	GraphControl.Cluster rootCluster;
	JPanel jPanel4 = new JPanel();
	JCheckBox expandedCheckBox = new JCheckBox();

}
