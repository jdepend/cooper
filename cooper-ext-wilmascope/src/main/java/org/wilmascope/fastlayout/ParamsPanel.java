package org.wilmascope.fastlayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import org.wilmascope.control.GraphControl;

/**
 * Description: Window for setting up and running a FastLayout engine test
 * 
 * @author James Cowling
 * @version 1.0
 */

// this code is rather ugly but does the job and isn't too important
public class ParamsPanel extends JPanel implements ActionListener {

	FastLayout layout;

	GraphControl.Cluster root;

	JSlider attractSlider = new JSlider(0, 20, Defaults.ATTRACTION);

	JSlider repelSlider = new JSlider(0, 20, Defaults.REPULSION);

	JSlider footSlider = new JSlider(1, 20, Defaults.NODE_FOOTPRINT);

	// JSlider minDenseSlider = new JSlider();
	JSlider boilJumpSlider = new JSlider(0, 5, (int) Defaults.BOIL_JUMP);

	JSlider radiusSlider = new JSlider(1, 5000, Defaults.FIELD_RADIUS);

	JSlider resSlider = new JSlider(1, 20, Defaults.FIELD_RES);

	JSlider iterSlider = new JSlider(1, 1000, Defaults.ITERATIONS);

	JSlider simmerSlider = new JSlider(0, 100, (int) (Defaults.SIMMER_RATE * 100));

	JSlider maxBarrierSlider = new JSlider((int) (Defaults.MIN_BARRIER_RATE * 100), 100,
			(int) (Defaults.MAX_BARRIER_RATE * 100));

	JSlider minBarrierSlider = new JSlider(0, (int) (Defaults.MAX_BARRIER_RATE * 100),
			(int) (Defaults.MIN_BARRIER_RATE * 100));

	JSlider boilLenSlider = new JSlider(0, 100, (int) (Defaults.BOIL_LENGTH * 100));

	JSlider quenchLenSlider = new JSlider(0, 100 - boilLenSlider.getValue(), (int) (Defaults.QUENCH_LENGTH * 100));

	JPanel checkPanel = new JPanel();

	JCheckBox centreCheck = new JCheckBox("Graph centering", Defaults.CENTRE_FLAG);

	JCheckBox colourCheck = new JCheckBox("Colour coding", Defaults.COLOUR_FLAG);

	JCheckBox candyCheck = new JCheckBox("Eye-candy", Defaults.EYE_CANDY_FLAG);

	JCheckBox naiveCheck = new JCheckBox("Naive repositioning", Defaults.NAIVE);

	JCheckBox barrierCheck = new JCheckBox("Barrier-jumping while boiling", Defaults.BOIL_BARRIER);

	JPanel buttonPanel = new JPanel();

	JButton pauseButton = new JButton("Run");

	JButton flattenButton = new JButton("Flatten graph");

	JProgressBar progressBar = new JProgressBar(0, Defaults.ITERATIONS);

	JProgressBar energyGuage = new JProgressBar(JProgressBar.VERTICAL, 0, 0);

	Box bigBox;

	Box box;

	Box boxLayout1;

	Box boxLayout2;

