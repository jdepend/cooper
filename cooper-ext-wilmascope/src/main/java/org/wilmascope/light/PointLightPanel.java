package org.wilmascope.light;

/**
 * @author dwyer
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
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
 * This class creates a panel to show the parameters of the point light
 */
public class PointLightPanel extends JPanel implements KeyListener {
	// position
	public JTextField xPos = new JTextField(10);
	public JTextField yPos = new JTextField(10);
	public JTextField zPos = new JTextField(10);
	// attenuation
	public JTextField Constant = new JTextField(10);
	public JTextField Linear = new JTextField(10);
	public JTextField Quadratic = new JTextField(10);

	private LightFrame lightFrame;

	public PointLightPanel(LightFrame l) {
		lightFrame = l;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(createPointLightPane());
		xPos.addKeyListener(this);
		yPos.addKeyListener(this);
		zPos.addKeyListener(this);

		Constant.addKeyListener(this);
		Linear.addKeyListener(this);
		Quadratic.addKeyListener(this);

		this.setSize(150, 110);
	}

	private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.insets = new Insets(4, 4, 4, 4);

	}

	private JTabbedPane createPointLightPane() {
		// postion panel
		JLabel x_axis = new JLabel("x axis:");
		JLabel y_axis = new JLabel("y axis:");
		JLabel z_axis = new JLabel("z axis:");

		JTabbedPane tabbedPane = new JTabbedPane();

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

		tabbedPane.addTab("Position", positionPane);
		tabbedPane.setSelectedIndex(0);
		tabbedPane.addTab("Attenuation", attenuationPane);

		return tabbedPane;
	}

	/**
	 * Sets the default value of point light to the textfields: position(0,0,0),
	 * attenuation(1,0,0)
	 * 
	 */
	public void setDefaultValue() {
		// postion

		xPos.setText("0");
		yPos.setText("0");
		zPos.setText("0");

		// attenuation
		Constant.setText("1");
		Linear.setText("0");
		Quadratic.setText("0");

	}

	/**
	 * Clears the textfields and make them uneditable
	 */
	public void clear() {
		// postion
		xPos.setText("");
		yPos.setText("");
		zPos.setText("");

		// attenuation
		Constant.setText("");
		Linear.setText("");
		Quadratic.setText("");

		xPos.setEditable(false);
		yPos.setEditable(false);
		zPos.setEditable(false);

		Constant.setEditable(false);
		Linear.setEditable(false);
		Quadratic.setEditable(false);

	}

	/**
	 * Makes the textfields editable
	 */
	public void EnableEdit() {
		xPos.setEditable(true);
		yPos.setEditable(true);
		zPos.setEditable(true);

		Constant.setEditable(true);
		Linear.setEditable(true);
		Quadratic.setEditable(true);
	}

	/**
	 * Reenables the OK button if any changes are made in the textfields
	 */
	public void keyTyped(KeyEvent e) {
		if (lightFrame.getselectedList().getSelectedIndex() != -1)
			lightFrame.enableOK();
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

	}

}
