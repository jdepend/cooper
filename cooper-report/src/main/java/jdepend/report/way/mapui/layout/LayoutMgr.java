package jdepend.report.way.mapui.layout;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.util.BundleUtil;
import jdepend.report.way.mapui.GraphJDepend;
import jdepend.report.way.mapui.layout.specifiedposition.CommandSpecifiedPosition;
import jdepend.report.way.mapui.layout.specifiedposition.SpecifiedPositionMgr;
import prefuse.action.layout.SpecifiedLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;

public class LayoutMgr {

	private final static LayoutMgr inst = new LayoutMgr();

	private LayoutInfo currentLayout;

	private List<LayoutInfo> layouts;

	private LayoutMgr() {
	}

	public static LayoutMgr getInstance() {
		return inst;
	}

	public void reset(String group, String command) {

		layouts = new ArrayList<LayoutInfo>();
		layouts.add(new LayoutInfo(BundleUtil.getString(BundleUtil.Command_RadialTreeLayout), new RadialTreeLayout(
				GraphJDepend.tree)));
		layouts.add(new LayoutInfo(BundleUtil.getString(BundleUtil.Command_NodeLinkTreeLayout), new NodeLinkTreeLayout(
				GraphJDepend.tree)));

		if (SpecifiedPositionMgr.getInstance().isHaveCommandSpecifiedPosition(group, command)) {
			LayoutInfo specifiedLayout = new LayoutInfo(BundleUtil.getString(BundleUtil.Command_SpecifiedLayout),
					new SpecifiedLayout(GraphJDepend.tree, "xField", "yField"));
			currentLayout = specifiedLayout;
			layouts.add(specifiedLayout);
		} else {
			currentLayout = layouts.get(0);
		}
	}

	public LayoutInfo getCurrentLayout() {
		return currentLayout;
	}

	public void setCurrentLayout(LayoutInfo currentLayout) {
		this.currentLayout = currentLayout;
	}

	public List<LayoutInfo> getLayouts() {
		return layouts;
	}

}
