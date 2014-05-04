/**
 * @author Christine
 *
 */
package org.wilmascope.light;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class creates a panel to show the parameters of the directional light
 */
public class DirectionalLightPanel extends JPanel implements KeyListener {
	// direction
	public JTextField xDir = new JTextField(10);
	public JTextField yDir = new JTextField(10);
	public JTextField zDir = new JTextField(10);

	private LightFrame lightFrame;

	public DirectionalLightPanel(LightFrame l) {
		lightFrame = l;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(createDirLightPane());
		xDir.addKeyListener(this);
		yDir.addKeyListener(this);
		zDir.addKeyListener(this);

	}

	private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy) {
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;
	}

	private JPanel createDirLightPane() {
		JLabel xDirLabel = new JLabel("x direction:");
		JLabel yDirLabel = new JLabel("y direction:");
		JLabel zDirLabel = new JLabel("z direction:");

		JPanel lightPane = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		lightPane.setLayout(gridbag);
		GridBagConstraints constraints = new GridBagConstraints();

		buildConstraints(constraints, 0, 0, 1, 1, 30, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(xDirLabel, constraints);
		lightPane.add(xDirLabel);

		buildConstraints(constraints, 1, 0, 1, 1, 70, 33);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(xDir, constraints);
		lightPane.add(xDir);

		buildConstraints(constraints, 0, 1, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(yDirLabel, constraints);
		lightPane.add(yDirLabel);

		buildConstraints(constraints, 1, 1, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(yDir, constraints);
		lightPane.add(yDir);

		buildConstraints(constraints, 0, 2, 1, 1, 0, 33);
		constraints.anchor = GridBagConstraints.EAST;
		gridbag.setConstraints(zDirLabel, constraints);
		lightPane.add(zDirLabel);

		buildConstraints(constraints, 1, 2, 1, 1, 0, 0);
		constraints.anchor = GridBagConstraints.WEST;
		gridbag.setConstraints(zDir, constraints);
		lightPane.add(zDir);
		lightPane.setBorder(BorderFactory.createTitledBorder("Light Direction"));
		return lightPane;
	}

	/**
	 * Sets the default direction of the directional light (0,0,-1) to the
	 * textfields
	 */
	public void setDefaultValue() {

		// direction
		xDir.setText("0");
		yDir.setText("0");
		zDir.setText("-1");
	}

	/**
	 * Clears the textfields and make them uneditable
	 */
	public void clear() {
		xDir.setText("");
		yDir.setText("");
		zDir.setText("");
		xDir.setEditable(false);
		yDir.setEditable(false);
		zDir.setEditable(false);
	}

	/**
	 * Makes all the textfields editable
	 */
	public void EnableEdit() {

		xDir.setEditable(true);
		yDir.setEditable(true);
		zDir.setEditable(true);
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
