package jdepend.client.report.way.mapui.layout;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.util.BundleUtil;
import jdepend.client.report.way.mapui.GraphJDepend;
import jdepend.client.report.way.mapui.layout.specifiedposition.SpecifiedPositionMgr;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.layout.SpecifiedLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;

public class LayoutMgr {

	private LayoutInfo currentLayout;

	private List<LayoutInfo> layouts;

	private GraphJDepend display;

	public LayoutMgr(GraphJDepend display) {

		this.display = display;

		layouts = new ArrayList<LayoutInfo>();
		layouts.add(new LayoutInfo(BundleUtil.getString(BundleUtil.Command_RadialTreeLayout), new RadialTreeLayout(
				GraphJDepend.tree)));
		layouts.add(new LayoutInfo(BundleUtil.getString(BundleUtil.Command_NodeLinkTreeLayout), new NodeLinkTreeLayout(
				GraphJDepend.tree)));

		if (display.getMapData().isHaveSpecifiedPosition()) {
			LayoutInfo specifiedLayout = new LayoutInfo(BundleUtil.getString(BundleUtil.Command_SpecifiedLayout),
					new SpecifiedLayout(GraphJDepend.tree, "xField", "yField"));
			currentLayout = specifiedLayout;
			layouts.add(specifiedLayout);
		} else {
			currentLayout = layouts.get(0);
		}
	}

	public void reset() {
		String group = this.display.getMapData().getGroup();
		String command = this.display.getMapData().getCommand();
		SpecifiedPositionMgr.getInstance().deleteTheCommandSpecifiedPosition(group, command);
		
		this.setCurrentLayout(layouts.get(0));
	}

	public LayoutInfo getCurrentLayout() {
		return currentLayout;
	}

	public void setCurrentLayout(LayoutInfo currentLayout) {
		// 切换布局管理器
		currentLayout.getLayout().setLayoutBounds(new Rectangle(this.display.getWidth(), this.display.getHeight()));
		Action treeLayout = this.display.getVisualization().getAction("treeLayout");
		this.display.getVisualization().putAction("treeLayout", currentLayout.getLayout());
		ActionList filter = (ActionList) this.display.getVisualization().getAction("filter");
		filter.remove(treeLayout);
		filter.add(currentLayout.getLayout());

		this.currentLayout = currentLayout;
	}

	public List<LayoutInfo> getLayouts() {
		return layouts;
	}

}
