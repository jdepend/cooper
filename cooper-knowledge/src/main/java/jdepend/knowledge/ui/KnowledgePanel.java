package jdepend.knowledge.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.domain.PersistentBean;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.PersistentBeanSettingDialog;
import jdepend.framework.util.BundleUtil;
import jdepend.knowledge.DomainAnalysis;
import jdepend.knowledge.DomainAnalysisMgr;

public final class KnowledgePanel extends JPanel {

	private JDependFrame frame;

	private List<DomainAnalysis> analysises;

	private int currentRow;

	private DefaultTableModel model;

	public KnowledgePanel(final JDependFrame frame) {
		this.frame = frame;
		this.setLayout(new BorderLayout());

		this.analysises = DomainAnalysisMgr.getIntance().getDomainAnalysises();

		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		JTable table = new JTable(model);

		final JPopupMenu popupMenu1 = new JPopupMenu();
		final JPopupMenu popupMenu2 = new JPopupMenu();

		// JMenuItem viewSrcItem1 = new JMenuItem("查看源文件");
		// viewSrcItem1.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// viewSrc();
		// }
		// });
		// JMenuItem viewSrcItem2 = new JMenuItem("查看源文件");
		// viewSrcItem2.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// viewSrc();
		// }
		// });
		// popupMenu1.add(viewSrcItem1);
		// popupMenu2.add(viewSrcItem2);

		JMenuItem settingItem1 = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Setting));
		settingItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setting();
			}
		});
		JMenuItem settingItem2 = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Setting));
		settingItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setting();
			}
		});
		popupMenu1.add(settingItem1);
		popupMenu2.add(settingItem2);

		JMenuItem disableItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Disable));
		disableItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeState();
			}
		});
		popupMenu1.add(disableItem);

		JMenuItem enableItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Enable));
		enableItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeState();
			}
		});
		popupMenu2.add(enableItem);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					JTable table = (JTable) e.getSource();
					currentRow = table.rowAtPoint(e.getPoint());
					if (analysises.get(currentRow).isEnable()) {
						popupMenu1.show(table, e.getX(), e.getY());
					} else {
						popupMenu2.show(table, e.getX(), e.getY());
					}

				}
			}
		});

		model.addColumn(BundleUtil.getString(BundleUtil.TableHead_Name));
		model.addColumn(BundleUtil.getString(BundleUtil.TableHead_Explain));
		model.addColumn(BundleUtil.getString(BundleUtil.TableHead_State));

		refresh();

		this.add(new JScrollPane(table));
	}

	private void viewSrc() {
		DomainAnalysis domainAnalysis = analysises.get(currentRow);
		// domainAnalysis.getClass().getName()

	}

	private void changeState() {
		if (analysises.get(currentRow).isEnable()) {
			analysises.get(currentRow).setEnable(false);
		} else {
			analysises.get(currentRow).setEnable(true);
		}
		refresh();
	}

	private void refresh() {

		model.setRowCount(0);

		Object[] row;
		for (DomainAnalysis analysis : analysises) {
			row = new Object[3];
			row[0] = analysis.getName();
			row[1] = analysis.getTip();
			if (analysis.isEnable()) {
				row[2] = BundleUtil.getString(BundleUtil.Command_Enable);
			} else {
				row[2] = BundleUtil.getString(BundleUtil.Command_Disable);
			}

			model.addRow(row);
		}
	}

	private void setting() {
		DomainAnalysis domainAnalysis = this.analysises.get(this.currentRow);

		PersistentBeanSettingDialog d = new PersistentBeanSettingDialog(frame, (PersistentBean) domainAnalysis);

		d.setModal(true);
		d.setVisible(true);

	}
}
