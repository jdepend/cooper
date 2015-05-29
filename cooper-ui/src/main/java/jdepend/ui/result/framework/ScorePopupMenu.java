package jdepend.ui.result.framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import jdepend.core.local.score.ScoreUtil;
import jdepend.framework.exception.JDependException;
import jdepend.ui.dialog.ScoreListDialog;

public class ScorePopupMenu extends JPopupMenu {

	private JMenuItem saveItem = new JMenuItem("保存分数");
	private JMenuItem saveAllItem = new JMenuItem("保存分数和结果");
	private JMenuItem viewItem = new JMenuItem("查看分数列表");

	public ScorePopupMenu(final PopupSummaryDialog dialog) {

		this.add(saveItem);
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ScoreUtil.saveScore(ScoreUtil.OnlyScoreMode);
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(ScorePopupMenu.this, "保存失败", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.add(saveAllItem);
		saveAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ScoreUtil.saveScore(ScoreUtil.ScoreAndResult);
				} catch (JDependException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(ScorePopupMenu.this, "保存失败", "alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.addSeparator();
		this.add(viewItem);
		viewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ScoreListDialog d = new ScoreListDialog(dialog.getFrame());
				d.setModal(true);
				d.setVisible(true);
			}
		});

	}

}
