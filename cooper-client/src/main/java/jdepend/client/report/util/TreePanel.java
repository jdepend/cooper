package jdepend.client.report.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jdepend.client.report.util.TreeGraphUtil.TreeView;
import prefuse.Visualization;
import prefuse.controls.ControlAdapter;
import prefuse.data.Tree;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.JSearchPanel;
import prefuse.visual.VisualItem;

public final class TreePanel extends JPanel {

	private TreeView tview;

	public TreePanel(Tree t, final String label) {

		this.setLayout(new BorderLayout());

		Color BACKGROUND = Color.WHITE;
		Color FOREGROUND = Color.BLACK;

		// create a new treemap
		tview = new TreeView(t, label);
		tview.setBackground(BACKGROUND);
		tview.setForeground(FOREGROUND);

		// create a search panel for the tree map
		JSearchPanel search = new JSearchPanel(tview.getVisualization(), TreeView.treeNodes,
				Visualization.SEARCH_ITEMS, label, true, true);
		search.setShowResultCount(true);
		search.setBorder(BorderFactory.createEmptyBorder(5, 5, 4, 0));
		search.setFont(FontLib.getFont("宋体", Font.PLAIN, 11));
		search.setBackground(BACKGROUND);
		search.setForeground(FOREGROUND);

		final JFastLabel title = new JFastLabel("                 ");
		title.setPreferredSize(new Dimension(900, 20));
		title.setVerticalAlignment(SwingConstants.BOTTOM);
		title.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		title.setFont(FontLib.getFont("宋体", Font.PLAIN, 10));
		title.setBackground(BACKGROUND);
		title.setForeground(FOREGROUND);

		tview.addControlListener(new ControlAdapter() {
			public void itemEntered(VisualItem item, MouseEvent e) {
				if (item.canGetString("path")) {
					title.setText(item.getString("path"));
				}
			}

			public void itemExited(VisualItem item, MouseEvent e) {
				title.setText(null);
			}
		});

		Box box = new Box(BoxLayout.X_AXIS);
		// box.add(Box.createHorizontalStrut(10));
		box.add(title);
		// box.add(Box.createHorizontalGlue());
		box.add(search);
		// box.add(Box.createHorizontalStrut(3));
		box.setBackground(BACKGROUND);

		this.setBackground(BACKGROUND);
		this.setForeground(FOREGROUND);
		this.add(tview, BorderLayout.CENTER);
		this.add(box, BorderLayout.SOUTH);
	}

	@Override
	public void setSize(int width, int height) {
		tview.setSize(width, height);
	}

	public void setFocus(String focus) {
		tview.setFocus(focus);
	}
}
