package org.wilmascope.light;

/**
 * @author Christine
 *
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * This class creates a panel to show the parameters of the spot light
 */
public class SpotLightPanel extends JPanel implements KeyListener {
	// position
	public JTextField xPos = new JTextField(10);
	public JTextField yPos = new JTextField(10);
	public JTextField zPos = new JTextField(10);
	// direction
	public JTextField xDir = new JTextField(10);
	public JTextField yDir = new JTextField(10);
	public JTextField zDir = new JTextField(10);
	// other
	public JTextField SpreadAngle = new JTextField(9);
	public JTextField Concentration = new JTextField(9);
	// attenuation
	public JTextField Constant = new JTextField(10);
	public JTextField Linear = new JTextField(10);
	public JTextField Quadratic = new JTextField(10);

	public boolean isChanged = false;
	public LightFrame lightFrame;

	public SpotLightPanel(LightFrame l) {
		lightFrame = l;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(createSpotLightPane());

		xPos.addKeyListener(this);
		yPos.addKeyListener(this);
		zPos.addKeyListener(this);

		Constant.addKeyListener(this);
		Linear.addKeyListener(this);
		Quadratic.addKeyListener(this);

		xDir.addKeyListener(this);
		yDir.addKeyListener(this);
		zDir.addKeyListener(this);

		SpreadAngle.addKeyListener(this);
		Concentration.addKeyListener(this);

	}

