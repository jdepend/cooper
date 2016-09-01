package jdepend.client.ui;

import java.awt.Dimension;

import jdepend.client.ui.shoppingcart.ShoppingCartPanel;
import jdepend.framework.ui.panel.StatusField;

public class ClientStatusField extends StatusField {

	private ShoppingCartPanel resultContainerPanel;

	public ClientStatusField(JDependCooper frame) {
		super(frame);

		this.getStatusLeft().setPreferredSize(new Dimension(this.getStatusLeft().getPreferredSize().width - 32, 20));

		resultContainerPanel = new ShoppingCartPanel(frame);
		this.add(resultContainerPanel);
	}

	@Override
	public void refresh() {
		this.resultContainerPanel.refreshState();
	}
}
