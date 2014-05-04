package jdepend.ui.motive;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.knowledge.motive.MotiveContainer;
import jdepend.model.AreaComponent;
import jdepend.model.JDependUnitMgr;

public abstract class ComponentListDialog extends JDialog {

	private DefaultListModel componentNames;
	private JList componentNameList;

	protected List<String> currentComponentNames;

	private Collection<String> includeComponents;

	protected MotiveContainer motiveContainer;

	protected MotiveOperationPanel motiveOperationPanel;

	public ComponentListDialog(MotiveOperationPanel motiveOperationPanel) {
		super();

		getContentPane().setLayout(new BorderLayout());
		setSize(500, 300);
		this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

		this.motiveOperationPanel = motiveOperationPanel;
		this.motiveContainer = motiveOperationPanel.getMotiveContainer();
		this.collectIncludeComponents();

		this.add(BorderLayout.CENTER, this.createComponentNameList());

		JPanel buttonBar = new JPanel(new FlowLayout());
		buttonBar.add(createCloseButton());

		this.add(BorderLayout.SOUTH, buttonBar);

		this.refreshComponentNames();
	}

	private void collectIncludeComponents() {
		this.includeComponents = new HashSet<String>();

		for (AreaComponent areaInfo : motiveContainer.getAreas()) {
			for (String component : areaInfo.getComponents()) {
				this.includeComponents.add(component);
			}
		}

	}

	private JList createComponentNameList() {

		componentNames = new DefaultListModel();

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem addItem = new JMenuItem("添加");
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					add();
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(ComponentListDialog.this, "添加失败", "error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(addItem);

		componentNameList = new JList(componentNames);
		componentNameList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		componentNameList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JList list = (JList) e.getSource();
				currentComponentNames = new ArrayList<String>();
				for (Object value : list.getSelectedValues()) {
					currentComponentNames.add((String) value);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				JList list = (JList) e.getSource();
				if (e.getButton() == 3) {
					popupMenu.show(list, e.getX(), e.getY());
				}
			}
		});

		return componentNameList;
	}

	protected abstract void add() throws JDependException;

	private void refreshComponentNames() {
		this.componentNames.setSize(0);
		for (jdepend.model.Component unit : JDependUnitMgr.getInstance().getComponents()) {
			if (!this.includeComponents.contains(unit.getName())) {
				this.componentNames.addElement(unit.getName());
			}
		}
	}

	private Component createCloseButton() {
		JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Close));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		return button;
	}

}
