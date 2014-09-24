package jdepend.report.way.mapui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jdepend.report.ui.ClassListDialog;
import prefuse.controls.ControlAdapter;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
import prefuse.visual.tuple.TableNodeItem;

public class JDependNodeControl extends ControlAdapter {

	private GraphJDepend display;

	public JDependNodeControl(GraphJDepend display) {
		this.display = display;
	}

	@Override
	public void itemClicked(VisualItem item, MouseEvent e) {
		if (e.getButton() == 3 && item instanceof NodeItem) {
			JPopupMenu popupMenu = this.popupMenu((NodeItem) item);
			popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
		} else {
			super.itemClicked(item, e);
		}
	}

	@Override
	public void itemEntered(VisualItem item, MouseEvent e) {
		super.itemEntered(item, e);
		if (item instanceof TableNodeItem && display.getPrinter() != null
				&& display.getPrinter().isPackageTreeVisible()) {
			String itemName = item.getString("label");
			display.getPrinter().setPackageTreeFocus(itemName);
		}
	}

	private JPopupMenu popupMenu(final NodeItem item) {
		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem viewRelation = new JMenuItem("查看类列表");
		viewRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nodeName = item.getString("label");
				showClassList(nodeName);
			}
		});
		popupMenu.add(viewRelation);

		popupMenu.addSeparator();

		JMenuItem hideSelfItem = new JMenuItem("隐藏自己");
		hideSelfItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideSelf(item);
			}
		});
		popupMenu.add(hideSelfItem);

		JMenuItem hidePartOthersItem = new JMenuItem("隐藏其它组件");
		hidePartOthersItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideOthers(item, false);
			}
		});
		popupMenu.add(hidePartOthersItem);

		JMenuItem hideOthersItem = new JMenuItem("隐藏其它组件和关系");
		hideOthersItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideOthers(item, true);
			}
		});
		popupMenu.add(hideOthersItem);

		return popupMenu;
	}

	private void showClassList(String source) {
		ClassListDialog d = new ClassListDialog(display.getFrame(), this.display.getTheElement(source).getComponent());
		d.setModal(true);
		d.setVisible(true);

	}

	private void hideSelf(NodeItem item) {
		// 收集需要隐藏的显示对象
		HashSet<VisualItem> currentHideItems = new HashSet<VisualItem>();
		currentHideItems.add(item);
		Iterator iter = item.edges();
		while (iter.hasNext()) {
			EdgeItem eitem = (EdgeItem) iter.next();
			currentHideItems.add(eitem);
		}

		this.hide(currentHideItems);
	}

	private void hideOthers(final NodeItem item, boolean hideOtherNodeEdges) {

		HashSet<VisualItem> currentHideItems = new HashSet<VisualItem>();
		// 收集需要隐藏的显示对象
		Collection<NodeItem> nodeItems = new HashSet<NodeItem>();
		Collection<EdgeItem> edgeItems = new HashSet<EdgeItem>();

		nodeItems.add(item);
		Iterator iter = item.edges();
		while (iter.hasNext()) {
			EdgeItem eitem = (EdgeItem) iter.next();
			edgeItems.add(eitem);
			nodeItems.add(eitem.getAdjacentItem(item));
		}

		Iterator iter1 = item.getVisualization().items();
		while (iter1.hasNext()) {
			VisualItem i = (VisualItem) iter1.next();
			if (!i.equals(item)) {
				if (hideOtherNodeEdges) {
					if (!nodeItems.contains(i) && !edgeItems.contains(i)) {
						currentHideItems.add(i);
					}
				} else {
					if (i instanceof EdgeItem) {
						EdgeItem eitem = (EdgeItem) i;
						if (!nodeItems.contains(eitem.getTargetItem()) || !nodeItems.contains(eitem.getSourceNode())) {
							currentHideItems.add(i);
						}
					} else {
						if (!nodeItems.contains(i)) {
							currentHideItems.add(i);
						}
					}
				}
			}
		}

		this.hide(currentHideItems);
	}

	private void hide(HashSet<VisualItem> currentHideItems) {
		// 记录本次隐藏的显示对象
		HideVisualItemMgr.addStepHideItems(currentHideItems);

		HideVisualItemMgr.repaint(this.display);
	}
}
