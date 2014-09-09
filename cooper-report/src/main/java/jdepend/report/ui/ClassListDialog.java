package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.util.BundleUtil;

public class ClassListDialog extends CooperDialog {

	private ClassListPanel classListPanel;

	private JLabel tipLabel;

	public ClassListDialog(jdepend.model.Component component) {
		super("类列表");
		getContentPane().setLayout(new BorderLayout());

		classListPanel = new ClassListPanel();
		this.add(classListPanel);
		classListPanel.showClassList(component);

		this.initPopupMenu();
	}

	public ClassListDialog(JDependFrame frame) {
		super("类列表");
		this.setSize(frame.getScrSize().width, frame.getScrSize().height);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示
		getContentPane().setLayout(new BorderLayout());

		this.add(BorderLayout.NORTH, this.createSearchPanel());

		classListPanel = new ClassListPanel();
		this.add(classListPanel);
		int classCount = classListPanel.showAllClassList();
		tipLabel.setText("共" + classCount + "个类");

		this.initPopupMenu();
	}

	private void initPopupMenu() {

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(classListPanel.getClassListTable());
			}
		});
		popupMenu.add(saveAsItem);

		classListPanel.getClassListTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.getButton() == 3) {
					popupMenu.show(classListPanel.getClassListTable(), e.getX(), e.getY());
				}
			}
		});
	}

	private JPanel createSearchPanel() {

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel titleLabel = new JLabel(BundleUtil.getString(BundleUtil.Metrics_Name) + ":");
		searchPanel.add(titleLabel);

		JTextField nameFilter = new JTextField();
		nameFilter.setPreferredSize(new Dimension(250, 20));
		nameFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				classListPanel.filterName(((JTextField) e.getSource()).getText());
				int classCount = classListPanel.reLoadClassList();
				tipLabel.setText("共" + classCount + "个类");
			}
		});
		searchPanel.add(nameFilter);

		tipLabel = new JLabel();
		searchPanel.add(tipLabel);

		return searchPanel;
	}

}
