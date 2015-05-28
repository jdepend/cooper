package jdepend.ui.property;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import jdepend.core.local.command.CommandAdapterMgr;
import jdepend.core.local.config.CommandConfMgr;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.util.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;
import jdepend.report.ui.ClassListPanel;
import jdepend.report.ui.MethodListDialog;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.JavaClassCompareTableCellRenderer;

public class ClassListPanelWrapper extends ClassListPanel {

	private JDependCooper frame;

	public ClassListPanelWrapper(JDependCooper frame) {
		super(frame);

		this.frame = frame;
	}

	@Override
	protected void initClassList() {

		super.initClassList();

		JavaClassCompareTableCellRenderer renderer = new JavaClassCompareTableCellRenderer(this.extendUnits);
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

		popupMenu.add(this.createCasItem());

		popupMenu.add(this.createCesItem());

		popupMenu.addSeparator();

		popupMenu.add(this.createMoveToItem(null));

		popupMenu.add(this.createSaveAsItem());

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
			String group = JDependUnitMgr.getInstance().getResult().getRunningContext().getGroup();
			StringBuilder src = CommandConfMgr.getInstance().getTheGroup(group).getSrcContent(current);
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
		JavaClassUnit javaClass = JDependUnitMgr.getInstance().getResult().getTheClass(current);
		MethodListDialog d = new MethodListDialog(javaClass);
		d.setModal(true);
		d.setVisible(true);
	}
}
