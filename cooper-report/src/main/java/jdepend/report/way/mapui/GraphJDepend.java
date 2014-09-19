package jdepend.report.way.mapui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jdepend.model.Element;
import jdepend.model.Relation;
import jdepend.report.filter.RelationFilter;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.GroupAction;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.HoverActionControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.query.SearchQueryBinding;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.search.SearchTupleSet;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.JSearchPanel;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * Demonstration of a node-link tree viewer
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GraphJDepend extends Display {

	private RelationFilter filter = new RelationFilter();

	public static final String tree = "tree";
	public static final String treeNodes = "tree.nodes";
	public static final String treeEdges = "tree.edges";
	public static final String linear = "linear";

	private LabelRenderer m_nodeRenderer;
	private JDependEdgeRenderer m_edgeRenderer;

	private String label;

	private Map<String, Element> elementForNames;

	public GraphJDepend(Collection<Relation> relations) {
		super(new Visualization());

		// 重置hideVisualItem
		HideVisualItemMgr.reset();

		Collection<Element> elements = Relation.calElements(relations);
		elementForNames = new HashMap<String, Element>();
		for (Element element : elements) {
			elementForNames.put(element.getName(), element);
		}
		Graph g = CovertorUtil.getGraph(relations, elements);

		this.label = "label";
		// initialize the display
		// setSize(1950, 1750);
		this.setSize(g);

		// -- set up visualization --
		m_vis.add(tree, g);
		// m_vis.setInteractive(treeEdges, null, false);

		// -- set up renderers --
		m_nodeRenderer = new LabelRenderer(label);
		m_nodeRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
		m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
		m_nodeRenderer.setRoundedCorner(8, 8);

		if (this.filter.isRelationIntensity())
			m_edgeRenderer = new JDependEdgeRenderer("weight");
		else
			m_edgeRenderer = new JDependEdgeRenderer();

		DefaultRendererFactory rf = new DefaultRendererFactory(m_nodeRenderer);
		rf.add(new InGroupPredicate(treeEdges), m_edgeRenderer);
		m_vis.setRendererFactory(rf);

		// -- set up processing actions --

		// colors
		ItemAction nodeColor = new NodeColorAction(treeNodes);
		ItemAction textColor = new TextColorAction(treeNodes);
		m_vis.putAction("textColor", textColor);

		ItemAction edgeColor = new EdgeColorAction(treeEdges);

		FontAction fonts = null;
		if (this.filter.isElementIntensity())
			fonts = new JDepnedFontAction(treeNodes, "Tahoma", "size");
		else
			fonts = new FontAction(treeNodes, FontLib.getFont("Tahoma", 12));

		fonts.add("ingroup('_focus_')", FontLib.getFont("Tahoma", 13));

		// recolor
		ActionList recolor = new ActionList();
		recolor.add(nodeColor);
		recolor.add(textColor);
		recolor.add(edgeColor);
		m_vis.putAction("recolor", recolor);

		// repaint
		ActionList repaint = new ActionList();
		repaint.add(recolor);
		repaint.add(new RepaintAction());
		m_vis.putAction("repaint", repaint);

		// animate paint change
		ActionList animatePaint = new ActionList(400);
		animatePaint.add(new ColorAnimator(treeNodes));
		animatePaint.add(new RepaintAction());
		m_vis.putAction("animatePaint", animatePaint);

		// create the tree layout action
		RadialTreeLayout treeLayout = new RadialTreeLayout(tree);
		// treeLayout.setAngularBounds(Math.PI/2, Math.PI * 2);
		treeLayout.setLayoutBounds(new Rectangle(this.getWidth(), this.getHeight()));

		m_vis.putAction("treeLayout", treeLayout);

		CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(tree);
		m_vis.putAction("subLayout", subLayout);

		// create the filtering and layout
		ActionList filter = new ActionList();
		filter.add(new TreeRootAction(tree));
		filter.add(fonts);
		filter.add(treeLayout);
		filter.add(subLayout);
		filter.add(textColor);
		filter.add(nodeColor);
		filter.add(edgeColor);
		m_vis.putAction("filter", filter);

		// animated transition
		ActionList animate = new ActionList(1250);
		animate.setPacingFunction(new SlowInSlowOutPacer());
		animate.add(new QualityControlAnimator());
		animate.add(new VisibilityAnimator(tree));
		animate.add(new PolarLocationAnimator(treeNodes, linear));
		animate.add(new ColorAnimator(treeNodes));
		animate.add(new RepaintAction());
		m_vis.putAction("animate", animate);
		m_vis.alwaysRunAfter("filter", "animate");

		// ------------------------------------------------

		setItemSorter(new TreeDepthItemSorter());
		addControlListener(new DragControl());
		// addControlListener(new ZoomToFitControl());
		addControlListener(new WheelZoomControl());
		addControlListener(new PanControl());
		if (this.filter.isMoveCenter()) {
			addControlListener(new FocusControl(1, "filter"));
		}
		addControlListener(new HoverActionControl("repaint"));
		addControlListener(new JDependHighlightControl());
		addControlListener(new JDependEdgeControl());
		addControlListener(new JDependNodeControl(this));
		addControlListener(new MainMenuControl(this));

		// ------------------------------------------------

		// filter graph and perform layout
		m_vis.run("filter");

		// maintain a set of items that should be interpolated linearly
		// this isn't absolutely necessary, but makes the animations nicer
		// the PolarLocationAnimator should read this set and act accordingly
		m_vis.addFocusGroup(linear, new DefaultTupleSet());
		m_vis.getGroup(Visualization.FOCUS_ITEMS).addTupleSetListener(new TupleSetListener() {
			public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
				TupleSet linearInterp = m_vis.getGroup(linear);
				if (add.length < 1)
					return;
				linearInterp.clear();
				for (Node n = (Node) add[0]; n != null; n = n.getParent())
					linearInterp.addTuple(n);
			}
		});

		PrefixSearchTupleSet search = new PrefixSearchTupleSet() {
			@Override
			public void index(Tuple t, String field) {
				try {
					String s;
					if (field == null || t == null || (s = t.getString(field)) == null)
						return;
					StringTokenizer st = new StringTokenizer(s, "\\.");
					while (st.hasMoreTokens()) {
						String tok = st.nextToken();

						Method m = Class.forName("prefuse.data.search.PrefixSearchTupleSet").getDeclaredMethod(
								"addString", String.class, Tuple.class);
						m.setAccessible(true);
						m.invoke(this, tok + s.substring(s.indexOf(tok) + tok.length()), t);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
		search.addTupleSetListener(new TupleSetListener() {
			public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
				m_vis.cancel("animatePaint");
				m_vis.run("recolor");
				m_vis.run("animatePaint");
			}
		});
	}

	private void setSize(Graph g) {
		int nodes = g.getNodes().getTupleCount();
		int minWidth = 600;
		int minHeight = 450;
		int maxWidth = 2175;
		int maxHeight = 1570;
		if (nodes * 70 < minWidth) {
			this.setSize(minWidth, minHeight);
		} else if (nodes * 70 > maxWidth) {
			this.setSize(maxWidth, maxHeight);
		} else {
			this.setSize(nodes * 70, nodes * 55);
		}

	}

	public String getLabel() {
		return label;
	}

	public Element getTheElement(String name) {
		return this.elementForNames.get(name);
	}

	// ------------------------------------------------------------------------

	/**
	 * Switch the root of the tree by requesting a new spanning tree at the
	 * desired root
	 */
	public static class TreeRootAction extends GroupAction {
		public TreeRootAction(String graphGroup) {
			super(graphGroup);
		}

		public void run(double frac) {
			TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
			if (focus == null || focus.getTupleCount() == 0)
				return;

			Graph g = (Graph) m_vis.getGroup(m_group);
			Node f = null;
			Iterator tuples = focus.tuples();
			while (tuples.hasNext() && !g.containsTuple(f = (Node) tuples.next())) {
				f = null;
			}
			if (f == null)
				return;
			g.getSpanningTree(f);
		}
	}

	/**
	 * Set node fill colors
	 */
	public static class NodeColorAction extends ColorAction {
		public NodeColorAction(String group) {
			super(group, VisualItem.FILLCOLOR, ColorLib.rgba(255, 255, 255, 0));
			add(VisualItem.HOVER, ColorLib.rgb(255, 200, 125));
			add("ingroup('_search_')", ColorLib.rgb(255, 190, 190));
			add("ingroup('_focus_')", ColorLib.rgb(198, 229, 229));
			add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 125));
			add("CaColor", ColorLib.rgb(0, 255, 0));
			add("CeColor", ColorLib.rgb(137, 0, 255));
			add("mutualColor", ColorLib.rgb(140, 140, 140));
		}

	} // end of inner class NodeColorAction

	/**
	 * Set node text colors
	 */
	public static class TextColorAction extends ColorAction {
		public TextColorAction(String group) {
			super(group, VisualItem.TEXTCOLOR, ColorLib.gray(0));
			add(VisualItem.HOVER, ColorLib.rgb(255, 0, 0));
		}

		@Override
		public int getColor(VisualItem item) {
			Object o = lookup(item);
			if (o != null) {
				if (o instanceof ColorAction) {
					return ((ColorAction) o).getColor(item);
				} else if (o instanceof Integer) {
					return ((Integer) o).intValue();
				} else {
					Logger.getLogger(this.getClass().getName()).warning("Unrecognized Object from predicate chain.");
				}
			}
			// 处理外部分析单元颜色
			if (item.getTable().getBoolean(item.getRow(), "isInner")) {
				return this.getDefaultColor();
			} else {
				return ColorLib.gray(150);
			}

		}
	} // end of inner class TextColorAction

	/**
	 * Set edge colors
	 */
	public static class EdgeColorAction extends ColorAction {
		public EdgeColorAction(String group) {
			super(group, VisualItem.STROKECOLOR, ColorLib.rgb(100, 200, 200));
			add(VisualItem.HOVER, ColorLib.rgb(255, 200, 125));
			add(VisualItem.HIGHLIGHT, ColorLib.rgb(255, 200, 125));
			add("CaColor", ColorLib.rgb(0, 255, 0));
			add("CeColor", ColorLib.rgb(137, 0, 255));
			add("mutualColor", ColorLib.rgb(140, 140, 140));
		}

		@Override
		public int getColor(VisualItem item) {
			Object o = lookup(item);
			if (o != null) {
				if (o instanceof ColorAction) {
					return ((ColorAction) o).getColor(item);
				} else if (o instanceof Integer) {
					return ((Integer) o).intValue();
				} else {
					Logger.getLogger(this.getClass().getName()).warning("Unrecognized Object from predicate chain.");
				}
			}
			// 处理关注级别
			int attentionLevel = item.getTable().getInt(item.getRow(), "attentionLevel");
			if (attentionLevel == Relation.MutualDependAttentionType) {
				return ColorLib.rgba(255, 0, 0, 255);
			} else if (attentionLevel == Relation.ComponentLayerAttentionType) {
				return ColorLib.rgba(255, 0, 0, 120);
			} else if (attentionLevel == Relation.SDPAttentionType) {
				return ColorLib.rgba(255, 0, 0, 80);
			} else if (attentionLevel == Relation.CycleDependAttentionType) {
				return ColorLib.rgba(255, 0, 0, 40);
			} else {
				return this.getDefaultColor();
			}

		}
	} // end of inner class TextColorAction

} // end of class RadialGraphView
