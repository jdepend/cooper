package jdepend.client.report.way.mapui;

import java.awt.event.MouseEvent;
import java.util.Iterator;

import jdepend.model.Relation;
import prefuse.controls.NeighborHighlightControl;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

public class JDependHighlightControl extends NeighborHighlightControl {

	@Override
	protected void setNeighborHighlight(NodeItem n, boolean state) {
		Iterator iter = n.edges();
		while (iter.hasNext()) {
			EdgeItem eitem = (EdgeItem) iter.next();
			NodeItem nitem = eitem.getAdjacentItem(n);
			if (eitem.isVisible() || this.isHighlightWithInvisibleEdge()) {
				// 相互依赖
				String attentionType = eitem.getTable().getString(eitem.getRow(), "attentionType");
				if (attentionType.equals(Relation.MutualDependAttentionType)) {
					eitem.getTable().setBoolean(eitem.getRow(), "mutualColor", state);
					if (nitem.isVisible()) {
						nitem.getTable().setBoolean(nitem.getRow(), "mutualColor", state);
					}
					// 传出依赖
				} else if (eitem.getSourceItem().equals(n)) {
					eitem.getTable().setBoolean(eitem.getRow(), "CeColor", state);
					if (nitem.isVisible()) {
						nitem.getTable().setBoolean(nitem.getRow(), "CeColor", state);
					}
					// 传入依赖
				} else {
					eitem.getTable().setBoolean(eitem.getRow(), "CaColor", state);
					if (nitem.isVisible()) {
						nitem.getTable().setBoolean(nitem.getRow(), "CaColor", state);
					}
				}
			}
		}
	}

	protected void setEdgeHighlight(EdgeItem e, boolean state) {
		NodeItem sourceItem = e.getSourceItem();
		NodeItem targetItem = e.getTargetItem();
		if (e.isVisible()) {
			e.setHighlighted(state);
		}
		if (sourceItem.isVisible()) {
			sourceItem.setHighlighted(state);
		}
		if (targetItem.isVisible()) {
			targetItem.setHighlighted(state);
		}
	}

	/**
	 * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem,
	 *      java.awt.event.MouseEvent)
	 */
	public void itemEntered(VisualItem item, MouseEvent e) {
		if (item instanceof NodeItem) {
			setNeighborHighlight((NodeItem) item, true);
		} else if (item instanceof EdgeItem) {
			setEdgeHighlight((EdgeItem) item, true);
		}
	}

	/**
	 * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem,
	 *      java.awt.event.MouseEvent)
	 */
	public void itemExited(VisualItem item, MouseEvent e) {
		if (item instanceof NodeItem) {
			setNeighborHighlight((NodeItem) item, false);
		} else if (item instanceof EdgeItem) {
			setEdgeHighlight((EdgeItem) item, false);
		}
	}

}
