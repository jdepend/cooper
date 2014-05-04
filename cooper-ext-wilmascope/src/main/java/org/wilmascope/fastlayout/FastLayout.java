package org.wilmascope.fastlayout;

import java.awt.Color;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.wilmascope.control.GraphControl;
import org.wilmascope.graph.Cluster;
import org.wilmascope.graph.Edge;
import org.wilmascope.graph.EdgeLayout;
import org.wilmascope.graph.EdgeList;
import org.wilmascope.graph.LayoutEngine;
import org.wilmascope.graph.Node;
import org.wilmascope.graph.NodeLayout;
import org.wilmascope.graph.NodeList;

/**
 * Title: FastLayout Description: Wilma layout engine implementing a linear time
 * fast force directed placement algorithm
 * 
 * @author James Cowling
 * @version 1.1
 */

public class FastLayout extends LayoutEngine {

	Random rand = new Random();

	// the nodes being laid out
	NodeList nodes;

	// the previous number of nodes - used to determine if the density matrix
	// needs
	// to be updated after some new nodes have been added to the graph
	int oldSize = 0;

	// whether the model is to run in 3D
	boolean threeD = true;

	// how much to multiply the repulsion term by when calculating potential
	int repulsion = Defaults.REPULSION;

	// how much to multiply the attraction term by when calculating potential
	int attraction = Defaults.ATTRACTION;

	// the maximum distance a node can jump during the boiling phase
	float boilJump = Defaults.BOIL_JUMP;

	// the radius of the initial 'universe' containing the nodes
	int fieldRadius = Defaults.FIELD_RADIUS;

	// the number of times to run applyLayout before it is considered balanced
	int totalIterations = Defaults.ITERATIONS;

	// the fraction of the boiling-phase max jump (boilJump) that applies in the
	// simmering phase ( < 1)
	double simmerRate = Defaults.SIMMER_RATE;

	// the frequency of barrier jumping during the boiling phase ( < 1)
	double maxBarrierRate = Defaults.MAX_BARRIER_RATE;

	// the frequency of barrier jumping at the end of the quenching phase ( < 1)
	double minBarrierRate = Defaults.MIN_BARRIER_RATE;

	// the fraction of the total iterations that are performed in the boiling
	// phase
	// ( < 1 - QUENCH_LENGTH)
	double boilLength = Defaults.BOIL_LENGTH;

	// the fraction of the total iterations that are performed in the quenching
	// phase
	// ( < 1 - BOIL_LENGTH)
	double quenchLength = Defaults.QUENCH_LENGTH;

	// the amount to decrement maxJump by each iteration during quenching to
	// linearly
	// decrease it from boilJump to simmerRate
	double jumpDec;

	// the amount to decrement barrierRate by each iteration during quenching to
	// linearly
	// decrease it from maxBarrierRate to minBarrierRate
	double barrierDec;

	// enumeration of phase names for convenience sake
	static final int BOILING = 0;
	static final int QUENCHING = 1;
	static final int SIMMERING = 2;

	// the potential of a node required for it to be considered 'red' in
	// colour-coding
	static final double RED_LIMIT = 50d;

	// the amount to divide the potential by when setting node radius with
	// eye-candy enabled
	static final double POTENTIAL_SCALE = 200d;

	// the current phase
	int phase = BOILING;

	// the matrix storing the densities
	DensityMatrix universe;

	// the number of times applyLayout has been run
	int iterations = 0;

	// the current maximum jump length
	float maxJump = boilJump;

	// the current barrier jump rate
	double barrierRate = maxBarrierRate;

	// if graph centering should be enabled on each iteration
	boolean centreFlag = Defaults.CENTRE_FLAG;

	// if colour coding by potential is enabled
	boolean colourFlag = Defaults.COLOUR_FLAG;

	// if extra eye-candy is enabled
	boolean eyeCandyFlag = Defaults.EYE_CANDY_FLAG;

	// specifies if the algorithm should be naive (and faster) in not updating
	// the density term
	// at a position with the density of the current node, before deciding if
	// the potential is less
	// at that point
	boolean naive = Defaults.NAIVE;

	// specifies whether barrier jumping should be enabled in the boiling phase
	boolean boilBarrier = Defaults.BOIL_BARRIER;

	// a colour range from green to red for colour-coding
	Vector colours = new Vector();

	// must be run first to set initial densities
	public void calculateLayout() {

		// if new nodes have been added, reinitialize the density matrix
		if (nodes.size() != oldSize) {
			oldSize = nodes.size();
			universe.setZero();
			universe.set(nodes);
		}
	}

	private void updatePhase() {
		switch (phase) {
		case BOILING:
			if (iterations > boilLength * totalIterations) {
				System.out.println("Now quenching...");
				phase = QUENCHING;
				updatePhase(); // in case quenching length is zero
			}
			break;
		case QUENCHING:
			if (iterations > (quenchLength + boilLength) * totalIterations) {
				System.out.println("Now simmering...");
				phase = SIMMERING;
				barrierRate = 0;
			}
			break;
		}
	}

