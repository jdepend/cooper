/*
 * EdgeViewFrame.java
 *
 * Created on December 18, 2001, 5:11 PM
 */

package org.wilmascope.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.wilmascope.control.GraphControl;
import org.wilmascope.view.EdgeView;

/**
 * 
 * @author dwyer
 */
class ColourPanel extends JPanel {
	public ColourPanel(int leftInset, int rightInset) {
		this.leftInset = leftInset;
		this.rightInset = rightInset;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets insets = getInsets();
		int width = getWidth() - insets.left - insets.right - leftInset - rightInset;
		int height = getHeight() - insets.top - insets.bottom;
		// Paint a rectangle on top of the image.
		for (int i = 0; i < width; i++) {
			float hue = 1f - (float) i / (float) width;
			g.setColor(Color.getHSBColor(hue, 1f, 1f));
			g.drawLine(leftInset + i, 0, leftInset + i, height);
		}
	}

	int leftInset, rightInset;
}

public class EdgeViewFrame extends JFrame {

	/** Creates new form EdgeViewFrame */
	public EdgeViewFrame(GraphControl gc, GraphControl.Cluster root) {
		this.rootCluster = root;
		this.graphControl = gc;
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		Box optionsBox = Box.createVerticalBox();
		Box hideBox = Box.createHorizontalBox();
		Box hueBox = Box.createHorizontalBox();

		hideApplyButton = new JButton();
		thresholdLabel = new JLabel();
		hueLabel = new JLabel();
		hideThresholdSlider = new JSlider();
		hueApplyButton = new JButton();
		closePanel = new JPanel();
		closeButton = new JButton();

		thresholdLabel.setText("Hide by weight threshold:");
		hideBox.add(thresholdLabel);

		hideThresholdSlider.setMinorTickSpacing(10);
		hideThresholdSlider.setPaintTicks(true);
		hideThresholdSlider.setMajorTickSpacing(20);
		hideBox.add(hideThresholdSlider);

		hideApplyButton.setText("Apply");
		hideApplyButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				hideApplyButtonActionPerformed(evt);
			}
		});
		hideBox.add(Box.createHorizontalStrut(10));
		hideBox.add(hideApplyButton);

		optionsBox.add(hideBox);

		hueLabel.setText("Show weight by hue:");
		hueBox.add(hueLabel);

		hueBox.add(hueSettingsPanel);
		hueApplyButton.setText("Apply");
		hueApplyButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				hueApplyButtonActionPerformed(evt);
			}
		});

		hueBox.add(Box.createHorizontalStrut(10));
		hueBox.add(hueApplyButton);

		optionsBox.add(hueBox);

		getContentPane().add(optionsBox, java.awt.BorderLayout.CENTER);

		closeButton.setText("Close");
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeButtonActionPerformed(evt);
			}
		});

		closePanel.add(closeButton);

		getContentPane().add(closePanel, java.awt.BorderLayout.SOUTH);

		pack();
	}

	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private void hueApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {
		GraphControl.Edge edges[] = rootCluster.getEdges();
		for (int i = 0; i < edges.length; i++) {
			EdgeView v = (EdgeView) edges[i].getView();
			if (v != null) {
				v.setHueByWeight(hueSettingsPanel.getMinHue(), hueSettingsPanel.getMaxHue());
			}
		}
	}

	private void hideApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {
		GraphControl.Edge edges[] = rootCluster.getEdges();
		float threshold = (float) (hideThresholdSlider.getValue()) / 100f;
		System.out.println("Threshold = " + threshold);
		for (int i = 0; i < edges.length; i++) {
			EdgeView v = (EdgeView) edges[i].getView();
			if (edges[i].getWeight() < threshold) {
				v.hide();
			} else {
				v.show(graphControl.getGraphCanvas());
			}
		}
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private JSlider hideThresholdSlider;
	private JButton closeButton;
	private JButton hideApplyButton;
	private JButton hueApplyButton;
	private JPanel closePanel;
	private JLabel thresholdLabel;
	private JLabel hueLabel;
	private HueSettingsPanel hueSettingsPanel = new HueSettingsPanel();

	private GraphControl.Cluster rootCluster;
	private GraphControl graphControl;
}
