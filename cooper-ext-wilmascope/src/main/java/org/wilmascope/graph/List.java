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
package org.wilmascope.graph;

import java.util.Iterator;
import java.util.Vector;

public abstract class List<T extends GraphElement> implements Iterable<T> {
	protected Vector<T> elements = new Vector<T>();

	public int size() {
		return elements.size();
	}

	/**
	 * Delete all elements in the list, removing them from the scene and
	 * references from their owner cluster's member list.
	 */
	public void delete() {
		// delete will remove the element from the owner clusters member list
		// therefore we use a temporary vector in case delete is being called
		// directly on the cluster's member lists (node or edge).
		Vector<T> tmpElements = new Vector<T>(elements);
		for (int i = 0; i < tmpElements.size(); i++) {
			tmpElements.get(i).delete();
		}
	}

	public void draw() {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).draw();
		}
	}

	protected Vector<T> getElementsVector() {
		return elements;
	}

	public void removeAll(List<T> l) {
		elements.removeAll(l.getElementsVector());
	}

	public void addAll(List<T> l) {
		elements.addAll(l.getElementsVector());
	}

	public void hide() {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).hide();
		}
	}

	public void show(org.wilmascope.view.GraphCanvas graphCanvas) {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).show(graphCanvas);
		}
	}

	public Iterator<T> iterator() {
		return elements.iterator();
	}
}
