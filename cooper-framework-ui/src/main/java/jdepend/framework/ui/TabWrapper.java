package jdepend.framework.ui;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public final class TabWrapper extends JPanel {

	private Component component;

	public TabWrapper(LayoutManager layout) {
		super(layout);
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	}

	public Component getComponent() {
		return component;
	}

	@Override
	public Component add(Component comp) {
		Component component = super.add(comp);
		this.component = component;
		return comp;
	}

}