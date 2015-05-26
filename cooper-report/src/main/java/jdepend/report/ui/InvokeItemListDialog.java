package jdepend.report.ui;

import java.awt.BorderLayout;
import java.util.Collection;

import jdepend.framework.ui.CooperDialog;
import jdepend.metadata.InvokeItem;

public class InvokeItemListDialog extends CooperDialog {

	public InvokeItemListDialog(Collection<InvokeItem> invokedItems) {
		super();
		getContentPane().setLayout(new BorderLayout());

		InvokeItemListPanel invokeItemListPanel = new InvokeItemListPanel(invokedItems);
		invokeItemListPanel.loadInvokeItemList();
		this.add(invokeItemListPanel);
	}
}
