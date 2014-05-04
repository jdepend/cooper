package org.wilmascope.view;

import java.awt.event.ActionListener;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author
 * @version 1.0
 */

public class ElementData {

	public ElementData() {
	}

	public ActionListener getActionListener() {
		return actionListener;
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	public void setActionDescription(String description) {
		this.actionDescription = description;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	private String actionDescription = "No Action Available";
	private ActionListener actionListener;
}
