package jdepend.ui.shoppingcart;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.TableData;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.ui.JDependCooper;
import jdepend.util.refactor.AdjustHistory;
import jdepend.util.shoppingcart.Product;
import jdepend.util.shoppingcart.ShoppingCart;

public final class ProductListDialog extends CooperDialog {

	private CooperTable productListTable;

	private JDependCooper frame;

	private List<Date> selectedIDs;

	private Date currentId;

	public ProductListDialog(JDependCooper frame) {

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

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem viewItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_View));
		viewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view();
			}
		});
		popupMenu.add(viewItem);

		JMenuItem compareItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Compare));
		compareItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedIDs == null || selectedIDs.size() != 2) {
					JOptionPane.showMessageDialog(ProductListDialog.this, "请选择2条需要比较的记录", "alert",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					compare(selectedIDs.get(0), selectedIDs.get(1));
				}
			}
		});
		popupMenu.add(compareItem);

		productListTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				selectedIDs = new ArrayList<Date>();
				for (int row : table.getSelectedRows()) {
					selectedIDs.add((Date) table.getValueAt(row, 0));
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				int currentRow = table.rowAtPoint(e.getPoint());
				currentId = (Date) table.getValueAt(currentRow, 0);
				if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
				if (e.getClickCount() == 2)
					view();
			}
		});

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

	private void view() {
		AnalysisResult result = ShoppingCart.getInstance().getTheProduct(currentId).getResult();
		JDependUnitMgr.getInstance().setResult(result);
		frame.getResultPanelWrapper().showResults();
		// 刷新TODOList
		new Thread() {
			@Override
			public void run() {
				try {
					frame.getPropertyPanel().getToDoListPanel().refresh();
				} catch (JDependException e) {
					e.printStackTrace();
					frame.getResultPanel().showError(e);
				}
			}
		}.start();
	}

	private void compare(Date id1, Date id2) {
		AdjustHistory.getInstance().clear();
		AnalysisResult result1 = ShoppingCart.getInstance().getTheProduct(id1).getResult();
		JDependUnitMgr.getInstance().setResult(result1);
		AdjustHistory.getInstance().addMemento();
		AnalysisResult result2 = ShoppingCart.getInstance().getTheProduct(id2).getResult();
		JDependUnitMgr.getInstance().setResult(result2);
		frame.getResultPanelWrapper().showResults();
	}
}
