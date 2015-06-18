package jdepend.client.ui.shoppingcart;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.ui.graph.CooperTable;
import jdepend.framework.ui.graph.model.TableData;
import jdepend.framework.util.BundleUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.client.ui.JDependCooper;
import jdepend.client.ui.shoppingcart.model.Product;
import jdepend.client.ui.shoppingcart.model.ShoppingCart;
import jdepend.util.refactor.AdjustHistory;

public final class ProductListDialog extends CooperDialog {

	private CooperTable productListTable;

	private JDependCooper frame;

	public ProductListDialog(JDependCooper frame) throws JDependException {

		super("购物车");

		this.frame = frame;

		this.add(BorderLayout.CENTER, this.initTable());

		this.refresh();

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.createClearButton());
		buttonPanel.add(this.createCloseButton());
		this.add(BorderLayout.SOUTH, buttonPanel);

	}

	protected JButton createClearButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Clear));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ShoppingCart.getInstance().clear();
				try {
					refresh();
				} catch (JDependException e1) {
					frame.getResultPanel().showError(e1);
				}
			}
		});
		return button;
	}

	protected JScrollPane initTable() throws JDependException {

		productListTable = new CooperTable(this.calTableData());

		JScrollPane pane = new JScrollPane(productListTable);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem viewItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_View));
		viewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view((Date) productListTable.getCurrentes().get(0));
				dispose();
			}
		});
		popupMenu.add(viewItem);

		JMenuItem compareItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Compare));
		compareItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (productListTable.getCurrentes() == null || productListTable.getCurrentes().size() != 2) {
					JOptionPane.showMessageDialog(ProductListDialog.this, "请选择2条需要比较的记录", "alert",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					compare((Date) productListTable.getCurrentes().get(0), (Date) productListTable.getCurrentes()
							.get(1));
					dispose();
				}
			}
		});
		popupMenu.add(compareItem);

		productListTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
				if (e.getClickCount() == 2)
					view((Date) productListTable.getCurrentes().get(0));
			}
		});

		return pane;
	}

	private void refresh() throws JDependException {
		productListTable.refresh(this.calTableData());
		frame.getStatusField().refresh();
	}

	private TableData calTableData() throws JDependException {

		TableData tableData = new TableData();
		for (Product product : ShoppingCart.getInstance().getProducts()) {

			AnalysisResult result = product.getResult();
			tableData.setData(BundleUtil.getString(BundleUtil.TableHead_ExecuteDate), product.getCreateDate());
			tableData.setData(BundleUtil.getString(BundleUtil.TableHead_GroupName), result.getRunningContext()
					.getGroup());
			tableData.setData(BundleUtil.getString(BundleUtil.TableHead_CommandName), result.getRunningContext()
					.getCommand());
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_LC), result.getSummary().getLineCount());
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_ComponentCount), result.getSummary()
					.getComponentCount());
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_RelationCount), result.getSummary()
					.getRelationCount());
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_Cohesion),
					MetricsFormat.toFormattedMetrics(result.getSummary().getCohesion()));
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_Coupling),
					MetricsFormat.toFormattedMetrics(result.getSummary().getCoupling()));
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_D),
					MetricsFormat.toFormattedMetrics(result.getDistance()));
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_Balance),
					MetricsFormat.toFormattedMetrics(result.getBalance()));
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_Encapsulation),
					MetricsFormat.toFormattedMetrics(result.getEncapsulation()));
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_RelationRationality),
					MetricsFormat.toFormattedMetrics(result.getRelationRationality()));
			tableData.setData(BundleUtil.getString(BundleUtil.Metrics_TotalScore),
					MetricsFormat.toFormattedMetrics(result.getScore()));
		}
		tableData.setSortColName(BundleUtil.getString(BundleUtil.Metrics_TotalScore));
		tableData.setSortOperation(TableData.DESC);

		return tableData;

	}

	private void view(Date id) {
		AnalysisResult result = ShoppingCart.getInstance().getTheProduct(id).getResult();
		JDependUnitMgr.getInstance().setResult(result);
		frame.getResultPanelWrapper().showResults(true);
	}

	private void compare(Date id1, Date id2) {
		AdjustHistory.getInstance().clear();
		AnalysisResult result1 = ShoppingCart.getInstance().getTheProduct(id1).getResult();
		JDependUnitMgr.getInstance().setResult(result1);
		AdjustHistory.getInstance().addMemento();
		AnalysisResult result2 = ShoppingCart.getInstance().getTheProduct(id2).getResult();
		JDependUnitMgr.getInstance().setResult(result2);
		frame.getResultPanelWrapper().showResults(false);
	}
}
