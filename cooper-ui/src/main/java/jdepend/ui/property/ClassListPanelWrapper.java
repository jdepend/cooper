package jdepend.ui.property;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import jdepend.core.command.CommandAdapterMgr;
import jdepend.core.config.CommandConfMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.Measurable;
import jdepend.model.result.AnalysisResult;
import jdepend.report.ui.ClassListPanel;
import jdepend.report.ui.MethodListDialog;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.CompareTableCellRenderer;
import jdepend.util.refactor.CompareObject;

public class ClassListPanelWrapper extends ClassListPanel {

	private JDependCooper frame;

	public ClassListPanelWrapper(JDependCooper frame) {
		super(frame);

		this.frame = frame;
	}

	protected void initClassList() {

		super.initClassList();

		JavaClassCompareTableCellRenderer renderer = new JavaClassCompareTableCellRenderer();
		for (int i = 0; i < classListTable.getColumnCount(); i++) {
			classListTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem viewSrcItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewSourceCode));
		viewSrcItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewSrc();
			}
		});
		popupMenu.add(viewSrcItem);

		JMenuItem viewDetailItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewDetail));
		viewDetailItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewDetail();
			}
		});
		popupMenu.add(viewDetailItem);

		JMenuItem viewMethodListItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewMethodList));
		viewMethodListItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewMethodList();
			}
		});
		popupMenu.add(viewMethodListItem);

		popupMenu.add(this.createMoveToItem());

		popupMenu.addSeparator();

		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(classListTable);
			}
		});
		popupMenu.add(saveAsItem);

		classListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

	}

	private void viewSrc() {
		try {
			StringBuilder src = CommandConfMgr.getInstance().getTheGroup(CommandAdapterMgr.getCurrentGroup())
					.getSrcContent(current);
			frame.getResultPanel().addResult(current, src);
			frame.getResultPanel().setLastedTab();
		} catch (JDependException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog((Component) this, "读取Class源文件[" + current + "]失败！", "alert",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private void viewDetail() {
		StringBuilder detail = new StringBuilder(JDependUnitMgr.getInstance().getResult().getTheClass(current)
				.toString());
		frame.getResultPanel().addResult(current, detail);
		frame.getResultPanel().setLastedTab();
	}

	private void viewMethodList() {
		JavaClass javaClass = JDependUnitMgr.getInstance().getResult().getTheClass(current);
		MethodListDialog d = new MethodListDialog(javaClass);
		d.setModal(true);
		d.setVisible(true);
	}

	class JavaClassCompareTableCellRenderer extends CompareTableCellRenderer {

		public JavaClassCompareTableCellRenderer() {
			super();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, final int row, final int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (this.getComponentCount() > 0) {
				Component component = this.getComponent(0);
				if (extendUnits.contains(table.getValueAt(row, 0))) {
					component.setForeground(Color.GRAY);
				}
			}
			return this;
		}

		@Override
		protected CompareObject getCompareObject(Object value, String id, String metrics) {
			return new CompareObject(value, id, metrics) {
				@Override
				public Object getOriginalityValue(AnalysisResult result) {
					Measurable measurable = result.getTheClass(this.getId());
					if (measurable != null) {
						return measurable.getValue(this.getMetrics());
					} else {
						return null;
					}
				}

				@Override
				public Boolean evaluate(int result, String metrics) {
					if (metrics.equals(JavaClass.Stable)) {
						if (result < 0) {
							return true;
						} else {
							return false;
						}
					} else if (metrics.equals(JavaClass.isPrivateElement)) {
						if (result < 0) {
							return false;
						} else {
							return true;
						}
					} else {
						return null;
					}
				}

			};
		}

	}
}
