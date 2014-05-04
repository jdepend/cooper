package org.wilmascope.multiscalelayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * $Id: MSParamsPanel.java,v 1.2 2004/10/11 11:58:59 tgdwyer Exp $
 * </p>
 * <p>
 * @author
 * </p>
 * <p>
 * @version $Revision: 1.2 $
 * </p>
 * unascribed
 * 
 */

public class MSParamsPanel extends JPanel {
	private BorderLayout borderLayout1 = new BorderLayout();
	private org.wilmascope.control.GraphControl.Cluster root;
	private Box box2;
	private JPanel jPanel1 = new JPanel();
	private TitledBorder titledBorder1;
	private JSlider springSlider = new JSlider();
	private JSlider coolingSlider = new JSlider();
	private JPanel jPanel2 = new JPanel();
	private JPanel jPanel3 = new JPanel();
	private JSlider toleranceSlider = new JSlider();
	private JPanel jPanel4 = new JPanel();
	private JSlider repulsiveSlider = new JSlider();
	private TitledBorder titledBorder2;
	private TitledBorder titledBorder3;
	private JPanel jPanel5 = new JPanel();
	private JSlider kRatioSlider = new JSlider();
	private TitledBorder titledBorder4;
	private TitledBorder titledBorder5;
	private JPanel jPanel6 = new JPanel();
	private JSlider maxDeltaSlider = new JSlider();
	private JButton startButton = new JButton();
	private TitledBorder titledBorder6;
	private JSlider iterationsPerFrameSlider = new JSlider();
	private JPanel jPanel7 = new JPanel();
	private TitledBorder titledBorder7;
	private JPanel jPanel8 = new JPanel();
	private JSlider scaleSlider = new JSlider();
	private TitledBorder titledBorder8;

