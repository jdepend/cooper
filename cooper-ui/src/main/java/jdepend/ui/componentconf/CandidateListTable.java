package jdepend.ui.componentconf;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import jdepend.core.config.CommandConf;
import jdepend.core.config.CommandConfMgr;
import jdepend.core.config.GroupConf;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.JTableUtil;
import jdepend.framework.ui.TableSorter;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JavaPackage;
import jdepend.model.component.modelconf.Candidate;
import jdepend.model.component.modelconf.CandidateComparator;
import jdepend.model.component.modelconf.CandidateUtil;
import jdepend.model.util.JavaClassUtil;
import jdepend.parse.util.SearchUtil;

public class CandidateListTable extends JTable {

	private ComponentModelPanel componentModelPanel;

	// 候选中最大size
	private int maxSize;
	// 候选集合
	private DefaultTableModel candidateTableModel;

	// 候选集合
	private List<Candidate> candidates;
	private Map<String, Candidate> candidateForIds;
	// 当前候选集合
	private List<String> currentCandidateList;

	// 正在操作的命令组名称
	private String currentGroup;

	private String path;

	// 缓存
	private Collection<JavaPackage> packages;

	public CandidateListTable(ComponentModelPanel componentModelPanel, String path, String currentGroup) {

		this.componentModelPanel = componentModelPanel;
		this.path = path;
		this.currentGroup = currentGroup;

		candidateTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};

