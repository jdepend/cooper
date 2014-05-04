package org.wilmascope.light;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Christine
 *
 */
/**
 * This class creates a panel to show the parameters of the AmbientLight
 */
public class AmbientLightPanel extends JPanel {
	/**
	 * A label to represent the color of the ambient light
	 */
	public JLabel label = new JLabel();

	public AmbientLightPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(createColorPane());
		add(Box.createRigidArea(new Dimension(0, 10)));

	}

	private JPanel createColorPane() {
		JPanel ColorPane = new JPanel();
		label.setBackground(Color.white);
		label.setOpaque(true);

		label.setPreferredSize(new Dimension(85, 85));
		ColorPane.add(label, BorderLayout.CENTER);
		ColorPane.setBorder(BorderFactory.createTitledBorder("light color"));
		return ColorPane;
	}

	/**
	 * On the panel, there is a label representing the color of the ambient
	 * light,this method sets the label colour to white(default colour of
	 * ambient light).
	 */
	public void setDefaultValue() {
		label.setBackground(Color.white);
	}

	/**
	 * Sets the label colour to the panel's background color.
	 */
	public void clear() {
		label.setBackground(new Color(204, 204, 204));
	}
}
