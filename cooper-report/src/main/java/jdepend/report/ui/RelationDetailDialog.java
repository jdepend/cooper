package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.util.BundleUtil;
import jdepend.model.Component;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassUnit;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.model.component.modelconf.CandidateUtil;
import jdepend.report.util.ReportConstant;
import jdepend.report.way.mapui.GraphJDepend;

public class RelationDetailDialog extends CooperDialog {

	private JDependFrame frame;

	private Relation relation;

	private JRadioButton current;

	private JRadioButton depend;

	private GraphJDepend display;

	public RelationDetailDialog(JDependFrame frame, Component current, Component depend) {
		super(current.getName() + " 依赖于 " + depend.getName());

		this.frame = frame;

		this.relation = current.getCeTheRelation(depend);

		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, this.createOperationPanel());
		this.add(BorderLayout.CENTER, new RelationDetailPanel(this.frame, this.relation.getDetail()));
	}

	public RelationDetailDialog(GraphJDepend display, String current, String depend) {
		super(current + " 依赖于 " + depend);

		this.display = display;
		this.frame = display.getGraphPanel().getFrame();

		for (Relation r : display.getRelations()) {
			if (r.equals(current, depend)) {
				this.relation = r;
			}
		}

		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, this.createOperationPanel());
		this.add(BorderLayout.CENTER, new RelationDetailPanel(this.frame, this.relation.getDetail()));

	}

	@Override
	public void dispose() {
		if (this.display != null && this.display.getGraphPanel().getParentDialog() != null) {
			this.display.getGraphPanel().getParentDialog().dispose();
		}
		super.dispose();
	}

	private JPanel createOperationPanel() {

		JPanel relationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		relationPanel.add(new JLabel("要移动的类："));
		current = new JRadioButton("源：" + this.relation.getCurrent().getName()) {
			@Override
			protected void fireItemStateChanged(ItemEvent event) {
				if (current.isSelected()) {
					depend.setSelected(false);
				} else {
					depend.setSelected(true);
				}
			}
		};
		relationPanel.add(current);

		depend = new JRadioButton("目标：" + this.relation.getDepend().getName()) {
			@Override
			protected void fireItemStateChanged(ItemEvent event) {
				if (depend.isSelected()) {
					current.setSelected(false);
				} else {
					current.setSelected(true);
				}
			}
		};
		relationPanel.add(depend);

		current.setSelected(true);

		JButton moveTo = new JButton(BundleUtil.getString(BundleUtil.Command_Move));
		moveTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MoveToClassListDialog d = new MoveToClassListDialog();
				d.setModal(true);
				d.setVisible(true);
			}
		});
		relationPanel.add(moveTo);

		JButton analyse = new JButton(BundleUtil.getString(BundleUtil.Command_Analyse));
		analyse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RelationDetailForMoveDialog d = new RelationDetailForMoveDialog(frame, RelationDetailDialog.this,
						relation);
				d.setModal(true);
				d.setVisible(true);
			}
		});
		relationPanel.add(analyse);

		return relationPanel;
	}

	private List<String> calSelectedJavaClass() {
		List<String> selectedJavaClass = new ArrayList<String>();
		for (JavaClassRelationItem item : this.relation.getItems()) {
			if (current.isSelected()) {
				selectedJavaClass.add(CandidateUtil.getId(item.getSource().getPlace(), item.getSource().getName()));
			} else {
				selectedJavaClass.add(CandidateUtil.getId(item.getTarget().getPlace(), item.getTarget().getName()));
			}
		}
		return selectedJavaClass;
	}

	class MoveToClassListDialog extends JDialog {

		private DefaultTableModel classListModel;

		private JTable classListTable;

		MoveToClassListDialog() {

			getContentPane().setLayout(new BorderLayout());
			setSize(ResultPopDialogWidth, ResultPopDialogHeight);
			this.setLocationRelativeTo(null);// 窗口在屏幕中间显示

			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());

			JPanel content = new JPanel(new BorderLayout());
			content.add(initTable());

			JPanel buttonBar = new JPanel(new FlowLayout());
			buttonBar.add(createNextButton());
			buttonBar.add(createCancelButton());

			panel.add(BorderLayout.CENTER, content);

			panel.add(BorderLayout.SOUTH, buttonBar);

			getContentPane().add(BorderLayout.CENTER, panel);

		}

		private JComponent initTable() {

			classListModel = new DefaultTableModel() {
				@Override
				public Class getColumnClass(int c) {
					Object value = getValueAt(0, c);
					if (value != null) {
						return value.getClass();
					} else {
						return String.class;
					}
				}

			};

			classListTable = new JTable(classListModel);

			classListModel.addColumn("是否移动");
			classListModel.addColumn(ReportConstant.JavaClass_Place);
			classListModel.addColumn(ReportConstant.Name);

			final CheckBoxRenderer check = new CheckBoxRenderer("是否移动");
			classListTable.getColumn("是否移动").setHeaderRenderer(check);

			classListTable.getTableHeader().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (classListTable.getColumnModel().getColumnIndexAtX(e.getX()) == 0) {// 如果点击的是第0列，即checkbox这一列
						boolean b = !check.isSelected();
						check.setSelected(b);
						classListTable.getTableHeader().repaint();
						for (int i = 0; i < classListTable.getRowCount(); i++) {
							classListTable.getModel().setValueAt(b, i, 0);// 把这一列都设成和表头一样
						}
					}
				}
			});

			Object[] row;
			for (String className : calSelectedJavaClass()) {
				row = new Object[3];
				row[0] = new Boolean(true);
				row[1] = CandidateUtil.getPlace(className);
				row[2] = CandidateUtil.getName(className);
				classListModel.addRow(row);
			}

			classListTable.getColumnModel().getColumn(1).setMinWidth(0);
			classListTable.getColumnModel().getColumn(1).setMaxWidth(0);

			return new JScrollPane(classListTable);
		}

		private JButton createCancelButton() {

			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_Cancel));
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			return button;
		}

		private JButton createNextButton() {

			JButton button = new JButton(BundleUtil.getString(BundleUtil.Command_NextStep));
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					nextStep(e);
				}
			});

			return button;
		}

		private void nextStep(ActionEvent e) {

			Collection<String> moveToClassList = new HashSet<String>();
			for (int i = 0; i < classListModel.getRowCount(); i++) {
				if ((Boolean) classListModel.getValueAt(i, 0)) {
					moveToClassList.add(CandidateUtil.getId((String) classListModel.getValueAt(i, 1),
							(String) classListModel.getValueAt(i, 2)));
				}
			}

			dispose();

			this.openJavaClassMoveToDialog(moveToClassList);
		}

		private void openJavaClassMoveToDialog(Collection<String> moveToClassList) {
			Collection<JavaClassUnit> javaClasses = new ArrayList<JavaClassUnit>();
			for (String javaClassId : moveToClassList) {
				javaClasses.add(JDependUnitMgr.getInstance().getResult().getTheClass(javaClassId));
			}
			JavaClassMoveToDialog d = new JavaClassMoveToDialog(frame, javaClasses);
			d.setListener(new JavaClassMoveToDialogListener() {
				@Override
				public void onFinish() {
					RelationDetailDialog.this.dispose();
				}
			});
			d.setModal(true);
			d.setVisible(true);
		}

		class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

			public CheckBoxRenderer(String name) {
				this.setText(name);
				this.setBorderPainted(true);
				this.setSelected(true);
			}

			@Override
			public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				return this;
			}
		}
	}
}
