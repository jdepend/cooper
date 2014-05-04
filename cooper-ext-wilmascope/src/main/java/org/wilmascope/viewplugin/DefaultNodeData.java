package org.wilmascope.viewplugin;

import org.wilmascope.view.ElementData;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author
 * @version 1.0
 */

public class DefaultNodeData extends ElementData {
	public DefaultNodeData() {
		javax.swing.JMenuItem menuItem = new javax.swing.JMenuItem();
		setActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				menuItemActionPerformed();
			}
		});
		setActionDescription("Show Details...");
	}

	public void menuItemActionPerformed() {
		DetailFrame details = new DetailFrame(this, "text/html", text);
		details.show();
	}

	public void setData(String text) {
		this.text = text;
	}

	public String getData() {
		return text;
	}

	private String text = new String();
}
