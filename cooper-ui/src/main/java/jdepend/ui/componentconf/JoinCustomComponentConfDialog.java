package jdepend.ui.componentconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.ComponentModelConf;

abstract class JoinCustomComponentConfDialog extends JDialog {

	protected List<String> joinPackages;

	private DefaultTableModel componentListTableModel;

	private JTable componentListTable;

	private ComponentModelConf componentModelConf;

	public JoinCustomComponentConfDialog(List<String> packages, ComponentModelConf componentModelConf) {
		this.joinPackages = packages;
		this.componentModelConf = componentModelConf;

		this.setLayout(new BorderLayout());
		setSize(300, 400);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createOkButton());
		buttonBar.add(createCancelButton());

		this.add(BorderLayout.CENTER, createComponentList());

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private JComponent createComponentList() {
		componentListTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		componentListTableModel.addColumn("组件名");
		componentListTableModel.addColumn("组件类型");
		Object[] row;
		for (ComponentConf componentConf : componentModelConf.getComponentConfs()) {
			row = new Object[2];
			row[0] = componentConf.getName();
			row[1] = jdepend.model.Component.layerDesc(componentConf.getLayer());
			componentListTableModel.addRow(row);
		}

		componentListTable = new JTable(componentListTableModel);

		return new JScrollPane(componentListTable);
	}

	protected Component createOkButton() {
		JButton button = new JButton("确认");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (componentListTable.getSelectedRowCount() != 1) {
					JOptionPane.showMessageDialog((Component) e.getSource(), "请选择一个组件", "alert",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					String joinComponentName = (String) componentListTable.getValueAt(componentListTable
							.getSelectedRow(), 0);
					componentModelConf.getTheComponentConf(joinComponentName).addPackages(joinPackages);
					doService();
					JoinCustomComponentConfDialog.this.dispose();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog((Component) e.getSource(), e1.getMessage(), "alert",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		return button;
	}

	protected abstract void doService() throws JDependException;

	protected Component createCancelButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JoinCustomComponentConfDialog.this.dispose();
			}
		});

		return button;
	}
}