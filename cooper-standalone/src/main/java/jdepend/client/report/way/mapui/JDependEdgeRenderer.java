package jdepend.client.report.way.mapui;

import java.awt.Graphics2D;

import prefuse.render.EdgeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class JDependEdgeRenderer extends EdgeRenderer {

	private String width = null;// 设置线宽的field名

	public JDependEdgeRenderer() {
	}

	public JDependEdgeRenderer(String width) {
		this.width = width;
	}

	@Override
	protected double getLineWidth(VisualItem item) {

		if (this.width == null) {
			return super.getLineWidth(item);
		}
		return item.getDouble(width);
	}
	
	@Override
	public void render(Graphics2D g, VisualItem item) {
		// render the edge line
		super.render(g, item);
		// render the edge arrow head, if appropriate wangdg
		if (m_curArrow != null) {
			g.setPaint(ColorLib.getColor(item.getStrokeColor()));
			g.fill(m_curArrow);
		}
	}
}
