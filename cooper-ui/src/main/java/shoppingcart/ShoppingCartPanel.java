package shoppingcart;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.JDependUIUtil;
import jdepend.util.shoppingcart.ShoppingCart;

public class ShoppingCartPanel extends JPanel {

	private JLabel state;

	public ShoppingCartPanel(final JDependFrame frame) {

		this.setLayout(new BorderLayout());
		this.setBorder(javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		state = new JLabel();
		JDependUIUtil.addClickTipEffect(state);

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
		} else {
			state.setIcon(new ImageIcon(JDependUIUtil.getImage("cart/have.png")));
		}
	}

}
