package jdepend.client.report.util;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import jdepend.metadata.tree.Node;
import jdepend.client.report.way.mapui.JDepnedFontAction;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.LocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.filter.FisheyeTreeFilter;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.FocusControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Tree;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;
import prefuse.visual.tuple.TableNodeItem;

public final class TreeGraphUtil {

	private prefuse.data.Node node;

	public TreePanel createTree(Node root) {
		return TreeView.getTree(convertTree(root));
	}

	private prefuse.data.Tree convertTree(Node root) {

		prefuse.data.Tree pTree = new prefuse.data.Tree();
		pTree.getNodeTable().addColumn("name", String.class);
		pTree.getNodeTable().addColumn("path", String.class);
		pTree.getNodeTable().addColumn("size", double.class);

		node = pTree.addRoot();
		node.set("name", root.getName());
		node.set("path", root.getPath());
		node.set("size", new Double(root.getSize()));

		buildTree(root, pTree);

		return pTree;
	}

	private void buildTree(Node current, prefuse.data.Tree pTree) {
		if (current.getChildren() == null || current.getChildren().size() == 0) {
			node = node.getParent();
		} else {
			for (Node child : current.getChildren()) {
				node = pTree.addChild(node);
				node.set("name", child.getName());
				node.set("path", child.getPath());
				node.set("size", new Double(child.getSize()));
				buildTree(child, pTree);
			}
			node = node.getParent();
		}
	}

	public static class TreeView extends Display {

		public static final String tree = "tree";
		public static final String treeNodes = "tree.nodes";
		public static final String treeEdges = "tree.edges";

		private LabelRenderer m_nodeRenderer;
		private EdgeRenderer m_edgeRenderer;

		private String m_label = "label";
		private int m_orientation = Constants.ORIENT_LEFT_RIGHT;

		public TreeView(Tree t, String label) {
			super(new Visualization());
			m_label = label;

			m_vis.add(tree, t);

			m_nodeRenderer = new LabelRenderer(m_label);
			m_nodeRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
			m_nodeRenderer.setHorizontalAlignment(Constants.LEFT);
			m_nodeRenderer.setRoundedCorner(8, 8);
			m_edgeRenderer = new EdgeRenderer(Constants.EDGE_TYPE_CURVE);

			DefaultRendererFactory rf = new DefaultRendererFactory(m_nodeRenderer);
			rf.add(new InGroupPredicate(treeEdges), m_edgeRenderer);
			m_vis.setRendererFactory(rf);

			JDepnedFontAction fonts = new JDepnedFontAction(treeNodes, "Tahoma", "size");

			// colors
			ItemAction nodeColor = new NodeColorAction(treeNodes);
			ItemAction textColor = new ColorAction(treeNodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0));
			m_vis.putAction("textColor", textColor);

			ItemAction edgeColor = new ColorAction(treeEdges, VisualItem.STROKECOLOR, ColorLib.rgb(200, 200, 200));

			// quick repaint
			ActionList repaint = new ActionList();
			repaint.add(nodeColor);
			repaint.add(new RepaintAction());
			m_vis.putAction("repaint", repaint);

			// full paint
			ActionList fullPaint = new ActionList();
			fullPaint.add(nodeColor);
			m_vis.putAction("fullPaint", fullPaint);

			// animate paint change
			ActionList animatePaint = new ActionList(400);
			animatePaint.add(new ColorAnimator(treeNodes));
			animatePaint.add(new RepaintAction());
			m_vis.putAction("animatePaint", animatePaint);

			// create the tree layout action
			NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout(tree, m_orientation, 50, 0, 8);
			treeLayout.setLayoutAnchor(new Point2D.Double(25, 300));
			m_vis.putAction("treeLayout", treeLayout);

			CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(tree, m_orientation);
			m_vis.putAction("subLayout", subLayout);

			AutoPanAction autoPan = new AutoPanAction();

			// create the filtering and layout
			ActionList filter = new ActionList();
			filter.add(new FisheyeTreeFilter(tree, 2));
			filter.add(new FontAction(treeNodes, FontLib.getFont("Tahoma", 16)));
			filter.add(treeLayout);
			filter.add(subLayout);
			filter.add(fonts);
			filter.add(textColor);
			filter.add(nodeColor);
			filter.add(edgeColor);
			m_vis.putAction("filter", filter);

