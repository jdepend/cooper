package jdepend.ui.command;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.core.local.config.CommandConfMgr;
import jdepend.core.local.config.GroupConf;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.ui.JDependCooper;

/**
 * The <code>AboutDialog</code> displays the about information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class SelectGroupDialog extends JDialog {

	private JDependCooper frame;

	private JTable groupsTable;

	/**
	 * Constructs an <code>AboutDialog</code> with the specified parent frame.
	 * 
	 * @param parent
	 *            Parent frame.
	 */
	public SelectGroupDialog(JDependCooper parent) {
		super(parent);

		this.frame = parent;

		setTitle("选择显示的组");

		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		setSize(450, 400);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel content = new JPanel(new BorderLayout());
		content.add(createGroups());

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createSaveButton());
		buttonBar.add(createCloseButton());

		panel.add(BorderLayout.CENTER, content);

		panel.add(BorderLayout.SOUTH, buttonBar);

		getContentPane().add(BorderLayout.CENTER, panel);

	}

	private JComponent createGroups() {

		DefaultTableModel model = new DefaultTableModel() {
			
			@Override
			public Class getColumnClass(int c) {
				Object value = getValueAt(0, c);
				if (value != null) {
					return value.getClass();
				} else {
					return String.class;
				}
			}
		};

		TableSorter sorter = new TableSorter(model);
		groupsTable = new JTable(sorter);

		model.addColumn("是否显示");
		model.addColumn("命令组");
		model.addColumn("属性");

		try {
			Object[] row;
			GroupConf groupConf;
			for (String group : CommandConfMgr.getInstance().getGroupNames()) {
				row = new Object[3];
				groupConf = CommandConfMgr.getInstance().getTheGroup(group);
				row[0] = new Boolean(groupConf.isVisible());
				row[1] = group;
				row[2] = groupConf.getAttribute();
				model.addRow(row);
			}
		} catch (JDependException e) {
			e.printStackTrace();
		}

		sorter.setTableHeader(groupsTable.getTableHeader());
		sorter.setSortingStatus(2, TableSorter.ASCENDING);

		return new JScrollPane(groupsTable);
	}

	/**
	 * Creates and returns a button with the specified label.
	 * 
	 * @param label
	 *            Button label.
	 * @return Button.
	 */
	private JButton createCloseButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

	private JButton createSaveButton() {

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Save));
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				save(e);
			}
		});

		return button;
	}

	private void save(ActionEvent e) {

		int numRows = groupsTable.getRowCount();
		javax.swing.table.TableModel model = groupsTable.getModel();
		try {
			for (int i = 0; i < numRows; i++) {
				CommandConfMgr.getInstance().getTheGroup((String) model.getValueAt(i, 1)).setVisible(
						(Boolean) model.getValueAt(i, 0));
			}
			CommandConfMgr.getInstance().updateGroups();
			frame.getGroupPanel().refreshGroup();
			dispose();
		} catch (JDependException ex) {
			Component source = (Component) e.getSource();
			JOptionPane.showMessageDialog(source, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
		}

	}
}
