package jdepend.client.report.way.mapui;

import java.awt.Font;

import prefuse.action.assignment.FontAction;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;

public class JDepnedFontAction extends FontAction {

	private String fontSizeFieldName = null;// Node字体大小field名

	private String name = null;

	public JDepnedFontAction(String group, String name, String fontSizeFieldName) {
		super(group);
		this.name = name;
		this.fontSizeFieldName = fontSizeFieldName;
	}

	@Override
	public Font getFont(VisualItem item) {
		return FontLib.getFont(this.name, item.getDouble(fontSizeFieldName));
	}

}
