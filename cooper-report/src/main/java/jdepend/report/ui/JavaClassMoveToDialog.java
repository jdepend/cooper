package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.Operation;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.util.refactor.RefactorToolFactory;

public class JavaClassMoveToDialog extends JDialog {

	private JDependFrame frame;

	private JTable componentTable;

	private List<String> selectedJavaClass;

	private Relation relation;

	private JRadioButton current;

	private JRadioButton depend;

	private JPanel content;

	public JavaClassMoveToDialog(JDependFrame frame, Relation relation) {
		this(frame);
		this.relation = relation;

		content.add(BorderLayout.NORTH, this.createRelationPanel());
	}

	public JavaClassMoveToDialog(JDependFrame frame, List<String> selectedJavaClass) {
		this(frame);
		this.selectedJavaClass = selectedJavaClass;
	}

	private JavaClassMoveToDialog(JDependFrame frame) {
		this.frame = frame;

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

		content = new JPanel(new BorderLayout());
		content.add(BorderLayout.CENTER, createComponentTable());

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createOKButton());
		buttonBar.add(createCloseButton());

		panel.add(BorderLayout.CENTER, content);

		panel.add(BorderLayout.SOUTH, buttonBar);

		this.add(panel);
	}

	private JPanel createRelationPanel() {

		JPanel relationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		relationPanel.add(new JLabel("要移动的类："));
		current = new JRadioButton("源：" + this.relation.getCurrent().getName()) {
			@Override
			protected void fireItemStateChanged(ItemEvent event) {
				if (current.isSelected()) {
					depend.setSelected(false);
				} else {
					depend.setSelected(true);
				}
				calSelectedJavaClass();
			}
		};
		relationPanel.add(current);

		depend = new JRadioButton("目标：" + this.relation.getDepend().getName()) {
			@Override
			protected void fireItemStateChanged(ItemEvent event) {
				if (depend.isSelected()) {
					current.setSelected(false);
				} else {
					current.setSelected(true);
				}
				calSelectedJavaClass();
			}
		};
		relationPanel.add(depend);

		current.setSelected(true);

		return relationPanel;
	}

	private void calSelectedJavaClass() {
		boolean fromCurrent;
		if (current.isSelected()) {
			fromCurrent = true;
		} else {
			fromCurrent = false;
		}
		selectedJavaClass = new ArrayList<String>();
		for (JavaClassRelationItem item : this.relation.getItems()) {
			if (fromCurrent) {
				selectedJavaClass.add(item.getCurrent().getName());
			} else {
				selectedJavaClass.add(item.getDepend().getName());
			}
		}
	}

	private JComponent createComponentTable() {

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
			for (JavaClass javaClass : JDependUnitMgr.getInstance().getResult().getClasses()) {
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
