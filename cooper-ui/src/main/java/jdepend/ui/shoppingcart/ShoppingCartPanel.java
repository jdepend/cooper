package jdepend.ui.shoppingcart;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import jdepend.framework.ui.util.JDependUIUtil;
import jdepend.ui.JDependCooper;
import jdepend.ui.shoppingcart.model.ShoppingCart;

public class ShoppingCartPanel extends JPanel {

	private JLabel state;

	public ShoppingCartPanel(final JDependCooper frame) {

		this.setLayout(new BorderLayout());
		this.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		state = new JLabel();
		state.setFocusable(false);
		state.setHorizontalAlignment(JTextField.LEFT);

		state.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ProductListDialog d = new ProductListDialog(frame);
				d.setModal(true);
				d.setVisible(true);
			}
		});

		this.add(state);

		this.refreshState();
	}

	public void refreshState() {
		if (ShoppingCart.getInstance().isEmpty()) {
			state.setIcon(new ImageIcon(JDependUIUtil.getImage("cart/default.png")));
			state.setText("  ");
		} else {
			state.setIcon(new ImageIcon(JDependUIUtil.getImage("cart/have.png")));
			state.setText(String.valueOf(ShoppingCart.getInstance().getProducts().size()));
		}
	}

}
