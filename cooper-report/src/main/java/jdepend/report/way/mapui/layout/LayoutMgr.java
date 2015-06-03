package jdepend.report.way.mapui.layout;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.util.BundleUtil;
import jdepend.report.way.mapui.GraphJDepend;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;

public class LayoutMgr {

	private final static LayoutMgr inst = new LayoutMgr();

	private LayoutInfo currentLayout;

	private List<LayoutInfo> layouts;

	private LayoutMgr() {
		this.init();
	}

	private void init() {
		layouts = new ArrayList<LayoutInfo>();
		layouts.add(new LayoutInfo(BundleUtil.getString(BundleUtil.Command_RadialTreeLayout), new RadialTreeLayout(
				GraphJDepend.tree)));
		layouts.add(new LayoutInfo(BundleUtil.getString(BundleUtil.Command_NodeLinkTreeLayout), new NodeLinkTreeLayout(
				GraphJDepend.tree)));

		currentLayout = layouts.get(0);
	}

	public static LayoutMgr getInstance() {
		return inst;
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
