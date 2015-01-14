package jdepend.ui.componentconf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jdepend.framework.exception.JDependException;
import jdepend.framework.util.BundleUtil;
import jdepend.model.component.modelconf.Candidate;
import jdepend.model.component.modelconf.CandidateUtil;
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.component.modelconf.ComponentModelConfMgr;
import jdepend.model.component.modelconf.JavaClassComponentModelConf;
import jdepend.model.component.modelconf.JavaPackageComponentModelConf;
import jdepend.ui.JDependCooper;
import jdepend.ui.wizard.IgnoreSettingDialog;

public class ComponentModelPanel extends JPanel {

	private JTextField componentModelField;

	// 包集合
	private CandidateListTable candidateTable;
	// 过滤器
	protected JTextField itemListFilter;
	// 是否包含外部项目
	protected JCheckBox filterExt;
	// 列表所占区域
	private boolean itemListNormal = true;

	// 组件结果
	private ComponentModelConf componentModelConf = new JavaPackageComponentModelConf();

	// 已创建的组件
	private JList componentListUI;

	private DefaultListModel componentListModel = new DefaultListModel();

	private String currentComponent;

	// 组件关联的元素
	private DefaultListModel elementListModel = new DefaultListModel();

	public static final int Width = 650;
	public static final int Height = 580;

	public static final int NormalHeight = 355;

	private JDependCooper frame;

	/**
	 * 创建组件模型窗口
	 * 
	 * @param frame
	 * @param path
	 * @param group
	 */
	public ComponentModelPanel(JDependCooper frame, String path, String group) {
		this.init(frame, path, group, null);
		candidateTable.loadCandidateList();
	}

	/**
	 * 修改组件模型窗口
	 * 
	 * @param frame
	 * @param path
	 * @param groupName
	 * @param componentModelName
	 */
	public ComponentModelPanel(JDependCooper frame, String path, String groupName, String componentModelName) {
		this.init(frame, path, groupName, componentModelName);

		componentModelField.setText(componentModelName);
		this.setReadOnlyName();

		candidateTable.loadCandidateList();
		// 更新componentListModel和packageListModel
		for (ComponentConf componentConf : componentModelConf.getComponentConfs()) {
			componentListModel.addElement(componentConf.getName());
			for (String packageName : componentConf.getItemIds()) {
				if (componentListModel.size() == 1) {
					elementListModel.addElement(packageName);
				}
			}
		}

		// 删除已经包含在组件模型中的packages
		candidateTable.removeTheCandidateList(componentModelConf.getContainItems());

		// 设置默认组件
		if (this.currentComponent == null && componentListModel.size() > 0) {
			currentComponent = (String) componentListModel.getElementAt(0);
			componentListUI.setSelectedIndex(0);
		}
	}