	private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.insets = new Insets(2, 2, 2, 2);

	}

	private JTabbedPane createSpotLightPane() {
		// postion panel
		JLabel x_axis = new JLabel("x coordinate:");
		JLabel y_axis = new JLabel("y coordinate:");
		JLabel z_axis = new JLabel("z coordinate:");
		// position

		JPanel positionPane = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		positionPane.setLayout(gridbag);
		GridBagConstraints constraints = new GridBagConstraints();

		buildConstraints(constraints, 0, 0, 1, 1, 30, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(x_axis, constraints);
		positionPane.add(x_axis);

		buildConstraints(constraints, 1, 0, 1, 1, 70, 33);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(xPos, constraints);
		positionPane.add(xPos);

		buildConstraints(constraints, 0, 1, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(y_axis, constraints);
		positionPane.add(y_axis);

		buildConstraints(constraints, 1, 1, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(yPos, constraints);
		positionPane.add(yPos);

		buildConstraints(constraints, 0, 2, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(z_axis, constraints);
		positionPane.add(z_axis);

		buildConstraints(constraints, 1, 2, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(zPos, constraints);
		positionPane.add(zPos);

		// attenuation panel

		JLabel cons = new JLabel("Constant:");
		JLabel lin = new JLabel("Linear:");
		JLabel qua = new JLabel("Quadratic:");

		JPanel attenuationPane = new JPanel();
		attenuationPane.setLayout(gridbag);
		buildConstraints(constraints, 0, 0, 1, 1, 30, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(cons, constraints);
		attenuationPane.add(cons);

		buildConstraints(constraints, 1, 0, 1, 1, 70, 33);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(Constant, constraints);
		attenuationPane.add(Constant);

		buildConstraints(constraints, 0, 1, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(lin, constraints);
		attenuationPane.add(lin);

		buildConstraints(constraints, 1, 1, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(Linear, constraints);
		attenuationPane.add(Linear);

		buildConstraints(constraints, 0, 2, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(qua, constraints);
		attenuationPane.add(qua);

		buildConstraints(constraints, 1, 2, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(Quadratic, constraints);
		attenuationPane.add(Quadratic);

		// direction panel

		JLabel xDirLabel = new JLabel("x direction:");
		JLabel yDirLabel = new JLabel("y direction:");
		JLabel zDirLabel = new JLabel("z direction:");

		JPanel directionPane = new JPanel();
		directionPane.setLayout(gridbag);

		buildConstraints(constraints, 0, 0, 1, 1, 30, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(xDirLabel, constraints);
		directionPane.add(xDirLabel);

		buildConstraints(constraints, 1, 0, 1, 1, 70, 33);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(xDir, constraints);
		directionPane.add(xDir);

		buildConstraints(constraints, 0, 1, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(yDirLabel, constraints);
		directionPane.add(yDirLabel);

		buildConstraints(constraints, 1, 1, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(yDir, constraints);
		directionPane.add(yDir);

		buildConstraints(constraints, 0, 2, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(zDirLabel, constraints);
		directionPane.add(zDirLabel);

		buildConstraints(constraints, 1, 2, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(zDir, constraints);
		directionPane.add(zDir);

		// others
		JPanel otherPane = new JPanel();
		JLabel SpreadAngleLabel = new JLabel("Spread Angle:");
		JLabel ConcentrationLabel = new JLabel("Concentration:");

		otherPane.setLayout(gridbag);
		buildConstraints(constraints, 0, 0, 1, 1, 30, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(SpreadAngleLabel, constraints);
		otherPane.add(SpreadAngleLabel);

		buildConstraints(constraints, 1, 0, 1, 1, 70, 33);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(SpreadAngle, constraints);
		otherPane.add(SpreadAngle);

		buildConstraints(constraints, 0, 1, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(ConcentrationLabel, constraints);
		otherPane.add(ConcentrationLabel);

		buildConstraints(constraints, 1, 1, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(Concentration, constraints);
		otherPane.add(Concentration);

		// create tabbed pane
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Position", positionPane);
		// tabbedPane.setSelectedIndex(0);
		tabbedPane.addTab("Direction", directionPane);
		tabbedPane.addTab("Attenuation", attenuationPane);
		tabbedPane.addTab("Others", otherPane);

		return tabbedPane;
	}

	/**
	 * Sets the default spot light parameters' value into the textfields
	 * Position(0,0,0),Direction(0,0,-1),SpreadAngle=Math.PI/6,
	 * Concentration=0.0 Attenuation(1,0,0)
	 */
	public void setDefaultValue() {
		// position
		xPos.setText("0");
		yPos.setText("0");
		zPos.setText("0");
		// direction
		xDir.setText("0");
		yDir.setText("0");
		zDir.setText("-1");
		// other
		SpreadAngle.setText("" + (float) Math.PI / 6);
		Concentration.setText("0.0");
		// attenuation
		Constant.setText("1");
		Linear.setText("0");
		Quadratic.setText("0");

	}

	/**
	 * Clears the textfields and makes them uneditable
	 */
	public void clear() {
		// position
		xPos.setText("");
		yPos.setText("");
		zPos.setText("");
		// direction
		xDir.setText("");
		yDir.setText("");
		zDir.setText("");
		// other
		SpreadAngle.setText("");
		Concentration.setText("");
		// attenuation
		Constant.setText("");
		Linear.setText("");
		Quadratic.setText("");

		xPos.setEditable(false);
		yPos.setEditable(false);
		zPos.setEditable(false);

		xDir.setEditable(false);
		yDir.setEditable(false);
		zDir.setEditable(false);

		SpreadAngle.setEditable(false);
		Concentration.setEditable(false);

		Constant.setEditable(false);
		Linear.setEditable(false);
		Quadratic.setEditable(false);
	}

	/**
	 * Makes the textsfields editable
	 */
	public void EnableEdit() {
		xPos.setEditable(true);
		yPos.setEditable(true);
		zPos.setEditable(true);

		xDir.setEditable(true);
		yDir.setEditable(true);
		zDir.setEditable(true);

		SpreadAngle.setEditable(true);
		Concentration.setEditable(true);

		Constant.setEditable(true);
		Linear.setEditable(true);
		Quadratic.setEditable(true);
	}

	/**
	 * Reenable the OK button if any changes are made in the textfields
	 */
	public void keyTyped(KeyEvent e) {
		if (lightFrame.getselectedList().getSelectedIndex() != -1)
			lightFrame.enableOK();
	}

	public void keyPressed(KeyEvent e) {
		if (lightFrame.getselectedList().getSelectedIndex() != -1)
			lightFrame.enableOK();
	}

	public void keyReleased(KeyEvent e) {

	}
}
