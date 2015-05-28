package jdepend.report.way.mapui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jdepend.framework.ui.component.JDependFrame;
import jdepend.model.Relation;
import prefuse.Visualization;
import prefuse.controls.ControlAdapter;
import prefuse.data.Table;
import prefuse.data.query.SearchQueryBinding;
import prefuse.data.search.SearchTupleSet;
import prefuse.util.FontLib;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.JSearchPanel;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;

public class GraphPanel extends JPanel {

	private JDependFrame frame;

	private GraphJDepend gview;

	private JDialog parentDialog;

	public GraphPanel(GraphPrinter printer, Collection<Relation> relations) {
		this(printer.getFrame(), null, relations);
		gview.setPrinter(printer);
	}

	public GraphPanel(JDependFrame frame, JDialog parentDialog, Collection<Relation> relations) {

		this.frame = frame;

		this.parentDialog = parentDialog;

		this.setLayout(new BorderLayout());
		// create a new radial tree view
		gview = new GraphJDepend(this, relations);
		Visualization vis = gview.getVisualization();

		// create a search panel for the tree map
		SearchQueryBinding sq = new SearchQueryBinding((Table) vis.getGroup(GraphJDepend.treeNodes), gview.getLabel(),
				(SearchTupleSet) vis.getGroup(Visualization.SEARCH_ITEMS));
		JSearchPanel search = sq.createSearchPanel();
		search.setShowResultCount(true);
		search.setBorder(BorderFactory.createEmptyBorder(5, 5, 4, 0));
		search.setFont(FontLib.getFont("宋体", Font.PLAIN, 16));

		final JFastLabel title = new JFastLabel("                 ");
		title.setPreferredSize(new Dimension(350, 20));
		title.setVerticalAlignment(SwingConstants.BOTTOM);
		title.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		title.setFont(FontLib.getFont("宋体", Font.PLAIN, 16));

		gview.addControlListener(new ControlAdapter() {
			public void itemEntered(VisualItem item, MouseEvent e) {
				if (item.canGetString("info")) {
					title.setText(item.getString("info"));
				}
			}

			public void itemExited(VisualItem item, MouseEvent e) {
				title.setText(null);
			}
		});

		Box box = new Box(BoxLayout.X_AXIS);
		box.add(title);
		// box.add(Box.createHorizontalGlue());
		box.add(search);
		// box.add(Box.createHorizontalStrut(3));

		this.add(gview, BorderLayout.CENTER);
		this.add(box, BorderLayout.SOUTH);

		Color BACKGROUND = Color.WHITE;
		Color FOREGROUND = Color.DARK_GRAY;
		UILib.setColor(this, BACKGROUND, FOREGROUND);
	}

	public JDependFrame getFrame() {
		return frame;
	}

	public JDialog getParentDialog() {
		return parentDialog;
	}
}
