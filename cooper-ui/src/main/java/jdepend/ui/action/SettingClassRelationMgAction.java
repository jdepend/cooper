package jdepend.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.framework.ui.PersistentBeanSettingDialog;
import jdepend.model.relationtype.JavaClassRelationTypeMgr;
import jdepend.ui.JDependCooper;

public class SettingClassRelationMgAction extends AbstractAction {

	private JDependCooper frame;

	public SettingClassRelationMgAction(JDependCooper frame) {
		super("设置类关系管理器");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		PersistentBeanSettingDialog d = new PersistentBeanSettingDialog(frame, JavaClassRelationTypeMgr.getInstance());
		d.setModal(true);
		d.setVisible(true);
	}
}
