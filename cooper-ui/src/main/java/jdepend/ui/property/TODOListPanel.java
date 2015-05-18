package jdepend.ui.property;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.ui.JDependCooper;
import jdepend.util.todolist.TODOItem;
import jdepend.util.todolist.TODOListIdentifyerFacade;

public final class TODOListPanel extends JPanel {

	private JDependCooper frame;

	private JTable listTable;

	private DefaultTableModel listModel;

	private String current;

	private List<String> selectedTODOItems;

	private List<TODOItem> todoList = new ArrayList<TODOItem>();

	public TODOListPanel(JDependCooper frame) {
		this.frame = frame;

		setLayout(new BorderLayout());

		this.add(this.initList());
	}

	private JComponent initList() {
		listModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		listModel.addColumn("ID");
		listModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_Desc));
		listModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_According));

		this.listTable = new JTable(listModel);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem viewItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_View));
		viewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view();
			}
		});
		popupMenu.add(viewItem);
		JMenuItem executeItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_VirtualExecute));
		executeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					execute();
				} catch (JDependException e1) {
					e1.printStackTrace();
					frame.getResultPanel().showError(e1);
				}
			}
		});
		popupMenu.add(executeItem);

		popupMenu.addSeparator();

		JMenuItem saveAsItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAs));
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTableUtil.exportTableToExcel(listTable);
			}
		});
		popupMenu.add(saveAsItem);

		listTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				current = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
				selectedTODOItems = new ArrayList<String>();
				for (int row : table.getSelectedRows()) {
					selectedTODOItems.add((String) table.getValueAt(row, 0));
				}
				if (e.getClickCount() == 2) {
					try {
						execute();
					} catch (JDependException e1) {
						e1.printStackTrace();
						frame.getResultPanel().showError(e1);
					}
				} else if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

		this.listTable.getColumnModel().getColumn(0).setMaxWidth(0);
		this.listTable.getColumnModel().getColumn(0).setMinWidth(0);

		JScrollPane pane = new JScrollPane(this.listTable);
		pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		return pane;
	}

	private void view() {
		TODOItem item = this.getCurrent();
		Map<String, JComponent> groupComponents = new LinkedHashMap<String, JComponent>();
		int index = 0;
		for (Object info : item.getInfo()) {
			TODOItemRender render = TODOItemRenderMgr.getInstance().getItemRender(info);
			if (render != null) {
				index++;
				groupComponents.put(item.getAccording() + index, render.render(frame, info));

			}
		}
		this.frame.getResultPanel().addResult(item.getAccording(), this.compositeComponent(groupComponents));
	}

	private void execute() throws JDependException {
		if (this.selectedTODOItems == null || this.selectedTODOItems.size() == 0) {
			throw new JDependException("请选择需要执行的待做事项");
		}
		Map<TODOItem, List<Object>> infos = new LinkedHashMap<TODOItem, List<Object>>();
		for (TODOItem item : this.getCurrents()) {
			List<Object> info = null;
			try {
				info = item.execute();
			} catch (JDependException e) {
				e.printStackTrace();
				frame.showStatusError(e.getMessage());
			}
			if (info != null && info.size() > 0) {
				infos.put(item, info);
			}
		}

		frame.onRefactoring();

		int index;
		for (TODOItem item : infos.keySet()) {
			index = 0;
			for (Object info : infos.get(item)) {
				TODOItemRender render = TODOItemRenderMgr.getInstance().getItemRender(info);
				if (render != null) {
					index++;
					frame.getResultPanel().addResult(item.getAccording() + index, render.render(frame, info));
				}
			}
		}
	}

	private JComponent compositeComponent(Map<String, JComponent> components) {
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setTabPlacement(JTabbedPane.BOTTOM);
		for (String title : components.keySet()) {
			tabPane.add(title, components.get(title));
		}
		return tabPane;
	}

	private TODOItem getCurrent() {
		TODOItem item = null;
		for (TODOItem element : todoList) {
			if (element.getId().equals(this.current)) {
				item = element;
				break;
			}
		}
		return item;
	}

	private List<TODOItem> getCurrents() {
		List<TODOItem> items = new ArrayList<TODOItem>();
		for (TODOItem element : todoList) {
			if (this.selectedTODOItems.contains(element.getId())) {
				items.add(element);
			}
		}
		return items;
	}

	public void refresh() throws JDependException {
		TODOListIdentifyerFacade identify = new TODOListIdentifyerFacade();
		todoList = identify.identify(JDependUnitMgr.getInstance().getResult());

		listModel.setRowCount(0);
		Object[] row;
		for (TODOItem item : todoList) {

			row = new Object[3];
			row[0] = item.getId();
			row[1] = item.getContent();
			row[2] = item.getAccording();
			listModel.addRow(row);
		}

	}

	public void clear() {
		listModel.setRowCount(0);
	}

}
