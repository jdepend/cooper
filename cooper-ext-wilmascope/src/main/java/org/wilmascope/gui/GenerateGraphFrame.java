package org.wilmascope.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.graphgen.GeneratorManager;
import org.wilmascope.graphgen.GraphGenerator;
import org.wilmascope.util.UnknownTypeException;
import org.wilmascope.view.ViewManager;

/**
 * @author dwyer
 * 
 *         Controls for generating random test graphs
 */
public class GenerateGraphFrame extends JFrame implements ActionListener {
	String title;

	boolean lineRend = true;

	private JComboBox generatorsComboBox = new JComboBox(GeneratorManager.getInstance().getTypeList());;

	ButtonGroup radioGroup = new ButtonGroup();

	JRadioButton lineRadio = new JRadioButton("Line Primitives (for faster rendering)", lineRend);

	JRadioButton primitiveRadio = new JRadioButton("3D Primitives (defaults from panel)", !lineRend);

	ButtonGroup radioGroup2 = new ButtonGroup();

	ButtonGroup radioGroup3 = new ButtonGroup();

	Box boxLayout;

	JPanel controlPanel = new JPanel();

	JButton okButton = new JButton("Generate Graph");

	GraphControl gc;

	GraphGenerator generator;

	public GenerateGraphFrame(String title, GraphControl gc) {
		this.title = title;
		this.gc = gc;
		generator = GeneratorManager.getInstance().getDefault();
		ImageIcon icon = new ImageIcon(org.wilmascope.images.Images.class.getResource("WilmaW24.png"));
		this.setIconImage(icon.getImage());
		setTitle(title);
		boxLayout = Box.createVerticalBox();
		this.getContentPane().add(boxLayout, null);
		generatorsComboBox.setSelectedItem(generator.getName());
		boxLayout.add(generatorsComboBox);
		lineRadio.setActionCommand("line");
		radioGroup2.add(lineRadio);
		primitiveRadio.setActionCommand("primitive");
		radioGroup2.add(primitiveRadio);
		lineRadio.addActionListener(this);
		primitiveRadio.addActionListener(this);
		JPanel radioPanel2 = new JPanel();
		radioPanel2.setLayout(new GridLayout(0, 1));
		radioPanel2.setBorder(new TitledBorder("Graph Rendering Mode"));
		radioPanel2.add(lineRadio);
		radioPanel2.add(primitiveRadio);
		boxLayout.add(radioPanel2);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 1));
		// buttonPanel.setBorder(new TitledBorder(""));
		okButton.addActionListener(this);
		okButton.setActionCommand("OK");
		buttonPanel.add(okButton);
		boxLayout.add(buttonPanel); // i should tidy up the heirachy
		controlPanel = generator.getControls();
		boxLayout.add(controlPanel);
		generatorsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generatorsComboBox_actionPerformed(e);
			}
		});
		pack();
	}

	public void generatorsComboBox_actionPerformed(ActionEvent e) {
		try {
			generator = GeneratorManager.getInstance().getPlugin((String) generatorsComboBox.getSelectedItem());
		} catch (UnknownTypeException e1) {
			WilmaMain.showErrorDialog("Unknown generator error!", e1);
		}
		if (controlPanel != null) {
			boxLayout.remove(controlPanel);
		}
		controlPanel = generator.getControls();
		boxLayout.add(controlPanel);
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			if (lineRend) {
				generator.setView("LineNode", "LineEdge");
			} else {
				String nodeView = ViewManager.getInstance().getDefaultNodeViewType();
				String edgeView = ViewManager.getInstance().getDefaultEdgeViewType();
				generator.setView(nodeView, edgeView);
			}
			generator.generate(gc);
		} else if (e.getActionCommand().equals("line")) {
			lineRend = true;
		} else if (e.getActionCommand().equals("primitive")) {
			lineRend = false;
		}
	}
}
