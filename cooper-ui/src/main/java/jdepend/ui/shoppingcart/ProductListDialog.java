package jdepend.ui.shoppingcart;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.framework.util.BundleUtil;
import jdepend.ui.ScoreListDialog;
import jdepend.util.shoppingcart.Product;
import jdepend.util.shoppingcart.ShoppingCart;

public final class ProductListDialog extends CooperDialog {

	private CooperTable productListTable;

	private JDependFrame frame;

	public ProductListDialog(JDependFrame frame) {

		super("购物车");

		this.setSize(500, 600);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		this.frame = frame;

		try {
			this.add(BorderLayout.CENTER, this.initTable());
		} catch (JDependException e) {
			e.printStackTrace();
			frame.showStatusError(e.getMessage());
		}

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.createClearButton());
		buttonPanel.add(this.createCloseButton());
		this.add(BorderLayout.SOUTH, buttonPanel);

	}

	protected JButton createClearButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Clear));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ShoppingCart.getInstance().clear();
					refresh();
				} catch (JDependException ex) {
					JOptionPane.showMessageDialog(ProductListDialog.this, ex.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return button;
	}

	protected JScrollPane initTable() throws JDependException {
		productListTable = new CooperTable(this.calTableData());
		return new JScrollPane(productListTable);
	}

	protected void refresh() throws JDependException {
		productListTable.refresh(this.calTableData());
		frame.getStatusField().refresh();
	}

	private TableData calTableData() throws JDependException {

		TableData tableData = new TableData();
		for (Product product : ShoppingCart.getInstance().getProducts()) {
			tableData.setData(BundleUtil.getString(BundleUtil.TableHead_ExecuteDate), product.getCreateDate());
			tableData.setData(BundleUtil.getString(BundleUtil.TableHead_GroupName), product.getResult()
					.getRunningContext().getGroup());
			tableData.setData(BundleUtil.getString(BundleUtil.TableHead_CommandName), product.getResult()
					.getRunningContext().getCommand());
		}
		return tableData;
	}

}
