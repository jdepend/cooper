package jdepend.report.way.mapui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jdepend.framework.ui.JDependFrame;
import jdepend.model.JDependUnit;
import jdepend.model.JDependUnitMgr;
import jdepend.report.ui.RelationDetailDialog;
import prefuse.controls.ControlAdapter;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

public class JDependEdgeControl extends ControlAdapter {

	private GraphJDepend display;

	public JDependEdgeControl(GraphJDepend display) {
		super();
		this.display = display;
	}

	@Override
	public void itemClicked(VisualItem item, MouseEvent e) {
		if (e.getButton() == 3 && item instanceof EdgeItem) {
			JPopupMenu popupMenu = this.popupMenu((EdgeItem) item);
			popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
		} else {
			super.itemClicked(item, e);
		}
	}

	private JPopupMenu popupMenu(final EdgeItem item) {

		final NodeItem sourceItem = item.getSourceItem();
		final NodeItem targetItem = item.getTargetItem();

		final String sourceName = sourceItem.getString("label");
		final String targetName = targetItem.getString("label");
		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem viewRelation = new JMenuItem("查看关系信息");
		viewRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewRelation(sourceName, targetName);
			}
		});
		popupMenu.add(viewRelation);

		// 计算是否在循环依赖链上
		List<String> currentCycles;
		L: for (jdepend.model.Component source : JDependUnitMgr.getInstance().getComponents()) {
			// 清空收集集合
			currentCycles = new ArrayList<String>();
			// 收集循环依赖集合点
			for (JDependUnit unit : source.collectCycle()) {
				currentCycles.add(unit.getName());
			}
			if (currentCycles.contains(sourceName) && currentCycles.contains(targetName)) {
				final List<String> showCycles = new ArrayList<String>(currentCycles.size());
				for (String element : currentCycles) {
					showCycles.add(element);
				}
				JMenuItem viewCycle = new JMenuItem("显示循环依赖链");
				viewCycle.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						viewCycle(sourceItem, showCycles);
					}
				});
				popupMenu.add(viewCycle);
				break L;
			}
		}

		return popupMenu;
	}

	private void viewCycle(NodeItem sourceItem, List<String> currentCycles) {

		String itemName;
		// 遍历所有节点对象，隐藏其他节点对象和线对象
		Iterator iter = sourceItem.getVisualization().items();
		while (iter.hasNext()) {
			VisualItem item = (VisualItem) iter.next();
			if (item instanceof NodeItem) {
				itemName = item.getString("label");
				if (!currentCycles.contains(itemName)) {
					item.setVisible(false);
				} else {
					item.setVisible(true);
				}
			} else if (item instanceof EdgeItem) {
				EdgeItem edgeItem = (EdgeItem) item;
				NodeItem sItem = edgeItem.getSourceItem();
				NodeItem tItem = edgeItem.getTargetItem();
				String sourceName = sItem.getString("label");
				String targetName = tItem.getString("label");
				int sIndex = -1;
				int tIndex = -1;
				int i;

				i = 0;
				for (String element : currentCycles) {
					if (element.equals(sourceName)) {
						sIndex = i;
					}
					i++;
				}
				i = 0;
				for (String element : currentCycles) {
					if (element.equals(targetName)) {
						tIndex = i;
					}
					i++;
				}
				if (sIndex != -1 && tIndex != -1
						&& (sIndex + 1 == tIndex || sIndex == currentCycles.size() - 1 && tIndex == 0)) {
					item.setVisible(true);
				} else {
					item.setVisible(false);
				}
			}
		}
		sourceItem.getVisualization().repaint();
	}

	private void viewRelation(String source, String target) {
		RelationDetailDialog d = new RelationDetailDialog(display, source, target);
		d.setModal(true);
		d.setVisible(true);
	}
}
