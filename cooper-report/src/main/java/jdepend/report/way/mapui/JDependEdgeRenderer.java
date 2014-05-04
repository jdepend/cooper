package jdepend.report.way.mapui;

import prefuse.render.EdgeRenderer;
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

}
