package jdepend.client.report.way.mapui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import prefuse.visual.VisualItem;

class HideVisualItemMgr {
	
	private GraphJDepend display;

	private Map<String, HashSet<VisualItem>> hideItems = new LinkedHashMap<String, HashSet<VisualItem>>();

	private Map<String, HashSet<VisualItem>> unHideItems = new LinkedHashMap<String, HashSet<VisualItem>>();
	
	HideVisualItemMgr(GraphJDepend display) {
		super();
		this.display = display;
	}

	void addStepHideItems(HashSet<VisualItem> currentHideItems) {
		// 记录本次隐藏的显示对象
		String operationId = UUID.randomUUID().toString();
		hideItems.put(operationId, currentHideItems);
	}

	HashSet<VisualItem> getHideItems() {
		HashSet<VisualItem> hItems = new HashSet<VisualItem>();
		for (String operId : hideItems.keySet()) {
			hItems.addAll(hideItems.get(operId));
		}
		return hItems;
	}

	void reset() {
		hideItems = new LinkedHashMap<String, HashSet<VisualItem>>();
		unHideItems = new LinkedHashMap<String, HashSet<VisualItem>>();
	}

	void unStepHide() {
		if (hideItems.size() > 0) {
			String lastOperationId = null;
			for (String operationId : hideItems.keySet()) {
				lastOperationId = operationId;
			}
			unHideItems.put(lastOperationId, hideItems.get(lastOperationId));
			hideItems.remove(lastOperationId);
		}
	}

	void nextStepHide() {
		if (unHideItems.size() > 0) {
			String lastOperationId = null;
			for (String operationId : unHideItems.keySet()) {
				lastOperationId = operationId;
			}
			hideItems.put(lastOperationId, unHideItems.get(lastOperationId));
			unHideItems.remove(lastOperationId);
		}
	}

	void repaint() {

		// 计算所有被隐藏的显示对象
		HashSet<VisualItem> hItems = getHideItems();
		// 遍历所有显示对象，隐藏记录的hideItems
		Iterator iter = display.getVisualization().items();
		while (iter.hasNext()) {
			VisualItem i = (VisualItem) iter.next();
			if (hItems.contains(i)) {
				i.setVisible(false);
			} else {
				i.setVisible(true);
			}
		}
		display.getVisualization().repaint();
	}

}
