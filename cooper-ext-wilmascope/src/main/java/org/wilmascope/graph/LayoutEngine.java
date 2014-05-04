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

import java.util.Properties;

import javax.swing.JPanel;

/**
 * Classes which determine layout for a particular cluster must implement this
 * interface.
 * 
 * @author Tim Dwyer
 * @version $Id: LayoutEngine.java,v 1.15 2004/11/19 09:14:24 tgdwyer Exp $
 * 
 */
public abstract class LayoutEngine<N extends NodeLayout, E extends EdgeLayout> {
	/**
	 * calculate the changes required to move the graph to a nicer layout. This
	 * method does not actually update the position of the nodes, rather this
	 * should be done by the {@link #applyLayout} method. Potentially, this
	 * means that the caller can calculate new positions (stored in the
	 * NodeLayouts), then opt not to apply the changes if it does not lead to an
	 * improvement. In practice this has not yet been utilised and such
	 * decisions are usually made internally to the layout engine.
	 */
	public void calculateLayout() {
	};

	/**
	 * apply the changes calculated by {@link #calculateLayout}
	 * 
	 * @return true when a stable state is reached
	 */
	public abstract boolean applyLayout();

	/**
	 * Return a string descriptor for the layout engine type. Useful in GUI
	 * elements such as comboboxes
	 */
	public abstract String getName();

	/**
	 * Factory method to create a new NodeLayout implementation compatible with
	 * the layout engine implementing this interface.
	 */
	public abstract N createNodeLayout(Node n);

	/**
	 * Factory method to create a new EdgeLayout implementation compatible with
	 * the layout engine implementing this interface.
	 */
	public abstract E createEdgeLayout(Edge e);

	Properties properties;

	/**
	 * If you want custom properties for your layout you will need to override
	 * this method but be sure to super.getProperties()
	 * 
	 * @return properties of this layout
	 */
	public Properties getProperties() {
		// lazy instantiation
		if (properties == null) {
			properties = new Properties();
		}
		return properties;
	}

	public final void setProperties(Properties p) {
		this.properties = p;
		resetProperties();
	}

	/**
	 * process your custom properties by overriding this method, but, again call
	 * super.resetProperties in your overriding implementation
	 * 
	 */
	public void resetProperties() {
		if (properties == null) {
			return;
		}
	}

	public abstract JPanel getControls();

	/**
	 * The LayoutEngine should have no constructor. It should be initialised
	 * with this method.
	 */
	public void init(Cluster root) {
		this.root = root;
		NodeList nodes = root.getNodes();
		for (Node n : nodes) {
			n.setLayout(createNodeLayout(n));
		}
		EdgeList edges = root.getInternalEdges();
		for (Edge e : edges) {
			e.setLayout(createEdgeLayout(e));
		}
	}

	public Cluster getRoot() {
		return root;
	}

	private Cluster root;

	/**
	 * The iterationsPerFrameSlider should be enabled for iterative layouts
	 * (like force directed) and disabled for layouts that complete in a single
	 * step (sugiyama)
	 * 
	 * @return false by default. Override to return true if layout engine is
	 *         iterative.
	 */
	public boolean isIterative() {
		return false;
	}
}
