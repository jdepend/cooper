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
package org.wilmascope.forcelayout;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JPanel;
import javax.vecmath.Vector3f;

import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.forcelayout.ForceManager.UnknownForceTypeException;
import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.EdgeList;
import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeList;

/**
 * Main class force for calculating forces on all nodes and moving them
 * incrementally.
 */

public class ForceLayout extends LayoutEngine<NodeForceLayout, EdgeForceLayout> {

	public void calculateLayout() {
		// Calculate the force on each node for each of our forces
		for (int j = 0; j < forces.size(); j++) {
			Force f = (Force) forces.get(j);
			f.calculate();
		}
	}

	float cool = 1f;

	public void reset() {
		cool = 1f;
	}

	public boolean applyLayout() {
		NodeForceLayout nodeLayout;
		// reposition nodes, calculating maxNetForce as you go
		float maxNetForce = 0;
		float currentNetForce = 0;
		NodeList nodes = getRoot().getNodes();
		EdgeList edges = getRoot().getInternalEdges();
		for (Node n : nodes) {
			nodeLayout = (NodeForceLayout) (n.getLayout());
			nodeLayout.getNetForce().scale(cool);
			// cool*=0.99;
			currentNetForce = nodeLayout.getNetForce().length();
			if (currentNetForce > maxNetForce) {
				maxNetForce = currentNetForce;
			}
			nodeLayout.applyForce(velocityAttenuation);
		}
		for (int i = 0; i < edges.size(); i++) {
			edges.get(i).recalculate();
		}
		if (maxNetForce < balancedThreshold) {
			if (balancedEventClient != null)
				balancedEventClient.callback();
			reset();
			return true;
		}
		return false;
	}

	/**
	 * Add a force to ForceLayout's list of forces to apply
	 */
	public void addForce(Force force) {
		// Only want one of each force!
		for (Iterator<Force> i = forces.iterator(); i.hasNext();) {
			Force f = i.next();
			if (f.getTypeName() == force.getTypeName()) {
				i.remove();
			}
		}
		forces.add(force);
		force.setCluster(getRoot());
	}

	/**
	 * Remove a force from ForceLayout's list of forces to apply
	 */
	public void removeForce(Force force) {
		if (!forces.remove(force)) {
			WilmaMain.showErrorDialog("Removing non existant force!", new Exception(
					"Removing non existant force exception!"));
		}
	}

	public void removeAllForces() {
		forces.clear();
	}

	public Vector getForces() {
		return forces;
	}

	/**
	 * Get a reference to one of our forces by name
	 */
	public Force getForce(String name) {
		for (int i = 0; i < forces.size(); i++) {
			Force force = (Force) forces.get(i);
			if (name.equals(force.getTypeName()))
				return force;
		}
		return null;
	}

	public void setBalancedEventClient(BalancedEventClient c) {
		balancedEventClient = c;
	}

	public void setBalancedThreshold(float threshold) {
		this.balancedThreshold = threshold;
	}

	public float getBalancedThreshold() {
		return balancedThreshold;
	}

	public float getVelocityAttenuation() {
		return velocityAttenuation;
	}

	public void setVelocityAttenuation(float va) {
		velocityAttenuation = va;
		System.out.println("VA=" + va);
	}

	public void setConstrained() {
		constrained = true;
	}

	public NodeForceLayout createNodeLayout(org.wilmascope.graph.Node n) {
		return new NodeForceLayout();
	}

	public EdgeForceLayout createEdgeLayout(org.wilmascope.graph.Edge e) {
		return new EdgeForceLayout();
	}

	public void setFrictionCoefficient(float friction) {
		Constants.frictionCoefficient = friction;
	}

	// an array of the forces to apply
	Vector<Force> forces = new Vector<Force>();

	float velocityAttenuation = Constants.velocityAttenuation;

	Vector3f vY = new Vector3f(0, 1f, 0);

	boolean constrained = false;

	private float balancedThreshold = 0.01f;

	private BalancedEventClient balancedEventClient = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getControls()
	 */
	public JPanel getControls() {
		return new ForceControlsPanel((GraphControl.Cluster) getRoot().getUserData("Facade"));
	}

	public String getName() {
		return "Force Directed";
	}

	public int getLevels() {
		return levels;
	}

	public float getLevelSeparation() {
		return levelSeparation;
	}

	private int levels = -1;

	private float levelSeparation = 1f;

	public int getOrbits() {
		return orbits;
	}

	private int orbits = -1;

	public float getOrbitSeparation() {
		return orbitSeparation;
	}

	private float orbitSeparation = 1f;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#getProperties()
	 */
	public Properties getProperties() {
		if (levels >= 0) {
			super.getProperties().setProperty("Levels", "" + levels);
			super.getProperties().setProperty("LevelSeparation", "" + levelSeparation);
		}
		if (orbits >= 0) {
			super.getProperties().setProperty("Orbits", "" + orbits);
			super.getProperties().setProperty("OrbitSeparation", "" + orbitSeparation);
		}
		for (Iterator i = forces.iterator(); i.hasNext();) {
			Force f = (Force) i.next();
			super.getProperties().setProperty(f.getTypeName(), "" + f.getStrengthConstant());
		}
		super.getProperties().setProperty("VelocityAttenuation", "" + getVelocityAttenuation());

		return super.getProperties();
	}

	public void resetProperties() {
		super.resetProperties();
		ForceManager m = ForceManager.getInstance();
		for (Enumeration k = super.getProperties().keys(); k.hasMoreElements();) {
			String key = (String) k.nextElement();
			float strength = Float.parseFloat(super.getProperties().getProperty(key));
			if (key.equals("VelocityAttenuation")) {
				setVelocityAttenuation(strength);
			} else if (key.equals("Levels")) {
				levels = Integer.parseInt(super.getProperties().getProperty(key));
			} else if (key.equals("LevelSeparation")) {
				levelSeparation = Float.parseFloat(super.getProperties().getProperty(key));
			} else if (key.equals("Orbits")) {
				orbits = Integer.parseInt(super.getProperties().getProperty(key));
			} else if (key.equals("OrbitSeparation")) {
				orbitSeparation = Float.parseFloat(super.getProperties().getProperty(key));
			} else {
				try {
					Force f = m.createForce(key);
					f.setStrengthConstant(strength);
					addForce(f);
				} catch (ForceManager.UnknownForceTypeException e) {
					WilmaMain.showErrorDialog("Unknown Force Type", e);
				}
			}
		}
	}

	public static ForceLayout createDefaultForceLayout(Cluster root) {
		ForceLayout fl = new ForceLayout();
		fl.init(root);
		try {
			fl.addForce(ForceManager.getInstance().createForce("Spring"));
			fl.addForce(ForceManager.getInstance().createForce("Repulsion"));
			fl.addForce(ForceManager.getInstance().createForce("Origin"));
			fl.getForce("Origin").setStrengthConstant(2f);
		} catch (UnknownForceTypeException e) {
			WilmaMain.showErrorDialog("Problems creating default ForceLayout", e);
		}
		return fl;
	}

	public static ForceLayout createDefaultClusterForceLayout(Cluster root) {
		ForceLayout fl = createDefaultForceLayout(root);
		fl.getForce("Origin").setStrengthConstant(10f);
		return fl;
	}

	public boolean isIterative() {
		return true;
	}
}