		TableSorter sorter = new TableSorter(candidateTableModel);

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem createItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_CreateComponent));
		createItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CandidateListTable.this.componentModelPanel.createComponent();
				} catch (JDependException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(createItem);
		JMenuItem create1Item = new JMenuItem(BundleUtil.getString(BundleUtil.Command_CreateComponentWithPackages));
		create1Item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CandidateListTable.this.componentModelPanel.batchCreateComponent();
				} catch (JDependException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(create1Item);

		popupMenu.addSeparator();

		JMenuItem joinItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_JoinComponent));
		joinItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CandidateListTable.this.componentModelPanel.joinComponent();
				} catch (JDependException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(joinItem);

		popupMenu.addSeparator();

		JMenuItem viewClassItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewClassList));
		viewClassItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewClassList();
				} catch (JDependException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage(), "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		popupMenu.add(viewClassItem);

		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				if (e.getButton() == 3) {
					popupMenu.show(table, e.getX(), e.getY());
				}
			}
		});

		sorter.setTableHeader(this.getTableHeader());

		candidateTableModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_Place));
		candidateTableModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_Name));
		candidateTableModel.addColumn(BundleUtil.getString(BundleUtil.TableHead_Scale));

		sorter.setSortingStatus(1, TableSorter.ASCENDING);

		this.setModel(sorter);

	}

	protected void loadCandidateList() {

		candidateTableModel.setRowCount(0);

		currentCandidateList = new ArrayList<String>();

		candidates = new ArrayList<Candidate>(this.getCandidates(this.path));

		Collections.sort(candidates, new CandidateComparator());

		this.candidateForIds = new HashMap<String, Candidate>();
		for (Candidate candidate : candidates) {
			this.candidateForIds.put(candidate.getId(), candidate);
		}
		maxSize = 0;

		Object[] row;
		int size;
		for (Candidate candidate : candidates) {
			if (this.componentModelPanel.filterExt.isSelected() && candidate.isInner()
					|| !this.componentModelPanel.filterExt.isSelected()) {
				row = new Object[3];
				row[0] = candidate.getPlace();
				row[1] = candidate.getName();
				size = candidate.size();
				row[2] = size;
				if (maxSize < size) {
					maxSize = size;
				}
				candidateTableModel.addRow(row);
			}
			currentCandidateList.add(candidate.getId());
		}

		List<String> fitColNames = new ArrayList<String>();
		fitColNames.add(BundleUtil.getString(BundleUtil.TableHead_Name));
		JTableUtil.fitTableColumns(this, fitColNames);
	}

	protected void reLoadCandidateList() {
		this.clear();
		this.loadCandidateList();
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if (column == 2) {
			return new TableCellRenderer() {
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					JProgressBar progressBar = new JProgressBar();
					progressBar.setMinimum(0);
					progressBar.setMaximum(maxSize);
					if (value != null) {
						progressBar.setValue((Integer) value);
						progressBar.setToolTipText(Integer.toString((Integer) value));
					}
					if (isSelected) {
						progressBar.setBackground(table.getSelectionBackground());
					} else {
						progressBar.setBackground(table.getBackground());
					}
					progressBar.setBorderPainted(false);
					return progressBar;
				}
			};
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	private void viewClassList() throws JDependException {
		int[] rows = this.getSelectedRows();
		if (rows == null || rows.length != 1)
			throw new JDependException("请选择一个包！");

		String place = (String) candidateTableModel.getValueAt(rows[0], 0);
		String name = (String) candidateTableModel.getValueAt(rows[0], 1);
		Candidate candidate = this.candidateForIds.get(CandidateUtil.getId(place, name));

		ClassListInThePackageDialog d = new ClassListInThePackageDialog(candidate);
		d.setModal(true);
		d.setVisible(true);
	}

	protected void removeTheCandidateList(Collection<String> candidateIds) {

		if (candidateIds.size() == 0) {
			return;
		}

		for (int row = candidateTableModel.getRowCount() - 1; row >= 0; row--) {
			String place = (String) candidateTableModel.getValueAt(row, 0);
			String name = (String) candidateTableModel.getValueAt(row, 1);
			if (candidateIds.contains(CandidateUtil.getId(place, name))) {
				candidateTableModel.removeRow(row);
			}
		}
		for (int row = this.currentCandidateList.size() - 1; row >= 0; row--) {
			if (candidateIds.contains(currentCandidateList.get(row))) {
				currentCandidateList.remove(row);
			}
		}
	}

	protected void addTheCandidateList(Collection<String> candidateIds) {
		for (Candidate candidate : candidates) {
			if (candidateIds.contains(candidate.getId()) && !this.currentCandidateList.contains(candidate.getId())) {
				this.currentCandidateList.add(candidate.getId());
			}
		}
		filterCandidateList();
	}

	protected void filterCandidateList() {

		String filter = this.componentModelPanel.itemListFilter.getText();
		boolean filterExtSetting = this.componentModelPanel.filterExt.isSelected();
		boolean filterString;
		boolean filterExtResult;
		List<String> matchCandidateList = new ArrayList<String>();

		for (String candidateId : this.currentCandidateList) {
			filterString = filter == null || filter.length() == 0
					|| JavaClassUtil.match(filter, CandidateUtil.getName(candidateId));
			filterExtResult = filterExtSetting ? this.candidateForIds.get(candidateId).isInner() ? true : false : true;
			if (filterString && filterExtResult) {
				matchCandidateList.add(candidateId);
			}
		}

		candidateTableModel.setRowCount(0);
		Object[] row;
		for (String candidateId : matchCandidateList) {
			row = new Object[3];
			row[0] = CandidateUtil.getPlace(candidateId);
			row[1] = CandidateUtil.getName(candidateId);
			for (Candidate javaPackage : candidates) {
				if (javaPackage.getId().equals(candidateId)) {
					row[2] = javaPackage.size();
				}
			}
			candidateTableModel.addRow(row);
		}
		candidateTableModel.fireTableDataChanged();
	}

	private Collection<? extends Candidate> getCandidates(String path) {
		if (this.packages == null) {
			// 转换
			path = CommandConf.covertDefaultClassesPath(path);

			List<String> paths = new ArrayList<String>();
			paths.add(path);

			SearchUtil searchUtil = new SearchUtil(paths);
			searchUtil.setParseConfigs(false);
			searchUtil.setSupplyJavaClassDetail(false);
			searchUtil.setBuildClassRelation(false);

			try {
				// 设置当前命令组配置的FilteredPackages
				GroupConf groupConf = CommandConfMgr.getInstance().getTheGroup(this.currentGroup);
				if (groupConf != null) {
					searchUtil.addFilters(groupConf.getFilteredPackages());
				}
			} catch (JDependException e) {
				e.printStackTrace();
			}
			packages = searchUtil.getPackages();
		}

		return componentModelPanel.getComponentModelConf().getCandidates(packages);
	}

	protected List<Candidate> getCandidates() {
		return candidates;
	}

	private void clear() {
		this.packages = null;
	}
}