			// animated transition
			ActionList animate = new ActionList(1000);
			animate.setPacingFunction(new SlowInSlowOutPacer());
			animate.add(autoPan);
			animate.add(new QualityControlAnimator());
			animate.add(new VisibilityAnimator(tree));
			animate.add(new LocationAnimator(treeNodes));
			animate.add(new ColorAnimator(treeNodes));
			animate.add(new RepaintAction());
			m_vis.putAction("animate", animate);
			m_vis.alwaysRunAfter("filter", "animate");

			// create animator for orientation changes
			ActionList orient = new ActionList(2000);
			orient.setPacingFunction(new SlowInSlowOutPacer());
			orient.add(autoPan);
			orient.add(new QualityControlAnimator());
			orient.add(new LocationAnimator(treeNodes));
			orient.add(new RepaintAction());
			m_vis.putAction("orient", orient);

			// ------------------------------------------------

			// initialize the display
			setSize(700, 600);
			setItemSorter(new TreeDepthItemSorter());
			addControlListener(new ZoomToFitControl());
			addControlListener(new ZoomControl());
			addControlListener(new WheelZoomControl());
			addControlListener(new PanControl());
			addControlListener(new FocusControl(1, "filter"));

			registerKeyboardAction(new OrientAction(Constants.ORIENT_LEFT_RIGHT), "left-to-right", KeyStroke
					.getKeyStroke("ctrl 1"), WHEN_FOCUSED);
			registerKeyboardAction(new OrientAction(Constants.ORIENT_TOP_BOTTOM), "top-to-bottom", KeyStroke
					.getKeyStroke("ctrl 2"), WHEN_FOCUSED);
			registerKeyboardAction(new OrientAction(Constants.ORIENT_RIGHT_LEFT), "right-to-left", KeyStroke
					.getKeyStroke("ctrl 3"), WHEN_FOCUSED);
			registerKeyboardAction(new OrientAction(Constants.ORIENT_BOTTOM_TOP), "bottom-to-top", KeyStroke
					.getKeyStroke("ctrl 4"), WHEN_FOCUSED);

			// ------------------------------------------------

			// filter graph and perform layout
			setOrientation(m_orientation);
			m_vis.run("filter");