	public MSParamsPanel(org.wilmascope.control.GraphControl.Cluster root) {
		this.root = root;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		box2 = Box.createVerticalBox();
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),
				"Spring Force");
		titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),
				"Repulsive Force");
		titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),
				"Tolerance");
		titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),
				"Cooling");
		titledBorder5 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),
				"Max Delta");
		titledBorder6 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),
				"Natural Spring Length Ratio");
		titledBorder7 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),
				"Iterations per Frame");
		titledBorder8 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134)),
				"Post Scale");
		this.setLayout(borderLayout1);
		jPanel1.setBorder(titledBorder1);
		springSlider.setPaintLabels(true);
		springSlider.setPaintTicks(true);
		springSlider.setValue((int) (MultiScaleGraph.springForceConstant * 50f));
		springSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				springSlider_stateChanged(e);
			}
		});
		coolingSlider.setValue((int) (MultiScaleGraph.cooling * 100f));
		coolingSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				coolingSlider_stateChanged(e);
			}
		});
		coolingSlider.setPaintTicks(true);
		coolingSlider.setPaintLabels(true);
		jPanel2.setBorder(titledBorder4);
		jPanel3.setBorder(titledBorder3);
		toleranceSlider.setValue((int) (MultiScaleGraph.tol * 10000f));
		toleranceSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				toleranceSlider_stateChanged(e);
			}
		});
		toleranceSlider.setPaintTicks(true);
		toleranceSlider.setPaintLabels(true);
		jPanel4.setBorder(titledBorder2);
		repulsiveSlider.setValue((int) (MultiScaleGraph.repulsiveForceConstant * 50f));
		repulsiveSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				repulsiveSlider_stateChanged(e);
			}
		});
		repulsiveSlider.setPaintTicks(true);
		repulsiveSlider.setPaintLabels(true);
		jPanel5.setBorder(titledBorder6);
		kRatioSlider.setValue((int) (QuickGraph.kscale * 50f));
		kRatioSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				kRatioSlider_stateChanged(e);
			}
		});
		kRatioSlider.setPaintTicks(true);
		kRatioSlider.setPaintLabels(true);
		jPanel6.setBorder(titledBorder5);
		maxDeltaSlider.setValue((int) (MultiScaleGraph.maxDelta * 50f));
		maxDeltaSlider.setPaintLabels(true);
		maxDeltaSlider.setPaintTicks(true);
		maxDeltaSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				maxDeltaSlider_stateChanged(e);
			}
		});
		startButton.setText("Start");
		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButton_actionPerformed(e);
			}
		});
		iterationsPerFrameSlider.setValue((int) (QuickGraph.iterationsPerFrame));
		iterationsPerFrameSlider.setPaintLabels(true);
		iterationsPerFrameSlider.setPaintTicks(true);
		iterationsPerFrameSlider.setMajorTickSpacing(20);
		iterationsPerFrameSlider.setMinorTickSpacing(5);
		iterationsPerFrameSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				iterationsPerFrameSlider_stateChanged(e);
			}
		});
		jPanel7.setBorder(titledBorder7);
		jPanel8.setBorder(titledBorder8);
		scaleSlider.setValue((int) (MultiScaleLayout.scale * 100f));
		scaleSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scaleSlider_stateChanged(e);
			}
		});
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		jPanel1.add(springSlider, null);
		box2.add(startButton, null);
		box2.add(jPanel4, null);
		jPanel4.add(repulsiveSlider, null);
		box2.add(jPanel1, null);
		box2.add(jPanel6, null);
		jPanel6.add(maxDeltaSlider, null);
		box2.add(jPanel3, null);
		jPanel3.add(toleranceSlider, null);
		box2.add(jPanel2, null);
		jPanel2.add(coolingSlider, null);
		box2.add(jPanel5, null);
		jPanel5.add(kRatioSlider, null);
		this.add(box2, BorderLayout.CENTER);
		box2.add(jPanel8, null);
		jPanel8.add(scaleSlider, null);
		box2.add(jPanel7, null);
		jPanel7.add(iterationsPerFrameSlider, null);
	}

	void startButton_actionPerformed(ActionEvent e) {
		root.unfreeze();
	}

	void springSlider_stateChanged(ChangeEvent e) {
		MultiScaleGraph.springForceConstant = (float) springSlider.getValue() / 50f;
		System.out.println("Spring Force = " + MultiScaleGraph.springForceConstant);
	}

	void coolingSlider_stateChanged(ChangeEvent e) {
		MultiScaleGraph.cooling = (float) coolingSlider.getValue() / 100f;
		System.out.println("Spring Force = " + MultiScaleGraph.cooling);
	}

	void toleranceSlider_stateChanged(ChangeEvent e) {
		MultiScaleGraph.tol = (float) toleranceSlider.getValue() / 10000f;
		System.out.println("Tolerance = " + MultiScaleGraph.tol);
	}

	void repulsiveSlider_stateChanged(ChangeEvent e) {
		MultiScaleGraph.repulsiveForceConstant = (float) repulsiveSlider.getValue() / 50f;
		System.out.println("Repulsive Force = " + MultiScaleGraph.repulsiveForceConstant);
	}

	void kRatioSlider_stateChanged(ChangeEvent e) {
		QuickGraph.kscale = (float) kRatioSlider.getValue() / 50f;
		System.out.println("kRatio = " + QuickGraph.kscale);
	}

	void maxDeltaSlider_stateChanged(ChangeEvent e) {
		MultiScaleGraph.maxDelta = (float) maxDeltaSlider.getValue() / 50f;
		System.out.println("Max Delta = " + MultiScaleGraph.maxDelta);
	}

	void iterationsPerFrameSlider_stateChanged(ChangeEvent e) {
		QuickGraph.iterationsPerFrame = iterationsPerFrameSlider.getValue();
		System.out.println("iterations per frame = " + QuickGraph.iterationsPerFrame);
	}

	void scaleSlider_stateChanged(ChangeEvent e) {
		MultiScaleLayout.scale = (float) scaleSlider.getValue() / 100f;
		System.out.println("post scale = " + MultiScaleLayout.scale);
	}
}
