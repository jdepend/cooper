package jdepend.client.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import jdepend.framework.ui.dialog.PersistentBeanSettingDialog;
import jdepend.metadata.relationtype.JavaClassRelationTypeMgr;
import jdepend.client.ui.JDependCooper;

public class SettingClassRelationMgrAction extends AbstractAction {

	private JDependCooper frame;

	public SettingClassRelationMgrAction(JDependCooper frame) {
		super("设置类关系管理器");
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		PersistentBeanSettingDialog d = new PersistentBeanSettingDialog(frame, JavaClassRelationTypeMgr.getInstance());
		d.setModal(true);
		d.setVisible(true);
	}
}