	public ParamsPanel(GraphControl.Cluster root) {
		this.layout = (FastLayout) root.getLayoutEngine();
		this.root = root;
		bigBox = Box.createVerticalBox();
		box = Box.createHorizontalBox();
		boxLayout1 = Box.createVerticalBox();
		boxLayout2 = Box.createVerticalBox();
		box.add(boxLayout1);

		energyGuage.setValue(energyGuage.getMaximum());
		energyGuage.setStringPainted(true);
		energyGuage.setString("Energy");
		energyGuage.setBorderPainted(true);
		box.add(energyGuage);

		box.add(boxLayout2);
		bigBox.add(box);
		add(bigBox, null);

		attractSlider.setBorder(new TitledBorder("Attraction scale factor"));
		attractSlider.setMinorTickSpacing(1);
		attractSlider.setPaintLabels(true);
		attractSlider.setPaintTicks(true);
		attractSlider.setMajorTickSpacing(5);
		attractSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				layout.setAttract(attractSlider.getValue());
			}
		});
		boxLayout1.add(attractSlider);

		repelSlider.setBorder(new TitledBorder("Repulsion scale factor"));
		repelSlider.setMinorTickSpacing(1);
		repelSlider.setPaintLabels(true);
		repelSlider.setPaintTicks(true);
		repelSlider.setMajorTickSpacing(5);
		repelSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				layout.setRepel(repelSlider.getValue());
			}
		});
		boxLayout1.add(repelSlider);

		boilJumpSlider.setBorder(new TitledBorder("Maximum jump"));
		boilJumpSlider.setMinorTickSpacing(1);
		boilJumpSlider.setPaintLabels(true);
		boilJumpSlider.setPaintTicks(true);
		boilJumpSlider.setMajorTickSpacing(5);
		boilJumpSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				layout.setBoilJump(boilJumpSlider.getValue());
			}
		});
		boxLayout1.add(boilJumpSlider);

		footSlider.setBorder(new TitledBorder("Node footprint"));
		footSlider.setMinorTickSpacing(1);
		footSlider.setPaintLabels(true);
		footSlider.setPaintTicks(true);
		footSlider.setMajorTickSpacing(5);
		footSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				layout.getUniverse().setFootprint(footSlider.getValue());
			}
		});
		boxLayout1.add(footSlider);

		resSlider.setBorder(new TitledBorder("Field Resolution"));
		resSlider.setMinorTickSpacing(1);
		resSlider.setPaintLabels(true);
		resSlider.setPaintTicks(true);
		resSlider.setMajorTickSpacing(5);
		resSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				layout.getUniverse().setRes(resSlider.getValue());
			}
		});
		boxLayout1.add(resSlider);

		radiusSlider.setBorder(new TitledBorder("Field Radius"));
		radiusSlider.setMinorTickSpacing(250);
		radiusSlider.setPaintLabels(true);
		radiusSlider.setPaintTicks(true);
		radiusSlider.setMajorTickSpacing(1250);
		radiusSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				layout.setRadius(radiusSlider.getValue());
			}
		});
		boxLayout1.add(radiusSlider);

		simmerSlider.setBorder(new TitledBorder("Simmer Rate (%)"));
		simmerSlider.setMinorTickSpacing(5);
		simmerSlider.setPaintLabels(true);
		simmerSlider.setPaintTicks(true);
		simmerSlider.setMajorTickSpacing(25);
		simmerSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				layout.setSimmerRate(simmerSlider.getValue() / 100d);
			}
		});
		boxLayout1.add(simmerSlider);

		boilLenSlider.setBorder(new TitledBorder("Time spent in boiling phase (%)"));
		boilLenSlider.setMinorTickSpacing(5);
		boilLenSlider.setPaintLabels(true);
		boilLenSlider.setPaintTicks(true);
		boilLenSlider.setMajorTickSpacing(25);
		boilLenSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				quenchLenSlider.setMaximum(100 - boilLenSlider.getValue());
				layout.setBoilLen(boilLenSlider.getValue() / 100d);
			}
		});
		boxLayout2.add(boilLenSlider);

		quenchLenSlider.setBorder(new TitledBorder("Time spent in quenching phase (%)"));
		quenchLenSlider.setMinorTickSpacing(5);
		quenchLenSlider.setPaintLabels(true);
		quenchLenSlider.setPaintTicks(true);
		quenchLenSlider.setMajorTickSpacing(25);
		quenchLenSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				boilLenSlider.setMaximum(100 - quenchLenSlider.getValue());
				layout.setQuenchLen(quenchLenSlider.getValue() / 100d);
			}
		});
		boxLayout2.add(quenchLenSlider);

		maxBarrierSlider.setBorder(new TitledBorder("Max barrier jump rate (%)"));
		maxBarrierSlider.setMinorTickSpacing(5);
		maxBarrierSlider.setPaintLabels(true);
		maxBarrierSlider.setPaintTicks(true);
		maxBarrierSlider.setMajorTickSpacing(25);
		maxBarrierSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				minBarrierSlider.setMaximum(maxBarrierSlider.getValue());
				layout.setMaxBarrier(maxBarrierSlider.getValue() / 100d);
			}
		});
		boxLayout2.add(maxBarrierSlider);

		minBarrierSlider.setBorder(new TitledBorder("Min barrier jump rate (%)"));
		minBarrierSlider.setMinorTickSpacing(5);
		minBarrierSlider.setPaintLabels(true);
		minBarrierSlider.setPaintTicks(true);
		minBarrierSlider.setMajorTickSpacing(25);
		minBarrierSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				maxBarrierSlider.setMinimum(minBarrierSlider.getValue());
				layout.setMinBarrier(minBarrierSlider.getValue() / 100d);
			}
		});
		boxLayout2.add(minBarrierSlider);

		iterSlider.setBorder(new TitledBorder("Iterations"));
		iterSlider.setMinorTickSpacing(50);
		iterSlider.setPaintLabels(true);
		iterSlider.setPaintTicks(true);
		iterSlider.setMajorTickSpacing(250);
		iterSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				progressBar.setMaximum(iterSlider.getValue());
				layout.setIterations(iterSlider.getValue());
			}
		});
		boxLayout2.add(iterSlider);

		checkPanel.setLayout(new GridLayout(5, 1));
		checkPanel.setBorder(new TitledBorder("Algorithm Flags"));

		centreCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				layout.setCentreFlag(centreCheck.isSelected());
			}
		});
		checkPanel.add(centreCheck);

		naiveCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				layout.setNaive(naiveCheck.isSelected());
			}
		});
		checkPanel.add(naiveCheck);

		barrierCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				layout.setBoilBarrier(barrierCheck.isSelected());
			}
		});
		checkPanel.add(barrierCheck);

		colourCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				layout.setColourFlag(colourCheck.isSelected());
			}
		});
		checkPanel.add(colourCheck);

		candyCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				layout.setEyeCandyFlag(candyCheck.isSelected());
			}
		});
		/*
		 * if(test.lineView()) { candyCheck.setSelected(false);
		 * candyCheck.setEnabled(false); } else candyCheck.setEnabled(true);
		 */
		checkPanel.add(candyCheck);

		boxLayout2.add(checkPanel);

		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.setBorder(new TitledBorder("Control:"));

		pauseButton.addActionListener(this);
		pauseButton.setActionCommand("pause");
		buttonPanel.add(pauseButton);

		flattenButton.addActionListener(this);
		flattenButton.setActionCommand("flatten");
		buttonPanel.add(flattenButton);

		bigBox.add(buttonPanel);

		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setString("");
		bigBox.add(progressBar);

	}

	public void setLayout(FastLayout layout) {
		this.layout = layout;
		/*
		 * if(test.lineView()) { candyCheck.setSelected(false);
		 * candyCheck.setEnabled(false); } else candyCheck.setEnabled(true);
		 */
		pauseButton.setText("Run");
		pauseButton.setEnabled(true);

		progressBar.setValue(0);
		energyGuage.setValue(0);

		// set all parameters of layout engine accordingly
		layout.setAttract(attractSlider.getValue());
		layout.setRepel(repelSlider.getValue());
		layout.setBoilJump(boilJumpSlider.getValue());
		layout.getUniverse().setFootprint(footSlider.getValue());
		layout.getUniverse().setRes(resSlider.getValue());
		layout.setRadius(radiusSlider.getValue());
		layout.setSimmerRate(simmerSlider.getValue() / 100d);
		layout.setBoilLen(boilLenSlider.getValue() / 100d);
		layout.setQuenchLen(quenchLenSlider.getValue() / 100d);
		layout.setMaxBarrier(maxBarrierSlider.getValue() / 100d);
		layout.setMinBarrier(minBarrierSlider.getValue() / 100d);
		layout.setIterations(iterSlider.getValue());
		layout.setCentreFlag(centreCheck.isSelected());
		layout.setColourFlag(colourCheck.isSelected());
		layout.setEyeCandyFlag(candyCheck.isSelected());
		layout.setNaive(naiveCheck.isSelected());
		layout.setBoilBarrier(barrierCheck.isSelected());

	}

	public final static int ONE_SECOND = 1000;

	Timer timer = new Timer(ONE_SECOND, new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			progressBar.setValue(layout.getIterations());
			progressBar.setString(layout.getPhase());
			if (progressBar.getValue() == iterSlider.getValue()) {
				progressBar.setString("Finished");
				pauseButton.setEnabled(false);
				timer.stop();
			}
			int energy = (int) layout.systemEnergy();
			if (energy > energyGuage.getMaximum())
				energyGuage.setMaximum(energy);
			energyGuage.setValue(energy);
		}
	});

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("pause")) {
			if (pauseButton.getText().equals("Run")) {
				pauseButton.setText("Pause");
				timer.start();
				root.unfreeze();
			} else {
				pauseButton.setText("Run");
				timer.stop();
				root.freeze();
			}
		} else if (e.getActionCommand().equals("flatten")) {
			layout.flattenGraph();
		}
	}
}
