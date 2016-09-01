package jdepend.client.ui.command;

import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ToolTipManager;

import jdepend.client.core.command.CommandAdapterMgr;
import jdepend.client.ui.JDependCooper;
import jdepend.framework.exception.JDependException;

public class CommandList extends JList {

	private String group;

	private JDependCooper frame;

	public CommandList(ListModel model, JDependCooper frame) {
		super(model);
		ToolTipManager.sharedInstance().registerComponent(this);

		this.frame = frame;
	}

	@Override
	public String getToolTipText(MouseEvent e) {

		ListModel model = this.getModel();

		int index = this.locationToIndex(e.getPoint());
		if (index >= 0) {
			String currentCommand = (String) model.getElementAt(index);
			try {
				return CommandAdapterMgr.getInstance().getCommands(this.group).get(currentCommand).getTip();
			} catch (JDependException e1) {
				e1.printStackTrace();
				frame.showStatusError(e1.getMessage());
				return null;
			}
		}
		return null;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