	public boolean applyLayout() {

		iterations++;

		updatePhase();

		if (phase == QUENCHING) {
			maxJump -= jumpDec;
			barrierRate -= barrierDec;
		}

		for (Node current : nodes) {

			Point3f oldPosition = new Point3f(current.getPosition());
			double newPotential = 0d;

			if ((boilBarrier || phase != BOILING) && (rand.nextDouble() < barrierRate)) {
				// true the fraction of times specified by barrierRate
				// the first half of the test is to ensure that the user hasn't
				// disabled barrier jumping in the boiling phase

				current.setPosition(computeCentroid(current));
				universe.update(oldPosition, current.getPosition(), current);

				// only calculating this because of colour coding
				if (colourFlag)
					newPotential = getPotential(current);
			} else {
				double oldPotential = getPotential(current);
				current.reposition(getRandomOffset());
				if (!naive)
					universe.update(oldPosition, current.getPosition(), current);

				newPotential = getPotential(current);

				// make a guess as to what the potential would be
				if (naive)
					newPotential += current.getMass() * repulsion * universe.getStandard();

				if (newPotential > oldPotential) {
					if (!naive)
						universe.update(current.getPosition(), oldPosition, current);
					current.setPosition(oldPosition);
					if (colourFlag)
						newPotential = oldPotential;
				} else {
					if (naive)
						universe.update(oldPosition, current.getPosition(), current);
				}
			}

			// scale the current colour by newPotential
			if (colourFlag) {
				if (newPotential < RED_LIMIT)
					((GraphControl.Node) (current.getUserData("Facade"))).setColour((Color) colours
							.get((int) ((newPotential / RED_LIMIT) * 100d)));
				else
					((GraphControl.Node) (current.getUserData("Facade"))).setColour(Color.red);
			}

			// very wasteful in terms of efficiency - used only for visual
			// appeal
			if (eyeCandyFlag) {
				EdgeList edges = current.getEdges();
				for (Edge temp : edges) {
					Color colour1 = ((GraphControl.Node) temp.getStart().getUserData("Facade")).getColour();
					Color colour2 = ((GraphControl.Node) temp.getEnd().getUserData("Facade")).getColour();
					Color3f edgeColour = new Color3f();
					edgeColour.interpolate(new Color3f(colour1), new Color3f(colour2), 0.5f);
					((GraphControl.Edge) temp.getUserData("Facade")).setColour(edgeColour.get());
				}
				((GraphControl.Node) current.getUserData("Facade")).setRadius((float) (newPotential / POTENTIAL_SCALE));
			}

		}

		if (centreFlag)
			recentreGraph();

		return (iterations > totalIterations);
	}

	public int getIterations() {
		return iterations;
	}

	public String getPhase() {
		switch (phase) {
		case BOILING:
			return "boiling";
		case QUENCHING:
			return "quenching";
		default:
			return "simmering";
		}
	}

	// will returrn the total energy of the system
	// this could easily be calculated incrementally in applyLayout() but that
	// would
	// slow down the layout engine unnecessarily when the energy does not need
	// to be known
	public double systemEnergy() {
		double total = 0;
		for (Node n : nodes) {
			total += getPotential(n);
		}
		return total;
	}

	private void recentreGraph() {
		Point3f centre = nodes.getBarycenter();
		// subtract current centre vector from all nodes to bring their centre
		// back to the origin
		for (Node n : nodes) {
			n.getPosition().sub(centre);
		}

		// reset density matrix - will be expensive
		universe.setZero();
		universe.set(nodes);
	}

	// will have to take into account a lot of factors, including the phase:
	// boiling, quenching, simmering etc
	// return a position offset within the maxJump factor
	private Vector3f getRandomOffset() {
		float x = (rand.nextFloat() - 0.5f) * 2f * maxJump;
		float y = (rand.nextFloat() - 0.5f) * 2f * maxJump;
		float z = threeD ? (rand.nextFloat() - 0.5f) * 2f * maxJump : 0f;

		return new Vector3f(x, y, z);
	}

	private double getPotential(Node node) {

		double potential = 0;
		EdgeList edges = node.getEdges();

		for (Edge edge : edges) {
			edge.recalculate();
			potential += attraction * edge.getWeight() * Math.pow(edge.getLength(), 2) * edge.getStart().getMass()
					* edge.getEnd().getMass();
			// this is modified from that given in the research paper - it
			// creates greater attraction for heavier nodes
		}
		potential += repulsion * universe.get(node.getPosition());

		return potential;
	}

	// for barrier jumping
	// computes a weighted centroid over the adjacent nodes and returns the
	// position
	//
	// Weighted centroid = Sum(edgeWeight*position)/Sum(weight)
	private Point3f computeCentroid(Node node) {
		if (node.getDegree() == 0)
			return node.getPosition();
		Point3f centroid = new Point3f();
		float totalWeight = 0f;

		EdgeList edges = node.getEdges();
		for (Edge current : edges) {
			float weight = current.getWeight();
			Point3f scaledPos = new Point3f(current.getNeighbour(node).getPosition());
			scaledPos.scale(weight);
			centroid.add(scaledPos);
			totalWeight += weight;
		}
		centroid.scale(1f / totalWeight); // average over weights
		return centroid;
	}

