package jdepend.report.way.mapui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import prefuse.Display;
import prefuse.visual.VisualItem;

public class HideVisualItemMgr {

	private static Map<String, HashSet<VisualItem>> hideItems = new LinkedHashMap<String, HashSet<VisualItem>>();

	private static Map<String, HashSet<VisualItem>> unHideItems = new LinkedHashMap<String, HashSet<VisualItem>>();

	public static void addStepHideItems(HashSet<VisualItem> currentHideItems) {
		// 记录本次隐藏的显示对象
		String operationId = UUID.randomUUID().toString();
		hideItems.put(operationId, currentHideItems);
	}

	public static HashSet<VisualItem> getHideItems() {
		HashSet<VisualItem> hItems = new HashSet<VisualItem>();
		for (String operId : hideItems.keySet()) {
			hItems.addAll(hideItems.get(operId));
		}
		return hItems;
	}

	public static void reset() {
		hideItems = new LinkedHashMap<String, HashSet<VisualItem>>();
		unHideItems = new LinkedHashMap<String, HashSet<VisualItem>>();
	}

	public static void unStepHide() {
		if (hideItems.size() > 0) {
			String lastOperationId = null;
			for (String operationId : hideItems.keySet()) {
				lastOperationId = operationId;
			}
			unHideItems.put(lastOperationId, hideItems.get(lastOperationId));
			hideItems.remove(lastOperationId);
		}
	}

	public static void nextStepHide() {
		if (unHideItems.size() > 0) {
			String lastOperationId = null;
			for (String operationId : unHideItems.keySet()) {
				lastOperationId = operationId;
			}
			hideItems.put(lastOperationId, unHideItems.get(lastOperationId));
			unHideItems.remove(lastOperationId);
		}
	}

	public static void repaint(Display display) {

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
