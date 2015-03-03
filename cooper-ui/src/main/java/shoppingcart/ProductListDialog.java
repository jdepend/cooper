package shoppingcart;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.framework.util.BundleUtil;
import jdepend.util.shoppingcart.Product;
import jdepend.util.shoppingcart.ShoppingCart;

public final class ProductListDialog extends CooperDialog {

	public ProductListDialog(final JDependFrame frame) {

		super("结果列表");

		this.setSize(500, 600);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		try {
			this.add(BorderLayout.CENTER, this.initTable());
		} catch (JDependException e) {
			e.printStackTrace();
			frame.showStatusError(e.getMessage());
		}

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.createCloseButton());
		this.add(BorderLayout.SOUTH, buttonPanel);

	}

	protected JScrollPane initTable() throws JDependException {
		CooperTable scoreListTable = new CooperTable(this.calTableData());
		return new JScrollPane(scoreListTable);
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
