package jdepend.framework.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class StatusField extends JPanel {

	private JTextField statusLeft;
	private JTextField statusCenter;
	private JTextField statusRight;

	private JDependFrame frame;

	public static final String Left = "Left";
	public static final String Center = "Center";
	public static final String Right = "Right";

	public StatusField(JDependFrame frame) {
		super();

		this.frame = frame;
		// Keinen Layout-Manager verwenden
		this.setLayout(new BorderLayout());
		// Aussehen des Randes des Panels setzen
		this.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		int width = this.frame.getToolkit().getScreenSize().width;

		// Linkes Textfeld zeigt Mul, Div ... an
		// Objekt des Textfeldes erzeugen
		statusLeft = new JTextField();
		// Es ist nicht editierbar
		statusLeft.setEditable(false);
		// und nicht fokussierbar
		statusLeft.setFocusable(false);
		// Textausrichtung ist links
		statusLeft.setHorizontalAlignment(JTextField.LEFT);
		// Aussehen wie Panel
		statusLeft.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		// Textfeld mit dem Panel verbinden
		this.add(BorderLayout.WEST, statusLeft);
		statusLeft.setPreferredSize(new Dimension(((int) (width * 0.8F)), 20));

		// Mittleres Textfeld ist die Anzeige, wenn ein Wert gespeichert wird
		statusCenter = new JTextField();
		statusCenter.setEditable(false);
		statusCenter.setFocusable(false);
		statusCenter.setHorizontalAlignment(JTextField.CENTER);
		statusCenter.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.add(BorderLayout.CENTER, statusCenter);
		statusCenter.setPreferredSize(new Dimension(((int) (width * 0.1F)), 20));

		// Rechtes Textfeld gibt Meldungen aus
		statusRight = new JTextField();
		statusRight.setEditable(false);
		statusRight.setFocusable(false);
		statusRight.setHorizontalAlignment(JTextField.CENTER);
		statusRight.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.add(BorderLayout.EAST, statusRight);
		statusRight.setPreferredSize(new Dimension(((int) (width * 0.1F)), 20));
	}

	public StatusField() {
		super();
	}

	public void setText(String text, String pos) {
		if (Left.equals(pos)) {
			this.statusLeft.setText(text);
		} else if (Center.equals(pos)) {
			this.statusCenter.setText(text);
		} else if (Right.equals(pos)) {
			this.statusRight.setText(text);
		}
	}

	public void setText(String text) {
		this.statusLeft.setText(text);
	}

	public String getText() {
		return this.statusLeft.getText();
	}

	public JTextField getStatusLeft() {
		return statusLeft;
	}

	public JTextField getStatusCenter() {
		return statusCenter;
	}

	public JTextField getStatusRight() {
		return statusRight;
	}
}