	// flattens the graph in the z-plane
	public void flattenGraph() {
		for (Node n : nodes) {
			n.getPosition().z = 0f;
		}
	}

	// establishes colours for green, amber & red, then interpolates all the
	// in-between
	// colours to determine the colour spectrum (of length 101) for the nodes
	private void calcColours() {
		Color3f green = new Color3f(Color.green);
		Color3f orange = new Color3f(Color.orange);
		Color3f red = new Color3f(Color.red);

		colours.add(green.get());
		// interpolate the next 50 colours between green and orange:
		for (int i = 0; i < 50; i++) {
			Color3f temp = new Color3f();
			float alpha = (float) i / (float) 49;
			temp.interpolate(green, orange, alpha);
			colours.add(temp.get());
		}

		colours.add(orange.get());
		// interpolate the next 48 colours between orange and red:
		for (int i = 0; i < 48; i++) {
			Color3f temp = new Color3f();
			float alpha = (float) i / (float) 47;
			temp.interpolate(orange, red, alpha);
			colours.add(temp.get());
		}

		colours.add(red.get());
	}

	private void updateJumpDec() {
		jumpDec = (boilJump - boilJump * simmerRate) / (quenchLength * totalIterations);
	}

	private void updateBarrierDec() {
		barrierDec = (maxBarrierRate - minBarrierRate) / (quenchLength * totalIterations);
	}

	public void setIterations(int val) {
		totalIterations = val;
		updateJumpDec();
		updateBarrierDec();
	}

	public void setAttract(int val) {
		attraction = val;
	}

	public void setRepel(int val) {
		repulsion = val;
	}

	public void setBoilJump(int val) {

		// scale current maxJump using:
		//
		// maxJump - boilJump boilJump
		// -------------------------- = -------------
		// newMaxJump - newBoilJump newBoilJump
		//

		maxJump = val * (maxJump - boilJump) / boilJump + val;
		boilJump = val;

		updateJumpDec();
	}

	public void setRadius(int val) {
		fieldRadius = val;
		universe.setRadius(fieldRadius);
		universe.set(nodes);
	}

	public void setSimmerRate(double val) {
		simmerRate = val;
		if (phase == SIMMERING)
			maxJump = (float) (boilJump * simmerRate);
		else
			updateJumpDec();
	}

	public void setBoilLen(double val) {
		boilLength = val;
		phase = BOILING; // the phase will cascade down the to correct one
		updatePhase();
	}

	public void setQuenchLen(double val) {
		quenchLength = val;
		phase = BOILING;
		updatePhase();
		updateJumpDec();
		updateBarrierDec();
	}

	public void setMinBarrier(double val) {
		minBarrierRate = val;
		updateBarrierDec();
	}

	public void setMaxBarrier(double val) {
		// scale barrier rate (see setBoilJump):
		barrierRate = val * (barrierRate - maxBarrierRate) / maxBarrierRate + val;

		maxBarrierRate = val;
		updateBarrierDec();
	}

	public void setCentreFlag(boolean val) {
		centreFlag = val;
	}

	public void setNaive(boolean val) {
		naive = val;
	}

	// sets whether to use barrier jumping in the boiling phase
	public void setBoilBarrier(boolean val) {
		boilBarrier = val;
	}

	public void setColourFlag(boolean val) {
		colourFlag = val;
	}

	public void setEyeCandyFlag(boolean val) {
		eyeCandyFlag = val;
	}

	public void reset() {
		iterations = 0;
		universe.reset();
		updateJumpDec();
		updateBarrierDec();
		phase = BOILING;
		barrierRate = maxBarrierRate;
		maxJump = boilJump;
		nodes = getRoot().getNodes();
	}

	public DensityMatrix getUniverse() {
		return universe;
	}

	public NodeLayout createNodeLayout(org.wilmascope.graph.Node n) {
		return new NodeLayout() {
		};
	}

	public EdgeLayout createEdgeLayout(org.wilmascope.graph.Edge e) {
		return new EdgeLayout() {
		};
	}

	public JPanel getControls() {
		return new ParamsPanel((GraphControl.Cluster) getRoot().getUserData("Facade"));
	}

	public String getName() {
		return "Simulated Annealing";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#init(org.wilmascope.graph.Cluster)
	 */
	public void init(Cluster root) {
		super.init(root);
		nodes = root.getNodes();
		updateJumpDec();
		updateBarrierDec();
		universe = new DensityMatrix(2 * fieldRadius, threeD);
		calcColours();
	}

	public void setThreeD(boolean threeD) {
		this.threeD = threeD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wilmascope.graph.LayoutEngine#resetProperties()
	 */
	public void resetProperties() {
		// TODO Auto-generated method stub

	}
}