	/**
	 * 初始化页面控件
	 * 
	 * @param frame
	 * @param path
	 * @param group
	 * @param componentModelName
	 */
	private void init(JDependCooper frame, String path, String group, String componentModelName) {

		this.frame = frame;

		this.setLayout(new BorderLayout());

		JPanel componentGroupPanel = new JPanel(new BorderLayout());
		componentGroupPanel.add(BorderLayout.WEST,
				new JLabel(BundleUtil.getString(BundleUtil.ClientWin_ComponentModel_Name) + ":"));

		JPanel contentPanel = new JPanel(new GridLayout(1, 3));
		componentModelField = new JTextField();
		componentModelField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				componentModelConf.setName(componentModelField.getText());
			}
		});
		contentPanel.add(componentModelField);

		if (componentModelName != null) {
			componentModelConf = ComponentModelConfMgr.getInstance()
					.getTheComponentModelConf(group, componentModelName);
		} else {
			componentModelConf = new JavaPackageComponentModelConf();
		}

		JCheckBox candidateType = new JCheckBox(BundleUtil.getString(BundleUtil.ClientWin_ComponentModel_PackageModel));
		if (componentModelConf instanceof JavaPackageComponentModelConf) {
			candidateType.setSelected(true);
		} else {
			candidateType.setSelected(false);
		}

		candidateType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (((JCheckBox) event.getSource()).isSelected()) {
					componentModelConf = new JavaPackageComponentModelConf();
				} else {
					componentModelConf = new JavaClassComponentModelConf();
				}
				if (componentModelField.getText() != null && componentModelField.getText().length() > 0) {
					componentModelConf.setName(componentModelField.getText());
				}
				currentComponent = null;
				candidateTable.loadCandidateList();
				refreshComponentList();

			}
		});
		contentPanel.add(candidateType);

		filterExt = new JCheckBox(BundleUtil.getString(BundleUtil.ClientWin_ComponentModel_FilterExt));
		filterExt.setSelected(true);
		filterExt.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				candidateTable.filterCandidateList();
			}
		});
		contentPanel.add(filterExt);

		componentGroupPanel.add(BorderLayout.CENTER, contentPanel);

		JPanel layoutPanel = new JPanel(new BorderLayout());
		layoutPanel.add(BorderLayout.WEST,
				new JLabel(BundleUtil.getString(BundleUtil.ClientWin_ComponentModel_PackageListFilter) + ":"));
		itemListFilter = new JTextField();
		itemListFilter.setPreferredSize(new Dimension(150, 20));
		itemListFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				candidateTable.filterCandidateList();
			}
		});

		layoutPanel.add(BorderLayout.CENTER, itemListFilter);

		componentGroupPanel.add(BorderLayout.EAST, layoutPanel);

		this.add(BorderLayout.NORTH, componentGroupPanel);

		JPanel packageComponentPanel = new JPanel(new BorderLayout());
		JComponent javaPackageList = this.createCandidateList(path, group);
		JPanel componentPanel = new JPanel(new BorderLayout());

		JList componentList = createComponentList();

		JList packageList = createElementList();

		JSplitPane componentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, new JScrollPane(
				componentList), new JScrollPane(packageList));

		componentPanel.add(componentSplitPane);

		final JSplitPane packageComponentSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, javaPackageList,
				componentPanel);

		packageComponentSplitPane.setDividerLocation(NormalHeight);

		candidateTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (itemListNormal) {
						packageComponentSplitPane.setDividerLocation(Height);
						itemListNormal = false;
					} else {
						packageComponentSplitPane.setDividerLocation(NormalHeight);
						itemListNormal = true;
					}
				}
			}
		});

		packageComponentPanel.add(packageComponentSplitPane);

		this.add(BorderLayout.CENTER, packageComponentPanel);
	}

	public void setReadOnlyName() {
		componentModelField.setEditable(false);
	}

	public JComponent createCandidateList(String path, String group) {

		candidateTable = new CandidateListTable(this, path, group);

		JScrollPane pane = new JScrollPane(candidateTable);
		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					JPopupMenu menu = new JPopupMenu();
					JMenuItem gobalIgnoreItem = new JMenuItem(BundleUtil
							.getString(BundleUtil.Command_ViewGobalIgnorePackages));
					gobalIgnoreItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								gobalIgnore();
							} catch (Exception ex) {
								ex.printStackTrace();
								JOptionPane.showMessageDialog(ComponentModelPanel.this, ex.getMessage(), "alert",
										JOptionPane.ERROR_MESSAGE);
							}
						}

						private void gobalIgnore() {
							IgnoreSettingDialog d = new IgnoreSettingDialog(frame);
							d.setModal(true);
							d.setVisible(true);
						}
					});
					menu.add(gobalIgnoreItem);
					JMenuItem refreshItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Refresh));
					refreshItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							refresh();
						}
					});
					menu.add(refreshItem);
					menu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});
		return pane;

	}

	public void refresh() {
		candidateTable.reLoadCandidateList();
		// 删除已经包含在组件模型中的packages
		candidateTable.removeTheCandidateList(componentModelConf.getContainItems());
		// 进行关键字过滤
		candidateTable.filterCandidateList();
	}

	public void setPath(String path) {
		candidateTable.setPath(path);
	}

	public String getPath() {
		return candidateTable.getPath();
	}

	protected void createComponent() throws JDependException {
		int[] rows = candidateTable.getSelectedRows();
		if (rows == null || rows.length == 0)
			throw new JDependException("您没有选择包！");

		ArrayList<String> selectedCandidates = new ArrayList<String>();
		for (int i = 0; i < rows.length; i++) {
			String place = (String) candidateTable.getValueAt(rows[i], 0);
			String name = (String) candidateTable.getValueAt(rows[i], 1);
			selectedCandidates.add(CandidateUtil.getId(place, name));
		}

		CreateCustomComponentConfDialog d = new CreateCustomComponentConfDialog(selectedCandidates);
		d.setModal(true);
		d.setVisible(true);
	}

	protected void batchCreateComponent() throws JDependException {
		int[] rows = candidateTable.getSelectedRows();
		if (rows == null || rows.length == 0)
			throw new JDependException("您没有选择包！");

		ArrayList<String> selectedCandidates = new ArrayList<String>();
		for (int i = 0; i < rows.length; i++) {
			String place = (String) candidateTable.getValueAt(rows[i], 0);
			String name = (String) candidateTable.getValueAt(rows[i], 1);
			selectedCandidates.add(CandidateUtil.getId(place, name));
		}

		ArrayList<String> candidateList;
		for (String unit : selectedCandidates) {
			candidateList = new ArrayList<String>();
			candidateList.add(unit);
			componentModelConf.addComponentConf(unit, jdepend.model.Component.UndefinedComponentLevel, candidateList);
		}
		candidateTable.removeTheCandidateList(selectedCandidates);
		refreshComponentList();
	}

	protected void joinComponent() throws JDependException {
		int[] rows = candidateTable.getSelectedRows();
		if (rows == null || rows.length == 0)
			throw new JDependException("您没有选择包！");

		ArrayList<String> selectedCandidates = new ArrayList<String>();
		for (int i = 0; i < rows.length; i++) {
			String place = (String) candidateTable.getValueAt(rows[i], 0);
			String name = (String) candidateTable.getValueAt(rows[i], 1);
			selectedCandidates.add(CandidateUtil.getId(place, name));
		}

		JoinCustomComponentConfDialog d = new JoinCustomComponentConfDialog(selectedCandidates, componentModelConf) {
			@Override
			protected void doService() {
				candidateTable.removeTheCandidateList(joinPackages);
				refreshComponentList();
			}
		};
		d.setModal(true);
		d.setVisible(true);

	}

	private JList createComponentList() {

		componentListUI = new JList(componentListModel);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_DeleteComponent));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteComponent();
				refreshComponentList();
			}
		});
		popupMenu.add(deleteItem);

		componentListUI.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList theList = (JList) e.getSource();
				ListModel model = theList.getModel();

				int index = theList.locationToIndex(e.getPoint());
				if (index >= 0) {
					currentComponent = (String) model.getElementAt(index);
					theList.setSelectedIndex(index);
					refreshElementList();
				}
				if (e.getButton() == 3)
					popupMenu.show(theList, e.getX(), e.getY());
			}
		});

		return componentListUI;
	}

	private JList createElementList() {
		final JList packageList = new JList(elementListModel);
		packageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem removeItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_MoveToOtherComponent));
		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (packageList.getSelectedValues() == null || packageList.getSelectedValues().length == 0) {
					JOptionPane.showMessageDialog((Component) e.getSource(), "请选择至少一个包", "alert",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				List<String> selectedPackages = new ArrayList<String>();
				for (Object value : packageList.getSelectedValues()) {
					selectedPackages.add((String) value);
				}
				JoinCustomComponentConfDialog d = new JoinCustomComponentConfDialog(selectedPackages,
						componentModelConf) {
					@Override
					protected void doService() {
						componentModelConf.getTheComponentConf(currentComponent).deleteItemIds(joinPackages);
						refreshComponentList();
					}
				};
				d.setModal(true);
				d.setVisible(true);
			}
		});
		popupMenu.add(removeItem);

		JMenuItem deleteItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_Delete));
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (packageList.getSelectedValues() == null || packageList.getSelectedValues().length == 0) {
					JOptionPane.showMessageDialog((Component) e.getSource(), "请选择至少一个包", "alert",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				List<String> selectedPackages = new ArrayList<String>();
				for (Object value : packageList.getSelectedValues()) {
					selectedPackages.add((String) value);
				}

				componentModelConf.getTheComponentConf(currentComponent).deleteItemIds(selectedPackages);
				candidateTable.addTheCandidateList(selectedPackages);
				refreshComponentList();
			}
		});
		popupMenu.add(deleteItem);

		packageList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JList theList = (JList) e.getSource();
				if (e.getButton() == 3)
					popupMenu.show(theList, e.getX(), e.getY());
			}
		});

		return packageList;
	}

	private void deleteComponent() {
		String componentName = (String) componentListUI.getSelectedValue();
		candidateTable.addTheCandidateList(componentModelConf.getTheComponentConf(componentName).getItemIds());
		componentModelConf.deleteComponentConf(componentName);
		this.currentComponent = null;
	}

	private void refreshComponentList() {
		componentListModel.removeAllElements();
		for (String componentName : componentModelConf.getComponentConfNames()) {
			componentListModel.addElement(componentName);
		}
		this.refreshElementList();
	}

	private void refreshElementList() {
		elementListModel.removeAllElements();
		if (currentComponent != null) {
			for (String itemId : componentModelConf.getTheComponentConf(currentComponent).getItemIds())
				elementListModel.addElement(itemId);
		}
	}

	class CreateCustomComponentConfDialog extends CreateComponentConfDialog {

		public CreateCustomComponentConfDialog(ArrayList<String> units) {
			super(units, false);
		}

		protected void doService(ActionEvent e) throws JDependException {
			componentModelConf.addComponentConf(componentname.getText(), this.getComponentLayer(), units);
			candidateTable.removeTheCandidateList(units);
			refreshComponentList();
		}
	}

	public void validateData() throws JDependException {

		this.componentModelConf.validateData();

		List<String> ignorePackages = this.calIgnoreItems();
		if (ignorePackages != null && ignorePackages.size() > 0) {
			if (JOptionPane.showConfirmDialog(this, "[" + ignorePackages.get(0) + "]等" + ignorePackages.size()
					+ "个没有被包含的组件中，你是否确认继续？") != JOptionPane.OK_OPTION) {
				throw new JDependException();
			}
		}
		// 设置未包含的packages
		this.componentModelConf.setIgnoreItems(ignorePackages);
	}

	public ComponentModelConf getComponentModelConf() {
		return this.componentModelConf;
	}

	public List<String> calIgnoreItems() {
		Collection<String> containItems = this.componentModelConf.getContainItems();

		List<String> ignorePackages = new ArrayList<String>();
		for (Candidate javaPackage : candidateTable.getCandidates()) {
			if (!containItems.contains(javaPackage.getId()) && javaPackage.isInner()) {
				ignorePackages.add(javaPackage.getId());
			}
		}
		return ignorePackages;
	}
}
