/*
 * The following source code is part of the WilmaScope 3D Graph Drawing Engine
 * which is distributed under the terms of the GNU Lesser General Public License
 * (LGPL - http://www.gnu.org/copyleft/lesser.html).
 *
 * As usual we distribute it with no warranties and anything you chose to do
 * with it you do at your own risk.
 *
 * Copyright for this work is retained by Tim Dwyer and the WilmaScope organisation
 * (www.wilmascope.org) however it may be used or modified to work as part of
 * other software subject to the terms of the LGPL.  I only ask that you cite
 * WilmaScope as an influence and inform us (tgdwyer@yahoo.com)
 * if you do anything really cool with it.
 *
 * The WilmaScope software source repository is hosted by Source Forge:
 * www.sourceforge.net/projects/wilma
 *
 * -- Tim Dwyer, 2001
 */
package org.wilmascope.control;

/**
 * This class implements a centralised listener which handles all pick events
 * for registered objects
 * @version 1.0
 */
import java.util.Stack;
import java.util.Vector;

import org.wilmascope.view.PickingClient;

class GenericPickingClient implements PickingClient {
	GenericPickingClient(PickListener pl, OptionsClient optionsClient, GraphControl.GraphElementFacade element) {
		this.optionsClient = optionsClient;
		this.element = element;
		this.pickListener = pl;
	}

	public void callback(java.awt.event.MouseEvent e) {
		if (e.isMetaDown()) {
			// right mouse button click
			if (pickListener.isOptionPickingEnabled()) {
				optionsClient.callback(e, element);
			}
		} else if (e.isAltDown()) {
			// middle mouse button click
		} else {
			pickListener.picked(element);
		}
	}

	OptionsClient optionsClient;
	GraphControl.GraphElementFacade element;
	PickListener pickListener;
}

public class PickListener {
	public PickListener() {
	}

	public void register(final GraphControl.GraphElementFacade element) {
		if (element instanceof GraphControl.Edge) {
			element.addPickingClient(new GenericPickingClient(this, edgeOptionsClient, element));
		} else if (element instanceof GraphControl.Cluster) {
			element.addPickingClient(new GenericPickingClient(this, clusterOptionsClient, element));
		} else if (element instanceof GraphControl.Node) {
			element.addPickingClient(new GenericPickingClient(this, nodeOptionsClient, element));
		}
	}

	void picked(GraphControl.GraphElementFacade element) {
		if (!isPickableType(element)) {
			return;
		}
		if (multiPickingEnabled && !pickedList.contains(element)) {
			element.highlightColour();
			pickedList.push(element);
			// if we have too many elements then pop one off the front of the
			// list
			// ie FIFO queue
			if (pickedList.size() > pickedListLimit) {
				GraphControl.GraphElementFacade head = (GraphControl.GraphElementFacade) pickedList.get(0);
				pickedList.removeElementAt(0);
				head.defaultColour();
			}
		} else if (singlePickClient != null) {
			singlePickClient.callback(element);
			singlePickClient = null;
			pickableTypes.removeAllElements();
			setOptionPickingEnabled(true);
		}
	}

	private boolean isPickableType(GraphControl.GraphElementFacade e) {
		for (int i = 0; i < pickableTypes.size(); i++) {
			Class type = (Class) pickableTypes.get(i);
			if (type.equals(e.getClass())) {
				return true;
			}
		}
		return false;
	}

	public void setPickedListLimit(int limit) {
		pickedListLimit = limit;
	}

	public int getPickedListSize() {
		return pickedList.size();
	}

	public GraphControl.GraphElementFacade pop() {
		GraphControl.GraphElementFacade e = (GraphControl.GraphElementFacade) pickedList.pop();
		e.defaultColour();
		return e;
	}

	public boolean isOptionPickingEnabled() {
		return optionPickingEnabled;
	}

	public void setOptionPickingEnabled(boolean enabled) {
		optionPickingEnabled = enabled;
	}

	boolean optionPickingEnabled = true;

	/**
	 * Enable multiple objects to be selected and added to the "picked list".
	 * 
	 * @param pickedListLimit
	 *            maximum number of objects which may be picked. Implemented as
	 *            a FIFO queue so when more than the max are selected they are
	 *            added to the end and the first removed from the list.
	 * @param pickableTypes
	 *            array of the facade classes from {@link GraphControl} example:
	 *            <p>
	 *            <blockquote>
	 * 
	 *            <pre>
	 * enableMultiPicking(2, new Class[] { GraphControl.nodeClass, GraphControl.clusterClass });
	 * </pre>
	 * 
	 *            </blockquote>
	 */
	public void enableMultiPicking(int pickedListLimit, Class pickableTypes[]) {
		addPickableTypes(pickableTypes);
		enableMultiPicking(pickedListLimit);
	}

	public void enableMultiPicking(int pickedListLimit, Class pickableType) {
		addPickableType(pickableType);
		enableMultiPicking(pickedListLimit);
	}

	public void enableMultiPicking(int pickedListLimit) {
		this.pickedListLimit = pickedListLimit;
		multiPickingEnabled = true;
		setOptionPickingEnabled(false);
	}

	public void disableMultiPicking() {
		multiPickingEnabled = false;
		pickableTypes.removeAllElements();
		setOptionPickingEnabled(true);
	}

	private void addPickableTypes(Class pickableTypes[]) {
		for (int i = 0; i < pickableTypes.length; i++) {
			addPickableType(pickableTypes[i]);
		}
	}

	private void addPickableType(Class pickableType) {
		if (!GraphControl.graphElementClass.isAssignableFrom(pickableType)) {
			throw new Error("Not a pickable Type: " + pickableType);
		}
		pickableTypes.add(pickableType);
	}

	public void setNodeOptionsClient(OptionsClient optionsClient) {
		this.nodeOptionsClient = optionsClient;
	}

	public void setEdgeOptionsClient(OptionsClient optionsClient) {
		this.edgeOptionsClient = optionsClient;
	}

	public void setClusterOptionsClient(OptionsClient optionsClient) {
		this.clusterOptionsClient = optionsClient;
	}

	public void setSinglePickClient(PickClient client, Class pickableType) {
		addPickableType(pickableType);
		this.singlePickClient = client;
		setOptionPickingEnabled(false);
	}

	public void setSinglePickClient(PickClient client, Class pickableTypes[]) {
		addPickableTypes(pickableTypes);
		this.singlePickClient = client;
		setOptionPickingEnabled(false);
	}

	private OptionsClient nodeOptionsClient;
	private OptionsClient edgeOptionsClient;
	private OptionsClient clusterOptionsClient;
	private PickClient singlePickClient;
	private Stack pickedList = new Stack();
	private boolean multiPickingEnabled = false;
	private int pickedListLimit = 0;
	private Vector pickableTypes = new Vector();
}
