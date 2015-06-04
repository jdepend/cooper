package jdepend.report.way.mapui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jdepend.core.local.command.CommandAdapterMgr;
import jdepend.report.way.mapui.layout.specifiedposition.CommandSpecifiedPosition;
import jdepend.report.way.mapui.layout.specifiedposition.SpecifiedNodePosition;
import jdepend.report.way.mapui.layout.specifiedposition.SpecifiedPositionMgr;
import prefuse.controls.DragControl;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;

public class JDependDragControl extends DragControl {

	private GraphJDepend display;

	public JDependDragControl(GraphJDepend display) {
		this.display = display;
	}

	@Override
	public void itemReleased(VisualItem item, MouseEvent e) {
		super.itemReleased(item, e);

		if (this.display.getPrinter() != null) {

			List<SpecifiedNodePosition> nodePositions = new ArrayList<SpecifiedNodePosition>();
			SpecifiedNodePosition specifiedNodePosition;

			Iterator nodeIter = display.getVisualization().items(GraphJDepend.treeNodes);
			while (nodeIter.hasNext()) {
				item = (NodeItem) nodeIter.next();
				specifiedNodePosition = new SpecifiedNodePosition();
				specifiedNodePosition.setName(item.getString("label"));
				specifiedNodePosition.setX(item.getX());
				specifiedNodePosition.setY(item.getY());

				nodePositions.add(specifiedNodePosition);
			}

			CommandSpecifiedPosition commandSpecifiedPosition = new CommandSpecifiedPosition();

			String group = this.display.getPrinter().getGroup();
			String command = this.display.getPrinter().getCommand();
			commandSpecifiedPosition.setGroup(group);
			commandSpecifiedPosition.setCommand(command);
			commandSpecifiedPosition.setNodePositions(nodePositions);

			SpecifiedPositionMgr.getInstance().updateCommandSpecifiedPosition(commandSpecifiedPosition);
		}

	}
}
