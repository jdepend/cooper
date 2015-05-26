package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableMouseMotionAdapter;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.InvokeItem;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.report.util.ReportConstant;

public class JavaClassCaCeDetailDialog extends CooperDialog {

	private JavaClassUnit javaClass;

	private Collection<JavaClassUnit> javaClasses;

	private String metrics;

	private JLabel couplingLabel;

	private JLabel outerCouplingLabel;

	private JLabel inCouplingLabel;

	private JTable listTable;

	private DefaultTableModel listModel;

	private boolean includeInner;

	public JavaClassCaCeDetailDialog(JavaClassUnit javaClass, String metrics, boolean includeInner) {

		super(javaClass.getName() + " " + metrics + " list" + (includeInner ? "（全部）" : "（组件外）"));

		this.javaClass = javaClass;
		this.metrics = metrics;
		this.includeInner = includeInner;

		init();

	}

	public JavaClassCaCeDetailDialog(Collection<JavaClassUnit> javaClasses, String metrics) {
		this.javaClasses = javaClasses;
		this.metrics = metrics;

		init();
	}

	private void init() {
		getContentPane().setLayout(new BorderLayout());

		this.add(BorderLayout.NORTH, this.createTitlePanel());

		initList();
		showList();
		this.add(BorderLayout.CENTER, new JScrollPane(listTable));
	}

	private JPanel createTitlePanel() {
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		titlePanel.add(new JLabel("耦合值："));
		this.couplingLabel = new JLabel();
		titlePanel.add(this.couplingLabel);

		titlePanel.add(new JLabel("外部耦合值："));
		this.outerCouplingLabel = new JLabel();
		titlePanel.add(this.outerCouplingLabel);

		titlePanel.add(new JLabel("内部耦合值："));
		this.inCouplingLabel = new JLabel();
		titlePanel.add(this.inCouplingLabel);

		return titlePanel;
	}

	private void showList() {
		listModel.setRowCount(0);
		this.loadList();
	}

	private void loadList() {

		Object[] row;

		Collection<JavaClassRelationItem> items = new ArrayList<JavaClassRelationItem>();

		if (metrics.equals(ReportConstant.Ca)) {
			if (javaClass != null) {
				items = javaClass.getJavaClass().getCaItems();
			} else {
				for (JavaClassUnit javaClass : javaClasses) {
					for (JavaClassRelationItem item : javaClass.getJavaClass().getCaItems()) {
						if (!javaClasses.contains(javaClass.getResult().getTheClass(item.getSource().getId()))) {
							items.add(item);
						}
					}
				}
			}
		} else if (metrics.equals(ReportConstant.Ce)) {
			if (javaClass != null) {
				items = javaClass.getJavaClass().getCeItems();
			} else {
				for (JavaClassUnit javaClass : javaClasses) {
					for (JavaClassRelationItem item : javaClass.getJavaClass().getCeItems()) {
						if (!javaClasses.contains(javaClass.getResult().getTheClass(item.getTarget().getId()))) {
							items.add(item);
						}
					}
				}
			}
		}

		AnalysisResult result = JDependUnitMgr.getInstance().getResult();

		boolean isInner;
		String metrics1 = null;
		float coupling = 0F;
		float inCoupling = 0F;
		float outerCoupling = 0F;
		for (JavaClassRelationItem item : items) {
			isInner = !JavaClassUnitUtil.crossComponent(item, result);
			// 判断是否是环境外的
			if ((javaClass != null && (includeInner || !isInner)) || (javaClasses != null)) {
				row = new Object[listTable.getColumnCount()];
				for (int i = 0; i < listTable.getColumnCount(); i++) {
					if (i == 0) {
						row[0] = item.getId();
					} else if (i == 3) {
						row[3] = item.getType().getName();
					} else if (i == 4) {
						if (isInner) {
							inCoupling += item.getRelationIntensity();
							row[4] = "否";
						} else {
							outerCoupling += item.getRelationIntensity();
							row[4] = "是";
						}
					} else {
						metrics1 = ReportConstant.toMetrics(listTable.getColumnName(i));
						if (metrics.equals(ReportConstant.Ca)) {
							row[i] = result.getTheClass(item.getSource().getId()).getValue(metrics1);
						} else {
							row[i] = result.getTheClass(item.getTarget().getId()).getValue(metrics1);
						}
					}
				}
				listModel.addRow(row);

				coupling += item.getRelationIntensity();
			}
		}

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(ReportConstant.Name);
		fitColNames.add(ReportConstant.DependType);
		JTableUtil.fitTableColumns(listTable, fitColNames);

		this.couplingLabel.setText(MetricsFormat.toFormattedMetrics(coupling).toString());
		this.inCouplingLabel.setText(MetricsFormat.toFormattedMetrics(inCoupling).toString());
		this.outerCouplingLabel.setText(MetricsFormat.toFormattedMetrics(outerCoupling).toString());

	}

	private void initList() {

		listModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(listModel);

		listTable = new JTable(sorter);

		sorter.setTableHeader(listTable.getTableHeader());

		listTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				final JTable table = (JTable) e.getSource();
				Point p = new Point(e.getX(), e.getY());
				int col = table.columnAtPoint(p);
				int row = table.rowAtPoint(p);

				String currentCol = (String) table.getColumnModel().getColumn(col).getHeaderValue();
				if (currentCol.equals(ReportConstant.DependType)) {
					String id = (String) table.getValueAt(row, 0);
					AnalysisResult result = JDependUnitMgr.getInstance().getResult();
					JavaClassRelationItem item = result.getTheJavaClassRelationItem(id);
					Collection<InvokeItem> invokedItems = item.getInvokeDetail();
					if (invokedItems.size() > 0) {
						InvokeItemListDialog d = new InvokeItemListDialog(invokedItems);
						d.setModal(true);
						d.setVisible(true);
					}
				}
			}
		});

		listModel.addColumn("id");
		listModel.addColumn(ReportConstant.JavaClass_Place);
		listModel.addColumn(ReportConstant.Name);
		listModel.addColumn(ReportConstant.DependType);
		listModel.addColumn(ReportConstant.JavaClass_isExt);
		listModel.addColumn(ReportConstant.LC);
		listModel.addColumn(ReportConstant.AC);
		listModel.addColumn(ReportConstant.Ca);
		listModel.addColumn(ReportConstant.Ce);
		listModel.addColumn(ReportConstant.Coupling);
		listModel.addColumn(ReportConstant.Cohesion);
		listModel.addColumn(ReportConstant.Cycle);
		listModel.addColumn(ReportConstant.JavaClass_State);
		listModel.addColumn(ReportConstant.JavaClass_Stable);
		listModel.addColumn(ReportConstant.JavaClass_isPrivateElement);

		listTable.getColumnModel().getColumn(0).setMinWidth(0);
		listTable.getColumnModel().getColumn(0).setMaxWidth(0);
		listTable.getColumnModel().getColumn(1).setMinWidth(0);
		listTable.getColumnModel().getColumn(1).setMaxWidth(0);

		// 增加点击图标
		List<String> colNames = new ArrayList<String>();
		colNames.add(ReportConstant.DependType);

		listTable.addMouseMotionListener(new TableMouseMotionAdapter(listTable, colNames));
	}
}