			TupleSet search = new PrefixSearchTupleSet();
			m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
			search.addTupleSetListener(new TupleSetListener() {
				public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
					m_vis.cancel("animatePaint");
					m_vis.run("fullPaint");
					m_vis.run("animatePaint");
				}
			});
		}

		// ------------------------------------------------------------------------

		public void setOrientation(int orientation) {
			NodeLinkTreeLayout rtl = (NodeLinkTreeLayout) m_vis.getAction("treeLayout");
			CollapsedSubtreeLayout stl = (CollapsedSubtreeLayout) m_vis.getAction("subLayout");
			switch (orientation) {
			case Constants.ORIENT_LEFT_RIGHT:
				m_nodeRenderer.setHorizontalAlignment(Constants.LEFT);
				m_edgeRenderer.setHorizontalAlignment1(Constants.RIGHT);
				m_edgeRenderer.setHorizontalAlignment2(Constants.LEFT);
				m_edgeRenderer.setVerticalAlignment1(Constants.CENTER);
				m_edgeRenderer.setVerticalAlignment2(Constants.CENTER);
				break;
			case Constants.ORIENT_RIGHT_LEFT:
				m_nodeRenderer.setHorizontalAlignment(Constants.RIGHT);
				m_edgeRenderer.setHorizontalAlignment1(Constants.LEFT);
				m_edgeRenderer.setHorizontalAlignment2(Constants.RIGHT);
				m_edgeRenderer.setVerticalAlignment1(Constants.CENTER);
				m_edgeRenderer.setVerticalAlignment2(Constants.CENTER);
				break;
			case Constants.ORIENT_TOP_BOTTOM:
				m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
				m_edgeRenderer.setHorizontalAlignment1(Constants.CENTER);
				m_edgeRenderer.setHorizontalAlignment2(Constants.CENTER);
				m_edgeRenderer.setVerticalAlignment1(Constants.BOTTOM);
				m_edgeRenderer.setVerticalAlignment2(Constants.TOP);
				break;
			case Constants.ORIENT_BOTTOM_TOP:
				m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
				m_edgeRenderer.setHorizontalAlignment1(Constants.CENTER);
				m_edgeRenderer.setHorizontalAlignment2(Constants.CENTER);
				m_edgeRenderer.setVerticalAlignment1(Constants.TOP);
				m_edgeRenderer.setVerticalAlignment2(Constants.BOTTOM);
				break;
			default:
				throw new IllegalArgumentException("Unrecognized orientation value: " + orientation);
			}
			m_orientation = orientation;
			rtl.setOrientation(orientation);
			stl.setOrientation(orientation);
		}

		public int getOrientation() {
			return m_orientation;
		}

		void setFocus(String focus) {
			Iterator iter = m_vis.items();
			while (iter.hasNext()) {
				VisualItem item = (VisualItem) iter.next();
				if (item instanceof TableNodeItem && focus.equals(item.getString("name"))) {
					TupleSet ts = m_vis.getFocusGroup(Visualization.FOCUS_ITEMS);
					ts.setTuple(item);
					m_vis.run("filter");
					break;
				}
			}

		}

		// ------------------------------------------------------------------------

		public static TreePanel getTree(prefuse.data.Tree tree) {
			return new TreePanel(tree, "name");
		}

		// ------------------------------------------------------------------------

		public class OrientAction extends AbstractAction {
			private int orientation;

			public OrientAction(int orientation) {
				this.orientation = orientation;
			}

			public void actionPerformed(ActionEvent evt) {
				setOrientation(orientation);
				getVisualization().cancel("orient");
				getVisualization().run("treeLayout");
				getVisualization().run("orient");
			}
		}

		public class AutoPanAction extends Action {
			private Point2D m_start = new Point2D.Double();
			private Point2D m_end = new Point2D.Double();
			private Point2D m_cur = new Point2D.Double();
			private int m_bias = 150;

			public void run(double frac) {
				TupleSet ts = m_vis.getFocusGroup(Visualization.FOCUS_ITEMS);
				if (ts.getTupleCount() == 0)
					return;

				if (frac == 0.0) {
					int xbias = 0, ybias = 0;
					switch (m_orientation) {
					case Constants.ORIENT_LEFT_RIGHT:
						xbias = m_bias;
						break;
					case Constants.ORIENT_RIGHT_LEFT:
						xbias = -m_bias;
						break;
					case Constants.ORIENT_TOP_BOTTOM:
						ybias = m_bias;
						break;
					case Constants.ORIENT_BOTTOM_TOP:
						ybias = -m_bias;
						break;
					}

					VisualItem vi = (VisualItem) ts.tuples().next();
					m_cur.setLocation(getWidth() / 2, getHeight() / 2);
					getAbsoluteCoordinate(m_cur, m_start);
					m_end.setLocation(vi.getX() + xbias, vi.getY() + ybias);
				} else {
					m_cur.setLocation(m_start.getX() + frac * (m_end.getX() - m_start.getX()), m_start.getY() + frac
							* (m_end.getY() - m_start.getY()));
					panToAbs(m_cur);
				}
			}
		}

	} // end of class TreeMap

	public static class NodeColorAction extends ColorAction {

		public NodeColorAction(String group) {
			super(group, VisualItem.FILLCOLOR);
		}

		public int getColor(VisualItem item) {
			if (m_vis.isInGroup(item, Visualization.SEARCH_ITEMS))
				return ColorLib.rgb(255, 190, 190);
			else if (m_vis.isInGroup(item, Visualization.FOCUS_ITEMS))
				return ColorLib.rgb(198, 229, 229);
			else if (item.getDOI() > -1)
				return ColorLib.rgb(164, 193, 193);
			else
				return ColorLib.rgba(255, 255, 255, 0);
		}

	} // end of inner class TreeMapColorAction

}
