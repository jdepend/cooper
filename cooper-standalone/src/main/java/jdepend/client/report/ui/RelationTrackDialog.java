package jdepend.client.report.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.client.report.util.ReportConstant;
import jdepend.framework.ui.component.JDependFrame;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.ui.dialog.CooperDialog;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;

public class RelationTrackDialog extends CooperDialog {

	private JDependFrame frame;

	private JTable listTable;

	private DefaultTableModel listModel;

	private Collection<JavaClass> classes;

	private Collection<JavaClass> dependClasses;

	private Collection<JavaClass> allClasses;

	public RelationTrackDialog(JDependFrame frame,
			Collection<JavaClass> classes, Collection<JavaClass> allClasses) {

		this.frame = frame;
		this.classes = classes;
		this.allClasses = allClasses;

		this.init();

	}

	private void init() {
		getContentPane().setLayout(new BorderLayout());

		initList();
		showList();
		this.add(BorderLayout.NORTH, createOperationPanel());
		this.add(BorderLayout.CENTER, new JScrollPane(listTable));
	}

	private JPanel createOperationPanel() {

		JPanel relationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JButton track = new JButton(
				BundleUtil.getString(BundleUtil.Command_Track));
		track.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RelationTrackDialog d = new RelationTrackDialog(frame,
						dependClasses, allClasses);
				d.setModal(true);
				d.setVisible(true);
			}
		});
		relationPanel.add(track);
		
		JButton export = new JButton(
				BundleUtil.getString(BundleUtil.Command_Export));
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		relationPanel.add(export);

		return relationPanel;
	}
	
	private void export(){
		
		DefaultTableModel allClassModel = new DefaultTableModel();
		JTable allClassTable = new JTable(allClassModel); 

		allClassModel.addColumn(ReportConstant.JavaClass_Place);
		allClassModel.addColumn(ReportConstant.Name);
		
		Object[] row;

		for (JavaClass dependClass : allClasses) {
			row = new Object[allClassModel.getColumnCount()];
			row[0] = dependClass.getPlace();
			row[1] = dependClass.getName();

			allClassModel.addRow(row);
		}
		
		JTableUtil.exportTableToExcel(allClassTable);
		
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

		listModel.addColumn("id");
		listModel.addColumn(ReportConstant.JavaClass_Place);
		listModel.addColumn(ReportConstant.Name);

		listTable.getColumnModel().getColumn(0).setMinWidth(0);
		listTable.getColumnModel().getColumn(0).setMaxWidth(0);
		listTable.getColumnModel().getColumn(1).setMinWidth(0);
		listTable.getColumnModel().getColumn(1).setMaxWidth(0);

	}

	private void showList() {
		listModel.setRowCount(0);
		this.loadList();
	}

	private Collection<JavaClass> collectDependClass() {

		dependClasses = new ArrayList<JavaClass>();

		for (JavaClass javaClass : classes) {
			for (JavaClassRelationItem item : javaClass.getCeItems()) {
				JavaClass dependClass = item.getTarget();
				if (!dependClasses.contains(dependClass)
						&& !this.allClasses.contains(dependClass)) {
					dependClasses.add(dependClass);
					this.allClasses.add(dependClass);
				}
			}
		}

		return dependClasses;
	}

	private void loadList() {

		Object[] row;

		Collection<JavaClass> dependClasses = this.collectDependClass();

		for (JavaClass dependClass : dependClasses) {
			row = new Object[listTable.getColumnCount()];
			row[0] = dependClass.getId();
			row[1] = dependClass.getPlace();
			row[2] = dependClass.getName();

			listModel.addRow(row);
		}
	}

}
