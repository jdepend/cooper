package jdepend.report.way.mapui;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import jdepend.framework.ui.util.JDependUIUtil;
import jdepend.framework.util.BundleUtil;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.layout.Layout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.controls.ControlAdapter;
import prefuse.util.ColorLib;
import prefuse.util.GraphicsLib;
import prefuse.util.display.DisplayLib;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

public class MainMenuControl extends ControlAdapter {

	private GraphJDepend display;

	private JPopupMenu popupMenu;

	private JMenuItem packageTreeItem;

	private JMenuItem changeLayouttem;

	public MainMenuControl(GraphJDepend display) {
		this.display = display;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == 3) {
			getMenus().show((Component) e.getSource(), e.getX(), e.getY());
		} else {
			super.mouseClicked(e);
		}
	}

	private JPopupMenu getMenus() {
		if (popupMenu == null) {
			popupMenu = createMenus();
		}
		return popupMenu;
	}

	private JPopupMenu createMenus() {
		final JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem zoomToFit = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ZoomToFit));
		zoomToFit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ZoomToFit();
			}
		});
		popupMenu.add(zoomToFit);

		JMenuItem showProblemRelation = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ShowProblemRelation));
		showProblemRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showProblemRelation();
			}
		});
		popupMenu.add(showProblemRelation);

		popupMenu.addSeparator();

		JMenuItem unStepHideItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_UnStepHide));
		unStepHideItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display.getHideVisualItemMgr().unStepHide();
				display.getHideVisualItemMgr().repaint();
			}
		});
		popupMenu.add(unStepHideItem);

		JMenuItem nextStepHideItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_NextStepHide));
		nextStepHideItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display.getHideVisualItemMgr().nextStepHide();
				display.getHideVisualItemMgr().repaint();
			}
		});
		popupMenu.add(nextStepHideItem);

		JMenuItem unHideItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_UnHide));
		unHideItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display.getHideVisualItemMgr().reset();
				display.getHideVisualItemMgr().repaint();
			}
		});
		popupMenu.add(unHideItem);

		popupMenu.addSeparator();

		changeLayouttem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_NodeLinkTreeLayout));
		changeLayouttem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (changeLayouttem.getText().equals(BundleUtil.getString(BundleUtil.Command_NodeLinkTreeLayout))) {
					convertLayout(new NodeLinkTreeLayout(GraphJDepend.tree));
					changeLayouttem.setText(BundleUtil.getString(BundleUtil.Command_RadialTreeLayout));
				} else {
					convertLayout(new RadialTreeLayout(GraphJDepend.tree));
					changeLayouttem.setText(BundleUtil.getString(BundleUtil.Command_NodeLinkTreeLayout));
				}
			}
		});
		popupMenu.add(changeLayouttem);

		popupMenu.addSeparator();

		if (this.display.getPrinter() != null) {
			packageTreeItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_ViewPackageTree));
			packageTreeItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					packageTree();
				}
			});
			popupMenu.add(packageTreeItem);

			popupMenu.addSeparator();
		}

		JMenuItem saveItem = new JMenuItem(BundleUtil.getString(BundleUtil.Command_SaveAsPic));
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAs();
			}
		});
		popupMenu.add(saveItem);

		return popupMenu;
	}

	private void saveAs() {
		File selectedFile = JDependUIUtil.getSelectedFile(".png", this.display);
		if (selectedFile != null) {
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(selectedFile);
				this.display.saveImage(stream, "png", 1);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void packageTree() {
		if (packageTreeItem.getText().equals(BundleUtil.getString(BundleUtil.Command_ViewPackageTree))) {
			display.getPrinter().showPackageTree();
			packageTreeItem.setText(BundleUtil.getString(BundleUtil.Command_ClosePackageTree));
		} else {
			display.getPrinter().hiddenPackageTree();
			packageTreeItem.setText(BundleUtil.getString(BundleUtil.Command_ViewPackageTree));
		}
	}

	private void showProblemRelation() {

		HashSet<VisualItem> currentHideItems = new HashSet<VisualItem>();

		Iterator iter = display.getVisualization().items();
		while (iter.hasNext()) {
			VisualItem item = (VisualItem) iter.next();
			if (item.getStrokeColor() == ColorLib.rgb(100, 200, 200)) {
				currentHideItems.add(item);
			}
		}
		Boolean isVisible;
		NodeItem item;
		EdgeItem eitem;
		Iterator it;
		Iterator nodeIter = display.getVisualization().items(GraphJDepend.treeNodes);
		while (nodeIter.hasNext()) {
			item = (NodeItem) nodeIter.next();
			it = item.edges();
			isVisible = false;
			L: while (it.hasNext()) {
				eitem = (EdgeItem) it.next();
				if (eitem.getStrokeColor() != ColorLib.rgb(100, 200, 200)) {
					isVisible = true;
					break L;
				}
			}
			if (!isVisible) {
				currentHideItems.add(item);
			}
		}

		display.getHideVisualItemMgr().addStepHideItems(currentHideItems);

		display.getHideVisualItemMgr().repaint();
	}

	private void ZoomToFit() {

		if (!display.isTranformInProgress()) {
			long m_duration = 2000;
			int m_margin = 50;
			Visualization vis = display.getVisualization();
			Rectangle2D bounds = vis.getBounds(Visualization.ALL_ITEMS);
			GraphicsLib.expand(bounds, m_margin + (int) (1 / display.getScale()));
			DisplayLib.fitViewToBounds(display, bounds, m_duration);
		}

	}

	private void convertLayout(Layout layout) {

		layout.setLayoutBounds(new Rectangle(this.display.getWidth(), this.display.getHeight()));

		Action treeLayout = this.display.getVisualization().getAction("treeLayout");
		this.display.getVisualization().putAction("treeLayout", layout);
		ActionList filter = (ActionList) this.display.getVisualization().getAction("filter");
		filter.remove(treeLayout);
		filter.add(layout);
		this.display.getVisualization().run("filter");
	}
}
