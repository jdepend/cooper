package jdepend.ui.property;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.core.command.CommandAdapterMgr;
import jdepend.core.config.CommandConfMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.component.JavaClassComponent;
import jdepend.report.ui.ClassListPanel;
import jdepend.report.ui.MethodListDialog;
import jdepend.ui.JDependCooper;
import jdepend.util.refactor.RefactorToolFactory;

public class ClassListPanelWrapper extends ClassListPanel {

	private JDependCooper frame;

	public ClassListPanelWrapper(JDependCooper frame) {
		super();

		this.frame = frame;

	}

	protected void initClassList() {

		super.initClassList();

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

		JMenuItem moveToItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Move));
		moveToItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedJavaClass.size() > 0) {
					if (!frame.getPropertyPanel().getMementoPanel().isCurrent()) {
						JOptionPane.showMessageDialog(frame, "请到移动历史列表中切换到当前结果再进行移动.", "alert",
								JOptionPane.WARNING_MESSAGE);
					} else if (JDependUnitMgr.getInstance().getComponents().iterator().next() instanceof JavaClassComponent) {
						JOptionPane.showMessageDialog(frame, "当前的分析单元不能进行移动操作.", "alert", JOptionPane.WARNING_MESSAGE);
					} else {
						moveTo();
					}
				} else {
					JOptionPane.showMessageDialog(frame, "请选择至少一个JavaClass.", "alert", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		popupMenu.add(moveToItem);

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
		StringBuilder detail = new StringBuilder(JDependUnitMgr.getInstance().getTheClass(current).toString());
		frame.getResultPanel().addResult(current, detail);
		frame.getResultPanel().setLastedTab();
	}

	private void viewMethodList() {
		JavaClass javaClass = JDependUnitMgr.getInstance().getTheClass(current);
		MethodListDialog d = new MethodListDialog(javaClass);
		d.setModal(true);
		d.setVisible(true);
	}

	private void moveTo() {
		JavaClassMoveToDialog d = new JavaClassMoveToDialog(frame, selectedJavaClass);
		d.setModal(true);
		d.setVisible(true);
	}

	class JavaClassMoveToDialog extends JDialog {

		private JDependCooper frame;

		private JTable componentTable;

		private List<String> selectedJavaClass;

		public JavaClassMoveToDialog(JDependCooper frame, List<String> selectedJavaClass) {

			this.frame = frame;

			this.selectedJavaClass = selectedJavaClass;

			getContentPane().setLayout(new BorderLayout());
			setSize(650, 380);
			this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

			addWindowListener(new WindowAdapter() {

				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});

			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());

			JPanel content = new JPanel(new BorderLayout());

			content.add(BorderLayout.CENTER, createJavaPackageTable());

			JPanel buttonBar = new JPanel(new FlowLayout());
			buttonBar.add(createOKButton());
			buttonBar.add(createCloseButton());

			panel.add(BorderLayout.CENTER, content);

			panel.add(BorderLayout.SOUTH, buttonBar);

			this.add(panel);

		}

		private JComponent createJavaPackageTable() {

			DefaultTableModel model = new DefaultTableModel() {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

			};

			TableSorter sorter = new TableSorter(model);
			componentTable = new JTable(sorter);

			model.addColumn("组件名称");

			Object[] row;
			for (jdepend.model.Component unit : JDependUnitMgr.getInstance().getComponents()) {
				row = new Object[1];
				row[0] = unit.getName();
				model.addRow(row);
			}

			return new JScrollPane(componentTable);
		}

		private JButton createCloseButton() {

			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			return button;
		}

		private JButton createOKButton() {

			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					moveTo(e);
				}
			});

			return button;
		}

		private void moveTo(ActionEvent e) {

			if (this.componentTable.getSelectedRows().length != 1) {
				JOptionPane.showMessageDialog(this, "请选择一个組件.", "alert", JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				String target = (String) this.componentTable.getValueAt(this.componentTable.getSelectedRows()[0], 0);

				List<JavaClass> javaClasses = new ArrayList<JavaClass>();
				for (JavaClass javaClass : JDependUnitMgr.getInstance().getClasses()) {
					if (selectedJavaClass.contains(javaClass.getName())) {
						javaClasses.add(javaClass);
					}
				}

				boolean adjust = false;
				for (JavaClass javaClass : javaClasses) {
					if (!javaClass.getComponent().getName().equals(target)) {
						adjust = true;
						break;
					}
				}

				if (!adjust) {
					JOptionPane.showMessageDialog(this, "JavaClass所在的组件与目标组件相同，请选择其它组件.", "alert",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				jdepend.model.Component targetUnit = null;
				for (jdepend.model.Component unit : JDependUnitMgr.getInstance().getComponents()) {
					if (unit.getName().equals(target)) {
						targetUnit = unit;
						break;
					}
				}
				RefactorToolFactory.createTool().moveClass(javaClasses, targetUnit);

				frame.onRefactoring();
				// 记录日志
				BusiLogUtil.getInstance().businessLog(Operation.moveToClass);

				dispose();
			} catch (Exception ex) {
				ex.printStackTrace();
				Component source = (Component) e.getSource();
				JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
