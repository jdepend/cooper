package jdepend.ui.framework;

import java.awt.event.MouseEvent;

import jdepend.framework.ui.panel.JClosableTabbedPane;
import jdepend.ui.JDependCooper;

public class CooperTabbedPane extends JClosableTabbedPane {

	private JDependCooper frame;

	private String state = "normal";

	private boolean doubleClick = false;

	private String postion;

	public static final String Workspace = "Workspace";
	public static final String Property = "Property";

	public CooperTabbedPane(JDependCooper frame, boolean closeEnabled, boolean doubleClick, String postion) {
		super(closeEnabled);

		this.frame = frame;
		this.doubleClick = doubleClick;

		this.postion = postion;
	}

	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());
		if (tabNumber < 0)
			return;
		
		if (this.doubleClick && e.getClickCount() == 2) {
			if ("normal".equals(state)) {
				if (Workspace.equals(postion)) {
					this.frame.maxWorkspace();
				} else if (Property.equals(postion)) {
					this.frame.maxProperty();
				}
				this.state = "max";
			} else {
				this.frame.resume();
				this.state = "normal";
			}

		}
	}
}
