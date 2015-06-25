package jdepend.client.report.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import jdepend.client.report.util.ReportConstant;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.util.BundleUtil;
import jdepend.metadata.CandidateUtil;
import jdepend.metadata.util.JavaClassUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;

public class SelectClassDialog extends CooperDialog {

	private JTable classListTable;

	private DefaultTableModel classListModel;

	protected String current;

	public SelectClassDialog() {
		super("类列表");
		getContentPane().setLayout(new BorderLayout());

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel titleLabel = new JLabel(BundleUtil.getString(BundleUtil.Metrics_Name) + ":");
		searchPanel.add(titleLabel);

		final JTextField nameFilter = new JTextField();
		nameFilter.setPreferredSize(new Dimension(250, 20));
		nameFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterClassList(nameFilter.getText());
			}

			private void filterClassList(String name) {
				refresh(name);
			}
		});

		searchPanel.add(nameFilter);

		this.add(BorderLayout.NORTH, searchPanel);

		this.initTable();
		JScrollPane pane = new JScrollPane(classListTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(BorderLayout.CENTER, pane);

		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_OK));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callback();
				dispose();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);

		this.add(BorderLayout.SOUTH, buttonPanel);

		this.refresh();
	}

	protected void callback() {

	}

	private JTable initTable() {
		classListModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(classListModel);

		classListTable = new JTable(sorter);
		classListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point p = new Point(e.getX(), e.getY());
				int row = table.rowAtPoint(p);
				current = CandidateUtil.getId((String) table.getValueAt(row, 0), (String) table.getValueAt(row, 1));
			}
		});

		sorter.setTableHeader(classListTable.getTableHeader());

		classListModel.addColumn(ReportConstant.JavaClass_Place);
		classListModel.addColumn(ReportConstant.Name);

		classListTable.getColumnModel().getColumn(0).setMinWidth(0);
		classListTable.getColumnModel().getColumn(0).setMaxWidth(0);

		return classListTable;

	}

	private void refresh() {
		refresh(null);
	}

	private void refresh(String name) {

		classListModel.setRowCount(0);

		Object row[];
		for (JavaClassUnit javaClassUnit : JDependUnitMgr.getInstance().getResult().getClasses()) {
			if (name == null || name.length() == 0 || JavaClassUtil.match(name, javaClassUnit.getJavaClass())) {
				row = new Object[2];
				row[0] = javaClassUnit.getJavaClass().getPlace();
				row[1] = javaClassUnit.getJavaClass().getName();
				classListModel.addRow(row);
			}
		}
	}
}
