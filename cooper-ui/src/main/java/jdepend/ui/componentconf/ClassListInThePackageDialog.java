package jdepend.ui.componentconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JavaClass;
import jdepend.model.component.modelconf.Candidate;

/**
 * 显示执行Package下的类列表
 * 
 * @author wangdg
 * 
 */
public class ClassListInThePackageDialog extends JDialog {

	public ClassListInThePackageDialog(Candidate candidate) {
		this.setTitle(candidate.getName() + " 类列表");
		this.setLayout(new BorderLayout());
		setSize(400, 250);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		JPanel content = new JPanel(new BorderLayout());

		content.add(BorderLayout.CENTER, new JScrollPane(this.createClassList(candidate)));

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createCloseButton());

		this.add(BorderLayout.CENTER, content);

		this.add(BorderLayout.SOUTH, buttonBar);
	}

	private JTable createClassList(Candidate candidate) {
		DefaultTableModel listModel = new DefaultTableModel();

		TableSorter sorter = new TableSorter(listModel);

		JTable listTable = new JTable(sorter);

		sorter.setTableHeader(listTable.getTableHeader());

		listModel.addColumn("类名");
		Object[] row;

		for (JavaClass javaClass : candidate.getClasses()) {
			row = new Object[1];
			row[0] = javaClass.getName();
			listModel.addRow(row);
		}

		return listTable;
	}

	protected Component createCloseButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClassListInThePackageDialog.this.dispose();
			}
		});

		return button;
	}

}
