package jdepend.report.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.framework.util.BundleUtil;
import jdepend.model.JDependUnitMgr;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.Relation;
import jdepend.report.way.mapui.GraphJDepend;

public class RelationDetailDialog extends CooperDialog {

	private JDependFrame frame;

	private Relation relation;

	private JRadioButton current;

	private JRadioButton depend;

	public RelationDetailDialog(JDependFrame frame, String current, String depend) {
		super(current + " 依赖于 " + depend);

		this.frame = frame;

		this.relation = JDependUnitMgr.getInstance().getResult().getTheRelation(current, depend);

		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, this.createOperationPanel());
		this.add(BorderLayout.CENTER, new RelationDetailPanel(this.relation));
	}

	public RelationDetailDialog(GraphJDepend display, String current, String depend) {
		super(current + " 依赖于 " + depend);

		this.frame = display.getFrame();

		for (Relation r : display.getRelations()) {
			if (r.equals(current, depend)) {
				this.relation = r;
			}
		}
		
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, this.createOperationPanel());
		this.add(BorderLayout.CENTER, new RelationDetailPanel(this.relation));

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
				calSelectedJavaClass();
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
				calSelectedJavaClass();
			}
		};
		relationPanel.add(depend);

		current.setSelected(true);

		JButton moveTo = new JButton(BundleUtil.getString(BundleUtil.Command_Move));
		moveTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JavaClassMoveToDialog d = new JavaClassMoveToDialog(frame, calSelectedJavaClass());
				d.setListener(new JavaClassMoveToDialogListener() {
					@Override
					public void onFinish() {
						RelationDetailDialog.this.dispose();
					}
				});
				d.setModal(true);
				d.setVisible(true);
			}
		});
		relationPanel.add(moveTo);

		JButton analyse = new JButton(BundleUtil.getString(BundleUtil.Command_Analyse));
		analyse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RelationDetailForMoveDialog d = new RelationDetailForMoveDialog(frame, relation);
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
				selectedJavaClass.add(item.getCurrent().getName());
			} else {
				selectedJavaClass.add(item.getDepend().getName());
			}
		}
		return selectedJavaClass;
	}
}
